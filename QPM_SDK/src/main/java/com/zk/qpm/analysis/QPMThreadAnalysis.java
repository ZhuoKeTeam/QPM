package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

public class QPMThreadAnalysis extends QPMBaseAnalysis {

    public void onCollectThreadInfo(int threadCount, int gtThreadCount) {
        analysisResult.cmntInfo.threadCount = threadCount;
        analysisResult.cmntInfo.gtThreadCount = gtThreadCount;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_THREAD_INFO, analysisResult);
    }

}
