package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

public class QPMTopActivityAnalysis extends QPMBaseAnalysis {

    public void onCollectTopActivityInfo(String topActivity) {
        analysisResult.activityStackInfos.topActivityName = topActivity;

        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_TOP_ACTIVITY, analysisResult);
    }

}
