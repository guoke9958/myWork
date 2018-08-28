package com.cn.xa.qyw.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    private static SharedPreferences sp;

    private static PreferenceUtils pfUtils;

    private String prefName = "station";
    private static Context mContext;

    private PreferenceUtils() {
    }

    public static PreferenceUtils getInstance() {
        if (pfUtils == null) {
            pfUtils = new PreferenceUtils();
        }
        return pfUtils;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public static String getPrefString(String key,
                                       final String defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        return settings.getString(key, defaultValue);
    }

    public static void setPrefString(final String key,
                                     final String value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        settings.edit().putString(key, value).commit();
    }

    public static boolean getPrefBoolean(final String key,
                                         final boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        return settings.getBoolean(key, defaultValue);
    }

    public static boolean hasKey(final String key) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).contains(
                key);
    }

    public static void setPrefBoolean(final String key,
                                      final boolean value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        settings.edit().putBoolean(key, value).commit();
    }

    public static void setPrefInt(final String key,
                                  final int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        settings.edit().putInt(key, value).commit();
    }

    public static int getPrefInt(final String key,
                                 final int defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        return settings.getInt(key, defaultValue);
    }

    public static void setPrefFloat(final String key,
                                    final float value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        settings.edit().putFloat(key, value).commit();
    }

    public static float getPrefFloat(final String key,
                                     final float defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        return settings.getFloat(key, defaultValue);
    }

    public static void setSettingLong(final String key,
                                      final long value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        settings.edit().putLong(key, value).commit();
    }

    public static long getPrefLong(final String key,
                                   final long defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        return settings.getLong(key, defaultValue);
    }
}
