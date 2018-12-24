package com.jm.android.gt.analysis;

import com.jm.android.gt.callback.IAnalysisCallback;

public class JMDeviceAnalysis extends JMBaseAnalysis {

    public void onCollectDeviceInfo(String vendor, String model, String sdkName, int sdkInt) {
        analysisResult.deviceInfo.vendor = vendor;
        analysisResult.deviceInfo.model = model;
        analysisResult.deviceInfo.sdkName = sdkName;
        analysisResult.deviceInfo.sdkInt = sdkInt;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_DEVICE_INFO, analysisResult);
    }
}
