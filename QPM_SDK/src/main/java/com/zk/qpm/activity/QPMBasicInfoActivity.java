package com.zk.qpm.activity;


import com.zk.qpm.R;
import com.zk.qpm.function.BasicInfoFunction;
import com.zk.qpm.function.TitleFunction;

public class QPMBasicInfoActivity extends QPMFunctionBaseActivity {

    @Override
    protected int contentViewId() {
        return R.layout.jm_gt_activity_basic_info;
    }

    @Override
    protected void initAllFunction() {
        functions.add(new TitleFunction(this, getString(R.string.jm_gt_basic_info)));
        functions.add(new BasicInfoFunction(this));
    }
}
