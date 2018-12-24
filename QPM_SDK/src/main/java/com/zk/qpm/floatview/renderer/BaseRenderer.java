package com.zk.qpm.floatview.renderer;

import android.view.LayoutInflater;
import android.view.View;

import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.manager.QPMSwitchManager;

public abstract class BaseRenderer implements IFloatViewRenderer {

    protected View mView;

    @Override
    public boolean isShow() {
        return QPMSwitchManager.getInstance().isSwitchOpen(QPMManager.getInstance().getContext(), type());
    }

    @Override
    public View getView() {
        if (mView == null) {
            LayoutInflater inflater = LayoutInflater.from(QPMManager.getInstance().getContext());
            mView = inflater.inflate(getLayoutId(), null, false);
        }
        return mView;
    }

    @Override
    public void renderer() {
        if (mView == null) {
            return;
        }
        renderer(mView);
    }

    protected abstract int getLayoutId();

    protected abstract void renderer(View mView);

}
