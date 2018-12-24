package com.zk.qpm.manager;

import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 注意：本类不是线程池，不能当做线程池来用
 */
public class QPMThreadManager {

    private static volatile QPMThreadManager instance;

    private Object SYNC_OBJECT = new Object();
    private final List<Thread> threads = new ArrayList<>();

    private QPMThreadManager() {
    }

    public static QPMThreadManager getInstance() {
        if (instance == null) {
            synchronized (QPMThreadManager.class) {
                if (instance == null) {
                    instance = new QPMThreadManager();
                }
            }
        }
        return instance;
    }

    private void excludeAllDiedThread() {
        Iterator<Thread> iterator = threads.iterator();
        while (iterator.hasNext()) {
            Thread eachThread = iterator.next();
            if (!eachThread.isAlive()) {
                iterator.remove();
            }
        }
    }

    private void addThread(Thread thread) {
        synchronized (SYNC_OBJECT) {
            // 排除已经结束的线程
            excludeAllDiedThread();
            threads.add(thread);
        }
    }

    public int getAllThreadCount() {
        synchronized (SYNC_OBJECT) {
            // 排除已经结束的线程
            excludeAllDiedThread();
            return threads.size();
        }
    }

    public void clearAllThread() {
        synchronized (SYNC_OBJECT) {
            threads.clear();
        }
    }

    public Thread createCommonThread() {
        Thread thread = new Thread();
        addThread(thread);
        return thread;
    }

    public Thread createCommonThread(String threadName) {
        Thread thread = new Thread(threadName);
        addThread(thread);
        return thread;
    }

    public Thread createCommonThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        addThread(thread);
        return thread;
    }

    public HandlerThread createHandlerThread(String threadName) {
        HandlerThread thread = new HandlerThread(threadName);
        addThread(thread);
        return thread;
    }

}
