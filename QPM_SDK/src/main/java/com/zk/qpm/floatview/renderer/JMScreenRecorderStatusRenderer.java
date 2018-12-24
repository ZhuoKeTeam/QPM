package com.jm.android.gt.floatview.renderer;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.android.gt.JMGTRAnalysisResult;
import com.jm.android.gt.R;
import com.jm.android.gt.activity.GTScreenRecorderActivity;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.manager.JMGTManager;
import com.jm.android.gt.manager.JMGTRAnalysisManager;
import com.jm.android.gt.manager.JMScreenRecorderManager;

public class JMScreenRecorderStatusRenderer extends BaseRenderer {

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_SCREEN_RECORDER;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_screenrecorder;
    }

    @Override
    protected void renderer(View mView) {
        TextView keyView = mView.findViewById(R.id.tv_key);
        ImageView statusView = mView.findViewById(R.id.iv_status);
        TextView recorderTimeView = mView.findViewById(R.id.tv_time);

        keyView.setText(R.string.jm_gt_floatview_screenrecorder);
        JMGTRAnalysisResult analysisResult = JMGTRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        if (analysisResult.screenRecorderStatusInfo.startStatus) {
            statusView.setImageResource(R.drawable.jm_gt_recorder_stop);
            recorderTimeView.setVisibility(View.VISIBLE);
            long time = 0L;
            if (analysisResult.screenRecorderStatusInfo.startTime > 0) {
                time = System.currentTimeMillis() - analysisResult.screenRecorderStatusInfo.startTime;
            }
            String timeStr = formatTime(time);
            recorderTimeView.setText(timeStr);
        } else {
            statusView.setImageResource(R.drawable.jm_gt_recorder_start);
            recorderTimeView.setVisibility(View.GONE);
        }

        statusView.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            JMGTRAnalysisResult analysisResult = JMGTRAnalysisManager.getInstance().getJMGTRAnalysisResult();
            if (analysisResult.screenRecorderStatusInfo.startStatus) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    JMScreenRecorderManager.getInstance().stopRecorder();
                }
            } else {
                GTScreenRecorderActivity.startRecorder();
            }
        }
    };

    private String formatTime(long time) {
        time = time / 1000;
        long h = time / 3600;
        long m = (time - h * 3600) / 60;
        long s = time - h * 3600 - m * 60;
        String hour = "";
        if (h < 10) {
            hour = "0" + h;
        } else {
            hour = "" + h;
        }
        String minutes = "";
        if (m < 10) {
            minutes = "0" + m;
        } else {
            minutes = "" + m;
        }
        String seconds = "";
        if (s < 10) {
            seconds = "0" + s;
        } else {
            seconds = "" + s;
        }
        return JMGTManager.getInstance().getContext().getString(R.string.jm_gt_recorder_time, hour, minutes, seconds);
    }
}
