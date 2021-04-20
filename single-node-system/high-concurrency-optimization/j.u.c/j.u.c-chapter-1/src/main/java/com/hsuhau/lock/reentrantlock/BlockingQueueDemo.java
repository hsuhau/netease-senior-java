package com.hsuhau.lock.reentrantlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列
 *
 * @author hsuhau
 * @date 2020/7/20 15:52
 */
public class BlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue blockingQueue = new BlockingQueue(5);
        Thread thread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    blockingQueue.put("x" + i);
                }
            }
        };
        thread.start();
        Thread.sleep(1000L);
        System.out.println("开始取元素");
        for (int i = 0; i < 10; i++) {
            blockingQueue.take();
            Thread.sleep(1000L);
        }
    }
}

class BlockingQueue {
    // 容器
    List<Object> list = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition putCondition = lock.newCondition();
    private final Condition takeCondition = lock.newCondition();
    private final int length;

    public BlockingQueue(int length) {
        this.length = length;
    }

    public void put(Object object) {
        lock.lock();
        try {
            while (true) {
                if (list.size() < length) {
                    list.add(object);
                    System.out.println("队列中放入元素：" + object);
                    takeCondition.signal();
                    return;
                } else { // 满的，放不进了
                    putCondition.await(); // 挂起
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Object take() {
        lock.lock();

        try {
            if (list.size() > 0) {
                Object obj = list.remove(0);
                System.out.println("队列中获得元素" + obj);
                putCondition.signal();
                return obj;
            } else {
                takeCondition.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            return null;
        }
    }
}
