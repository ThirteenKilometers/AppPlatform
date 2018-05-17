package com.yw.platform.appmanger;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.yw.platform.beans.recevice.PolicyBean;
import com.yw.platform.global.MyApplication;
import com.yw.platform.service.DeviceManager;

/**
 * 描述策略处理类
 * Created by cxb on 2018/5/16.
 */
public class PolicyHande {

    /**
     * 描述：更新策略后调用
     */
    public static void updatePolicy(Context context){
        PolicyBean policy=MyApplication.getInstance().getPolicy();
        if(policy!=null){
            if("no".equals(policy.getCamera())){
                DeviceManager.getInstance(context).setCameraDisabled(true);
            }else{
                DeviceManager.getInstance(context).setCameraDisabled(false);
            }
            if("no".equals(policy.getBluetooth())){//蓝牙不可用
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(bluetoothAdapter!=null){
                    bluetoothAdapter.disable();
                }
            }
            if("no".equals(policy.getWifiVisit())){//wifi不可用
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(false);
            }
        }
    }











}
