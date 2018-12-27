package com.zk.qpm.executor;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ActivityUtils;
import com.zk.qpm.QPMException;
import com.zk.qpm.analysis.QPMActivityStackAnalysis;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMGetActivityStackRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;

import java.util.ArrayList;
import java.util.List;

public class QPMGetActivityStackExecutor implements IExecutor {

    private QPMActivityStackAnalysis activityStackAnalysis;

    public QPMGetActivityStackExecutor() {
        activityStackAnalysis = new QPMActivityStackAnalysis();
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_ACTIVITY_STACK;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer activityBean = new QPMGetActivityStackRenderer();
        QPMFloatViewManager.getInstance().addItem(activityBean);
    }


    @Override
    public void destoryShowView() {
        QPMFloatViewManager.getInstance().removeItem(type());
    }


    @Override
    public void exec() throws QPMException {
        List<String> activityNames = getRunningActivityNames();
        activityStackAnalysis.onCollectActivityStack(activityNames);
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
