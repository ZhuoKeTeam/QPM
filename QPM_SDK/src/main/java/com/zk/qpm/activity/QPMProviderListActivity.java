package com.zk.qpm.activity;

import com.zk.qpm.R;
import com.zk.qpm.function.ProviderFunction;
import com.zk.qpm.function.TitleFunction;

public class QPMProviderListActivity extends QPMFunctionBaseActivity {

    @Override
    protected int contentViewId() {
        return R.layout.jm_gt_activity_four_module;
    }

    @Override
    protected void initAllFunction() {
        functions.add(new TitleFunction(this, getString(R.string.jm_gt_provider_list)));
        functions.add(new ProviderFunction(this));
    }

}
