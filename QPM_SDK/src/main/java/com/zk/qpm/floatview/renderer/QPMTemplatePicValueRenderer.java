package com.zk.qpm.floatview.renderer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zk.qpm.R;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.utils.ImageLoadUtil;

import java.util.Map;

public class QPMTemplatePicValueRenderer extends BaseTemplateRenderer {

    public static final String PARAM_PIC_ID = "param_pic_id";
    public static final String PARAM_VALUE = "param_value";

    public QPMTemplatePicValueRenderer(Map<String, Object> mParam) {
        super(mParam);
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_PIC_VALUE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_template_picvalue;
    }

    @Override
    protected void renderer(View mView, Map<String, Object> mParam) {
        ImageView keyView = mView.findViewById(R.id.iv_key);
        TextView valueView = mView.findViewById(R.id.tv_value);

        Object picId = mParam.get(PARAM_PIC_ID);
        Object value = mParam.get(PARAM_VALUE);

        if (picId != null && picId instanceof Integer) {
            ImageLoadUtil.load(QPMManager.getInstance().getContext(), keyView, (Integer) picId);
        }
        if (value != null) {
            valueView.setText(value.toString());
        }
    }
}
