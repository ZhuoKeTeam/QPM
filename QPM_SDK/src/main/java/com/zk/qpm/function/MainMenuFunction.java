package com.zk.qpm.function;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewStub;

import com.zk.qpm.R;
import com.zk.qpm.activity.QPMBasicInfoActivity;
import com.zk.qpm.activity.QPMManifestInfoActivity;
import com.zk.qpm.activity.QPMNetworkAPIActivity;
import com.zk.qpm.activity.QPMPrefInfoActivity;
import com.zk.qpm.activity.QPMSwitchActivity;
import com.zk.qpm.manager.QPMFloatViewManager;


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
                if (QPMFloatViewManager.getInstance().isFloatViewShow()) {
                    QPMFloatViewManager.getInstance().floatViewHide();
                } else {
                    QPMFloatViewManager.getInstance().floatViewShow();
                }
            }
        });

        layout.findViewById(R.id.btn_phone_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, QPMBasicInfoActivity.class));
            }
        });

        layout.findViewById(R.id.btn_manifest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, QPMManifestInfoActivity.class));
            }
        });

        layout.findViewById(R.id.btn_pref_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, QPMPrefInfoActivity.class));
            }
        });

        layout.findViewById(R.id.btn_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, QPMSwitchActivity.class));
            }
        });

        layout.findViewById(R.id.btn_network_api).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, QPMNetworkAPIActivity.class));
            }
        });

//        layout.findViewById(R.id.btn_123).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                throw new RuntimeException("Test Crash");
//            }
//        });
    }

    @Override
    public ViewStub.OnInflateListener getCallback() {
        return null;
    }

}
