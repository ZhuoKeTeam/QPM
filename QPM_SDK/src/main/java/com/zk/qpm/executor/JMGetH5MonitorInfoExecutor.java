package com.jm.android.gt.executor;

import android.content.Context;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.JMGTRAnalysisResult;
import com.jm.android.gt.analysis.JMH5MonitorAnalysis;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMGetH5MonitorRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;
import com.jm.android.gt.manager.JMGTRAnalysisManager;


public class JMGetH5MonitorInfoExecutor implements IExecutor {

    private JMH5MonitorAnalysis analysis;
    private JMGTRAnalysisResult.H5MonitorInfo h5MonitorInfo;

    public JMGetH5MonitorInfoExecutor() {
        analysis = new JMH5MonitorAnalysis();
        JMGTRAnalysisResult analysisResult = JMGTRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        h5MonitorInfo = analysisResult.h5MonitorInfo;
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_H5_MONITOR;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer h5Monitor = new JMGetH5MonitorRenderer();
        JMFloatViewManager.getInstance().addItem(h5Monitor);
    }

    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws JMGTException {
        analysis.onCollectH5MonitorInfo(h5MonitorInfo.url, h5MonitorInfo.whitePageTime);
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }
}
