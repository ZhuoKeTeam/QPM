package com.jm.android.gt.activity;


import com.jm.android.gt.R;
import com.jm.android.gt.function.ReceiverFunction;
import com.jm.android.gt.function.TitleFunction;

public class GTReceiverListActivity extends GTFunctionBaseActivity {

    @Override
    protected int contentViewId() {
        return R.layout.jm_gt_activity_four_module;
    }

    @Override
    protected void initAllFunction() {
        functions.add(new TitleFunction(this, getString(R.string.jm_gt_receiver_list)));
        functions.add(new ReceiverFunction(this));
    }

}
