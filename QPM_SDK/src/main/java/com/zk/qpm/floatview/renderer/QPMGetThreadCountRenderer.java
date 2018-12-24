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

public class QPMGetThreadCountRenderer extends BaseRenderer {

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_THREAD_COUNT;
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
        Context context = QPMManager.getInstance().getContext();
        QPMRAnalysisResult analysisResult = QPMRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        final StringBuilder threadSB = new StringBuilder();
        threadSB.append(context.getString(R.string.jm_gt_floatview_all_thread)).append(analysisResult.cmntInfo.threadCount).append("\n")
                .append(context.getString(R.string.jm_gt_floatview_gt_thread)).append(analysisResult.cmntInfo.gtThreadCount);

        String data = threadSB.toString();
        if (!TextUtils.isEmpty(data)) {
            valueView.setText(data);
        }
    }
}
