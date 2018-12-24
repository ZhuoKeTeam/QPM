package com.zk.qpm.service;

import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.zk.qpm.utils.DeviceUtils;


public abstract class QPMFloatViewBaseComponent extends QPMBaseService {

    private static final int SCREEN_HORIZONTALLY = 0;
    private static final int SCREEN_VERTICAL = 1;

    protected boolean isFloatViewShow;
    private WindowManager wm = null;
    private WindowManager.LayoutParams wmParams = null;
    private View mRootView;
    private int mTouchSlop;

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        isFloatViewShow = true;
        mRootView = getLayout(context);
        createFloatView(context);
    }

    @Override
    public void onDestroy(Context context) {
        super.onDestroy(context);
        isFloatViewShow = false;
        try {
            wm.removeView(mRootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShow() {
        return isFloatViewShow;
    }

    private void createFloatView(final Context context) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        wmParams.flags |= 8;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.format = 1;
        try {
            wm.addView(mRootView, wmParams);
        } catch (Exception e) {
            e.printStackTrace();
            stopSelf();
            return;
        }

        mRootView.setOnTouchListener(new View.OnTouchListener() {

            private float mTouchStartX;
            private float mTouchStartY;
            private float x;
            private float y;
            private float startX;
            private float startY;
            private boolean isMoved; // 一次touch操作中是否有移动过

            public boolean onTouch(View v, MotionEvent event) {
                // 获取相对屏幕的坐标，即以屏幕左上角为原点
                x = event.getRawX();
                if (SCREEN_VERTICAL == checkScreenOrientation()) {
                    y = event.getRawY()
                            - DeviceUtils.getStatusBarHeight(context);
                } else {
                    y = event.getRawY();
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = x;
                        startY = y;
                        // 获取相对View的坐标，即以此View左上角为原点
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY();
                        isMoved = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updateViewPosition();
                        break;
                    case MotionEvent.ACTION_UP:
                        updateViewPosition();
                        mTouchStartX = mTouchStartY = 0;
                        if (!isMoved) {
                            isMoved = false;
                        }
                        // 模拟点击
                        if (Math.abs(x - startX) <= mTouchSlop
                                && Math.abs(y - startY) <= mTouchSlop) {
                            mRootView.performClick();
                        }
                        break;
                }
                return true;
            }

            private void updateViewPosition() {
                // 更新浮动窗口位置参数
                if (Math.abs(x - startX) > 10 || Math.abs(y - startY) > 10) {
                    wmParams.x = (int) (x - mTouchStartX);
                    wmParams.y = (int) (y - mTouchStartY);
                    isMoved = true;
                    wm.updateViewLayout(mRootView, wmParams);
                }
            }

            private int checkScreenOrientation() {
                int dev_width = ScreenUtils.getScreenWidth();
                int dev_height = ScreenUtils.getScreenHeight();
                int orientation = SCREEN_VERTICAL; // 0:竖屏 1:横屏
                if (dev_width > dev_height) {
                    orientation = SCREEN_HORIZONTALLY;
                }
                return orientation;
            }
        });
    }

    protected abstract View getLayout(Context context);

    public abstract void refreshContainerLayout();

    @Override
    public IBinder onBind(Context context) {
        return null;
    }
}
