package com.zk.qpm.executor;

import android.content.Context;

import com.zk.qpm.QPMException;
import com.zk.qpm.QPMRAnalysisResult;
import com.zk.qpm.analysis.QPMH5MonitorAnalysis;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMGetH5MonitorRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;
import com.zk.qpm.manager.QPMRAnalysisManager;


public class QPMGetH5MonitorInfoExecutor implements IExecutor {

    private QPMH5MonitorAnalysis analysis;
    private QPMRAnalysisResult.H5MonitorInfo h5MonitorInfo;

    public QPMGetH5MonitorInfoExecutor() {
        analysis = new QPMH5MonitorAnalysis();
        QPMRAnalysisResult analysisResult = QPMRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        h5MonitorInfo = analysisResult.h5MonitorInfo;
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_H5_MONITOR;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer h5Monitor = new QPMGetH5MonitorRenderer();
        QPMFloatViewManager.getInstance().addItem(h5Monitor);
    }

    @Override
    public void destoryShowView() {
        QPMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws QPMException {
        analysis.onCollectH5MonitorInfo(h5MonitorInfo.url, h5MonitorInfo.whitePageTime);
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }
}
