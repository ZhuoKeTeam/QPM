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

public class JMGetThreadCountRenderer extends BaseRenderer {

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_THREAD_COUNT;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_keylinevalue;
    }

    @Override
    protected void renderer(View mView) {
        TextView keyView = mView.findViewById(R.id.tv_key);
        TextView valueView = mView.findViewById(R.id.tv_value);
        keyView.setText(R.string.jm_gt_floatview_threadcount);
        Context context = JMGTManager.getInstance().getContext();
        JMGTRAnalysisResult analysisResult = JMGTRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        final StringBuilder threadSB = new StringBuilder();
        threadSB.append(context.getString(R.string.jm_gt_floatview_all_thread)).append(analysisResult.cmntInfo.threadCount).append("\n")
                .append(context.getString(R.string.jm_gt_floatview_gt_thread)).append(analysisResult.cmntInfo.gtThreadCount);

        String data = threadSB.toString();
        if (!TextUtils.isEmpty(data)) {
            valueView.setText(data);
        }
    }
}
