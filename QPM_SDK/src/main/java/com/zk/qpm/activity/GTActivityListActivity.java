package com.jm.android.gt.activity;


import com.jm.android.gt.R;
import com.jm.android.gt.function.ActivityFunction;
import com.jm.android.gt.function.TitleFunction;

public class GTActivityListActivity extends GTFunctionBaseActivity {

    @Override
    protected int contentViewId() {
        return R.layout.jm_gt_activity_four_module;
    }

    @Override
    protected void initAllFunction() {
        functions.add(new TitleFunction(this, getString(R.string.jm_gt_activity_list)));
        functions.add(new ActivityFunction(this));
    }

}
