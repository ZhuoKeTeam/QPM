package com.zk.qpm.floatview.renderer;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zk.qpm.QPMRAnalysisResult;
import com.zk.qpm.R;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.manager.QPMRAnalysisManager;
import com.zk.qpm.utils.DecimalFormatUtil;

public class QPMGetMemoryInfoRenderer extends BaseRenderer {

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_MEMORY_VIEW;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_keyvalue;
    }

    @Override
    protected void renderer(View mView) {
        TextView keyView = mView.findViewById(R.id.tv_key);
        TextView valueView = mView.findViewById(R.id.tv_value);
        keyView.setText(R.string.jm_gt_floatview_memory);
        QPMRAnalysisResult analysisResult = QPMRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        String data = DecimalFormatUtil.setScale(analysisResult.cmntInfo.memory, 1) + "MB";
        if (!TextUtils.isEmpty(data)) {
            valueView.setText(data);
        }
    }
}
