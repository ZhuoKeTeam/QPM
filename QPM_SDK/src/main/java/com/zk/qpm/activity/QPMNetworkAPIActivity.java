package com.zk.qpm.activity;

import com.zk.qpm.R;
import com.zk.qpm.function.NetworkAPIFunction;
import com.zk.qpm.function.TitleFunction;

public class QPMNetworkAPIActivity extends QPMFunctionBaseActivity {

    @Override
    protected int contentViewId() {
        return R.layout.jm_gt_activity_network_api;
    }

    @Override
    protected void initAllFunction() {
        functions.add(new TitleFunction(this, getString(R.string.jm_gt_network_api)));
        functions.add(new NetworkAPIFunction(this));
    }
}
