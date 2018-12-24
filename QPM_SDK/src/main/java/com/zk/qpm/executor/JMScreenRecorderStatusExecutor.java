package com.jm.android.gt.executor;

import android.content.Context;
import android.os.Build;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.analysis.JMScreenRecorderStatusAnalysis;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMScreenRecorderStatusRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;
import com.jm.android.gt.manager.JMScreenRecorderManager;

public class JMScreenRecorderStatusExecutor implements IExecutor {

    private JMScreenRecorderStatusAnalysis analysis;

    public JMScreenRecorderStatusExecutor() {
        analysis = new JMScreenRecorderStatusAnalysis();
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_SCREEN_RECORDER;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer renderer = new JMScreenRecorderStatusRenderer();
        JMFloatViewManager.getInstance().addItem(renderer);
    }

    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws JMGTException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            analysis.onCollectAppInfo(JMScreenRecorderManager.getInstance().isStart(),
                    JMScreenRecorderManager.getInstance().getStartTime());
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }
}
