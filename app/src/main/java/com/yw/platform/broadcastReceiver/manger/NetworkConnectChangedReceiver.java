package com.yw.platform.broadcastReceiver.manger;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.yw.platform.beans.recevice.PolicyBean;
import com.yw.platform.broadcastReceiver.AdminReceiver;
import com.yw.platform.global.MyApplication;
import com.yw.platform.service.DeviceManager;

/**
 * 描述：网络状态监听
 * Created by cxb on 2018/3/20.
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver {

    private static final String TAG = "xujun";
    public static final String TAG1 = "xxx";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 这个监听wifi的打开与关闭，与wifi的连接无关
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState ) {
                case WifiManager.WIFI_STATE_ENABLED://已打开
                    toggleWiFi(context);
                    break;
                case WifiManager.WIFI_STATE_ENABLING://打开中
                    toggleWiFi(context);
                    break;
                case WifiManager.WIFI_STATE_DISABLED://已关闭
                    break;
                case WifiManager.WIFI_STATE_DISABLING://关闭中
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN://未知
                    toggleWiFi(context);
                    break;
            }
        }
    }

    private void toggleWiFi(Context context) {
//        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//        ComponentName componentName = new ComponentName(context, AdminReceiver.class);
//        DeviceManager.getInstance(context).setApplicationRestrictions(devicePolicyManager,componentName,context.getPackageName());
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        PolicyBean policy= MyApplication.getInstance().getPolicy();
        if("no".equals(policy.getWifiVisit())){//强制关闭wifi
            wm.setWifiEnabled(false);
        }

    }


}