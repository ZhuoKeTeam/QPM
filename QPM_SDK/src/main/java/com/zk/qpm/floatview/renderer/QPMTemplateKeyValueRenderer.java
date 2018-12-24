package com.zk.qpm.floatview.renderer;

import android.view.View;
import android.widget.TextView;

import com.zk.qpm.R;
import com.zk.qpm.floatview.QPMFloatViewType;

import java.util.Map;

public class QPMTemplateKeyValueRenderer extends BaseTemplateRenderer {

    public static final String PARAM_KEY = "param_key";
    public static final String PARAM_VALUE = "param_value";

    public QPMTemplateKeyValueRenderer(Map<String, Object> mParam) {
        super(mParam);
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_VALUE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_template_keyvalue;
    }

    @Override
    protected void renderer(View mView, Map<String, Object> mParam) {
        TextView keyView = mView.findViewById(R.id.tv_key);
        TextView valueView = mView.findViewById(R.id.tv_value);

        Object key = mParam.get(PARAM_KEY);
        Object value = mParam.get(PARAM_VALUE);
        if (key != null) {
            keyView.setText(key.toString());
        }
        if (value != null) {
            valueView.setText(value.toString());
        }
    }
}
