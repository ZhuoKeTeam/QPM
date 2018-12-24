package com.zk.qpm.floatview.renderer;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zk.qpm.QPMRAnalysisResult;
import com.zk.qpm.R;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.manager.QPMRAnalysisManager;


public class QPMGetActivityStackRenderer extends BaseRenderer {

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_ACTIVITY_STACK;
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
        QPMRAnalysisResult analysisResult = QPMRAnalysisManager.getInstance().getJMGTRAnalysisResult();
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
