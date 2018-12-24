package com.jm.android.gt.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jm.android.gt.JMGTRAnalysisResult;
import com.jm.android.gt.R;
import com.jm.android.gt.activity.GTMainMenuActivity;
import com.jm.android.gt.callback.IAnalysisCallback;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;
import com.jm.android.gt.manager.JMGTCallBackManager;

import java.util.List;

public class JMFloatViewCustomComponent extends JMFloatViewBaseComponent {

    private LinearLayout containerLayout;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(final Context context) {
        super.onCreate(context);
        addCallback();
    }

    private void addCallback() {
        JMGTCallBackManager.getInstance().addCallBack(new IAnalysisCallback() {
            @Override
            public void refreshInfo(String type, final JMGTRAnalysisResult analysisResult) {
                refreshItemViewToFloatView();
            }
        });
    }

    private void refreshItemViewToFloatView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                JMFloatViewManager.getInstance().rendererFloatViewBean();
            }
        });
    }

    @Override
    public void refreshContainerLayout() {
        containerLayout.removeAllViews();
        List<IFloatViewRenderer> items = JMFloatViewManager.getInstance().getItems();
        for (IFloatViewRenderer item : items) {
            if (item.isShow()) {
                containerLayout.addView(item.getView());
            }
        }
    }

    @Override
    public void onDestroy(Context context) {
        super.onDestroy(context);
        containerLayout.removeAllViews();
        mHandler.removeCallbacksAndMessages(null);
        JMGTCallBackManager.getInstance().removeAllCallBack();
    }

    @Override
    protected View getLayout(final Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.jm_gt_layout_float_view, null);
        ImageView jmIcon = view.findViewById(R.id.jm_icon);

        jmIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GTMainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        containerLayout = view.findViewById(R.id.ll_container);
        return view;
    }

}
