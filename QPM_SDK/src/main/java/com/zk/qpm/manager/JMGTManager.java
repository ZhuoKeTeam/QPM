package com.jm.android.gt.manager;

import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.blankj.utilcode.util.Utils;
import com.jm.android.gt.analysis.JMH5MonitorAnalysis;
import com.jm.android.gt.executor.JMGetNetworkInfoExecutor;
import com.jm.android.gt.floatview.renderer.JMTemplateCustomRenderer;

/**
 * 聚美 GT 管理器
 */
public class JMGTManager {

    private Context mContext;

    private static JMGTManager instance = null;
    private JMGetNetworkInfoExecutor okhttpInterceptor;

    private JMGTManager() {
    }

    public static JMGTManager getInstance() {
        if (instance == null) {
            synchronized (JMGTManager.class) {
                JMGTManager temp = instance;
                if (temp == null) {
                    temp = new JMGTManager();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    public void init(Application application) {
        Utils.init(application);
        mContext = application.getApplicationContext();

        JMGTSortManager.getInstance().init();
        JMGTModeManager.getInstance().init();
        JMGTSwitchManager.getInstance().init();
        JMGTRAnalysisManager.getInstance().start(mContext, Process.myPid(), mContext.getPackageName());
    }

    public Context getContext() {
        return mContext;
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

    public JMGetNetworkInfoExecutor getOkHttpInterceptor() {
        if (JMGTManager.getInstance().isImplementOkHttp() && okhttpInterceptor == null) {
            okhttpInterceptor = new JMGetNetworkInfoExecutor();
        }
        return okhttpInterceptor;
    }

    public void setH5MonitorData(String url, long whitePageTime) {
        JMH5MonitorAnalysis analysis = new JMH5MonitorAnalysis();
        analysis.onCollectH5MonitorInfo(url, whitePageTime);
    }

    public void showBigText(String identity, String text) {
        JMGTApiTemplateManager.getInstance().addBigTextRenderer(identity, text);
    }

    public void showKeyValue(String identity, String key, String value) {
        JMGTApiTemplateManager.getInstance().addKeyValueRenderer(identity, key, value);
    }

    public void showKeyPic(String identity, String key, int picId) {
        JMGTApiTemplateManager.getInstance().addKeyPicRenderer(identity, key, picId);
    }

    private void showKeyPic(String identity, String key, String picUrl) {
        JMGTApiTemplateManager.getInstance().addKeyPicRenderer(identity, key, picUrl);
    }

    public void showPicValue(String identity, int picId, String value) {
        JMGTApiTemplateManager.getInstance().addPicValueRenderer(identity, picId, value);
    }

    private void showPicValue(String identity, String picUrl, String value) {
        JMGTApiTemplateManager.getInstance().addPicValueRenderer(identity, picUrl, value);
    }

    public void showCustom(String identity, JMTemplateCustomRenderer renderer) {
        JMGTApiTemplateManager.getInstance().addCustomRenderer(identity, renderer);
    }

    public void dismiss(String identity) {
        JMGTApiTemplateManager.getInstance().removeTemplateRendererByIdentity(identity);
    }
}
