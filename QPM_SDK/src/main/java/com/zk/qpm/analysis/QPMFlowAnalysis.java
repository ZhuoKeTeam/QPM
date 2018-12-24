package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

public class QPMFlowAnalysis extends QPMBaseAnalysis {

    private long lastTime = 0;
    private long lastFlowUpload = 0;
    private long lastFlowDownload = 0;

    public void onCollectFlowInfo(long time, long flowUp, long flowDown) {
        if (lastTime != 0) {
            analysisResult.cmntInfo.flowUp += (flowUp - lastFlowUpload) * 1.0 / 1024;
            analysisResult.cmntInfo.flowDown += (flowDown - lastFlowDownload) * 1.0 / 1024;
            analysisResult.cmntInfo.flowUpSpeed = (flowUp - lastFlowUpload) * 1000.0 / 1024 / (time - lastTime);
            analysisResult.cmntInfo.flowDownSpeed = (flowDown - lastFlowDownload) * 1000.0 / 1024 / (time - lastTime);
        }

        lastTime = time;
        lastFlowUpload = flowUp;
        lastFlowDownload = flowDown;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_FLOW_INFO, analysisResult);
    }

}
