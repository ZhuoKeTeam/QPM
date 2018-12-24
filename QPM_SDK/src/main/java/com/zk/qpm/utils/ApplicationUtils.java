package com.zk.qpm.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ApplicationUtils
 * 当前类的代码用了大量反射，不保证一定是安全有效的。
 * 不建议在release状态调用这个类的任何功能，如果确实需要用到，请处理好调用异常。
 *
 * @author xl
 * @version V1.0
 * @since 06/01/2017
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class ApplicationUtils {
    private static final String TAG = "ApplicationUtils";

    /**
     * 重启App，如果可以拿到application，推荐使用{@link #restartApplication(Context, int)}方法，
     *
     * @param delay 当前App被杀死之后，延迟多久重新启动。
     * @return true 重启成功，false 重启失败
     */
    public static boolean restartApplication(int delay) {
        try {
            restartApplication(getApplication(), delay);
            return true;
        } catch (ReflectUtils.ReflectException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 重启App
     *
     * @param context application
     */
    public static void restartApplication(Context context) {
        restartApplication(context, 500);
    }

    /**
     * 重启App
     *
     * @param context application
     * @param delay   当前App被杀死之后，延迟多久重新启动。
     */
    public static void restartApplication(Context context, int delay) {
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context.getApplicationContext(), 0
                , intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int type = AlarmManager.ELAPSED_REALTIME_WAKEUP;
        long triggerTime = SystemClock.elapsedRealtime() + delay;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mgr.setExact(type, triggerTime, restartIntent);
        } else {
            mgr.set(type, triggerTime, restartIntent);
        }
        Process.killProcess(Process.myPid());
    }

    /**
     * 获取当前App所有Activity的引用。
     * <p>
     * 注意：当前List是无序List，Activity在List中的排列顺序并不代表
     * Activity在ActivityStack中的顺序。如果需要获取栈顶Activity，请调用{@link #getTopActivity()}方法。
     *
     * @return Activity列表，获取不到的时候将返回null。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static List<Activity> getActivities() throws ReflectUtils.ReflectException {
        return getActivities(getActivitiesInActivityThread());
    }

    /**
     * 获取当前App所有Activity的引用。
     * <p>
     * 注意：当前List是无序List，Activity在List中的排列顺序并不代表
     * Activity在ActivityStack中的顺序。如果需要获取栈顶Activity，请调用{@link #getTopActivity()}方法。
     *
     * @param mActivities ActivityThread中的mActivities
     * @return Activity列表，获取不到的时候将返回null。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static List<Activity> getActivities(Object mActivities) throws ReflectUtils.ReflectException {
        if (mActivities != null) {
            return toActivityList(mActivities);
        }
        return null;
    }

    /**
     * 获取栈顶Activity引用。
     *
     * @return 栈顶Activity，获取不到的时候将返回null。
     */
    public static Activity getTopActivity() {
        try {
            List<Activity> activities = getActivities();
            return getTopActivity(activities);
        } catch (ReflectUtils.ReflectException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取栈顶Activity引用。
     *
     * @param activities 所有的Activity列表
     * @return 栈顶Activity，获取不到的时候将返回null。
     */
    public static Activity getTopActivity(List<Activity> activities) {
        try {
            Activity activity = getTopActivityByIsTopOfTask(activities);
            if (activity != null) {
                return activity;
            }
        } catch (ReflectUtils.ReflectException e) {
            e.printStackTrace();
        }
        try {
            Activity activity = getTopActivityByResume(activities);
            if (activity != null) {
                return activity;
            }
        } catch (ReflectUtils.ReflectException e) {
            e.printStackTrace();
        }

        try {
            Activity activity = getTopActivityByActivityManager(getApplication(), activities);
            if (activity != null) {
                return activity;
            }
        } catch (ReflectUtils.ReflectException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过ActivityManager拿到栈顶Activity的ClassName，然后跟列表中的Activity比对，获取栈顶Activity。
     * 注意：这种方式有个很严重的缺陷，如果某个Activity在栈里多次出现将导致判断错误，这时候将返回null。
     * 可靠性相对一般，不会返回错误结果。
     *
     * @param application application
     * @param activities  所有的Activity列表
     * @return 栈顶Activity，获取不到的时候将返回null，不会返回错误结果。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static Activity getTopActivityByActivityManager(Context application
            , List<Activity> activities) throws ReflectUtils.ReflectException {
        ActivityManager activityManager = (ActivityManager) application
                .getSystemService(Context.ACTIVITY_SERVICE);
        String topActivity = null;
        String packageName = application.getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
            for (int i = 0, size = appTasks.size(); i < size; i++) {
                ActivityManager.RecentTaskInfo taskInfo = appTasks.get(size - 1 - i).getTaskInfo();
                if (packageName.equals(taskInfo.baseActivity.getPackageName())) {
                    topActivity = taskInfo.topActivity.getClassName();
                }
            }
        }
        if (TextUtils.isEmpty(topActivity)) {
            Log.i(TAG, "尝试通过getTopActivityByActivityManager获取Activity失败");
            return null;
        }

        boolean check = false;
        Activity result = null;
        if (activities != null) {
            for (Activity activity : activities) {
                if (topActivity.equals(activity.getClass().getName())) {
                    if (check) {
                        // 出现重复Activity，返回null
                        Log.i(TAG, "尝试通过getTopActivityByActivityManager获取Activity失败");
                        return null;
                    }
                    check = true;
                    result = activity;
                }
            }
        }
        if (result == null) {
            Log.w(TAG, "尝试通过getTopActivityByActivityManager获取Activity失败");
        } else {
            Log.i(TAG, "尝试通过getTopActivityByActivityManager获取Activity成功");
        }
        return result;
    }

    /**
     * 通过反射Activity的isTopOfTask方法来获取栈顶Activity引用。可靠性相对最好，不会返回错误结果。
     *
     * @param activities 所有的Activity列表
     * @return 栈顶Activity，获取不到的时候将返回null，不会返回错误结果。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static Activity getTopActivityByIsTopOfTask(List<Activity> activities)
            throws ReflectUtils.ReflectException {
        for (Activity activity : activities) {
            boolean isTop = (boolean) ReflectUtils.reflect(activity, "isTopOfTask()");
            if (isTop) {
                Log.i(TAG, "尝试通过getTopActivityByIsTopOfTask获取Activity成功");
                return activity;
            }
        }
        Log.w(TAG, "尝试通过getTopActivityByIsTopOfTask获取Activity失败");
        return null;
    }

    /**
     * 通过反射Activity的isTopOfTask方法来获取栈顶Activity引用。可靠性相对一般。
     *
     * @param activities 所有的Activity列表
     * @return 栈顶Activity，获取不到的时候将返回null，可能会返回错误结果。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static Activity getTopActivityByResume(List<Activity> activities)
            throws ReflectUtils.ReflectException {
        for (Activity activity : activities) {
            boolean isTop = (boolean) ReflectUtils.reflect(activity, "mResumed");
            if (isTop) {
                Log.i(TAG, "尝试通过getTopActivityByResume获取Activity成功");
                return activity;
            }
        }
        Log.w(TAG, "尝试通过getTopActivityByResume获取Activity失败");
        return null;
    }

    /**
     * 获取ActivityThread对象
     *
     * @return ActivityThread被添加hide，所以这里是返回Object类型。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static Object getActivityThread() throws ReflectUtils.ReflectException {
        return ReflectUtils.reflect(null, "android.app.ActivityThread#currentActivityThread()");
    }

    /**
     * 获取当前App的Application对象
     *
     * @return 当前App的Application对象。
     * @throws ReflectUtils.ReflectException 可能会发生异常，强制要求处理异常情况。
     */
    public static Application getApplication() throws ReflectUtils.ReflectException {
        return (Application) ReflectUtils.reflect(null, "android.app.ActivityThread#currentApplication()");
    }

    private static Object getActivitiesInActivityThread() throws ReflectUtils.ReflectException {
        return ReflectUtils.reflect(null, "android.app.ActivityThread#currentActivityThread().mActivities");
    }

//    public static Object getActivities(Application application) throws Exception {
//        return ReflectUtils.reflect(application, "mLoadedApk.mActivityThread.mActivities");
//            Class<Application> applicationClass = Application.class;
//            Field mLoadedApkField = applicationClass.getDeclaredField("mLoadedApk");
//            mLoadedApkField.setAccessible(true);
//            Object mLoadedApk = mLoadedApkField.get(application);
//            Class<?> mLoadedApkClass = mLoadedApk.getClass();
//            Field mActivityThreadField = mLoadedApkClass.getDeclaredField("mActivityThread");
//            mActivityThreadField.setAccessible(true);
//            Object mActivityThread = mActivityThreadField.get(mLoadedApk);
//            Class<?> mActivityThreadClass = mActivityThread.getClass();
//            Field mActivitiesField = mActivityThreadClass.getDeclaredField("mActivities");
//            mActivitiesField.setAccessible(true);
//            return mActivitiesField.get(mActivityThread);
//    }

    public static List<Object> toKeyList(Object activities) {
        if (activities == null) {
            throw new NullPointerException("mActivities can't be null");
        }
        List<Object> list = new ArrayList<>();
        if (activities instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> arrayMap = (Map<Object, Object>) activities;
            for (Map.Entry<Object, Object> entry : arrayMap.entrySet()) {
                Object key = entry.getKey();
                list.add(key);
            }
        }
        return list;
    }

    public static List<Object> toValueList(Object activities) throws Exception {
        if (activities == null) {
            throw new NullPointerException("mActivities can't be null");
        }
        List<Object> list = new ArrayList<>();
        if (activities instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> arrayMap = (Map<Object, Object>) activities;
            for (Map.Entry<Object, Object> entry : arrayMap.entrySet()) {
                Object value = entry.getValue();
                list.add(value);
            }
        }
        return list;
    }

    private static List<Activity> toActivityList(Object activities) throws ReflectUtils.ReflectException {
        if (activities == null) {
            return null;
        }
        List<Activity> list = new ArrayList<>();
        if (activities instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> arrayMap = (Map<Object, Object>) activities;
            for (Map.Entry<Object, Object> entry : arrayMap.entrySet()) {
                Object value = entry.getValue();
                Object o = ReflectUtils.reflect(value, "activity");
//                    Class<?> activityClientRecordClass = value.getClass();
//                    Field activityField = activityClientRecordClass.getDeclaredField("activity");
//                    activityField.setAccessible(true);
//                    Object o = activityField.get(value);
                list.add((Activity) o);
            }
        }
        return list;
    }
}
