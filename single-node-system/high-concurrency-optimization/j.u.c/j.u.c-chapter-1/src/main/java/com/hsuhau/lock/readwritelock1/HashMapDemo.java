package com.hsuhau.lock.readwritelock1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author hsuhau
 * @date 2020/7/20 18:02
 */
public class HashMapDemo {
    private final Map<String, Object> map = new HashMap<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public Object get(String key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public Object put(String key, Object obj) {
        writeLock.lock();
        try {
            return map.put(key, obj);
        } finally {
            writeLock.unlock();
        }
    }

    public Object[] allKeys() {
        readLock.lock();
        try {
            return map.keySet().toArray();
        } finally {
            readLock.unlock();
        }
    }

    public void clean() {
        writeLock.lock();
        try {
            map.clear();
        } finally {
            writeLock.unlock();
        }
    }
}
