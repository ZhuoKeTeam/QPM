package com.zk.qpm.manager;


import com.zk.qpm.QPMRAnalysisResult;
import com.zk.qpm.callback.IAnalysisCallback;

import java.util.ArrayList;

public class QPMCallBackManager {

    private static QPMCallBackManager instance = null;

    private final ArrayList<IAnalysisCallback> callBacks = new ArrayList<>();

    private QPMCallBackManager() {

    }

    public static QPMCallBackManager getInstance() {
        if (instance == null) {
            synchronized (QPMCallBackManager.class) {
                QPMCallBackManager temp = instance;
                if (temp == null) {
                    temp = new QPMCallBackManager();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    public void addCallBack(IAnalysisCallback callBack) {
        synchronized (callBacks) {
            callBacks.remove(callBack);
            callBacks.add(callBack);
        }
    }

    public void removeCallBack(IAnalysisCallback callBack) {
        synchronized (callBacks) {
            callBacks.remove(callBack);
        }
    }

    public void removeAllCallBack() {
        synchronized (callBacks) {
            callBacks.clear();
        }
    }

    public void refreshInfo(@IAnalysisCallback.CallbackType String type, QPMRAnalysisResult analysisResult) {
        synchronized (callBacks) {
            for (int i = 0; i < callBacks.size(); i++) {
                IAnalysisCallback callback = callBacks.get(i);

                if (analysisResult == null) {
                    analysisResult = QPMRAnalysisManager.getInstance().getJMGTRAnalysisResult();
                }

                callback.refreshInfo(type, analysisResult);
            }
        }
    }

}
