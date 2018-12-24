package com.zk.qpm.analysis;


import com.zk.qpm.callback.IAnalysisCallback;

public class QPMFPSAnalysis extends QPMBaseAnalysis {

    private static final int ONE_SECOND = 1000;
    private static final int FPS_LOW_LIMIT = 40;
    private static final int BIG_BLOCK_LIMIT = 70;

    private long lastFrameTime = -1;
    private long lastNotifyTime = -1;

    public void onCollectFrameInfo(long nowTime) {
        analysisResult.frameInfo.drawTimeInSecond.add(nowTime);
        while (!analysisResult.frameInfo.drawTimeInSecond.isEmpty() &&
                analysisResult.frameInfo.drawTimeInSecond.get(0) < nowTime - ONE_SECOND) {
            analysisResult.frameInfo.drawTimeInSecond.remove(0);
        }

        analysisResult.frameInfo.fps = analysisResult.frameInfo.drawTimeInSecond.size();
        if (nowTime - lastNotifyTime >= ONE_SECOND) {
            callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_FPS_INFO, analysisResult);
            lastNotifyTime = nowTime;
        }

        //检测大卡顿：
        if (lastFrameTime != -1) {
            if (nowTime - lastFrameTime > BIG_BLOCK_LIMIT) {
                analysisResult.frameInfo.bigBlockTime++;
                callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_BIG_BLOCK_INFO, analysisResult);
            }
        }
        lastFrameTime = nowTime;

        //检测低流畅值区间：
        if (analysisResult.frameInfo.fps < FPS_LOW_LIMIT) {
            if (!analysisResult.frameInfo.isLowSM) {
                analysisResult.frameInfo.isLowSM = true;
                callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_LOW_SM_INFO, analysisResult);
            }
        } else {
            if (analysisResult.frameInfo.isLowSM) {
                analysisResult.frameInfo.lowSMTime++;
                analysisResult.frameInfo.isLowSM = false;
                callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_LOW_SM_INFO, analysisResult);
            }
        }
    }

}
