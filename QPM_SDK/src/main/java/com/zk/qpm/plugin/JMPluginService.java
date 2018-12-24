package com.jm.android.gt.plugin;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jm.android.gt.manager.JMGTManager;
import com.jm.android.gt.service.JMBaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 主 Service
 */
public class JMPluginService extends Service {


    private static final HashMap<Class<?>, JMBaseService> mServiceMap = new HashMap<Class<?>, JMBaseService>();
    private static final HashMap<Class<?>, ArrayList<JMBaseServiceConnection>> mServiceConnections =
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
        synchronized (JMBaseService.class) {
            for (JMBaseService service : mServiceMap.values()) {
                service.onDestroy(JMGTManager.getInstance().getContext());
            }
            mServiceMap.clear();
        }
        mServiceConnections.clear();
        super.onDestroy();
    }


    public static JMBaseService startService(JMBaseService service, Intent intent) {
        synchronized (JMBaseService.class) {
            if (mServiceMap.containsKey(service.getClass())) {
                ((JMBaseService) mServiceMap.get(service.getClass()))
                        .onStart(JMGTManager.getInstance().getContext(), intent);
            } else {
                service.onCreate(JMGTManager.getInstance().getContext());
                service.onStart(JMGTManager.getInstance().getContext(), intent);
                mServiceMap.put(service.getClass(), service);
            }
            return service;
        }
    }

    public static JMBaseService startService(JMBaseService service) {
        return startService(service, null);
    }

    public static boolean stopService(Class<? extends JMBaseService> claxx) {
        synchronized (JMBaseService.class) {
            if (mServiceMap.containsKey(claxx)) {
                List<JMBaseServiceConnection> serviceCons = mServiceConnections.get(claxx);
                if ((null == serviceCons) || (serviceCons.size() == 0)) {
                    JMBaseService theService = (JMBaseService) mServiceMap.get(claxx);
                    theService.onDestroy(JMGTManager.getInstance().getContext());
                    mServiceMap.remove(claxx);
                    mServiceConnections.remove(claxx);
                    return true;
                }
                return false;
            }

            return true;
        }
    }

    public static synchronized boolean stopService(JMBaseService service) {
        return stopService(service.getClass());
    }

    public static IBinder bindService(Class<? extends JMBaseService> claxx,
                                      JMBaseServiceConnection connection) {
        synchronized (JMBaseService.class) {
            IBinder binder = null;
            JMBaseService service = (JMBaseService) mServiceMap.get(claxx);
            if (service != null) {
                binder = service.getBinder(JMGTManager.getInstance().getContext());
                ArrayList<JMBaseServiceConnection> serviceCons = mServiceConnections
                        .get(claxx);
                if (null == serviceCons) {
                    serviceCons = new ArrayList<JMBaseServiceConnection>(1);
                    mServiceConnections.put(claxx, serviceCons);
                }
                serviceCons.add(connection);
            }

            return binder;
        }
    }

    public static void unBindService(Class<? extends JMBaseService> claxx,
                                     JMBaseServiceConnection connection) {
        synchronized (JMBaseService.class) {
            List<JMBaseServiceConnection> serviceCons = mServiceConnections.get(claxx);
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
