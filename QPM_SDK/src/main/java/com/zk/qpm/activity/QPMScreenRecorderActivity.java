package com.zk.qpm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.manager.QPMScreenRecorderManager;

public class QPMScreenRecorderActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            QPMScreenRecorderManager.getInstance().startRecorder(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            QPMScreenRecorderManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
        }
    }

    public static void startRecorder(){
        Context context = QPMManager.getInstance().getContext();
        Intent intent = new Intent(context, QPMScreenRecorderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
