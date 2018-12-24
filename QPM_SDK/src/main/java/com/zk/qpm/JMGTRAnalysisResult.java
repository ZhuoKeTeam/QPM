package com.jm.android.gt;

import java.util.ArrayList;
import java.util.List;

/**
 * GTR分析结果数据类
 */
public class JMGTRAnalysisResult {

    public static final Object SYNCHRONIZED_NETWORKINFO = new Object();

    public AppInfo appInfo = new AppInfo();
    public OtherInfo otherInfo = new OtherInfo();
    public DeviceInfo deviceInfo = new DeviceInfo();
    public FrameInfo frameInfo = new FrameInfo();
    public CMNTInfo cmntInfo = new CMNTInfo();
    public List<NetworkInfo> networkInfos = new ArrayList<>();
    public ActivityStackInfo activityStackInfos = new ActivityStackInfo();
    public ScreenRecorderStatusInfo screenRecorderStatusInfo = new ScreenRecorderStatusInfo();
    public H5MonitorInfo h5MonitorInfo = new H5MonitorInfo();

    public static class OtherInfo {
        public int pId = -1;
        public int threadCount = 0;
    }

    public static class AppInfo {
        //APP信息
        public String appName = null;
        public String packageName = null;
        public String versionName = null;
        public int versionCode = -1;
        public int gtrVersionCode = -1;
        public long startTestTime = -1;
        public int mainThreadId = -1;
    }

    public static class DeviceInfo {
        //Device信息：
        public String vendor = null;
        public String model = null;
        public String sdkName = null;
        public int sdkInt = -1;
    }

    public static class FrameInfo {
        // 帧率信息
        public List<Long> drawTimeInSecond = new ArrayList<>();
        public int fps;
        // 大卡顿次数
        public int bigBlockTime;
        // 低流畅次数
        public int lowSMTime;
        // 是否处于卡顿期间
        public boolean isLowSM;
    }

    public static class CMNTInfo {
        public double cpu;
        public double memory;
        public double flowUp;
        public double flowDown;
        public double flowUpSpeed;
        public double flowDownSpeed;
        public int threadCount;
        public int gtThreadCount;
    }

    public static class NetworkInfo {
        public String originUrl;
        public String method;
        public long requestSize;
        public long requestTime;
        public String requestHeader;
        public String requestContent;
        public long responseSize;
        public String responseHeader;
        public String responseContent;
        public String responseCode;
        public long currentTime;
    }

    public static class ActivityStackInfo {
        public String topActivityName = null;
        public List<String> activityNames = new ArrayList<>();
    }

    public static class ScreenRecorderStatusInfo {
        public boolean startStatus;
        public long startTime;
    }

    public static class H5MonitorInfo {
        public String url = "";
        public long whitePageTime;
    }

}
