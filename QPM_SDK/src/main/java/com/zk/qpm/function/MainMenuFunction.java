package com.jm.android.gt.function;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewStub;

import com.jm.android.gt.R;
import com.jm.android.gt.activity.GTBasicInfoActivity;
import com.jm.android.gt.activity.GTManifestInfoActivity;
import com.jm.android.gt.activity.GTNetworkAPIActivity;
import com.jm.android.gt.activity.GTPrefInfoActivity;
import com.jm.android.gt.activity.GTSwitchActivity;
import com.jm.android.gt.manager.JMFloatViewManager;


public class MainMenuFunction implements IFunction {

    private Context mContext;

    public MainMenuFunction(Context context) {
        this.mContext = context;
    }

    @Override
    public int viewStubId() {
        return R.id.vs_main_menu;
    }

    @Override
    public void renderer(View layout) {
        layout.findViewById(R.id.btn_switch_floatview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JMFloatViewManager.getInstance().isFloatViewShow()) {
                    JMFloatViewManager.getInstance().floatViewHide();
                } else {
                    JMFloatViewManager.getInstance().floatViewShow();
                }
            }
        });

        layout.findViewById(R.id.btn_phone_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, GTBasicInfoActivity.class));
            }
        });

        layout.findViewById(R.id.btn_manifest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, GTManifestInfoActivity.class));
            }
        });

        layout.findViewById(R.id.btn_pref_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, GTPrefInfoActivity.class));
            }
        });

        layout.findViewById(R.id.btn_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, GTSwitchActivity.class));
            }
        });

        layout.findViewById(R.id.btn_network_api).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, GTNetworkAPIActivity.class));
            }
        });
    }

    @Override
    public ViewStub.OnInflateListener getCallback() {
        return null;
    }

}
