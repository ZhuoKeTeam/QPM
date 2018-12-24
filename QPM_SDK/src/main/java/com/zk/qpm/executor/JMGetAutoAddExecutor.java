package com.jm.android.gt.executor;

import android.content.Context;
import android.os.Process;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.analysis.JMAutoAddAnalysis;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMGetAutoAddRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;

public class JMGetAutoAddExecutor implements IExecutor {

    private JMAutoAddAnalysis analysis;
    private int count = 1;

    public JMGetAutoAddExecutor() {
        analysis = new JMAutoAddAnalysis();
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_THREAD_AUTO_ADD;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer autoAddBean = new JMGetAutoAddRenderer();
        JMFloatViewManager.getInstance().addItem(autoAddBean);
    }

    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws JMGTException {
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
