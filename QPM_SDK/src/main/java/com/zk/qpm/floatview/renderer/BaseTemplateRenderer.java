package com.zk.qpm.floatview.renderer;

import android.view.LayoutInflater;
import android.view.View;

import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.manager.QPMSwitchManager;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseTemplateRenderer implements IFloatViewRenderer {

    protected View mView;
    protected Map<String, Object> mParam;

    public BaseTemplateRenderer(Map<String, Object> mParam) {
        this.mParam = mParam;
        if (this.mParam == null) {
            this.mParam = new HashMap<>();
        }
    }

    public void update(Map<String, Object> param) {
        mParam.clear();
        if (param != null && !param.isEmpty()) {
            mParam.putAll(param);
        }
    }

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
        renderer(mView, mParam);
    }

    protected abstract int getLayoutId();

    protected abstract void renderer(View mView, Map<String, Object> mParam);
}
