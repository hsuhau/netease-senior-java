package com.hsuhau.lock.readwritelock4;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * 重复的代码提取到这里
 *
 * @author hsuhau
 * @date 2020/7/20 19:42
 */
public class CommonMask {
    // 等待队列
    public volatile LinkedBlockingQueue<WaitNode> waiters = new LinkedBlockingQueue<>();
    volatile AtomicInteger readCount = new AtomicInteger(0);
    AtomicInteger writeCount = new AtomicInteger(0);
    // 独占锁
    AtomicReference<Thread> owner = new AtomicReference<>();

    /**
     * 获取独占锁
     */
    public void lock() {
        int arg = 1;
        // 尝试获取独占锁，若成功，推出方法，若失败

        if (!tryLock(arg)) {
            // 标记独占锁
            WaitNode waitNode = new WaitNode(0, Thread.currentThread(), arg);
            // 进入等待队列
            waiters.offer(waitNode);

            // 循环尝试拿锁
            for (; ; ) {
                // 若队列头部是当前线程
                WaitNode head = waiters.peek();
                if (head != null && head.thread == Thread.currentThread()) {
                    // 再次尝试获取，独占锁
                    if (!tryLock(arg)) {
                        // 若失败，挂起线程
                        LockSupport.park();
                    }
                    //若成功获取
                    else {
                        // 当前线程从队列头部移除
                        waiters.poll();
                        // 并退出方法
                        return;
                    }
                }
            }
        }
    }

    /**
     * 尝试获取独占锁
     *
     * @param acquires
     * @return
     */
    public boolean tryLock(int acquires) {
        throw new UnsupportedOperationException();
    }

    /**
     * 释放锁
     *
     * @return
     */
    public boolean unLock() {
        int arg = 1;
        // 尝试释放独占锁
        if (tryUnlock(arg)) {
            // 取出队列头部的元素
            WaitNode next = waiters.peek();
            if (next != null) {
                Thread thread = next.thread;
                // 唤醒队列头部的线程
                LockSupport.unpark(thread);
            }
            return true;
        }
        return false;
    }

    /**
     * 尝试释放独占锁
     *
     * @param releases
     * @return
     */
    public boolean tryUnlock(int releases) {
        // 若当前线程 没有持有独占锁
        if (owner.get() != Thread.currentThread()) {
            // 抛出IllegalMonitorStateException
            throw new IllegalMonitorStateException();
        }

        int wc = writeCount.get();
        // 计算 独占锁剩余占用
        int nextc = wc - releases;
        // 不曾是否完全释放，都更新count值
        writeCount.set(nextc);
        // 是否完全释放
        if (nextc == 0) {
            owner.compareAndSet(Thread.currentThread(), null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取共享锁
     */
    public void lockShared() {
        int arg = 1;
        // 如果tryLockShared()失败
        if (tryLockShared(arg) < 0) {
            WaitNode node = new WaitNode(1, Thread.currentThread(), arg);
            // 加入队列
            waiters.offer(node);

            for (; ; ) {
                // 若队列头部的元素是当前线程
                WaitNode head = waiters.peek();
                if (head != null && head.thread == Thread.currentThread()) {
                    // 尝试获取共享锁，若成功
                    if (tryLockShared(arg) >= 0) {
                        // 将当前线程对队列中移除
                        waiters.poll();

                        WaitNode next = waiters.peek();
                        // 如果下一个线程也是等待共享锁
                        if (next != null && next.type == 1) {
                            // 将其唤醒
                            LockSupport.unpark(next.thread);
                        }
                        return;
                    }
                    // 若尝试失败
                    else {
                        // 挂起线程
                        LockSupport.park();
                    }
                }
                // 若不是头部元素
                else {
                    LockSupport.park();
                }

            }
        }
    }

    /**
     * 尝试获取共享锁
     *
     * @param acquires
     * @return
     */
    public int tryLockShared(int acquires) {
        for (; ; ) {
            if (writeCount.get() != 0 && owner.get() != Thread.currentThread()) {
                return -1;
            }
            int rct = readCount.get();
            if (readCount.compareAndSet(rct, rct + acquires)) {
                return 1;
            }
        }
    }

    /**
     * 释放共享锁
     *
     * @return
     */
    public boolean unlockShared() {
        int arg = 1;
        // 当read count变为0，才叫release share成功
        if (tryUnlockShared(arg)) {
            WaitNode next = waiters.peek();
            if (next != null) {
                LockSupport.unpark(next.thread);
            }
            return true;
        }
        return false;
    }

    /**
     * 尝试释放共享锁
     *
     * @param releases
     * @return
     */
    public boolean tryUnlockShared(int releases) {
        for (; ; ) {
            int rc = readCount.get();
            int nextc = rc - releases;
            if (readCount.compareAndSet(rc, nextc)) {
                return nextc == 0;
            }
        }
    }

    class WaitNode {
        int type = 0; // 0 为想获取独占锁的线程 1 为想获取共享锁的线程
        Thread thread = null;
        int arg = 0;

        public WaitNode(int type, Thread thread, int arg) {
            this.type = type;
            this.thread = thread;
            this.arg = arg;
        }
    }
}
