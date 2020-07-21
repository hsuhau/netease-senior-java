package com.hsuhau.lock.reentrantlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁
 *
 * @author hsuhau
 * @date 2020/7/20 16:30
 */
public class ReentrantDemo1 {
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "获得第1次锁");
        Thread.sleep(1000L);

        lock.lock();
        System.out.println(Thread.currentThread().getName() + "获得第2次锁");
        Thread.sleep(1000L);
        lock.unlock(); // 释放不掉
        lock.unlock();

        Thread thread = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName() + "开始去释放锁");
                    lock.lock();
                    System.out.println("获得锁成功~");
                    lock.unlock();
                });
        thread.start();
    }
}
