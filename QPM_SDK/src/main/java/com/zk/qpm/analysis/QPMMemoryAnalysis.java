package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

public class QPMMemoryAnalysis extends QPMBaseAnalysis {

    public void onCollectMemoryInfo(int memory) {
        analysisResult.cmntInfo.memory = memory * 1.0 / 1024;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_MEMORY_INFO, analysisResult);
    }

}
