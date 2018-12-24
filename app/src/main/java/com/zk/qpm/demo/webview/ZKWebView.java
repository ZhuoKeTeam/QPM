package com.zk.qpm.demo.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.zk.qpm.manager.QPMManager;


public class ZKWebView extends WebView {
    public ZKWebView(Context context) {
        super(context);
    }

    public ZKWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZKWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        clearH5MonitorInfo();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        clearH5MonitorInfo();
    }

    /**
     * 清空相关数据
     */
    private void clearH5MonitorInfo() {
        QPMManager.getInstance().setH5MonitorData("", 0);
    }
}
