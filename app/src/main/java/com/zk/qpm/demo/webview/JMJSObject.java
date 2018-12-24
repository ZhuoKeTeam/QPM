package com.zk.qpm.demo.webview;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.TimeUtils;
import com.zk.qpm.manager.QPMManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JMJSObject {
    private static final String TAG = "WangQing";

    @JavascriptInterface
    public void sendResource(String msg) {
        handleResource(msg);
    }

    @JavascriptInterface
    public void sendError(String msg) {
        handleError(msg);
    }

    public void handleError(String msg) {
        Log.d(TAG, "handleError: ====================== come in handleError");
        Log.d(TAG, msg);
    }

    public void handleResource(String msg) {
        Log.d(TAG, "handleResource: ====================== come in handleResource");
        Log.d(TAG, msg);

        try {
//           参数配置项目： http://www.bubuko.com/infodetail-1228020.html
            JSONObject obj = new JSONObject(msg);


            String type = obj.optString("type");
            JSONObject payloadObj = obj.optJSONObject("payload");

            String url = payloadObj.optString("url");
            String domain = payloadObj.optString("domain");
            String uri = payloadObj.optString("uri");


            JSONObject navigationTimingObj = payloadObj.optJSONObject("navigationTiming");
            long pageTime = navigationTimingObj.optLong("pageTime");

            //DNS查询耗时、TCP链接耗时、request请求耗时、解析dom树耗时、白屏时间、domready时间、onload时间等，
            // 而这些参数是通过上面的performance.timing各个属性的差值组成的，计算方法如下：
            //
            //DNS查询耗时 ：domainLookupEnd - domainLookupStart
            long domainLookupStart = navigationTimingObj.optLong("domainLookupStart");
            long domainLookupEnd = navigationTimingObj.optLong("domainLookupEnd");
            long dsnTime = domainLookupEnd - domainLookupStart;
            //
            //TCP链接耗时 ：connectEnd - connectStart
            long connectStart = navigationTimingObj.optLong("connectStart");
            long connectEnd = navigationTimingObj.optLong("connectEnd");
            long tcpTime = connectEnd - connectStart;
            //
            //request请求耗时 ：responseEnd - responseStart
            long responseStart = navigationTimingObj.optLong("responseStart");
            long responseEnd = navigationTimingObj.optLong("responseEnd");
            long requestTime = responseEnd - responseStart;
            //
            //解析dom树耗时 ： domComplete- domInteractive
            long domInteractive = navigationTimingObj.optLong("domInteractive");
            long domComplete = navigationTimingObj.optLong("domComplete");
            long domTime = domComplete- domInteractive;
            //
            //白屏时间 ：responseStart - navigationStart
//            long responseStart = navigationTimingObj.optLong("responseStart");
            long navigationStart = navigationTimingObj.optLong("navigationStart");
            long whiteUITime = responseStart - navigationStart;



            //
            //domready时间 ：domContentLoadedEventEnd - navigationStart
//            long navigationStart = navigationTimingObj.optLong("navigationStart");
            long domContentLoadedEventEnd = navigationTimingObj.optLong("domContentLoadedEventEnd");
            long domReadyTime = domContentLoadedEventEnd - navigationStart;
            //
            //onload时间 ：loadEventEnd - navigationStart
//            long navigationStart = navigationTimingObj.optLong("navigationStart");
            long loadEventEnd = navigationTimingObj.optLong("loadEventEnd");
            long onLoadTime = domComplete- domInteractive;


            //添加数据到监控页面去。
            if (whiteUITime > 0 ){
                QPMManager.getInstance().setH5MonitorData(url, whiteUITime);
            }

            Log.d(TAG, "AAA_handleResource: " +
                    "\npageTime=" + pageTime +
                    "\npageTime=" + TimeUtils.millis2String(pageTime) +
                    "\n，dsnTime=" + dsnTime +
                    "\n，tcpTime=" + tcpTime +
                    "\n，requestTime=" + requestTime +
                    "\n，domTime=" + domTime +
                    "\n，whiteUITime=" + whiteUITime +
                    "\n，domReadyTime=" + domReadyTime +
                    "\n，onLoadTime=" + onLoadTime
            );


            JSONArray resourceTimingArray = payloadObj.optJSONArray("resourceTiming");

            for (int i = 0; i < resourceTimingArray.length(); i++) {
                String data = String.valueOf(resourceTimingArray.get(i));
                JSONObject resObj = new JSONObject(data);

                //TCP链接耗时 ：connectEndRes - connectStartRes
                long connectResStart = resObj.optLong("connectStart");
                long connectResEnd = resObj.optLong("connectEnd");
                long tcpResTime = connectResEnd - connectResStart;

                //DNS查询耗时 ：domainLookupEnd - domainLookupStart
                long domainLookupResStart = resObj.optLong("domainLookupStart");
                long domainLookupResEnd = resObj.optLong("domainLookupEnd");
                long dsnResTime = domainLookupResEnd - domainLookupResStart;


                //request请求耗时 ：responseEnd - responseStart
                long responseResStart = navigationTimingObj.optLong("responseStart");
                long responseResEnd = navigationTimingObj.optLong("responseEnd");
                long requestResTime = responseResEnd - responseResStart;


                long durationResTime = resObj.optLong("duration");
                String entryResType = resObj.optString("entryType"); //resource

                long fetchResStart = resObj.optLong("fetchStart");
                String initiatorResType = resObj.optString("initiatorType"); // script,html,css 等类型
                String resName = resObj.optString("name");

                long redirectResEnd = resObj.optLong("redirectEnd");
                long redirectResStart = resObj.optLong("redirectStart");

                long redirectResTime = redirectResEnd - redirectResStart;


                long requestTimeResStart = resObj.optLong("requestStart");
                long secureConnectionResStart = resObj.optLong("secureConnectionStart");
                long startResTime = resObj.optLong("startTime");


                Log.d(TAG, "AAA_Res_handleResource: " +
                        "\nstartResTime=" + startResTime +
                        "\n，dsnTime=" + dsnTime +
                        "\n，dsnResTime=" + dsnResTime +
                        "\n，requestResTime=" + requestResTime +
                        "\n，redirectResTime=" + redirectResTime +
                        "\n，requestTimeResStart=" + requestTimeResStart +
                        "\n，secureConnectionResStart=" + secureConnectionResStart +
                        "\n，durationResTime=" + durationResTime +
                        "\n，entryResType=" + entryResType +
                        "\n，initiatorResType=" + initiatorResType +
                        "\n，resName=" + resName
                );


            }




        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
