package com.zk.qpm.manager;

import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.blankj.utilcode.util.Utils;
import com.zk.qpm.analysis.QPMH5MonitorAnalysis;
import com.zk.qpm.executor.QPMGetNetworkInfoExecutor;
import com.zk.qpm.floatview.renderer.QPMTemplateCustomRenderer;

/**
 * 聚美 GT 管理器
 */
public class QPMManager {

    private Context mContext;

    private static volatile QPMManager instance = null;
    private QPMGetNetworkInfoExecutor okhttpInterceptor;

    private QPMManager() {
    }

    public static QPMManager getInstance() {
        if (instance == null) {
            synchronized (QPMManager.class) {
                QPMManager temp = instance;
                if (temp == null) {
                    temp = new QPMManager();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    public void init(Application application) {
        Utils.init(application);
        mContext = application.getApplicationContext();

        QPMSortManager.getInstance().init();
        QPMModeManager.getInstance().init();
        QPMSwitchManager.getInstance().init();
        QPMRAnalysisManager.getInstance().start(mContext, Process.myPid(), mContext.getPackageName());
    }

    public Context getContext() {
        return mContext;
    }


    public boolean floatViewShow() {
        return QPMFloatViewManager.getInstance().floatViewShow();
    }

    public void floatViewHide() {
        QPMFloatViewManager.getInstance().floatViewHide();
    }

    public boolean isImplementOkHttp() {
        try {
            Class clazz = Class.forName("okhttp3.Interceptor");
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }

    public QPMGetNetworkInfoExecutor getOkHttpInterceptor() {
        if (QPMManager.getInstance().isImplementOkHttp() && okhttpInterceptor == null) {
            okhttpInterceptor = new QPMGetNetworkInfoExecutor();
        }
        return okhttpInterceptor;
    }

    public void setH5MonitorData(String url, long whitePageTime) {
        QPMH5MonitorAnalysis analysis = new QPMH5MonitorAnalysis();
        analysis.onCollectH5MonitorInfo(url, whitePageTime);
    }

    public void showBigText(String identity, String text) {
        QPMApiTemplateManager.getInstance().addBigTextRenderer(identity, text);
    }

    public void showKeyValue(String identity, String key, String value) {
        QPMApiTemplateManager.getInstance().addKeyValueRenderer(identity, key, value);
    }

    public void showKeyPic(String identity, String key, int picId) {
        QPMApiTemplateManager.getInstance().addKeyPicRenderer(identity, key, picId);
    }

    public void showPicValue(String identity, int picId, String value) {
        QPMApiTemplateManager.getInstance().addPicValueRenderer(identity, picId, value);
    }

    public void showCustom(String identity, QPMTemplateCustomRenderer renderer) {
        QPMApiTemplateManager.getInstance().addCustomRenderer(identity, renderer);
    }

    public void dismiss(String identity) {
        QPMApiTemplateManager.getInstance().removeTemplateRendererByIdentity(identity);
    }
}
