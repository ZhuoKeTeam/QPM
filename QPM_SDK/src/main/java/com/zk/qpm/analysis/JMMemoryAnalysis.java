package com.jm.android.gt.analysis;

import com.jm.android.gt.callback.IAnalysisCallback;

public class JMMemoryAnalysis extends JMBaseAnalysis {

    public void onCollectMemoryInfo(int memory) {
        analysisResult.cmntInfo.memory = memory * 1.0 / 1024;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_MEMORY_INFO, analysisResult);
    }

}
