package com.zk.qpm.demo;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zk.qpm.activity.QPMMainMenuActivity;

public class NewMainActivity extends QPMMainMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TextView tvRules = findViewById(com.zk.qpm.R.id.tv_rules);
        tvRules.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMainActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.TAG_WEB_URL, "http://zkteam.cc/android/rules/about_rules_v2.html");
                startActivity(intent);
            }
        });

        findViewById(com.zk.qpm.R.id.btn_custom_layer_view).setOnClickListener(v ->
                startActivity(new Intent(NewMainActivity.this, MainActivity.class)));
    }
}
