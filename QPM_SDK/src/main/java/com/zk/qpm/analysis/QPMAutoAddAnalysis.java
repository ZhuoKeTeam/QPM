package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

public class QPMAutoAddAnalysis extends QPMBaseAnalysis {

    public void onCollectInfo(int pid, int threadCount) {
        analysisResult.otherInfo.pId = pid;
        analysisResult.otherInfo.threadCount = threadCount;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_AUTO_ADD_INFO, analysisResult);
    }
}
