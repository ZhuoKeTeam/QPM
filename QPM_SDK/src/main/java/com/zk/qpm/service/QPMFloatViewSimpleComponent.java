package com.zk.qpm.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zk.qpm.QPMRAnalysisResult;
import com.zk.qpm.R;
import com.zk.qpm.activity.QPMMainMenuActivity;
import com.zk.qpm.callback.IAnalysisCallback;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;
import com.zk.qpm.manager.QPMCallBackManager;

import java.util.List;


public class QPMFloatViewSimpleComponent extends QPMFloatViewBaseComponent {

    private LinearLayout containerLayout;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(final Context context) {
        super.onCreate(context);
        addCallback();
    }

    private void addCallback() {
        QPMCallBackManager.getInstance().addCallBack(new IAnalysisCallback() {
            @Override
            public void refreshInfo(String type, final QPMRAnalysisResult analysisResult) {
                refreshItemViewToFloatView();
            }
        });
    }

    private void refreshItemViewToFloatView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                QPMFloatViewManager.getInstance().rendererFloatViewBean();
            }
        });
    }

    @Override
    public void refreshContainerLayout() {
        containerLayout.removeAllViews();
        List<IFloatViewRenderer> items = QPMFloatViewManager.getInstance().getItems();
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
        QPMCallBackManager.getInstance().removeAllCallBack();
    }

    @Override
    protected View getLayout(final Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.jm_gt_layout_float_view_simple, null);
        containerLayout = view.findViewById(R.id.ll_container);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QPMMainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
