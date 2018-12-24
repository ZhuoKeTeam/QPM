package com.jm.android.gt.service;

import android.content.Context;
import android.os.IBinder;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.executor.JMGetFPSInfoExecutor;

public class JMFPSComponent extends JMBaseService {

    public JMFPSComponent() {
        executors.clear();
        executors.add(new JMGetFPSInfoExecutor());
    }

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
        try {
            exec();
        } catch (JMGTException e) {
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
