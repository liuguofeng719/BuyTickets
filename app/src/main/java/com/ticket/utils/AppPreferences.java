package com.ticket.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;
import com.ticket.TicketsApplication;

public class AppPreferences {

    private static SharedPreferences preferences;

    static {
        preferences = TicketsApplication.getTicketApplication().getSharedPreferences("local_kv", Context.MODE_PRIVATE);
    }
    private AppPreferences() {

    }

    public static void remove(String key) {
        preferences.edit().remove(key).commit();
    }

    public static void clearAll() {
        preferences.edit().clear().commit();
    }

    /**
     * 保存一个实体，类名为key
     */
    public static void putObject(Object obj) {
        putObject(obj.getClass().getName(), obj);
    }

    /**
     * 获取一个存储实体
     */
    public static <T> T getObject(Class<T> c) {
        return getObject(c.getName(), c);
    }

    public static void putString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        return preferences.getString(key, "");
    }

    public static int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public static long getLong(String key, long maxValue) {
        return preferences.getLong(key, maxValue);
    }

    public static float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }

    public static void putLong(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    public static void putInt(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    public static void putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).commit();
    }

    private static void putObject(String key, Object object) {
        if (object == null)
            return;
        String value = new GsonBuilder().create().toJson(object);
        preferences.edit().putString(key, value).commit();
    }

    private static <T> T getObject(String key, Class<T> c) {
        String value = preferences.getString(key, "");
        if (value.isEmpty())
            return null;
        T t = new GsonBuilder().create().fromJson(value, c);
        return t;
    }
}
