package com.zk.qpm.manager;

import android.os.Bundle;

import com.zk.qpm.plugin.QPMPluginControler;
import com.zk.qpm.plugin.QPMPluginItem;

import java.util.LinkedHashSet;
import java.util.Map;

public class QPMPluginManager {

    private LinkedHashSet<QPMPluginItem> pluginList;
    private Map<String, QPMPluginItem> piMap;
    private static QPMPluginItem[] EMPTY = {};
    private QPMPluginControler mPluginControler;


    private static QPMPluginManager instance = null;

    private QPMPluginManager() {
    }

    public static QPMPluginManager getInstance() {
        if (instance == null) {
            synchronized (QPMPluginManager.class) {
                QPMPluginManager temp = instance;
                if (temp == null) {
                    temp = new QPMPluginManager();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    public QPMPluginItem[] getPlugins() {
        return pluginList.toArray(EMPTY);
    }


    public void register(QPMPluginItem item) {
        if (null != item.name && item.name.length() > 0) {
            pluginList.add(item);
            piMap.put(item.name, item);
        }
    }

    public void addPlugin(QPMPluginItem item) {
        synchronized (instance) {
            pluginList.add(item);
            piMap.put(item.name, item);
        }
    }

    public void removePlugin(String name) {
        synchronized (instance) {
            QPMPluginItem item = piMap.remove(name);
            if (null != item) {
                pluginList.remove(item);
            }
        }
    }


    public QPMPluginControler getPluginControler() {
        if (null == mPluginControler) {
            mPluginControler = new QPMPluginControler();
        }
        return mPluginControler;
    }


    public void dispatchCommand(String sReceiver, Bundle bundle) {
        QPMPluginItem item = piMap.get(sReceiver);
        if (item != null) {
            // 加入任务队列，由插件自己决定如何执行
            item.addTask(bundle);
        }
    }

    public void dispatchCommandSync(String sReceiver, Bundle bundle) {
        QPMPluginItem item = piMap.get(sReceiver);
        if (item != null) {
            // 加入任务队列，由插件自己决定如何执行
            item.doTask(bundle);
        }
    }


}
