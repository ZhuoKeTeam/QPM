package com.jm.android.gt.analysis;

import com.jm.android.gt.callback.IAnalysisCallback;

import java.util.List;

public class JMActivityStackAnalysis extends JMBaseAnalysis{

    public void onCollectActivityStack(List<String> activityNames){
        analysisResult.activityStackInfos.activityNames = activityNames;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_ACTIVITY_STACK_INFO, analysisResult);
    }

}
