package com.jm.android.gt.floatview.renderer;

import android.view.LayoutInflater;
import android.view.View;

import com.jm.android.gt.manager.JMGTManager;
import com.jm.android.gt.manager.JMGTSwitchManager;

public abstract class BaseRenderer implements IFloatViewRenderer {

    protected View mView;

    @Override
    public boolean isShow() {
        return JMGTSwitchManager.getInstance().isSwitchOpen(JMGTManager.getInstance().getContext(), type());
    }

    @Override
    public View getView() {
        if (mView == null) {
            LayoutInflater inflater = LayoutInflater.from(JMGTManager.getInstance().getContext());
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
