package com.dennis.changelanguage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;

import com.google.gson.Gson;

import java.util.Locale;

public class LocaleUtils {
    /**
     * 保存SharedPreferences的文件名
     */
    private static final String LOCALE_FILE = "LOCALE_FILE";
    /**
     * 保存Locale的key
     */
    private static final String LOCALE_KEY = "LOCALE_KEY";

    /**
     * 获取用户设置的Locale
     *
     * @param pContext Context
     * @return Locale
     */
    public static Locale getUserLocale(Context pContext) {
        SharedPreferences _SpLocale = pContext.getSharedPreferences(LOCALE_FILE, Context.MODE_PRIVATE);
        String _LocaleJson = _SpLocale.getString(LOCALE_KEY, "");
        return jsonToLocale(_LocaleJson);
    }

    /**
     * 获取当前的Locale
     *
     * @param pContext Context
     * @return Locale
     */
    public static Locale getCurrentLocale(Context pContext) {
        Locale _Locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0有多语言设置获取顶部的语言
            _Locale = pContext.getResources().getConfiguration().getLocales().get(0);
        } else {
            _Locale = pContext.getResources().getConfiguration().locale;
        }
        return _Locale;
    }

    /**
     * 保存用户设置的Locale
     *
     * @param pContext    Context
     * @param pUserLocale Locale
     */
    public static void saveUserLocale(Context pContext, Locale pUserLocale) {
        SharedPreferences _SpLocal = pContext.getSharedPreferences(LOCALE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor _Edit = _SpLocal.edit();
        String _LocaleJson = localeToJson(pUserLocale);
        _Edit.putString(LOCALE_KEY, _LocaleJson);
        _Edit.apply();
    }

    /**
     * Locale转成json
     *
     * @param pUserLocale UserLocale
     * @return json String
     */
    private static String localeToJson(Locale pUserLocale) {
        Gson _Gson = new Gson();
        return _Gson.toJson(pUserLocale);
    }

    /**
     * json转成Locale
     *
     * @param pLocaleJson LocaleJson
     * @return Locale
     */
    private static Locale jsonToLocale(String pLocaleJson) {
        Gson _Gson = new Gson();
        return _Gson.fromJson(pLocaleJson, Locale.class);
    }

    /**
     * 更新Locale
     *
     * @param pContext       Context
     * @param pNewUserLocale New User Locale
     */
    public static void updateLocale(Context pContext, Locale pNewUserLocale) {
        if (needUpdateLocale(pContext, pNewUserLocale)) {
            Configuration _Configuration = pContext.getResources().getConfiguration();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(pNewUserLocale);
            } else {
                _Configuration.locale = pNewUserLocale;
            }
            DisplayMetrics _DisplayMetrics = pContext.getResources().getDisplayMetrics();
            pContext.getResources().updateConfiguration(_Configuration, _DisplayMetrics);
            saveUserLocale(pContext, pNewUserLocale);
        }
    }

    /**
     * 判断需不需要更新
     *
     * @param pContext       Context
     * @param pNewUserLocale New User Locale
     * @return true / false
     */
    public static boolean needUpdateLocale(Context pContext, Locale pNewUserLocale) {
        return pNewUserLocale != null && !getCurrentLocale(pContext).equals(pNewUserLocale);
    }

    /**
     * 在应用内切换了语言
     *
     * @param ctx
     * @param newConfig
     */
    public static void onConfigurationChanged(Context ctx, Configuration newConfig) {
        Locale _UserLocale = getUserLocale(ctx);
        //系统语言改变了应用保持之前设置的语言
        if (_UserLocale != null) {
            Locale.setDefault(_UserLocale);
            Configuration _Configuration = new Configuration(newConfig);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(_UserLocale);
            } else {
                _Configuration.locale = _UserLocale;
            }
            ctx.getResources().updateConfiguration(_Configuration, ctx.getResources().getDisplayMetrics());
        }
    }

    /**
     * 初始化
     * @param ctx
     */
    public static void initLocale(Context ctx){
        Locale _UserLocale= getUserLocale(ctx);
        LocaleUtils.updateLocale(ctx, _UserLocale);
    }

}
