package com.yw.platform.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yw.platform.global.MyApplication;


/**
 * @author xiaanming
 */
public class SharedPreferencesUtils {
    public static final String FILE_NAME = "default";

    public static void setParam(String key, Object object) {
        setParam(FILE_NAME, key, object);
    }

    public static void setParam(String name, String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = getBaseContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }

    public static Object getParam(String key, Object defaultObject) {
        return getParam(FILE_NAME, key, defaultObject);
    }

    public static Object getParam(String filename, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = getBaseContext().getSharedPreferences(filename, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    private static Context getBaseContext() {
        return MyApplication.getInstance();
    }
}