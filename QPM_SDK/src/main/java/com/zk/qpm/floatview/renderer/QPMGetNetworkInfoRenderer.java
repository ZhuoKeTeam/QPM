package com.zk.qpm.floatview.renderer;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zk.qpm.QPMRAnalysisResult;
import com.zk.qpm.R;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.manager.QPMRAnalysisManager;

import java.util.ArrayList;
import java.util.List;

public class QPMGetNetworkInfoRenderer extends BaseRenderer {

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_NETWORK_API;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_keylinevalue;
    }

    @Override
    protected void renderer(View mView) {
        TextView keyView = mView.findViewById(R.id.tv_key);
        TextView valueView = mView.findViewById(R.id.tv_value);
        keyView.setText(R.string.jm_gt_floatview_api);

        QPMRAnalysisResult analysisResult = QPMRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        final StringBuilder networkSB = new StringBuilder();
        List<QPMRAnalysisResult.NetworkInfo> networkInfos = new ArrayList<>();
        synchronized (QPMRAnalysisResult.SYNCHRONIZED_NETWORKINFO) {
            if (!analysisResult.networkInfos.isEmpty()) {
                for (int i = analysisResult.networkInfos.size() - 1; i >= 0; i--) {
                    networkInfos.add(analysisResult.networkInfos.get(i));
                    if (networkInfos.size() >= 3) {
                        break;
                    }
                }
            }
        }
        if (networkInfos.isEmpty()) {
            return;
        }
        for (QPMRAnalysisResult.NetworkInfo networkInfo : networkInfos) {
            try {
                networkSB.append(Uri.parse(networkInfo.originUrl).getPath()).append("\n");
            } catch (Exception e) {
                networkSB.append(networkInfo.originUrl).append("\n");
            }
        }
        if (networkSB.length() != 0) {
            networkSB.setLength(networkSB.length() - "\n".length());
        }
        String data = networkSB.toString();

        if (!TextUtils.isEmpty(data)) {
            valueView.setText(data);
        }
    }
}
