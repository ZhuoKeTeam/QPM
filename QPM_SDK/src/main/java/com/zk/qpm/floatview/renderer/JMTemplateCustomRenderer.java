package com.jm.android.gt.floatview.renderer;

import com.jm.android.gt.floatview.JMFloatViewType;

import java.util.Map;

public abstract class JMTemplateCustomRenderer extends BaseTemplateRenderer {

    public JMTemplateCustomRenderer(Map<String, Object> mParam) {
        super(mParam);
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_CUSTOM;
    }

    public Map<String, Object> getParam() {
        return mParam;
    }
}
