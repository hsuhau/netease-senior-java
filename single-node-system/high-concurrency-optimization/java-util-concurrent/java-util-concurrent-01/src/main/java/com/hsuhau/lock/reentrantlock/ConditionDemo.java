package com.hsuhau.lock.reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hsuhau
 * @date 2020/7/20 15:41
 */
public class ConditionDemo {
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.lock();

                try {
                    System.out.println("当前线程：" + Thread.currentThread().getName() + "获得锁");
                    condition.await();// 把线程挂起
                    System.out.println("当前线程：" + Thread.currentThread().getName() + "开始执行~");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });

        thread.start();
        Thread.sleep(2000L);
        System.out.println("休眠2秒，来控制线程");
        lock.lock();
        condition.signal(); // 报错
        lock.unlock();
    }
}
