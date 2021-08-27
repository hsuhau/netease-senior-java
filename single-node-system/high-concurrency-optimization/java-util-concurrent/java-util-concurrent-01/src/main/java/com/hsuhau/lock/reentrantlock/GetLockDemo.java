package com.hsuhau.lock.reentrantlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock接口
 * <table>
 *     <tr>
 *         <td>方法签名</td>
 *         <td>描述</td>
 *     </tr>
 *     <tr>
 *         <td>void lock();</td>
 *         <td>获取锁（不死不休）</td>
 *     </tr>
 *     <tr>
 *         <td>boolean tryLock();</td>
 *         <td>获取锁（浅尝辄止）</td>
 *     </tr>
 *     <tr>
 *         <td>boolean tryLock(long time, TimeUnit unit) throws InterruptedException;</td>
 *         <td>获取锁（过时不候）</td>
 *     </tr>
 *     <tr>
 *         <td>void lockInterruptibly() throws InterruptedException;</td>
 *         <td>获取锁（任人摆布）</td>
 *     </tr>
 *     <tr>
 *         <td>void unlock();</td>
 *         <td>释放锁</td>
 *     </tr>
 *     <tr>
 *         <td>Condition newCondition();</td>
 *         <td></td>
 *     </tr>
 * </table>
 *
 * 结论：
 * 1、lock()最常用；
 * 2、lockInterruptibly()方法一般最昂贵，有的impl可能没有实现lockInterruptibly()，
 * 只有真的需要效应中断时，才使用，使用之前看看impl对该方法的描述。
 *
 * @author hsuhau
 * @date 2020/7/20 15:05
 */
public class GetLockDemo {
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        lock.lock();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                /*boolean rs = lock.tryLock();
                try {
                    rs = lock.tryLock(1, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                /*boolean rs = false;
                try {
                    rs = lock.tryLock(1, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                /*boolean rs = false;
                try {
                    lock.lockInterruptibly();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("是否获取到锁：" + rs);*/

                System.out.println("尝试获取锁");
                lock.lock();
                System.out.println("获得锁了");
            }
        });
        th.start();
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        th.interrupt(); // 中断线程运行
        System.out.println("th 线程被中断了");

        Thread.sleep(5000L);
        lock.unlock();

    }
}
