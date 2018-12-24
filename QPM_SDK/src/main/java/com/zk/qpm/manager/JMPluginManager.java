package com.jm.android.gt.manager;

import android.os.Bundle;

import com.jm.android.gt.plugin.JMPluginControler;
import com.jm.android.gt.plugin.JMPluginItem;

import java.util.LinkedHashSet;
import java.util.Map;

public class JMPluginManager {

    private LinkedHashSet<JMPluginItem> pluginList;
    private Map<String, JMPluginItem> piMap;
    private static JMPluginItem[] EMPTY = {};
    private JMPluginControler mPluginControler;


    private static JMPluginManager instance = null;

    private JMPluginManager() {
    }

    public static JMPluginManager getInstance() {
        if (instance == null) {
            synchronized (JMPluginManager.class) {
                JMPluginManager temp = instance;
                if (temp == null) {
                    temp = new JMPluginManager();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    public JMPluginItem[] getPlugins() {
        return pluginList.toArray(EMPTY);
    }


    public void register(JMPluginItem item) {
        if (null != item.name && item.name.length() > 0) {
            pluginList.add(item);
            piMap.put(item.name, item);
        }
    }

    public void addPlugin(JMPluginItem item) {
        synchronized (instance) {
            pluginList.add(item);
            piMap.put(item.name, item);
        }
    }

    public void removePlugin(String name) {
        synchronized (instance) {
            JMPluginItem item = piMap.remove(name);
            if (null != item) {
                pluginList.remove(item);
            }
        }
    }


    public JMPluginControler getPluginControler() {
        if (null == mPluginControler) {
            mPluginControler = new JMPluginControler();
        }
        return mPluginControler;
    }


    public void dispatchCommand(String sReceiver, Bundle bundle) {
        JMPluginItem item = piMap.get(sReceiver);
        if (item != null) {
            // 加入任务队列，由插件自己决定如何执行
            item.addTask(bundle);
        }
    }

    public void dispatchCommandSync(String sReceiver, Bundle bundle) {
        JMPluginItem item = piMap.get(sReceiver);
        if (item != null) {
            // 加入任务队列，由插件自己决定如何执行
            item.doTask(bundle);
        }
    }


}
