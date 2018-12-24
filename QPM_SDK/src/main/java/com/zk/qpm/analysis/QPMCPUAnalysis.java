package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

public class QPMCPUAnalysis extends QPMBaseAnalysis {

    public void onCollectCPUInfo(double cpuPercent) {
        analysisResult.cmntInfo.cpu = cpuPercent;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_CPU_INFO, analysisResult);
    }

}
