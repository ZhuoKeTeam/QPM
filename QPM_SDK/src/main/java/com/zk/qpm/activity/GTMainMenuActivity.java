package com.jm.android.gt.activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.jm.android.gt.R;
import com.jm.android.gt.function.MainMenuFunction;
import com.jm.android.gt.function.TitleFunction;
import com.jm.android.gt.utils.PermissionTool;

public class GTMainMenuActivity extends GTFunctionBaseActivity {

    @Override
    protected int contentViewId() {
        return R.layout.jm_gt_activity_main_menu;
    }

    @Override
    protected void initAllFunction() {
        functions.add(new TitleFunction(this, getString(R.string.jm_gt_main_menu)));
        functions.add(new MainMenuFunction(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 请求权限
        requestPermission();
    }

    private void requestPermission(){
        String[] permissions = new String[] {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        PermissionTool.applyPermissions(permissions, this);
    }

    @RequiresApi(api = 23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //申请所有权限的回调结果：
        if (requestCode == PermissionTool.APPLY_PERMISSIONS) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//如果有权限被拒绝
                    Toast.makeText(this, R.string.jm_gt_without_permission, Toast.LENGTH_SHORT).show();
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
