package com.jm.android.gt.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 沉浸式修改状态栏工具类(非沉浸式请用StatusBarColorUtils)
 * <p>
 * 开启沉浸式
 * 修改沉浸式状态下  状态栏背景颜色以及状态栏文字颜色
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class StatusBarImmersiveUtils {
    public static final String TAG_FAKE_STATUS_BAR_VIEW = "statusBarView";
    private static final String TAG_PADDING_ADDED = "paddingAdded";

    /**
     * 开启状态栏沉浸式(默认状态栏文字深颜色)
     */
    public static void open(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup contentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            View mStatusBarView = contentView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
            if (mStatusBarView == null) {
                mStatusBarView = new View(activity);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , StatusBarColorUtils.getStatusBarHeight(activity));
                layoutParams.gravity = Gravity.TOP;
                mStatusBarView.setLayoutParams(layoutParams);
                contentView.addView(mStatusBarView);
                mStatusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);
                mStatusBarView.setBackgroundColor(Color.TRANSPARENT);
            }
            //设置状态栏图标为浅颜色
            StatusBarColorUtils.setStatusBarDarkIcon(activity, false);
        }
    }

    //需要沉浸式的view
    public static void immersiveView(View view) {
        Object tag = view.getTag();
        if (!(tag instanceof String && TextUtils.equals(TAG_PADDING_ADDED, (CharSequence) tag))) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + StatusBarColorUtils.getStatusBarHeight(view.getContext())
                    , view.getPaddingRight(), view.getPaddingBottom());
            view.setTag(TAG_PADDING_ADDED);
        }
    }

    /**
     * 状态栏沉浸模式下
     * 设置状态栏文字深颜色
     *
     * @param defaultDarkIconColor 如果状态栏图标无法设置为深色图标 设置默认的状态栏颜色
     */
    public static void setDarkIcon(Activity activity, int defaultDarkIconColor) {
        setDarkIconStatusBarColor(activity, Color.TRANSPARENT, defaultDarkIconColor);
    }

    /**
     * 状态栏沉浸模式下(状态栏文字深颜色)
     * 设置沉浸式状态栏颜色
     *
     * @param colorStatusbar       状态栏颜色
     * @param defaultDarkIconColor 如果状态栏图标无法设置为深色图标 设置默认的状态栏颜色
     */
    public static void setDarkIconStatusBarColor(Activity activity, int colorStatusbar, int defaultDarkIconColor) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mStatusBarView = contentView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (mStatusBarView == null) {
            open(activity);
        }
        mStatusBarView = contentView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (mStatusBarView != null) {
            if (!StatusBarColorUtils.setStatusBarDarkIcon(activity, true)) {
                mStatusBarView.setBackgroundColor(defaultDarkIconColor);
            } else {
                mStatusBarView.setBackgroundColor(colorStatusbar);
                StatusBarColorUtils.setStatusBarDarkIcon(activity, true);
            }
        }
    }


    /**
     * 状态栏沉浸模式下
     * 设置状态栏文字浅颜色
     */
    public static void setLightIcon(Activity activity) {
        setLightIconStatusBarColor(activity, Color.TRANSPARENT);
    }

    /**
     * 状态栏沉浸模式下(状态栏文字浅颜色)
     * 设置沉浸式状态栏颜色
     *
     * @param colorStatusbar 状态栏颜色
     */
    public static void setLightIconStatusBarColor(Activity activity, int colorStatusbar) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mStatusBarView = contentView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (mStatusBarView == null) {
            open(activity);
        }
        mStatusBarView = contentView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (mStatusBarView != null) {
            mStatusBarView.setBackgroundColor(colorStatusbar);
            StatusBarColorUtils.setStatusBarDarkIcon(activity, false);
        }
    }
    /**
     * 设置全屏
     *
     * @param activity
     */
    public static void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 取消全屏
     *
     * @param activity
     */
    public static void cancelFullScreen(Activity activity) {
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    /**
     * 是否开启了沉浸式
     * @return
     */
    public static boolean isOpenImmers(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
            return contentView.findViewWithTag(StatusBarImmersiveUtils.TAG_FAKE_STATUS_BAR_VIEW) != null;
        }
        return false;
    }
}
