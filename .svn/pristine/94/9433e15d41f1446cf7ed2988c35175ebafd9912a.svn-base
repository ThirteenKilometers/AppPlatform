package com.yw.platform.tools.info;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.yw.platform.global.MyApplication;

/**
 * Created by cxb on 2018/5/15.
 */

public class AppUtil {

    /**
     * 描述：获取应用程序名称
     */
    public static synchronized String getAppName() {
        try {
            PackageManager packageManager = MyApplication.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return MyApplication.getInstance().getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 描述：当前应用的版本名称
     */
    public static synchronized String getVersionName() {
        try {
            PackageManager packageManager = MyApplication.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    MyApplication.getInstance().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *描述：当前应用的版本号
     */
    public static synchronized int getVersionCode() {
        try {
            PackageManager packageManager = MyApplication.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    MyApplication.getInstance().getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     *描述：当前应用的版本包名
     */
    public static synchronized String getPackageName() {
        try {
            PackageManager packageManager = MyApplication.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    MyApplication.getInstance().getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图标 bitmap
     */
    public static synchronized Bitmap getBitmap() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = MyApplication.getInstance().getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    MyApplication.getInstance().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

}

