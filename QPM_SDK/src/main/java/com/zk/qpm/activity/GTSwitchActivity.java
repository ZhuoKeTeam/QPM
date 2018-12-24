package com.jm.android.gt.activity;

import com.jm.android.gt.R;
import com.jm.android.gt.function.FloatViewSwitchFunction;
import com.jm.android.gt.function.TitleFunction;

public class GTSwitchActivity extends GTFunctionBaseActivity {

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
