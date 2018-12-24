package com.zk.qpm.floatview.renderer;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zk.qpm.QPMRAnalysisResult;
import com.zk.qpm.R;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.manager.QPMRAnalysisManager;
import com.zk.qpm.utils.DecimalFormatUtil;


public class QPMGetFlowInfoRenderer extends BaseRenderer {

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_FLOW_DATA;
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
        Context context = QPMManager.getInstance().getContext();
        QPMRAnalysisResult analysisResult = QPMRAnalysisManager.getInstance().getJMGTRAnalysisResult();
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
