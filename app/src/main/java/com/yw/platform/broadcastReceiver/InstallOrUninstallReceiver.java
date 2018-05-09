package com.yw.platform.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lidroid.xutils.util.LogUtils;
import com.yw.platform.global.Constants;
import com.yw.platform.utils.DeviceUtil;

import java.io.File;

/**
 * Created by panda on 15-1-23.
 */
public class InstallOrUninstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        //接收安装广播
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)||intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            System.out.println("安装了:" + packageName + "包名的程序");
            DeviceUtil.deleteDir(new File(Constants.downloadFile+packageName+".apk"));
        }else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            //接收卸载广播
            String packageName = intent.getData().getSchemeSpecificPart();
            System.out.println("卸载了:"  + packageName + "包名的程序");
        }

    }
}
