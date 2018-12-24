package com.jm.android.gt.manager;


import com.jm.android.gt.JMGTRAnalysisResult;
import com.jm.android.gt.callback.IAnalysisCallback;

import java.util.ArrayList;

public class JMGTCallBackManager {

    private static JMGTCallBackManager instance = null;

    private final ArrayList<IAnalysisCallback> callBacks = new ArrayList<>();

    private JMGTCallBackManager() {

    }

    public static JMGTCallBackManager getInstance() {
        if (instance == null) {
            synchronized (JMGTCallBackManager.class) {
                JMGTCallBackManager temp = instance;
                if (temp == null) {
                    temp = new JMGTCallBackManager();
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

    public void refreshInfo(@IAnalysisCallback.CallbackType String type, JMGTRAnalysisResult analysisResult) {
        synchronized (callBacks) {
            for (int i = 0; i < callBacks.size(); i++) {
                IAnalysisCallback callback = callBacks.get(i);

                if (analysisResult == null) {
                    analysisResult = JMGTRAnalysisManager.getInstance().getJMGTRAnalysisResult();
                }

                callback.refreshInfo(type, analysisResult);
            }
        }
    }

}
