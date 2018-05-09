package com.yw.platform.tools;

import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by cxb on 2018/3/15.
 *
 */
public class AdminUtil {

    /**
     * 描述：清除锁屏密码
     * @param dpm
     */
    public static void clearPsd(DevicePolicyManager dpm){
        dpm.resetPassword("", 2);
    }

    /**
     * 描述：设置锁屏密码
     * @param dpm
     */
    public static void setPsd(DevicePolicyManager dpm,String psd){
        dpm.resetPassword(psd, 2);
    }

    /**
     * 描述：锁屏
     * @param dpm
     */
    public static void lockDesktop(DevicePolicyManager dpm){
        dpm.lockNow();
    }
    /**
     * 描述：恢复出厂设置
     */
    public static void WipeData(Context context ,DevicePolicyManager dpm) {
        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        //双清存储数据（包括外置sd卡），wipeData后重启
         //WIPE_EXTERNAL_STORAGE = 0x0001;
       // Toast.makeText(context, "代码屏蔽", Toast.LENGTH_SHORT).show();
    }
    /**
     * 相机设置
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void setCameraDisabled(DevicePolicyManager dpm,ComponentName admin,boolean disable) {
        dpm.setCameraDisabled(admin,disable);
    }

    public static void setStorageEncryption(DevicePolicyManager dpm,ComponentName admin,boolean encrypt) {
        dpm.setStorageEncryption(admin,encrypt);
    }

    /**
     * 设置应用程序权限
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void setCameraDisabledg(DevicePolicyManager dpm,ComponentName admin,boolean disable) {
//        dpm.setApplicationRestrictions(admin,disable);设置应用程序权限
//        dpm.setApplicationHidden()隐藏应用
//       dpm.clearDeviceOwnerApp();
       // dpm.enableSystemApp();
       // dpm.setApplicationRestrictions();
    }
    @TargetApi(21)
    public static void hiddenApp(DevicePolicyManager dpm,ComponentName admin,String packageName){
        dpm.setApplicationHidden(admin,packageName,true);
    }

    public void hian(){
//        PackageManager packageManager = getPackageManager();
//        ComponentName componentName = new ComponentName(this, StartActivity.class);
//        int res = packageManager.getComponentEnabledSetting(componentName);
//        if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
//                || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
//            // 隐藏应用图标
//            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP);
//        } else {
//            // 显示应用图标
//            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
//                    PackageManager.DONT_KILL_APP);
//        }
    }

    @TargetApi(21)
    public static void setApplicationRestrictions(DevicePolicyManager dpm,ComponentName admin,String packageName){
        Bundle set=new Bundle();
        set.putBoolean("android.permission.ACCESS_WIFI_STATE",true);
        set.putBoolean("android.permission.CHANGE_WIFI_STATE",true);
        set.putBoolean("android.permission.WAKE_LOCK",true);
        dpm.setApplicationRestrictions(admin,packageName,set);
    }

    public static void hidden(){
//        for (int i = 0; i < apps.size(); i++) {
//            LauncherActivityInfoCompat app = apps.get(i);
//            if("com.android.contacts".equals(apps.get(i).getApplicationInfo().packageName)
//                    || "com.android.mms".equals(apps.get(i).getApplicationInfo().packageName)){
//                continue;
//            }
//            mBgAllAppsList.add(new AppInfo(mContext, app, user, mIconCache, mLabelCache));
//        }
//        PackageManager p = getPackageManager();
//        p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }




}
