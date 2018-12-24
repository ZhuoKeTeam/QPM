package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

import java.util.List;

public class QPMActivityStackAnalysis extends QPMBaseAnalysis{

    public void onCollectActivityStack(List<String> activityNames){
        analysisResult.activityStackInfos.activityNames = activityNames;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_ACTIVITY_STACK_INFO, analysisResult);
    }

}
