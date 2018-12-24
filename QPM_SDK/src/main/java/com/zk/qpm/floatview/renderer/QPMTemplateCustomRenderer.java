package com.zk.qpm.floatview.renderer;

import com.zk.qpm.floatview.QPMFloatViewType;

import java.util.Map;

public abstract class QPMTemplateCustomRenderer extends BaseTemplateRenderer {

    public QPMTemplateCustomRenderer(Map<String, Object> mParam) {
        super(mParam);
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_CUSTOM;
    }

    public Map<String, Object> getParam() {
        return mParam;
    }
}
