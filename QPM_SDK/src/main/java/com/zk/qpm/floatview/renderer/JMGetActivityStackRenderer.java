package com.jm.android.gt.floatview.renderer;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jm.android.gt.JMGTRAnalysisResult;
import com.jm.android.gt.R;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.manager.JMGTRAnalysisManager;

public class JMGetActivityStackRenderer extends BaseRenderer {

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_ACTIVITY_STACK;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_keylinevalue;
    }

    @Override
    protected void renderer(View mView) {
        TextView keyView = mView.findViewById(R.id.tv_key);
        TextView valueView = mView.findViewById(R.id.tv_value);
        keyView.setText(R.string.jm_gt_floatview_activitystack);

        final StringBuilder stringBuilder = new StringBuilder();
        JMGTRAnalysisResult analysisResult = JMGTRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        if (analysisResult.activityStackInfos.activityNames != null
                && !analysisResult.activityStackInfos.activityNames.isEmpty()) {
            for (String activityNames : analysisResult.activityStackInfos.activityNames) {
                stringBuilder.append(activityNames).append("\n");
            }
            if (stringBuilder.length() != 0) {
                stringBuilder.setLength(stringBuilder.length() - "\n".length());
            }
        }
        String data = stringBuilder.toString();
        if (!TextUtils.isEmpty(data)) {
            valueView.setText(data);
        }
    }

}
