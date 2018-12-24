package com.zk.qpm.demo.webview;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ResourceUtils;

import java.io.InputStream;

public class JMWebViewClient extends WebViewClient {

    public static final String JM_LOCAL_COLLECTOR_JS_PATH = "/local/jm_local_collector.js";


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);


//        (function() {
//            var script=document.createElement('script');
//            script.setAttribute('type','text/javascript');
//            script.setAttribute('src', 'https://www.gdky005.com/jm_collector.js');
//            document.head.appendChild(script);
//            console.log("WangQing_script_start");
//            script.onload = function() {
//                console.log("WangQing_BBB");
//            };
//            console.log("WangQing_script_end");
//        })();


        //TODO collector.js文件的地址，收集的功能主要在这里面实现
//        String injectJs = "http://your_url/collector.js";
//        String injectJsUrl = "//www.gdky005.com/jm_collector.js";
//        String injectJsUrl = "//h5.st.jumei.com/Test/utilTest";

        Uri uri = Uri.parse(url);
        String injectJsUrl = uri.buildUpon().path(JM_LOCAL_COLLECTOR_JS_PATH).toString();

//        String injectJsUrl = "http://h5.st.jumei.com" + JM_LOCAL_COLLECTOR_JS_PATH;

        String injectJs = ResourceUtils.readAssets2String("collector.js");

        if (!injectJs.equals("")) {
            String msg = "javascript:(function() {" +
                    "var script=document.createElement('script');" +
                    "script.setAttribute('type','text/javascript');" +
                    "script.setAttribute('src', '" + injectJsUrl +"');" +
//                    "script.setAttribute('text', '" + injectJs +"');" +
                    "document.head.appendChild(script);" +
                    "console.log(\"WangQing_script_start\");" +
                    "script.onload = function() {" +
                    "console.log(\"WangQing_Running!\");" +
                    "startWebViewMonitor();" +
                    "};" +
                    "console.log(\"WangQing_script_end\");" +
                    "})();";

            view.loadUrl(msg);
        }
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

        try {
            if (!TextUtils.isEmpty(url) && url.contains(JM_LOCAL_COLLECTOR_JS_PATH)) {
                InputStream inputStream = ConvertUtils.string2InputStream(ResourceUtils.readAssets2String("collector.js"), "utf-8");
                return new WebResourceResponse("application/javascript", "UTF-8", inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.shouldInterceptRequest(view, url);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return shouldInterceptRequest(view, request.getUrl().toString());
    }
}
