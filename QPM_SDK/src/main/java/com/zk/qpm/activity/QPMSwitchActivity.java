package com.zk.qpm.activity;

import com.zk.qpm.R;
import com.zk.qpm.function.FloatViewSwitchFunction;
import com.zk.qpm.function.TitleFunction;

public class QPMSwitchActivity extends QPMFunctionBaseActivity {

    @Override
    protected int contentViewId() {
        return R.layout.jm_gt_activity_switch;
    }

    @Override
    protected void initAllFunction() {
        functions.add(new TitleFunction(this, getString(R.string.jm_gt_floatview_switch)));
        functions.add(new FloatViewSwitchFunction(this));
    }
}
