package com.jm.android.gt.analysis;

import com.jm.android.gt.manager.JMGTCallBackManager;
import com.jm.android.gt.manager.JMGTRAnalysisManager;
import com.jm.android.gt.JMGTRAnalysisResult;

public abstract class JMBaseAnalysis {

    protected JMGTRAnalysisResult analysisResult = null;
    protected JMGTCallBackManager callBackManager = null;

    public JMBaseAnalysis() {
        this.analysisResult = JMGTRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        this.callBackManager = JMGTCallBackManager.getInstance();
    }
}
