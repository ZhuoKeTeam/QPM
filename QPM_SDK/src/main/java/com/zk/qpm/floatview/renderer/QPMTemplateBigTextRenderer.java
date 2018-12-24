package com.zk.qpm.floatview.renderer;

import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zk.qpm.R;
import com.zk.qpm.floatview.QPMFloatViewType;

import java.util.Map;

public class QPMTemplateBigTextRenderer extends BaseTemplateRenderer {

    public static final String PARAM_BIG_TEXT = "param_big_text";

    public QPMTemplateBigTextRenderer(Map<String, Object> mParam) {
        super(mParam);
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_BIG_TEXT;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jm_gt_item_floatview_template_bigtext;
    }

    @Override
    protected void renderer(View mView, Map<String, Object> mParam) {
        TextView valueView = mView.findViewById(R.id.tv_value);
        Object o = mParam.get(PARAM_BIG_TEXT);
        if (o == null) {
            return;
        }
        String content = o.toString();
        if (!TextUtils.isEmpty(content)) {
            valueView.setText(formatContent(content));
        }
    }

    private Spanned formatContent(String content) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT);
            } else {
                return Html.fromHtml(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SpannableString(content);
        }
    }
}
