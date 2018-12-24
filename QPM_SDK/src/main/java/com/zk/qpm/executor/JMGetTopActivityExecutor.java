package com.jm.android.gt.executor;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ActivityUtils;
import com.jm.android.gt.JMGTException;
import com.jm.android.gt.analysis.JMTopActivityAnalysys;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMGetTopActivityRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;

public class JMGetTopActivityExecutor implements IExecutor {

    private JMTopActivityAnalysys analysys;

    public JMGetTopActivityExecutor() {
        analysys = new JMTopActivityAnalysys();
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_TOP_ACTIVITY;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer topActivityBean = new JMGetTopActivityRenderer();
        JMFloatViewManager.getInstance().addItem(topActivityBean);
    }

    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws JMGTException {
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
