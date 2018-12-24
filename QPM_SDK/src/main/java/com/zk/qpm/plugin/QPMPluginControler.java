package com.zk.qpm.plugin;

import android.content.Intent;
import android.os.IBinder;

import com.zk.qpm.service.QPMBaseService;


public class QPMPluginControler {
    protected QPMBaseServiceConnection mServiceConn = new QPMBaseServiceConnection(QPMPluginControler.class);

    /**
     * 启动插件服务
     * @param service 插件服务
     * @return 插件服务对象(即传进去的参数)
     */
    public final QPMBaseService startService(QPMBaseService service) {
        return QPMPluginService.startService(service);
    }

    /**
     * 启动插件服务
     * @param service 插件服务
     * @param intent 传入的intent
     * @return 插件服务对象(即传进去的参数)
     */
    public final QPMBaseService startService(QPMBaseService service, Intent intent) {
        return QPMPluginService.startService(service, intent);
    }

    /**
     * 停止插件服务,之前要先调用unBindService方法断开服务
     * @param service 插件服务
     */
    public final void stopService(QPMBaseService service){
        QPMPluginService.stopService(service.getClass());
    }

    /**
     * 断开插件服务
     * @param claxx 插件服务的类
     */
    public final void unBindService(Class<? extends QPMBaseService> claxx) {
        QPMPluginService.unBindService(claxx, mServiceConn);
        mServiceConn = null;
    }

    /**
     * 绑定插件服务
     * @param claxx 插件服务的类
     * @return 绑定接口
     */
    public final IBinder bindService(Class<? extends QPMBaseService> claxx) {
        return QPMPluginService.bindService(claxx, mServiceConn);
    }
}
