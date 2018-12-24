package com.jm.android.gt.activity;


import com.jm.android.gt.R;
import com.jm.android.gt.function.ManifestFunction;
import com.jm.android.gt.function.TitleFunction;

public class GTManifestInfoActivity extends GTFunctionBaseActivity {

    @Override
    protected int contentViewId() {
        return R.layout.jm_gt_activity_manifest_info;
    }

    @Override
    protected void initAllFunction() {
        functions.add(new TitleFunction(this, getString(R.string.jm_gt_manifest_info)));
        functions.add(new ManifestFunction(this));
    }

}
