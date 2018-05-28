package com.yw.platform.broadcastReceiver.manger;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.yw.platform.beans.recevice.PolicyBean;
import com.yw.platform.global.MyApplication;
import com.yw.platform.tools.DeviceInfo;


/**
 * Created by cxb on 2018/5/22.
 */
public class GpsStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
           boolean state= DeviceInfo.getGPSState(context);
            PolicyBean policy=MyApplication.getInstance().getPolicy();
            if(state&&policy!=null&&"no".equals(policy.getGps())) {//关闭Gps
                DeviceInfo.openGPS(context,false);
            }
        }
    }
}