package com.jm.android.gt.analysis;

import com.jm.android.gt.callback.IAnalysisCallback;

public class JMCPUAnalysis extends JMBaseAnalysis {

    public void onCollectCPUInfo(double cpuPercent) {
        analysisResult.cmntInfo.cpu = cpuPercent;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_CPU_INFO, analysisResult);
    }

}
