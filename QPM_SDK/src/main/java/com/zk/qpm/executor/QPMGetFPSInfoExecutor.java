package com.zk.qpm.executor;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Choreographer;

import com.zk.qpm.QPMException;
import com.zk.qpm.analysis.QPMFPSAnalysis;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMGetFPSInfoRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;

public class QPMGetFPSInfoExecutor implements IExecutor {

    private QPMFPSAnalysis fpsAnalysis;
    private boolean isStop;

    public QPMGetFPSInfoExecutor() {
        fpsAnalysis = new QPMFPSAnalysis();
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_FPS_VIEW;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer fpsBean = new QPMGetFPSInfoRenderer();
        QPMFloatViewManager.getInstance().addItem(fpsBean);
    }

    @Override
    public void destoryShowView() {
        QPMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws QPMException {
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
