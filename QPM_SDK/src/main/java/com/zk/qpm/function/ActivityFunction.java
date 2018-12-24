package com.zk.qpm.function;

import android.app.Activity;
import android.app.FragmentManager;
import android.view.View;
import android.view.ViewStub;

import com.zk.qpm.R;
import com.zk.qpm.dialog.JumpActivityWithParamDialog;


public class ActivityFunction extends FourModuleFunction {

    private FragmentManager mFragmentManager;

    public ActivityFunction(Activity mContext) {
        super(mContext);
        this.mFragmentManager = mContext.getFragmentManager();
    }

    @Override
    public int viewStubId() {
        return R.id.vs_module;
    }

    @Override
    public ViewStub.OnInflateListener getCallback() {
        return null;
    }

    @Override
    protected String moduleName() {
        return "activity";
    }

    @Override
    protected void onItemClick(View view, int position, Item data) {
        if (data.xmlInfo.getAttributeMap().containsKey("name")) {
            JumpActivityWithParamDialog.showDialog(mFragmentManager, data.xmlInfo.getAttributeMap().get("name"));
        }
    }

}
