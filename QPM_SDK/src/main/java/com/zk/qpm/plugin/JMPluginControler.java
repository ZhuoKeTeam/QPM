package com.jm.android.gt.plugin;

import android.content.Intent;
import android.os.IBinder;

import com.jm.android.gt.service.JMBaseService;


public class JMPluginControler {
    protected JMBaseServiceConnection mServiceConn = new JMBaseServiceConnection(JMPluginControler.class);

    /**
     * 启动插件服务
     * @param service 插件服务
     * @return 插件服务对象(即传进去的参数)
     */
    public final JMBaseService startService(JMBaseService service) {
        return JMPluginService.startService(service);
    }

    /**
     * 启动插件服务
     * @param service 插件服务
     * @param intent 传入的intent
     * @return 插件服务对象(即传进去的参数)
     */
    public final JMBaseService startService(JMBaseService service, Intent intent) {
        return JMPluginService.startService(service, intent);
    }

    /**
     * 停止插件服务,之前要先调用unBindService方法断开服务
     * @param service 插件服务
     */
    public final void stopService(JMBaseService service){
        JMPluginService.stopService(service.getClass());
    }

    /**
     * 断开插件服务
     * @param claxx 插件服务的类
     */
    public final void unBindService(Class<? extends JMBaseService> claxx) {
        JMPluginService.unBindService(claxx, mServiceConn);
        mServiceConn = null;
    }

    /**
     * 绑定插件服务
     * @param claxx 插件服务的类
     * @return 绑定接口
     */
    public final IBinder bindService(Class<? extends JMBaseService> claxx) {
        return JMPluginService.bindService(claxx, mServiceConn);
    }
}
