package com.jm.android.gt.analysis;

import com.jm.android.gt.callback.IAnalysisCallback;

public class JMAppAnalysis extends JMBaseAnalysis {

    public void onCollectAppInfo(String packageName, String appName,
                                 String versionName, int versionCode,
                                 int gtrVersionCode, long startTestTime, int mainThreadId) {
        analysisResult.appInfo.packageName = packageName;
        analysisResult.appInfo.appName = appName;
        analysisResult.appInfo.versionName = versionName;
        analysisResult.appInfo.versionCode = versionCode;
        analysisResult.appInfo.gtrVersionCode = gtrVersionCode;
        analysisResult.appInfo.startTestTime = startTestTime;
        analysisResult.appInfo.mainThreadId = mainThreadId;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_APP_INFO, analysisResult);
    }

}