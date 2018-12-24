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
import com.jm.android.gt.utils.DecimalFormatUtil;

public class JMGetFlowInfoRenderer extends BaseRenderer {

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_FLOW_DATA;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_keylinevalue;
    }

    @Override
    protected void renderer(View mView) {
        TextView keyView = mView.findViewById(R.id.tv_key);
        TextView valueView = mView.findViewById(R.id.tv_value);
        keyView.setText(R.string.jm_gt_floatview_flow);
        Context context = JMGTManager.getInstance().getContext();
        JMGTRAnalysisResult analysisResult = JMGTRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        final StringBuilder flowSB = new StringBuilder();
        flowSB.append(context.getString(R.string.jm_gt_upload_flow))
                .append(DecimalFormatUtil.setScale(analysisResult.cmntInfo.flowUp, 1))
                .append(context.getString(R.string.jm_gt_flow_unit)).append("\n")

                .append(context.getString(R.string.jm_gt_upload_speed))
                .append(DecimalFormatUtil.setScale(analysisResult.cmntInfo.flowUpSpeed, 1))
                .append(context.getString(R.string.jm_gt_speed_unit)).append("\n")

                .append(context.getString(R.string.jm_gt_download_flow))
                .append(DecimalFormatUtil.setScale(analysisResult.cmntInfo.flowDown, 1))
                .append(context.getString(R.string.jm_gt_flow_unit)).append("\n")

                .append(context.getString(R.string.jm_gt_download_speed))
                .append(DecimalFormatUtil.setScale(analysisResult.cmntInfo.flowDownSpeed, 1))
                .append(context.getString(R.string.jm_gt_speed_unit));

        String data = flowSB.toString();
        if (!TextUtils.isEmpty(data)) {
            valueView.setText(data);
        }
    }
}
