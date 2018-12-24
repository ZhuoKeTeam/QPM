package com.zk.qpm.plugin;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.service.QPMBaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 主 Service
 */
public class QPMPluginService extends Service {


    private static final HashMap<Class<?>, QPMBaseService> mServiceMap = new HashMap<Class<?>, QPMBaseService>();
    private static final HashMap<Class<?>, ArrayList<QPMBaseServiceConnection>> mServiceConnections =
            new HashMap<>();
    private static Handler handler;

    public static Handler getRootHandler() {
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mServiceMap.clear();
        mServiceConnections.clear();
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        synchronized (QPMBaseService.class) {
            for (QPMBaseService service : mServiceMap.values()) {
                service.onDestroy(QPMManager.getInstance().getContext());
            }
            mServiceMap.clear();
        }
        mServiceConnections.clear();
        super.onDestroy();
    }


    public static QPMBaseService startService(QPMBaseService service, Intent intent) {
        synchronized (QPMBaseService.class) {
            if (mServiceMap.containsKey(service.getClass())) {
                ((QPMBaseService) mServiceMap.get(service.getClass()))
                        .onStart(QPMManager.getInstance().getContext(), intent);
            } else {
                service.onCreate(QPMManager.getInstance().getContext());
                service.onStart(QPMManager.getInstance().getContext(), intent);
                mServiceMap.put(service.getClass(), service);
            }
            return service;
        }
    }

    public static QPMBaseService startService(QPMBaseService service) {
        return startService(service, null);
    }

    public static boolean stopService(Class<? extends QPMBaseService> claxx) {
        synchronized (QPMBaseService.class) {
            if (mServiceMap.containsKey(claxx)) {
                List<QPMBaseServiceConnection> serviceCons = mServiceConnections.get(claxx);
                if ((null == serviceCons) || (serviceCons.size() == 0)) {
                    QPMBaseService theService = (QPMBaseService) mServiceMap.get(claxx);
                    theService.onDestroy(QPMManager.getInstance().getContext());
                    mServiceMap.remove(claxx);
                    mServiceConnections.remove(claxx);
                    return true;
                }
                return false;
            }

            return true;
        }
    }

    public static synchronized boolean stopService(QPMBaseService service) {
        return stopService(service.getClass());
    }

    public static IBinder bindService(Class<? extends QPMBaseService> claxx,
                                      QPMBaseServiceConnection connection) {
        synchronized (QPMBaseService.class) {
            IBinder binder = null;
            QPMBaseService service = (QPMBaseService) mServiceMap.get(claxx);
            if (service != null) {
                binder = service.getBinder(QPMManager.getInstance().getContext());
                ArrayList<QPMBaseServiceConnection> serviceCons = mServiceConnections
                        .get(claxx);
                if (null == serviceCons) {
                    serviceCons = new ArrayList<QPMBaseServiceConnection>(1);
                    mServiceConnections.put(claxx, serviceCons);
                }
                serviceCons.add(connection);
            }

            return binder;
        }
    }

    public static void unBindService(Class<? extends QPMBaseService> claxx,
                                     QPMBaseServiceConnection connection) {
        synchronized (QPMBaseService.class) {
            List<QPMBaseServiceConnection> serviceCons = mServiceConnections.get(claxx);
            if (null != serviceCons)
                serviceCons.remove(connection);
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
