package com.jm.android.gt.executor;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ActivityUtils;
import com.jm.android.gt.JMGTException;
import com.jm.android.gt.analysis.JMActivityStackAnalysis;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMGetActivityStackRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;

import java.util.ArrayList;
import java.util.List;

public class JMGetActivityStackExecutor implements IExecutor {

    private JMActivityStackAnalysis activityStackAnalysis;

    public JMGetActivityStackExecutor() {
        activityStackAnalysis = new JMActivityStackAnalysis();
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_ACTIVITY_STACK;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer activityBean = new JMGetActivityStackRenderer();
        JMFloatViewManager.getInstance().addItem(activityBean);
    }


    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }


    @Override
    public void exec() throws JMGTException {
        List<String> activityNames = getRunningActivityNames();
        activityStackAnalysis.onCollectActivityStack(activityNames);
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }

    private List<String> getRunningActivityNames() {
        List<Activity> activityList = ActivityUtils.getActivityList();
        List<String> activitiNames = new ArrayList<>();
        for (Activity activity : activityList) {
            activitiNames.add(activity.getLocalClassName());
        }
        return activitiNames;
    }
}
