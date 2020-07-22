package com.hsuhau.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 基于ConcurrentHashMap实现缓存
 *
 * <table>
 *     <caption>缓存使用时的常见问题</caption>
 *     <tr>
 *         <td>缓存穿透</td>
 *         <td>请求数据中不存在的数据，导致每次都无法从缓存中命中，继而访问到数据库</td>
 *     </tr>
 *     <tr>
 *         <td>缓存击穿</td>
 *         <td>缓存失效的同时大量相同请求穿过缓存访问到数据</td>
 *     </tr>
 *     <tr>
 *         <td>缓存雪崩</td>
 *         <td>大量缓存同时失效，导致大量请求穿过缓存访问到数据库</td>
 *     </tr>
 * </table>
 *
 * <table>
 *     <caption>缓存的特点</caption>
 *     <tr>
 *         <td>设置存活时间（过期策略）</td>
 *         <td>策略通常设置有效期，过期后应当失效，常见的过期策略有：定时、定期、惰性失效</td>
 *     </tr>
 *     <tr>
 *         <td>空间占用有限（淘汰策略）</td>
 *         <td>缓存占用空间上线，超过上限需淘汰部分缓存数据，常见的淘汰策略有：FIFO、LRU、LFU</td>
 *     </tr>
 *     <tr>
 *         <td>支持并发更新</td>
 *         <td>缓存需要支持并发的读取写入</td>
 *     </tr>
 * </table>
 *
 * @author hsuhau
 * @date 2020/7/21 15:11
 */
public class CacheProvider {
    /**
     * 存放缓存的集合
     */
    private final static Map<String, CacheData> CACHE_DATAS = new ConcurrentHashMap<>();

    /**
     * 定时器线程池，用于清除过期的缓存
     */
    private final static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);


    /**
     * 读取缓存
     *
     * @param key 键
     * @param <T>
     * @return
     */
    public synchronized <T> T get(String key) {
        CacheData cacheData = CACHE_DATAS.get(key);
        return cacheData == null ? null : (T) cacheData.data;
    }

    // 设置

    /**
     * 添加缓存
     *
     * @param key   键
     * @param value 值
     */
    public synchronized void put(String key, Object value) {
        this.put(key, value, -1L);
    }

    /**
     * 添加缓存，默认不失效
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间，单位：ms，-1表示不失效
     */
    public synchronized void put(String key, Object value, Long expire) {
        // 清除原数据
        CACHE_DATAS.remove(key);
        // 大于0，才设置，不然没有意义
        if (expire > 0) {
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    // 过期后清除缓存
                    synchronized (this) {
                        CACHE_DATAS.remove(key);
                    }
                }
            }, expire, TimeUnit.MILLISECONDS);
            CACHE_DATAS.put(key, new CacheData(value, expire));
        } else {
            // 不设置过期时间
            CACHE_DATAS.put(key, new CacheData(value, -1L));
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @param <T>
     * @return
     */
    public synchronized <T> T remove(String key) {
        // 清除原缓存数据
        CacheData cacheData = CACHE_DATAS.remove(key);
        return cacheData == null ? null : (T) cacheData.data;
    }

    /**
     * 查询当前缓存的键值对数量
     *
     * @return
     */
    public synchronized int size() {
        return CACHE_DATAS.size();
    }

    /**
     * 缓存实体类
     */
    class CacheData {
        // 缓存数据
        private Object data;
        // 失效时间
        public Long expire;

        public CacheData(Object data, Long expire) {
            this.data = data;
            this.expire = expire;
        }
    }

}
