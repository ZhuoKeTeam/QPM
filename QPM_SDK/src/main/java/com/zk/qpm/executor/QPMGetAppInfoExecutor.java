package com.zk.qpm.executor;

import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.zk.qpm.BuildConfig;
import com.zk.qpm.QPMException;
import com.zk.qpm.analysis.QPMAppAnalysis;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMGetAppInfoRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;

public class QPMGetAppInfoExecutor implements IExecutor {

    private QPMAppAnalysis jmAppAnalysis;

    public QPMGetAppInfoExecutor() {
        this.jmAppAnalysis = new QPMAppAnalysis();
    }


    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_PACKAGE;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer packageBean = new QPMGetAppInfoRenderer();
        QPMFloatViewManager.getInstance().addItem(packageBean);
    }

    @Override
    public void destoryShowView() {
        QPMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws QPMException {
        jmAppAnalysis.onCollectAppInfo(AppUtils.getAppPackageName(),
                AppUtils.getAppName(),
                AppUtils.getAppVersionName(),
                AppUtils.getAppVersionCode(),
                10005,
                System.currentTimeMillis(),
                0);
    }

    @Override
    public void stop() {

    }

}
