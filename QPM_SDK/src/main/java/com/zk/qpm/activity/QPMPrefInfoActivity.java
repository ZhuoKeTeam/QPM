package com.zk.qpm.activity;

import com.zk.qpm.R;
import com.zk.qpm.function.PrefInfoFunction;
import com.zk.qpm.function.TitleFunction;

public class QPMPrefInfoActivity extends QPMFunctionBaseActivity {

    @Override
    protected int contentViewId() {
        return R.layout.jm_gt_activity_pref_info;
    }

    @Override
    protected void initAllFunction() {
        TitleFunction titleFunction = new TitleFunction(this, getString(R.string.jm_gt_pref_info));
        functions.add(titleFunction);
        functions.add(new PrefInfoFunction(this, titleFunction));
    }
}
