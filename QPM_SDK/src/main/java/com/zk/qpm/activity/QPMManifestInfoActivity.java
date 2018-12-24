package com.zk.qpm.activity;


import com.zk.qpm.R;
import com.zk.qpm.function.ManifestFunction;
import com.zk.qpm.function.TitleFunction;

public class QPMManifestInfoActivity extends QPMFunctionBaseActivity {

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
