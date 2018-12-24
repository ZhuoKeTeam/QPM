package com.jm.android.gt.analysis;

import com.jm.android.gt.callback.IAnalysisCallback;

public class JMAutoAddAnalysis extends JMBaseAnalysis {

    public void onCollectInfo(int pid, int threadCount) {
        analysisResult.otherInfo.pId = pid;
        analysisResult.otherInfo.threadCount = threadCount;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_AUTO_ADD_INFO, analysisResult);
    }
}
