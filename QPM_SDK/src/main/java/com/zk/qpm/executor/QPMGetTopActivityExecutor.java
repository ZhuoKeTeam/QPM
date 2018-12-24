package com.zk.qpm.executor;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ActivityUtils;
import com.zk.qpm.QPMException;
import com.zk.qpm.analysis.QPMTopActivityAnalysis;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMGetTopActivityRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;

public class QPMGetTopActivityExecutor implements IExecutor {

    private QPMTopActivityAnalysis analysys;

    public QPMGetTopActivityExecutor() {
        analysys = new QPMTopActivityAnalysis();
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_TOP_ACTIVITY;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer topActivityBean = new QPMGetTopActivityRenderer();
        QPMFloatViewManager.getInstance().addItem(topActivityBean);
    }

    @Override
    public void destoryShowView() {
        QPMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws QPMException {
        Activity topActivity = ActivityUtils.getTopActivity();
        if (topActivity != null) {
            analysys.onCollectTopActivityInfo(topActivity.getLocalClassName());
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }
}
