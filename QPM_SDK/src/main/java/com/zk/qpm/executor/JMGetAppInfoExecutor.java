package com.jm.android.gt.executor;

import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.jm.android.gt.BuildConfig;
import com.jm.android.gt.JMGTException;
import com.jm.android.gt.analysis.JMAppAnalysis;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMGetAppInfoRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;

public class JMGetAppInfoExecutor implements IExecutor {

    private JMAppAnalysis jmAppAnalysis;

    public JMGetAppInfoExecutor() {
        this.jmAppAnalysis = new JMAppAnalysis();
    }


    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_PACKAGE;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer packageBean = new JMGetAppInfoRenderer();
        JMFloatViewManager.getInstance().addItem(packageBean);
    }

    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws JMGTException {
        jmAppAnalysis.onCollectAppInfo(AppUtils.getAppPackageName(),
                AppUtils.getAppName(),
                AppUtils.getAppVersionName(),
                AppUtils.getAppVersionCode(),
                BuildConfig.VERSION_CODE,
                System.currentTimeMillis(),
                0);
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }

}
