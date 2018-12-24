package com.zk.qpm.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zk.qpm.manager.QPMFloatViewManager;
import com.zk.qpm.manager.QPMManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QPMManager.getInstance().init(this.getApplication());

        if (!QPMFloatViewManager.getInstance().floatViewShow()) {
            Toast.makeText(this, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
        }

    }
}
