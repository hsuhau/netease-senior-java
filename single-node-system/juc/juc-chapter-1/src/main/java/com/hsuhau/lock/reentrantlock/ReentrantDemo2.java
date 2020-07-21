package com.hsuhau.lock.reentrantlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hsuhau
 * @date 2020/7/20 16:42
 */
public class ReentrantDemo2 {
    // owner = null; count = 0
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        System.out.println("get lock 1...\n");
        // owner = mainThread; count = 1
        lock.lock(); // 当前线程已获取锁
        System.out.println("get lock 2...\n");
        // owner = mainThread; count = 2
        lock.lock(); // 再次获取，是否能成功
        // 打印消息，说明再次获取锁成功
        System.out.println("here i am ...\n");
        System.out.println("unlock 1...\n");
        // owner = mainThread; count = 1
        lock.unlock();
        System.out.println("unlock 2...\n");
        // owner = null; count = 0
        lock.unlock();
        System.out.println("unlock 3...\n");
        // java.lang.IllegalMonitorStateException
        lock.unlock(); // 第3次释放锁，报错
    }
}
