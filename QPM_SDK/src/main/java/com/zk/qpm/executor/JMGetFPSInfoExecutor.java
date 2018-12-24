package com.jm.android.gt.executor;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Choreographer;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.analysis.JMFPSAnalysis;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMGetFPSInfoRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;

public class JMGetFPSInfoExecutor implements IExecutor {

    private JMFPSAnalysis fpsAnalysis;
    private boolean isStop;

    public JMGetFPSInfoExecutor() {
        fpsAnalysis = new JMFPSAnalysis();
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_FPS_VIEW;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer fpsBean = new JMGetFPSInfoRenderer();
        JMFloatViewManager.getInstance().addItem(fpsBean);
    }

    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws JMGTException {
        Choreographer.getInstance().postFrameCallback(frameCallback);
    }

    @Override
    public void reset() {
        isStop = false;
    }

    @Override
    public void stop() {
        isStop = true;
    }

    private Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void doFrame(long frameTimeNanos) {
            long nowTime = System.currentTimeMillis();
            fpsAnalysis.onCollectFrameInfo(nowTime);

            if (!isStop) {
                Choreographer.getInstance().postFrameCallback(this);
            }
        }
    };
}
