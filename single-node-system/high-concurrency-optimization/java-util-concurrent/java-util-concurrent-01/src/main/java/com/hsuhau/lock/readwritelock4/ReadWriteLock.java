package com.hsuhau.lock.readwritelock4;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 读写锁实现
 * <img src="ReadWriteLock.png">
 * <p>
 * 模板方法模式
 * 定义一个算法的骨架，将骨架的特定步骤延迟到子类中（注：特定步骤有子类实现）。
 * 模板方法模式使得子类可以不改变算法的结构即可重新定义该算法的某些特定步骤。
 *
 * @author hsuhau
 * @date 2020/7/20 18:32
 */
public class ReadWriteLock {

    private boolean isFair;

    CommonMask mask = new CommonMask() {

        @Override
        public boolean tryLock(int acquires) {
            if (isFair) {
                return tryFairLock(acquires);
            } else {
                return tryNonFairLock(acquires);
            }
        }

        /**
         * 公平锁
         * @param acquires
         * @return
         */
        public boolean tryFairLock(int acquires) {
            // 如果read count ！= 0 返回false
            if (readCount.get() != 0) {
                return false;
            }

            // 拿到 独占锁，当前状态
            int wct = writeCount.get();

            if (wct == 0) {
                WaitNode head = waiters.peek();
                // 通过修改state来抢锁
                if (head != null && head.thread == Thread.currentThread() && writeCount.compareAndSet(wct, wct + acquires)) {
                    // 抢到锁后，直接修改owner为当前线程
                    owner.set(Thread.currentThread());
                    return true;
                }
            } else if (owner.get() == Thread.currentThread()) {
                writeCount.set(wct + acquires);
                return true;
            }
            return false;
        }

        public boolean tryNonFairLock(int acquires) {
            // 如果read count ！= 0 返回false
            if (readCount.get() != 0) {
                return false;
            }

            // 拿到 独占锁，当前状态
            int wct = writeCount.get();

            if (wct == 0) {
                // 通过修改state来抢锁
                if (writeCount.compareAndSet(wct, wct + acquires)) {
                    // 抢到锁后，直接修改owner为当前线程
                    owner.set(Thread.currentThread());
                    return true;
                }
            } else if (owner.get() == Thread.currentThread()) {
                writeCount.set(wct + acquires);
                return true;
            }
            return false;
        }
    };

    public Lock readLock() {
        return new Lock() {
            @Override
            public void lock() {

            }

            @Override
            public void lockInterruptibly() throws InterruptedException {

            }

            @Override
            public boolean tryLock() {
                return false;
            }

            @Override
            public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
                return false;
            }

            @Override
            public void unlock() {

            }

            @Override
            public Condition newCondition() {
                return null;
            }
        };
    }

    public Lock writeLock() {
        return new Lock() {
            @Override
            public void lock() {
                mask.lock();
            }

            @Override
            public void lockInterruptibly() throws InterruptedException {

            }

            @Override
            public boolean tryLock() {
                return false;
            }

            @Override
            public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
                return false;
            }

            @Override
            public void unlock() {

            }

            @Override
            public Condition newCondition() {
                return null;
            }
        };
    }

    /**
     * 获取独占锁
     */
    public void lock() {
        mask.lock();
    }

    /**
     * 尝试获取独占锁
     *
     * @param acquires
     * @return
     */
    public boolean tryLock(int acquires) {
        return mask.tryLock(acquires);
    }

    /**
     * 释放锁
     *
     * @return
     */
    public boolean unLock() {
        return mask.unLock();
    }

    /**
     * 尝试释放独占锁
     *
     * @param releases
     * @return
     */
    public boolean tryUnlock(int releases) {
        return mask.tryUnlock(releases);
    }

    /**
     * 获取共享锁
     */
    public void lockShared() {
        mask.lockShared();
    }

    /**
     * 尝试获取共享锁
     *
     * @param acquires
     * @return
     */
    private int tryLockShared(int acquires) {
        return mask.tryLockShared(acquires);
    }

    /**
     * 释放共享锁
     *
     * @return
     */
    public boolean unlockShared() {
        return mask.unlockShared();
    }

    /**
     * 尝试释放共享锁
     *
     * @param releases
     * @return
     */
    public boolean tryUnlockShared(int releases) {
        return mask.tryUnlockShared(releases);
    }
}

