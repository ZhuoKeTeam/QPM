package com.zk.qpm.analysis;

import com.zk.qpm.QPMRAnalysisResult;
import com.zk.qpm.manager.QPMCallBackManager;
import com.zk.qpm.manager.QPMRAnalysisManager;

public abstract class QPMBaseAnalysis {

    protected QPMRAnalysisResult analysisResult = null;
    protected QPMCallBackManager callBackManager = null;

    public QPMBaseAnalysis() {
        this.analysisResult = QPMRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        this.callBackManager = QPMCallBackManager.getInstance();
    }
}
