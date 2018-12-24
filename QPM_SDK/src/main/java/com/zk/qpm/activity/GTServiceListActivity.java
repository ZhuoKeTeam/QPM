package com.jm.android.gt.activity;


import com.jm.android.gt.R;
import com.jm.android.gt.function.ServiceFunction;
import com.jm.android.gt.function.TitleFunction;

public class GTServiceListActivity extends GTFunctionBaseActivity {

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
