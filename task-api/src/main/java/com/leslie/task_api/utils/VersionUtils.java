package com.leslie.task_api.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.leslie.task_annotation.Constant;


/**
 * 对扫描的类做一个简单的缓存，isDebug开启，每次都会重新扫描，否则按照是否版本号有更新
 *
 * 作者：xjzhao
 * 时间：2021-07-01 22:21
 */
public class VersionUtils {
    private static String NEW_VERSION_NAME;
    private static int NEW_VERSION_CODE;

    public static boolean isNewVersion(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (null != packageInfo) {
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;

            SharedPreferences sp = context.getSharedPreferences(Constant.TASK_CACHE_SP, Context.MODE_PRIVATE);
            if (!versionName.equals(sp.getString(Constant.LAST_VERSION_NAME, null)) || versionCode != sp.getInt(Constant.LAST_VERSION_CODE, -1)) {
                // new version
                NEW_VERSION_NAME = versionName;
                NEW_VERSION_CODE = versionCode;
                logger.info("versionName : " + versionName + "\n" + "versionCode : " + versionCode);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static void updateVersion(Context context) {
        if (!android.text.TextUtils.isEmpty(NEW_VERSION_NAME) && NEW_VERSION_CODE != 0) {
            SharedPreferences sp = context.getSharedPreferences(Constant.TASK_CACHE_SP_KEY, Context.MODE_PRIVATE);
            sp.edit().putString(Constant.LAST_VERSION_NAME, NEW_VERSION_NAME).putInt(Constant.LAST_VERSION_CODE, NEW_VERSION_CODE).apply();
        }
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (Exception ex) {
            logger.error("Get package info error.");
        }

        return packageInfo;
    }
}
