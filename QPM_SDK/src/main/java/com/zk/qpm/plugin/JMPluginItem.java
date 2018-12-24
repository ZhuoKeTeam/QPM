package com.jm.android.gt.plugin;

import android.app.Activity;
import android.os.Bundle;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 暂未使用
 */
public class JMPluginItem {
    public String name; // 作为接收从SDK发送来的命令的标识
    public String alias;
    public String description;
    public int logo_id;
    public Class<? extends Activity> activityClass;

    protected LinkedBlockingQueue<Bundle> taskQueue;
    private Thread consumerThread;
    private JMPluginTaskExecutor mExecutor;

    public JMPluginItem(String name,
                        String alias,
                        String descriotion,
                        int logo_id,
                        Class<? extends Activity> activityClass) {
        this.name = name;
        this.alias = alias;
        this.description = descriotion;
        this.logo_id = logo_id;
        this.activityClass = activityClass;

        // 任务限量100
        taskQueue = new LinkedBlockingQueue<Bundle>(100);
    }

    /**
     * 初始化连接 GT 时，该插件需要做的事情
     */
    public void onInitConnectJMGT() {

    }

    /**
     * 可以接收被测应用传递过来的bundle，至于具体怎么处理，插件拿着Queue看着办吧
     *
     * @param bundle
     */
    public void addTask(Bundle bundle) {
        taskQueue.offer(bundle);
    }

    public void setTaskExecutor(final JMPluginTaskExecutor executor) {
        if (null != consumerThread) {
            consumerThread.interrupt();
            consumerThread = null;
        }
        mExecutor = executor;
        consumerThread = new Thread(mExecutor.getClass().getName()) {
            @Override
            public void run() {
                try {
                    while (true) {
                        Bundle task = taskQueue.take();
                        mExecutor.execute(task);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        consumerThread.start();
    }

    public void removeTaskExecutor() {
        if (null != consumerThread) {
            consumerThread.interrupt();
            consumerThread = null;
        }
    }

    /**
     * 同步执行命令方法
     *
     * @param bundle bundle
     */
    public void doTask(Bundle bundle) {
        mExecutor.execute(bundle);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof JMPluginItem)) {
            return false;
        }
        JMPluginItem otherItem = (JMPluginItem) other;
        if (name.equals(otherItem.name)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (name == null) {
            return -1;
        }
        return name.hashCode();
    }
}
