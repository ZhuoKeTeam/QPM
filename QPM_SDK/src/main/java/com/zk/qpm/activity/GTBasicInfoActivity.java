package com.jm.android.gt.activity;


import com.jm.android.gt.R;
import com.jm.android.gt.function.BasicInfoFunction;
import com.jm.android.gt.function.TitleFunction;

public class GTBasicInfoActivity extends GTFunctionBaseActivity {

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
