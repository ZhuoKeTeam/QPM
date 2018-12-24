package com.zk.qpm.floatview.renderer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zk.qpm.R;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.utils.ImageLoadUtil;

import java.util.Map;

public class QPMTemplateKeyPicRenderer extends BaseTemplateRenderer {

    public static final String PARAM_KEY = "param_key";
    public static final String PARAM_PIC_ID = "param_pic_id";
    public static final String PARAM_PIC_URL = "param_pic_url";

    public QPMTemplateKeyPicRenderer(Map<String, Object> mParam) {
        super(mParam);
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_PIC;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_template_keypic;
    }

    @Override
    protected void renderer(View mView, Map<String, Object> mParam) {
        TextView keyView = mView.findViewById(R.id.tv_key);
        ImageView valueView = mView.findViewById(R.id.iv_value);

        Object key = mParam.get(PARAM_KEY);
        Object picId = mParam.get(PARAM_PIC_ID);
        Object picUrl = mParam.get(PARAM_PIC_URL);

        // TODO: 2018/12/20 Pic 使用本地图片资源，暂时去除使用网络资源

        if (key != null) {
            keyView.setText(key.toString());
        }
        if (picId != null && picId instanceof Integer) {
            ImageLoadUtil.load(QPMManager.getInstance().getContext(), valueView, (Integer) picId);
        }
//        if (picUrl != null) {
//            ImageLoadUtil.load(QPMManager.getInstance().getContext(), valueView, picUrl.toString());
//        }
    }
}
