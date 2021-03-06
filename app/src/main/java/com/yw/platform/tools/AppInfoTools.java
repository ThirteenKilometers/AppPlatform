package com.yw.platform.tools;

import android.content.pm.PackageManager;

import com.yw.platform.R;
import com.yw.platform.activity.SetActivity;
import com.yw.platform.activity.UCAppActivity;
import com.yw.platform.global.Constants;
import com.yw.platform.global.MyApplication;
import com.yw.platform.model.AppInfo;
import com.yw.platform.utils.SharedPreferencesUtils;
import com.yw.platform.yhtext.activity.DocumentListActivity;
import com.yw.platform.yhtext.beans.commonbeans.AppBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxb on 2018/4/28.
 */

public class AppInfoTools {
    //应用类型
    public static final int APK_APP = 0;// 0表示apk应用
    public static final int NATIVE_APP = 1;// 1表示自带原生应用
    public static final int VIRTUAL_APP = 2;// 2表示虚拟应用
    public static final int PHONEGAP_APP = 3;// 3表示自带cordova应用(类似代办公文等)
    public static final int WEB_APP = 4;// 4.表示webApp

    public static List<AppInfo> appBean2Appinfo(List<AppBean> apps) {
        List<AppInfo> resList = new ArrayList<AppInfo>();
        AppInfo tmpInfo;
        for (int i = 0; i < apps.size(); i++) {
            tmpInfo = new AppInfo();
            if(apps.get(i)==null)continue;
            tmpInfo.appId =  apps.get(i).getAppTypeId();
            tmpInfo.appName = apps.get(i).getAppName();
            tmpInfo.iconUrl = apps.get(i).getIcoPath();
            String type =apps.get(i).getAppPkgType();
            tmpInfo.version = apps.get(i).getAppVersion();
            tmpInfo.appCode = apps.get(i).getAppTypeId();
            if ("APP_PAGE_ADAPTER".equals(type)) {
                //适配的资源
                tmpInfo.type = PHONEGAP_APP;
                tmpInfo.native_pro = apps.get(i).getAppPkgType();
                tmpInfo.localFilePath = Constants.downloadFile + tmpInfo.native_pro + ".zip";
                tmpInfo.downloadUrl = apps.get(i).getAppUrl();
                String cur_version = (String) SharedPreferencesUtils.getParam(Constants.SYSTEMPREFERENT,tmpInfo.appName + "_version", "0");
                if (!cur_version.equals(tmpInfo.version)) {
                    tmpInfo.needUpdate = true;
                }
            } else if ("APP_VIRTUAL".equals(type)) {
                tmpInfo.type = VIRTUAL_APP;
                tmpInfo.virtualResourceName =  apps.get(i).getAppName();
            } else if ("android".equals(type)) {
                tmpInfo.type = APK_APP;
                tmpInfo.localFilePath = Constants.downloadFile + tmpInfo.packageName +"_"+tmpInfo.version+".apk";
                tmpInfo.packageName = apps.get(i).getStartClassName();
                tmpInfo.downloadUrl ="";
                if (checkAppIsNeedUpdate(tmpInfo.packageName, tmpInfo.version)) {
                    tmpInfo.needUpdate = true;
                }
            } else if ("web".equals(type)) {
                tmpInfo.type = WEB_APP;
                tmpInfo.ssoState = false;
                tmpInfo.web_url = apps.get(i).getAppUrl();
                if (tmpInfo.ssoState) {
                    String config ="";
                }
            }else{
                continue;
            }
            resList.add(tmpInfo);
//            int icount=20;
//            while (icount>0){
//
//                icount--;
//            }

        }
        if (MyApplication.getInstance().getShowSafeUcWeb()) {
            tmpInfo = new AppInfo();
            tmpInfo.appName = "浏览器";
            tmpInfo.className = UCAppActivity.class;
            tmpInfo.type = NATIVE_APP;
            tmpInfo.appIcon = R.drawable.email_icon;
            resList.add(tmpInfo);
        }
        tmpInfo = new AppInfo();
        tmpInfo.appName = "设置";
        tmpInfo.className = SetActivity.class;
        tmpInfo.type = NATIVE_APP;
        tmpInfo.appIcon = R.drawable.ic_launcher;
        //resList.add(tmpInfo);

        tmpInfo = new AppInfo();
        tmpInfo.appName = "文档列表";
        tmpInfo.className = DocumentListActivity.class;
        tmpInfo.type = NATIVE_APP;
        tmpInfo.appIcon = R.drawable.ic_launcher;
        resList.add(tmpInfo);
        return resList;
    }

    /**
     * 描述：判断设备是否需要升级
     * @param packageName
     * @param newVersion
     * @return
     */
    public static boolean checkAppIsNeedUpdate(String packageName, String newVersion) {
        if (newVersion.isEmpty() || packageName.isEmpty()) {
            return false;
        }
        int versionCode = 0;
        try {
            versionCode = MyApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean needUpdate = false;
        int newV = 3;
        try {
            newV = Integer.parseInt(newVersion);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (versionCode < newV) {
            needUpdate = true;
        } else {
            needUpdate = false;
        }
        return needUpdate;
    }






}
