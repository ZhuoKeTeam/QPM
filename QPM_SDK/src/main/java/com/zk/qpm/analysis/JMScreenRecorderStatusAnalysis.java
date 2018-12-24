package com.jm.android.gt.analysis;

import com.jm.android.gt.callback.IAnalysisCallback;

public class JMScreenRecorderStatusAnalysis extends JMBaseAnalysis{

    public void onCollectAppInfo(boolean startStatus, long startTime){
        analysisResult.screenRecorderStatusInfo.startStatus = startStatus;
        analysisResult.screenRecorderStatusInfo.startTime = startTime;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_SCREEN_RECORDER_STATUS_INFO, analysisResult);
    }

}
