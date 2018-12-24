package com.zk.qpm.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.zk.qpm.QPMException;
import com.zk.qpm.executor.IExecutor;
import com.zk.qpm.manager.QPMSwitchManager;
import com.zk.qpm.plugin.QPMPluginService;
import com.zk.qpm.utils.PrefsParser;

import java.util.ArrayList;
import java.util.List;


public abstract class QPMBaseService {
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
        QPMPluginService.stopService(this);
    }

    public final boolean equals(Object o) {
        return (o != null) && (getClass() == o.getClass());
    }

    protected void exec() throws QPMException {
        List<PrefsParser.PrefItem> switchs = QPMSwitchManager.getInstance().getSwitchs();
        for (IExecutor executor : executors) {
            if (QPMSwitchManager.getInstance().isSwitchOpen(switchs, executor)) {
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
