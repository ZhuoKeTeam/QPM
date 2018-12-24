package com.jm.android.gt.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.executor.IExecutor;
import com.jm.android.gt.manager.JMGTSwitchManager;
import com.jm.android.gt.plugin.JMPluginService;
import com.jm.android.gt.utils.PrefsParser;

import java.util.ArrayList;
import java.util.List;


public abstract class JMBaseService {
    protected IBinder mBinder;
    protected List<IExecutor> executors = new ArrayList<>();

    public synchronized IBinder getBinder(Context context) {
        if (this.mBinder == null) {
            this.mBinder = onBind(context);
        }

        return this.mBinder;
    }

    public abstract IBinder onBind(Context context);

    protected void createShowView(Context context) {
        for (IExecutor executor : executors) {
            executor.createShowView(context);
        }
    }

    protected void destoryShowView() {
        for (IExecutor executor : executors) {
            executor.destoryShowView();
        }
    }

    public void onCreate(Context context) {
        createShowView(context);
    }

    public void onStart(Context context, Intent intent) {
    }

    public void onDestroy(Context context) {
        destoryShowView();
    }

    protected final void stopSelf() {
        JMPluginService.stopService(this);
    }

    public final boolean equals(Object o) {
        return (o != null) && (getClass() == o.getClass());
    }

    protected void exec() throws JMGTException {
        List<PrefsParser.PrefItem> switchs = JMGTSwitchManager.getInstance().getSwitchs();
        for (IExecutor executor : executors) {
            if (JMGTSwitchManager.getInstance().isSwitchOpen(switchs, executor)) {
                executor.exec();
            }
        }
    }

    protected void stopExec() {
        for (IExecutor executor : executors) {
            executor.stop();
        }
    }

}
