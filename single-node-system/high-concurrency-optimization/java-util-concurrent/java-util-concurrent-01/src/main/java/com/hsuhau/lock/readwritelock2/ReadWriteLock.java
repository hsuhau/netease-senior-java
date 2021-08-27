package com.hsuhau.lock.readwritelock2;

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

    CommonMask mask = new CommonMask();

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

