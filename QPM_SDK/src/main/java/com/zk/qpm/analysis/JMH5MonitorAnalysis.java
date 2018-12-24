package com.jm.android.gt.analysis;

import com.jm.android.gt.callback.IAnalysisCallback;

public class JMH5MonitorAnalysis extends JMBaseAnalysis {

    public void onCollectH5MonitorInfo(String url, long whitePageTime) {
        analysisResult.h5MonitorInfo.url = url;
        analysisResult.h5MonitorInfo.whitePageTime = whitePageTime;

        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_H5_MONITOR_INFO, analysisResult);
    }

}
