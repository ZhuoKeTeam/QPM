package com.zk.qpm.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 集中管理基本信息的获取
 */
public class BasicInfoUtils {

    private static final String UNKNOW = "UNKNOW";

    public static class InfoItem {

        public static final int TYPE_TITLE = 0;
        public static final int TYPE_CONTENT = 1;

        private int type;
        private String label;
        private String content;

        public InfoItem(String label) {
            this.label = label;
            type = TYPE_TITLE;
        }

        InfoItem(String label, String content) {
            this.label = label;
            this.content = content;
            type = TYPE_CONTENT;
        }

        public int getType() {
            return type;
        }

        public String getLabel() {
            return label;
        }

        public String getContent() {
            if (TextUtils.isEmpty(content)) {
                return UNKNOW;
            }
            return content;
        }
    }

    public static List<InfoItem> getBaseInfo(Context context) {
        List<InfoItem> result = new ArrayList<>();
        // 基本信息
        result.add(new InfoItem("基本信息"));
        result.add(new InfoItem("是否ROOT", DeviceUtils.isDeviceRooted() ? "是" : "否"));
        result.add(new InfoItem("SDK版本", String.valueOf(DeviceUtils.getSDKVersionName())));
        result.add(new InfoItem("发布版本", Build.VERSION.RELEASE));
        result.add(new InfoItem("AndroidId", DeviceUtils.getAndroidID()));
        result.add(new InfoItem("设备厂商", DeviceUtils.getManufacturer()));
        result.add(new InfoItem("设备类型", DeviceUtils.getModel()));
        result.add(new InfoItem("产品型号", Build.PRODUCT));
        result.add(new InfoItem("主板型号", Build.BOARD));
        result.add(new InfoItem("显示型号", Build.DISPLAY));
        result.add(new InfoItem("序列号", Build.SERIAL));
        try{
            result.add(new InfoItem("IMEI", PhoneUtils.getIMEI()));
            result.add(new InfoItem("IMSI", PhoneUtils.getIMSI()));
        } catch (SecurityException e){
            //用户可能手动拒绝权限
        }
        result.add(new InfoItem("OCCID", getOCCID()));
        result.add(new InfoItem("SIM卡", PhoneUtils.isSimCardReady() ? "有" : "无"));

        // 网络相关
        result.add(new InfoItem("网络信息"));
        result.add(new InfoItem("Wifi名称", getWifiName(context)));
        result.add(new InfoItem("IP地址", NetworkUtils.getIPAddress(true)));
        result.add(new InfoItem("Mac地址", DeviceUtils.getMacAddress()));
        result.add(new InfoItem("运营商", NetworkUtils.getNetworkOperatorName()));
        result.add(new InfoItem("网络状态", getNetworkType()));
        result.add(new InfoItem("系统UA", System.getProperty("http.agent")));
        result.add(new InfoItem("聚美UA", getJMUserAgent(context)));

        //屏幕信息
        result.add(new InfoItem("屏幕信息"));
        result.add(new InfoItem("分辨率", getScreenSize(context)));
        result.add(new InfoItem("真实分辨率", ScreenUtils.getScreenWidth() + "x" + ScreenUtils.getScreenHeight()));
        result.add(new InfoItem("像素密度", String.valueOf(ScreenUtils.getScreenDensity())));
        result.add(new InfoItem("像素密度dp", String.valueOf(ScreenUtils.getScreenDensityDpi())));
        result.add(new InfoItem("屏幕尺寸", String.valueOf(getScreenInch(context) + "英寸")));

        // 硬件信息
        result.add(new InfoItem("硬件信息"));
        result.add(new InfoItem("CPU架构", getCPUType()));
        result.add(new InfoItem("CPU核数", String.valueOf(Runtime.getRuntime().availableProcessors())));
        result.add(new InfoItem("总内存", transferByte2MB(Runtime.getRuntime().totalMemory())));
        result.add(new InfoItem("最大内存", transferByte2MB(Runtime.getRuntime().maxMemory())));
        result.add(new InfoItem("剩余内存", transferByte2MB(Runtime.getRuntime().freeMemory())));
        result.add(new InfoItem("硬件信息", Build.HARDWARE));

        return result;
    }

    private static String getWifiName(Context context) {
        if (context == null) {
            return "";
        }
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr == null) {
            return "";
        }
        WifiInfo info = wifiMgr.getConnectionInfo();
        if (info == null || TextUtils.isEmpty(info.getSSID())) {
            return "";
        }
        return info.getSSID();
    }

    private static String getNetworkType() {
        NetworkUtils.NetworkType networkType = NetworkUtils.getNetworkType();
        switch (networkType) {
            case NETWORK_2G:
                return "2G";
            case NETWORK_3G:
                return "3G";
            case NETWORK_4G:
                return "4G";
            case NETWORK_WIFI:
                return "WIFI";
            case NETWORK_NO:
                return "无";
            case NETWORK_UNKNOWN:
                return UNKNOW;
        }
        return UNKNOW;
    }

    private static String getOCCID() {
        try {
            TelephonyManager tm =
                    (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
            return tm != null ? tm.getSimSerialNumber() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels + "x" + dm.heightPixels;
    }

    private static double getScreenInch(Context context) {
        try {
            int realWidth = 0, realHeight = 0;
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            if (Build.VERSION.SDK_INT >= 17) {
                Point size = new Point();
                display.getRealSize(size);
                realWidth = size.x;
                realHeight = size.y;
            } else if (Build.VERSION.SDK_INT < 17
                    && Build.VERSION.SDK_INT >= 14) {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } else {
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            }

            double inch = Math.sqrt((realWidth / metrics.xdpi) * (realWidth / metrics.xdpi) + (realHeight / metrics.ydpi) * (realHeight / metrics.ydpi));
            BigDecimal bd = new BigDecimal(inch);
            return bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static String getCPUType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return join(",", Build.SUPPORTED_ABIS);
        } else {
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                return Build.CPU_ABI + "," + Build.CPU_ABI2;
            }
            return Build.CPU_ABI;
        }
    }

    private static String getJMUserAgent(Context context) {
        // 暂时屏蔽子进程的方式
        /*// 这里要注意两种情况，区分是否新开了进程，通过解析AndroidManifest判断
        List<XMLParser> activities = ManifestParser.getModule(context, "activity");
        if (activities == null || activities.isEmpty()) {
            return null;
        }
        boolean isNewProcess = false;
        for (XMLParser activity : activities) {
            Map<String, String> attributeMap = activity.getAttributeMap();
            if (attributeMap == null) {
                continue;
            }
            if (!attributeMap.containsKey("name")) {
                continue;
            }
            if (attributeMap.get("name").endsWith("GTBasicInfoActivity")
                    && !TextUtils.isEmpty(attributeMap.get("process"))) {
                isNewProcess = true;
                break;
            }
        }
        if (isNewProcess) {
            if (GTRAnalysis.getGtrAnalysisResult() == null
                    || TextUtils.isEmpty(GTRAnalysis.getGtrAnalysisResult().jmUserAgent)) {
                // 默认返回系统的UA
                return System.getProperty("http.agent");
            }
            return GTRAnalysis.getGtrAnalysisResult().jmUserAgent;
        } else {
            return getJMUserAgentByReflect();
        }*/

        return getJMUserAgentByReflect();
    }

    /**
     * 因为涉及到多进程，所以反射不能跨进程，只能通过AIDL的方式获取
     */
    public static String getJMUserAgentByReflect() {
        try {
            Class clazz = Class.forName("com.jm.android.jmconnection.v2.header.JMHeaderStorage");
            Method getInstanceMethod = clazz.getDeclaredMethod("getInstance", new Class[]{});
            getInstanceMethod.setAccessible(true);
            Object headerStorage = getInstanceMethod.invoke(null, new Object[]{});
            Method getHeadersMethod = clazz.getDeclaredMethod("getHeaders", new Class[]{});
            getHeadersMethod.setAccessible(true);
            Object headers = getHeadersMethod.invoke(headerStorage, new Object[]{});
            if (headers instanceof Map) {
                Map<String, String> headersMap = (Map<String, String>) headers;
                if (headersMap != null && headersMap.containsKey("User-Agent")) {
                    return headersMap.get("User-Agent");
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        // 默认返回系统的UA
        return System.getProperty("http.agent");
    }

    private static String transferByte2MB(long b) {
        return String.valueOf(b >> 20) + "MB";
    }

    private static String join(String joinStr, String[] data) {
        if (joinStr == null) {
            joinStr = "";
        }
        if (data == null || data.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]);
            if (i != data.length - 1) {
                sb.append(joinStr);
            }
        }
        return sb.toString();
    }
}
