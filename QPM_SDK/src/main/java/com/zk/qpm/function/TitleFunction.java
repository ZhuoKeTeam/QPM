package com.jm.android.gt.function;

import android.app.Activity;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.android.gt.R;

public class TitleFunction implements IFunction {

    private Activity activity;
    private ImageView backView;
    private TextView titleView;
    private TextView operateView;
    private String title;

    public TitleFunction(Activity activity, String title) {
        this.activity = activity;
        this.title = title;
    }

    @Override
    public int viewStubId() {
        return R.id.vs_title;
    }

    @Override
    public void renderer(View layout) {
        backView = layout.findViewById(R.id.iv_back);
        titleView = layout.findViewById(R.id.tv_title);
        operateView = layout.findViewById(R.id.tv_operate);
        titleView.setText(title);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    @Override
    public ViewStub.OnInflateListener getCallback() {
        return null;
    }

    public void initOperateButton(String operateName, View.OnClickListener onClickListener, boolean defaultShow) {
        operateView.setText(operateName);
        operateView.setOnClickListener(onClickListener);
        refreshOperateView(defaultShow);
    }

    public void refreshOperateView(boolean show) {
        operateView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
