package com.zk.qpm.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zk.qpm.demo.webview.JMJSObject;
import com.zk.qpm.demo.webview.JMWebChromeClient;
import com.zk.qpm.demo.webview.ZKWebView;
import com.zk.qpm.demo.webview.JMWebViewClient;


public class WebViewActivity extends Activity {

    //    private String url = "https://github.com/Blankj/AndroidUtilCode/blob/master/utilcode/README-CN.md";
//    private String url = "http://www.zkteam.cc";
//    private String url = "https://www.baidu.com/";
    private String url = "http://wq.zkteam.cc";
//    private String url = "http://www.baidu.com";

    public static final String TAG_WEB_URL = "tag_web_url";

    private ZKWebView webview;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);

        Button showDialog = (Button) findViewById(R.id.showDialog);
        Button reloadBtn = (Button) findViewById(R.id.reloadBtn);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);
        linearLayout.setVisibility(View.GONE);

        String newUrl = getIntent().getStringExtra(TAG_WEB_URL);
        if (!TextUtils.isEmpty(newUrl)) {
            url = newUrl;
        }


        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.loadUrl(url);
            }
        });

        showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        webView.loadUrl("javascript:modifyQ('" + title + "');");
//                        webView.loadUrl("javascript:modifyA('" + content + "');");
//                        webView.loadUrl("javascript:modifyNumber('" + mPhoneNumber + "');");

//                        webview.loadUrl("javascript:(alert(\"hello\"));");
//                        webview.loadUrl("javascript:(console.log(\"WangQing_888\"));");
                    }
                });
            }
        });

        webview = (ZKWebView) findViewById(R.id.webview);



        WebSettings webSettings = webview.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webview.setWebViewClient(new JMWebViewClient());
        webview.setWebChromeClient(new JMWebChromeClient());


        webview.addJavascriptInterface(new JMJSObject(), "myObj");

//        String url = "http://www.ifanr.com/";
        webview.loadUrl(url);
    }
}
