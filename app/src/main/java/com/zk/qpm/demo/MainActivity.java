package com.zk.qpm.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.zk.qpm.floatview.renderer.QPMTemplateCustomRenderer;
import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.manager.QPMRAnalysisManager;
import com.zk.qpm.utils.PermissionTool;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "QPM";
    protected Context mContext;

    public static boolean threadFlag = true;

    private int count = 1;

    //需要申请的权限：
    private static final String[] permissions = {
            //存储卡
            Manifest.permission.READ_EXTERNAL_STORAGE,//SDK>=16才可以使用
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WAKE_LOCK,
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        QPMManager.getInstance().init(getApplication());

        setContentView(getLayoutId());
        initViews();
        initListener();
        initData();

    }

    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    protected void initViews() {
    }

    protected void initListener() {
        findViewById(R.id.test_network_btn).setOnClickListener(this);
        findViewById(R.id.gt_float_view_hide_btn).setOnClickListener(this);
        findViewById(R.id.gt_float_view_show_btn).setOnClickListener(this);
        findViewById(R.id.test_h5).setOnClickListener(this);
        findViewById(R.id.btn_refresh_big_test).setOnClickListener(this);
        findViewById(R.id.btn_add_big_test).setOnClickListener(this);
        findViewById(R.id.btn_remove_big_test).setOnClickListener(this);
        findViewById(R.id.btn_refresh_keypic).setOnClickListener(this);
        findViewById(R.id.btn_add_keypic).setOnClickListener(this);
        findViewById(R.id.btn_remove_keypic).setOnClickListener(this);
        findViewById(R.id.btn_refresh_keyvalue).setOnClickListener(this);
        findViewById(R.id.btn_add_keyvalue).setOnClickListener(this);
        findViewById(R.id.btn_remove_keyvalue).setOnClickListener(this);
        findViewById(R.id.btn_refresh_picvalue).setOnClickListener(this);
        findViewById(R.id.btn_add_picvalue).setOnClickListener(this);
        findViewById(R.id.btn_remove_picvalue).setOnClickListener(this);
        findViewById(R.id.btn_refresh_custom).setOnClickListener(this);
        findViewById(R.id.btn_add_custom).setOnClickListener(this);
        findViewById(R.id.btn_remove_custom).setOnClickListener(this);
    }

    protected void initData() {
        PermissionTool.applyPermissions(permissions, MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_network_btn:
                testOKHTTP();
                break;
            case R.id.gt_float_view_hide_btn:
                QPMManager.getInstance().floatViewHide();
                break;
            case R.id.gt_float_view_show_btn:
                if (!QPMManager.getInstance().floatViewShow()) {
                    Toast.makeText(mContext, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.test_h5:
                Intent intent = new Intent(mContext, WebViewActivity.class);
                mContext.startActivity(intent);
                break;

            case R.id.btn_refresh_big_test:
                String html1 = "<p>(%1$d).Name:&nbsp;Toking Hazard by Joking Hazard</p>";
                html1 = String.format(html1, count++);
                QPMManager.getInstance().showBigText("MainActivityBigTest", html1);
                break;

            case R.id.btn_add_big_test:
                String html2 = "<p>(%1$d).Name:&nbsp;Toking Hazard by Joking Hazard</p>";
                html2 = String.format(html2, count);
                QPMManager.getInstance().showBigText("MainActivityBigTest" + count, html2);
                count++;
                break;

            case R.id.btn_remove_big_test:
                QPMManager.getInstance().dismiss("MainActivityBigTest");
                break;

            case R.id.btn_refresh_keypic:
                QPMManager.getInstance().showKeyPic("MainActivityKeyPic", "KEY", R.drawable.jm_gt_ic_fps);
                break;

            case R.id.btn_add_keypic:
                QPMManager.getInstance().showKeyPic("MainActivityKeyPic" + count, "KEY", R.drawable.jm_gt_ic_fps);
                count++;
                break;

            case R.id.btn_remove_keypic:
                QPMManager.getInstance().dismiss("MainActivityKeyPic");
                break;

            case R.id.btn_refresh_keyvalue:
                QPMManager.getInstance().showKeyValue("MainActivityKeyValue", "KEY", "VALUE");
                break;

            case R.id.btn_add_keyvalue:
                QPMManager.getInstance().showKeyValue("MainActivityKeyValue" + count, "KEY", "VALUE");
                count++;
                break;

            case R.id.btn_remove_keyvalue:
                QPMManager.getInstance().dismiss("MainActivityKeyValue");
                break;

            case R.id.btn_refresh_picvalue:
                QPMManager.getInstance().showPicValue("MainActivityPicValue", R.drawable.jm_gt_ic_fps, "VALUE");
                break;

            case R.id.btn_add_picvalue:
                QPMManager.getInstance().showPicValue("MainActivityPicValue" + count, R.drawable.jm_gt_ic_fps, "VALUE");
                count++;
                break;

            case R.id.btn_remove_picvalue:
                QPMManager.getInstance().dismiss("MainActivityPicValue");
                break;

            case R.id.btn_refresh_custom:
                Map<String, Object> param1 = new HashMap<>();
                param1.put("src", R.drawable.jm_gt_icon);
                QPMManager.getInstance().showCustom("MainActivityCustom", new QPMTemplateCustomRenderer(param1) {
                    @Override
                    protected int getLayoutId() {
                        return R.layout.jm_custom;
                    }

                    @Override
                    protected void renderer(View mView, Map<String, Object> mParam) {
                        ImageView imageView = mView.findViewById(R.id.image);
                        Integer srcId = (Integer) mParam.get("src");
                        imageView.setImageResource(srcId);
                    }
                });
                break;

            case R.id.btn_add_custom:
                Map<String, Object> param2 = new HashMap<>();
                param2.put("src", R.drawable.jm_gt_icon);
                QPMManager.getInstance().showCustom("MainActivityCustom" + count, new QPMTemplateCustomRenderer(param2) {
                    @Override
                    protected int getLayoutId() {
                        return R.layout.jm_custom;
                    }

                    @Override
                    protected void renderer(View mView, Map<String, Object> mParam) {
                        ImageView imageView = mView.findViewById(R.id.image);
                        Integer srcId = (Integer) mParam.get("src");
                        imageView.setImageResource(srcId);
                    }
                });
                count++;
                break;

            case R.id.btn_remove_custom:
                QPMManager.getInstance().dismiss("MainActivityCustom");
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //申请所有权限的回调结果：
        if (requestCode == PermissionTool.APPLY_PERMISSIONS) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//如果有权限被拒绝
                    Toast.makeText(this, "对不起，您未给予相应的权限，程序将退出。", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
            //如果全部都同意了就进行配置加载
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void testOKHTTP() {
        String url = "http://suggest.taobao.com/sug?code=utf-8&q=车载";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().
                addInterceptor(QPMManager.getInstance().getOkHttpInterceptor())
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + (e == null ? "" : e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().string());
                }
            }
        });
    }

}
