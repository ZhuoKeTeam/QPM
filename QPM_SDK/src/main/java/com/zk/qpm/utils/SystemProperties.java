package com.zk.qpm.utils;

import android.text.TextUtils;

import java.lang.reflect.Method;

public class SystemProperties {
    private static final Method getStringProperty = getMethod(getClass("android.os.SystemProperties"));

    private static Class<?> getClass(String name) {
        try {
            Class<?> cls = Class.forName(name);
            if (cls == null) {
                throw new ClassNotFoundException();
            }
            return cls;
        } catch (ClassNotFoundException e) {
            try {
                return ClassLoader.getSystemClassLoader().loadClass(name);
            } catch (ClassNotFoundException e1) {
                return null;
            }
        }
    }

    private static Method getMethod(Class<?> clz) {
        if (clz == null) return null;
        try {
            return clz.getMethod("get", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static String get(String key) {
        if (getStringProperty != null) {
            try {
                Object value = getStringProperty.invoke(null, key);
                if (value == null) {
                    return "";
                }
                return trimToEmpty(value.toString());
            } catch (Exception ignored) {
            }
        }
        return "";
    }

    public static String get(String key, String def) {
        if (getStringProperty != null) {
            try {
                String value = (String) getStringProperty.invoke(null, key);
                return defaultString(trimToNull(value), def);
            } catch (Exception ignored) {
            }
        }
        return def;
    }

    private static String defaultString(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }

    private static String trimToNull(String str) {
        String ts = trim(str);
        return TextUtils.isEmpty(ts) ? null : ts;
    }

    private static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    private static String trim(String str) {
        return str == null ? null : str.trim();
    }

}
