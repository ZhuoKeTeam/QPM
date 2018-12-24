package com.zk.qpm.executor;

import android.content.Context;
import android.os.Process;

import com.zk.qpm.QPMException;
import com.zk.qpm.analysis.QPMAutoAddAnalysis;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMGetAutoAddRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;

public class QPMGetAutoAddExecutor implements IExecutor {

    private QPMAutoAddAnalysis analysis;
    private int count = 1;

    public QPMGetAutoAddExecutor() {
        analysis = new QPMAutoAddAnalysis();
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_THREAD_AUTO_ADD;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer autoAddBean = new QPMGetAutoAddRenderer();
        QPMFloatViewManager.getInstance().addItem(autoAddBean);
    }

    @Override
    public void destoryShowView() {
        QPMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws QPMException {
        int pid = Process.myPid();
        analysis.onCollectInfo(pid, ++count);
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }
}
