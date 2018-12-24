package com.jm.android.gt.activity;

import com.jm.android.gt.R;
import com.jm.android.gt.function.NetworkAPIFunction;
import com.jm.android.gt.function.TitleFunction;

public class GTNetworkAPIActivity extends GTFunctionBaseActivity {

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
