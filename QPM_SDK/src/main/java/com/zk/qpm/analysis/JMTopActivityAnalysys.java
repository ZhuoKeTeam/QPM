package com.jm.android.gt.analysis;

import com.jm.android.gt.callback.IAnalysisCallback;

public class JMTopActivityAnalysys extends JMBaseAnalysis {

    public void onCollectTopActivityInfo(String topActivity) {
        analysisResult.activityStackInfos.topActivityName = topActivity;

        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_TOP_ACTIVITY, analysisResult);
    }

}
