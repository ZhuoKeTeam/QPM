package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

public class QPMDeviceAnalysis extends QPMBaseAnalysis {

    public void onCollectDeviceInfo(String vendor, String model, String sdkName, int sdkInt) {
        analysisResult.deviceInfo.vendor = vendor;
        analysisResult.deviceInfo.model = model;
        analysisResult.deviceInfo.sdkName = sdkName;
        analysisResult.deviceInfo.sdkInt = sdkInt;
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_DEVICE_INFO, analysisResult);
    }
}
