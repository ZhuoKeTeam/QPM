package com.zk.qpm.function;

import android.app.Activity;
import android.view.View;
import android.view.ViewStub;

import com.zk.qpm.R;


public class ProviderFunction extends FourModuleFunction {

    public ProviderFunction(Activity mContext) {
        super(mContext);
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
        return "provider";
    }

    @Override
    protected void onItemClick(View view, int position, Item data) {

    }

}
