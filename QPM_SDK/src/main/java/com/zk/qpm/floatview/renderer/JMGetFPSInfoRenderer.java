package com.jm.android.gt.floatview.renderer;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jm.android.gt.JMGTRAnalysisResult;
import com.jm.android.gt.R;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.manager.JMGTManager;
import com.jm.android.gt.manager.JMGTRAnalysisManager;

public class JMGetFPSInfoRenderer extends BaseRenderer {

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_FPS_VIEW;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_fps;
    }

    @Override
    protected void renderer(View mView) {
        TextView keyView = mView.findViewById(R.id.tv_key);
        TextView valueView = mView.findViewById(R.id.tv_value);
        View statusView = mView.findViewById(R.id.v_status);
        keyView.setText(R.string.jm_gt_floatview_fps);
        Context context = JMGTManager.getInstance().getContext();
        JMGTRAnalysisResult analysisResult = JMGTRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        final JMGTRAnalysisResult.FrameInfo frameInfo = analysisResult.frameInfo;
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.jm_gt_floatview_bigblock)).append(frameInfo.bigBlockTime).append("\n")
                .append(context.getString(R.string.jm_gt_floatview_lowsm)).append(frameInfo.lowSMTime);
        String data = "" + analysisResult.frameInfo.fps + "\n" + sb.toString();
        if (!TextUtils.isEmpty(data)) {
            valueView.setText(data);
        }

        if (frameInfo.isLowSM) {
            statusView.setBackgroundResource(R.drawable.jm_gt_shape_fps_red);
        } else {
            statusView.setBackgroundResource(R.drawable.jm_gt_shape_fps_green);
        }
    }
}
