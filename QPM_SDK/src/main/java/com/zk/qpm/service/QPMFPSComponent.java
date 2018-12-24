package com.zk.qpm.service;

import android.content.Context;
import android.os.IBinder;

import com.zk.qpm.QPMException;
import com.zk.qpm.executor.QPMGetFPSInfoExecutor;

public class QPMFPSComponent extends QPMBaseService {

    public QPMFPSComponent() {
        executors.clear();
        executors.add(new QPMGetFPSInfoExecutor());
    }

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
        try {
            exec();
        } catch (QPMException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(Context context) {
        super.onDestroy(context);
        stopExec();
    }

    @Override
    public IBinder onBind(Context context) {
        return null;
    }

}
