package com.zk.qpm.activity;

import com.zk.qpm.R;
import com.zk.qpm.function.ReceiverFunction;
import com.zk.qpm.function.TitleFunction;

public class QPMReceiverListActivity extends QPMFunctionBaseActivity {

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
