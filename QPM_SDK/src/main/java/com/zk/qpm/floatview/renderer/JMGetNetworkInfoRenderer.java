package com.jm.android.gt.floatview.renderer;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jm.android.gt.JMGTRAnalysisResult;
import com.jm.android.gt.R;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.manager.JMGTRAnalysisManager;

import java.util.ArrayList;
import java.util.List;

public class JMGetNetworkInfoRenderer extends BaseRenderer {

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_NETWORK_API;
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

        JMGTRAnalysisResult analysisResult = JMGTRAnalysisManager.getInstance().getJMGTRAnalysisResult();
        final StringBuilder networkSB = new StringBuilder();
        List<JMGTRAnalysisResult.NetworkInfo> networkInfos = new ArrayList<>();
        synchronized (JMGTRAnalysisResult.SYNCHRONIZED_NETWORKINFO) {
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
        for (JMGTRAnalysisResult.NetworkInfo networkInfo : networkInfos) {
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
