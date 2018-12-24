package com.jm.android.gt.analysis;

import com.jm.android.gt.JMGTRAnalysisResult;
import com.jm.android.gt.callback.IAnalysisCallback;

public class JMNetworkAnalysis extends JMBaseAnalysis {

    private static final int MAX_CAPACITY = 300;

    public void onCollectNetworkInfo(String originUrl,
                                     String method,
                                     long requestSize,
                                     long requestTime,
                                     String requestHeader,
                                     String requestContent,
                                     long responseSize,
                                     String responseHeader,
                                     String responseContent,
                                     String responseCode,
                                     long currentTime) {
        synchronized (JMGTRAnalysisResult.SYNCHRONIZED_NETWORKINFO) {
            JMGTRAnalysisResult.NetworkInfo networkInfo = new JMGTRAnalysisResult.NetworkInfo();
            networkInfo.originUrl = originUrl;
            networkInfo.method = method;
            networkInfo.requestSize = requestSize;
            networkInfo.requestTime = requestTime;
            networkInfo.requestHeader = requestHeader;
            networkInfo.requestContent = requestContent;
            networkInfo.responseSize = responseSize;
            networkInfo.responseHeader = responseHeader;
            networkInfo.responseContent = responseContent;
            networkInfo.responseCode = responseCode;
            networkInfo.currentTime = currentTime;
            analysisResult.networkInfos.add(networkInfo);
            // 大于阈值，删除
            while (analysisResult.networkInfos.size() > MAX_CAPACITY) {
                analysisResult.networkInfos.remove(0);
            }
        }
        callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_NETWORK_INFO, analysisResult);
    }

}
