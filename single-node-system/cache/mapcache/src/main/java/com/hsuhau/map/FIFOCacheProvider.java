package com.hsuhau.map;

import java.util.LinkedHashMap;
import java.util.Map;
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
 * <p>
 * 建议内存缓存工具实现总结：
 * <p>
 * 基于ConcurrentHashMap实现并发读写
 * 基于定时器实现缓存的定时。定期清除
 * 基于队列实现缓存的FIFO淘汰
 *
 * @author hsuhau
 * @date 2020/7/21 15:11
 */
public class FIFOCacheProvider {
    /**
     * 存放缓存的集合
     */
    private Map<String, CacheData> cacheDatas;

    /**
     * 定时器线程池，用于清除过期的缓存
     */
    private final static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);

    /**
     * FIFO
     */
    private static int MAX_CACHE_SIZE = 0;

    /**
     * 填充因子
     */
    private final float LOAD_FACTORY = 0.75f;


    /**
     * 构造函数需要设置缓存的大小
     *
     * @param maxCacheSize
     */
    public FIFOCacheProvider(int maxCacheSize) {
        MAX_CACHE_SIZE = maxCacheSize;
        // 根据cacheSize和填充因子，计算cache的容量
        int capacity = ((int) (Math.ceil(MAX_CACHE_SIZE / LOAD_FACTORY) + 1));
        cacheDatas = new LinkedHashMap<String, CacheData>(capacity, LOAD_FACTORY, false) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, CacheData> eldest) {
                return FIFOCacheProvider.this.size() > MAX_CACHE_SIZE;
            }
        };
    }

    /**
     * 读取缓存
     *
     * @param key 键
     * @param <T>
     * @return
     */
    public synchronized <T> T get(String key) {
        CacheData cacheData = cacheDatas.get(key);
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
        cacheDatas.remove(key);
        // 大于0，才设置，不然没有意义
        if (expire > 0) {
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    // 过期后清除缓存
                    synchronized (this) {
                        cacheDatas.remove(key);
                    }
                }
            }, expire, TimeUnit.MILLISECONDS);
            cacheDatas.put(key, new CacheData(value, expire));
        } else {
            // 不设置过期时间
            cacheDatas.put(key, new CacheData(value, -1L));
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
        CacheData cacheData = cacheDatas.remove(key);
        return cacheData == null ? null : (T) cacheData.data;
    }

    /**
     * 查询当前缓存的键值对数量
     *
     * @return
     */
    public synchronized int size() {
        return cacheDatas.size();
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

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, CacheData> entry : cacheDatas.entrySet()) {
            stringBuffer.append(entry.getKey()).append(" = ").append(entry.getValue().data).append("\n");
        }
        return stringBuffer.toString();
    }
}
