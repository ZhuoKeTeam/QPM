package com.zk.qpm.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.zk.qpm.manager.QPMManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FloatViewUtils {

    /**
     * 是否展示了悬浮窗
     * 参考：https://blog.csdn.net/nian627/article/details/79216763
     *
     * @param context context
     */
    public static boolean applyCommonPermission(Context context) {
        if (!checkFloatWindowPermission()) {
            try {
                Class clazz = Settings.class;
                Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
                Intent intent = new Intent(field.get(null).toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        }
        return false;
    }

    /**
     * 检测悬浮窗
     * @return 是否显示了悬浮窗
     */
    public static boolean checkFloatWindowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(QPMManager.getInstance().getContext());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //AppOpsManager添加于API 19
            return checkOps();
        } else {
            //4.4以下一般都可以直接添加悬浮窗
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean checkOps() {
        try {
            Context context = QPMManager.getInstance().getContext();
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = (Integer) method.invoke(object, arrayOfObject1);
            //4.4至6.0之间的非国产手机，例如samsung，sony一般都可以直接添加悬浮窗
            return m == AppOpsManager.MODE_ALLOWED || !RomUtils.isDomesticSpecialRom();
        } catch (Exception ignore) {
        }
        return false;
    }
}
