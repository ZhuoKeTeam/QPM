package com.zk.qpm.executor;

import android.content.Context;
import android.os.Build;

import com.zk.qpm.QPMException;
import com.zk.qpm.analysis.QPMScreenRecorderStatusAnalysis;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMScreenRecorderStatusRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;
import com.zk.qpm.manager.QPMScreenRecorderManager;

public class QPMScreenRecorderStatusExecutor implements IExecutor {

    private QPMScreenRecorderStatusAnalysis analysis;

    public QPMScreenRecorderStatusExecutor() {
        analysis = new QPMScreenRecorderStatusAnalysis();
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_SCREEN_RECORDER;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer renderer = new QPMScreenRecorderStatusRenderer();
        QPMFloatViewManager.getInstance().addItem(renderer);
    }

    @Override
    public void destoryShowView() {
        QPMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws QPMException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            analysis.onCollectAppInfo(QPMScreenRecorderManager.getInstance().isStart(),
                    QPMScreenRecorderManager.getInstance().getStartTime());
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }
}
