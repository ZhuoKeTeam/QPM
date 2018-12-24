package com.jm.android.gt.floatview.renderer;

import android.view.View;
import android.widget.TextView;

import com.jm.android.gt.R;
import com.jm.android.gt.floatview.JMFloatViewType;

import java.util.Map;

public class JMTemplateKeyValueRenderer extends BaseTemplateRenderer {

    public static final String PARAM_KEY = "param_key";
    public static final String PARAM_VALUE = "param_value";

    public JMTemplateKeyValueRenderer(Map<String, Object> mParam) {
        super(mParam);
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_VALUE;
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
