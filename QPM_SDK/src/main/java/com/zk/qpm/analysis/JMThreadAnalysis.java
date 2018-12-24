package com.jm.android.gt.analysis;

import com.jm.android.gt.callback.IAnalysisCallback;

public class JMThreadAnalysis extends JMBaseAnalysis {

    public void onCollectThreadInfo(int threadCount, int gtThreadCount) {
        analysisResult.cmntInfo.threadCount = threadCount;
        analysisResult.cmntInfo.gtThreadCount = gtThreadCount;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_THREAD_INFO, analysisResult);
    }

}
