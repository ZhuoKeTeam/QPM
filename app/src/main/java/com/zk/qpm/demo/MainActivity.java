package com.zk.qpm.demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zk.qpm.floatview.renderer.QPMTemplateCustomRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;
import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.manager.QPMRAnalysisManager;
import com.zk.qpm.utils.PermissionTool;

import java.util.HashMap;
import java.util.Map;

// TODO: 2018/12/24 等待添加 网络测试。
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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

        QPMManager.getInstance().init(getApplication());

        mContext = this;
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
        findViewById(R.id.gt_thread_auto_add).setOnClickListener(this);
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
        // TODO: 2018/11/1 必须先初始化这个
        QPMRAnalysisManager.getInstance().start(this, Process.myPid(), getPackageName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_network_btn:
                // TODO: 2018/12/24  等待添加
                break;
            case R.id.gt_float_view_hide_btn:
                QPMFloatViewManager.getInstance().floatViewHide();
                break;
            case R.id.gt_float_view_show_btn:
                if (!QPMFloatViewManager.getInstance().floatViewShow()) {
                    Toast.makeText(mContext, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.gt_thread_auto_add:
                /*count++;

                if (count % 2 == 0) {
                    JMPluginManager.getInstance().getPluginControler().startService(JMTestComponent.getInstance());
                } else {
                    JMPluginManager.getInstance().getPluginControler().stopService(JMTestComponent.getInstance());
                }*/
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
//                JMGTManager.getInstance().showKeyPic("MainActivityKeyPic", "KEY", "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_86d58ae1.png");
                break;

            case R.id.btn_add_keypic:
                QPMManager.getInstance().showKeyPic("MainActivityKeyPic" + count, "KEY", R.drawable.jm_gt_ic_fps);
//                JMGTManager.getInstance().showKeyPic("MainActivityKeyPic" + count, "KEY", "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_86d58ae1.png");
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
//                JMGTManager.getInstance().showPicValue("MainActivityPicValue", "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_86d58ae1.png", "VALUE");
                break;

            case R.id.btn_add_picvalue:
                QPMManager.getInstance().showPicValue("MainActivityPicValue" + count, R.drawable.jm_gt_ic_fps, "VALUE");
//                JMGTManager.getInstance().showPicValue("MainActivityPicValue" + count, "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_86d58ae1.png", "VALUE");
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

    @RequiresApi(api = 23)
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

}
