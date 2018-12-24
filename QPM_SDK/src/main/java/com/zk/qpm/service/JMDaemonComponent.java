package com.jm.android.gt.service;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.executor.JMGetActivityStackExecutor;
import com.jm.android.gt.executor.JMGetAppInfoExecutor;
import com.jm.android.gt.executor.JMGetAutoAddExecutor;
import com.jm.android.gt.executor.JMGetCPUInfoExecutor;
import com.jm.android.gt.executor.JMGetFlowInfoExecutor;
import com.jm.android.gt.executor.JMGetH5MonitorInfoExecutor;
import com.jm.android.gt.executor.JMGetMemoryInfoExecutor;
import com.jm.android.gt.executor.JMGetNetworkInfoExecutor;
import com.jm.android.gt.executor.JMGetThreadCountExecutor;
import com.jm.android.gt.executor.JMGetTopActivityExecutor;
import com.jm.android.gt.executor.JMScreenRecorderStatusExecutor;
import com.jm.android.gt.manager.JMGTManager;
import com.jm.android.gt.manager.JMThreadManager;

public class JMDaemonComponent extends JMBaseService {

    private static final int INTERVAL = 1000;

    private HandlerThread mThread;
    private Handler mHandler;

    public JMDaemonComponent() {
        ActivityManager am = (ActivityManager) JMGTManager.getInstance().getContext().getSystemService(Context.ACTIVITY_SERVICE);
        executors.clear();
        executors.add(new JMGetAppInfoExecutor());
        executors.add(new JMGetTopActivityExecutor());
        executors.add(new JMGetAutoAddExecutor());
        executors.add(new JMGetCPUInfoExecutor());
        executors.add(new JMGetThreadCountExecutor());
        executors.add(new JMGetMemoryInfoExecutor(am));
        executors.add(new JMGetFlowInfoExecutor());
        executors.add(new JMGetActivityStackExecutor());
        executors.add(new JMScreenRecorderStatusExecutor());
        executors.add(new JMGetH5MonitorInfoExecutor());
        if (JMGTManager.getInstance().isImplementOkHttp()) {
            JMGetNetworkInfoExecutor networkInfoExecutor = JMGTManager.getInstance().getOkHttpInterceptor();
            networkInfoExecutor.reset();
            executors.add(networkInfoExecutor);
        }
    }

    private void initIfNeed(Context context) {
        if (mThread == null) {
            mThread = JMThreadManager.getInstance().createHandlerThread(JMDaemonComponent.class.getSimpleName());
            mThread.setDaemon(true);
            mThread.start();
            mHandler = new Handler(mThread.getLooper());
        }
    }

    private void destoryIfNeed() {
        stopExec();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mThread != null) {
            mThread.quitSafely();
            mThread = null;
        }
    }

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
        initIfNeed(context);
        // 开启定时获取
        mHandler.post(runnable);
    }

    @Override
    public void onDestroy(Context context) {
        super.onDestroy(context);
        destoryIfNeed();
    }

    @Override
    public IBinder onBind(Context context) {
        return null;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                exec();
            } catch (JMGTException e) {
                e.printStackTrace();
            }
            if (mHandler != null) {
                mHandler.postDelayed(this, INTERVAL);
            }
        }
    };


}
