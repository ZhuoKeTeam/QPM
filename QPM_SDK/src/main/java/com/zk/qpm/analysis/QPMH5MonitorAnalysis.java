package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

public class QPMH5MonitorAnalysis extends QPMBaseAnalysis {

    public void onCollectH5MonitorInfo(String url, long whitePageTime) {
        analysisResult.h5MonitorInfo.url = url;
        analysisResult.h5MonitorInfo.whitePageTime = whitePageTime;

        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_H5_MONITOR_INFO, analysisResult);
    }

}
