package com.jm.android.gt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jm.android.gt.manager.JMGTManager;
import com.jm.android.gt.manager.JMScreenRecorderManager;

public class GTScreenRecorderActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JMScreenRecorderManager.getInstance().startRecorder(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JMScreenRecorderManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
        }
    }

    public static void startRecorder(){
        Context context = JMGTManager.getInstance().getContext();
        Intent intent = new Intent(context, GTScreenRecorderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
