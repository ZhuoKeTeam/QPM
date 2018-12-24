package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

public class QPMScreenRecorderStatusAnalysis extends QPMBaseAnalysis{

    public void onCollectAppInfo(boolean startStatus, long startTime){
        analysisResult.screenRecorderStatusInfo.startStatus = startStatus;
        analysisResult.screenRecorderStatusInfo.startTime = startTime;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_SCREEN_RECORDER_STATUS_INFO, analysisResult);
    }

}
