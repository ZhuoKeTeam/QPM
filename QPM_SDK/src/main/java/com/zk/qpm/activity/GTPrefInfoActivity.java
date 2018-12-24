package com.jm.android.gt.activity;

import com.jm.android.gt.R;
import com.jm.android.gt.function.PrefInfoFunction;
import com.jm.android.gt.function.TitleFunction;

public class GTPrefInfoActivity extends GTFunctionBaseActivity {

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
