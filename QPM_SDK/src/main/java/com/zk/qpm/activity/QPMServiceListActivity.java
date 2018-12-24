package com.zk.qpm.activity;


import com.zk.qpm.R;
import com.zk.qpm.function.ServiceFunction;
import com.zk.qpm.function.TitleFunction;

public class QPMServiceListActivity extends QPMFunctionBaseActivity {

    @Override
    protected int contentViewId() {
        return R.layout.jm_gt_activity_four_module;
    }

    @Override
    protected void initAllFunction() {
        functions.add(new TitleFunction(this, getString(R.string.jm_gt_service_list)));
        functions.add(new ServiceFunction(this));
    }

}
