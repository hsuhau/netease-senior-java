package com.hsuhau.lock.readwritelock1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock
 * <table>
 *     <tr>
 *         <td>概念</td>
 *         <td>维护一对关键锁，一个只用于读操作，一个只用于写操作；<br>
 *             读锁可以有多个读线程同时持有，写锁是排他的。<br>
 *             <b>同一时间，两把锁不能被不同线程持有。<b>
 *     </tr>
 *     <tr>
 *         <td>适用场景</td>
 *         <td>适合读取操作多于写入操作的场景，改进互斥锁的性能，<br>
 *             比如：<b>集合的并发线程安全性改造。缓存组件</b></td>
 *     </tr>
 *     <tr>
 *         <td>锁降级</td>
 *         <td>指的是<b>写锁降级成为读锁</b>。持有写锁的同时，再获取读锁，随后释放写锁的过程。<br>
 *             写锁是线程独占，读锁是共享，所以写->读是降级。（读->写，是不能实现的）</td>
 *     </tr>
 * </table>
 *
 * @author hsuhau
 * @date 2020/7/20 17:38
 */
public class ReadWriteLockDemo {
    // 不希望读写的内容都不一样，需要加入锁机制
    volatile long i = 0;
    Lock lock = new ReentrantLock();

    ReadWriteLock rwLock = new ReentrantReadWriteLock();


    public void read() {
        rwLock.readLock().lock();
        long a = 1;
        rwLock.readLock().unlock();
    }

    public void write() {
        rwLock.writeLock().lock();
        i++;
        rwLock.writeLock().unlock();
    }

    /*public void read() {
        lock.lock();
        long a = 1;
        lock.unlock();
    }

    public void write() {
        lock.lock();
        i++;
        lock.unlock();
    }*/

    public static void main(String[] args) throws InterruptedException {
        final ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 30; i++) {
            int n = i;
            new Thread(() -> {
                for (int j = 0; j < 80000; j++) {
                    if (n % 3 == 0) {
                        readWriteLockDemo.write();
                    } else {
                        readWriteLockDemo.read();
                    }
                }
            }).start();

            long endTime = System.currentTimeMillis();
            System.out.println("耗时：" + (endTime - startTime));
        }
    }

}
