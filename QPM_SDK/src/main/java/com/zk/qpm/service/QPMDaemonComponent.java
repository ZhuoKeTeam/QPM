package com.zk.qpm.service;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import com.zk.qpm.QPMException;
import com.zk.qpm.executor.QPMGetActivityStackExecutor;
import com.zk.qpm.executor.QPMGetAppInfoExecutor;
import com.zk.qpm.executor.QPMGetAutoAddExecutor;
import com.zk.qpm.executor.QPMGetCPUInfoExecutor;
import com.zk.qpm.executor.QPMGetFlowInfoExecutor;
import com.zk.qpm.executor.QPMGetH5MonitorInfoExecutor;
import com.zk.qpm.executor.QPMGetMemoryInfoExecutor;
import com.zk.qpm.executor.QPMGetNetworkInfoExecutor;
import com.zk.qpm.executor.QPMGetThreadCountExecutor;
import com.zk.qpm.executor.QPMGetTopActivityExecutor;
import com.zk.qpm.executor.QPMScreenRecorderStatusExecutor;
import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.manager.QPMThreadManager;

public class QPMDaemonComponent extends QPMBaseService {

    private static final int INTERVAL = 1000;

    private HandlerThread mThread;
    private Handler mHandler;

    public QPMDaemonComponent() {
        ActivityManager am = (ActivityManager) QPMManager.getInstance().getContext().getSystemService(Context.ACTIVITY_SERVICE);
        executors.clear();
        executors.add(new QPMGetAppInfoExecutor());
        executors.add(new QPMGetTopActivityExecutor());
        executors.add(new QPMGetAutoAddExecutor());
        executors.add(new QPMGetCPUInfoExecutor());
        executors.add(new QPMGetThreadCountExecutor());
        executors.add(new QPMGetMemoryInfoExecutor(am));
        executors.add(new QPMGetFlowInfoExecutor());
        executors.add(new QPMGetActivityStackExecutor());
        executors.add(new QPMScreenRecorderStatusExecutor());
        executors.add(new QPMGetH5MonitorInfoExecutor());
        if (QPMManager.getInstance().isImplementOkHttp()) {
            QPMGetNetworkInfoExecutor networkInfoExecutor = QPMManager.getInstance().getOkHttpInterceptor();
            networkInfoExecutor.reset();
            executors.add(networkInfoExecutor);
        }
    }

    private void initIfNeed(Context context) {
        if (mThread == null) {
            mThread = QPMThreadManager.getInstance().createHandlerThread(QPMDaemonComponent.class.getSimpleName());
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
            } catch (QPMException e) {
                e.printStackTrace();
            }
            if (mHandler != null) {
                mHandler.postDelayed(this, INTERVAL);
            }
        }
    };


}
