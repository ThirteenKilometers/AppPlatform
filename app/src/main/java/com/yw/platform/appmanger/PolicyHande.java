package com.yw.platform.appmanger;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.yw.platform.beans.recevice.PolicyBean;
import com.yw.platform.global.MyApplication;
import com.yw.platform.service.DeviceManager;
import com.yw.platform.tools.DeviceInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
            if("no".equals(policy.getGps())){//gps不可用
                boolean state= DeviceInfo.getGPSState(context);
                if(state) {//关闭Gps
                    DeviceInfo.openGPS(context,false);
                }
            }
            SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm");
            try {
                long startTime=dateFormat.parse(policy.getStartTime()).getTime();
                long endTime=dateFormat.parse(policy.getEndTime()).getTime();
                Calendar calendar=Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                long nowTime=(hour*60+minute)*60*1000l;
                if(nowTime<startTime||nowTime>endTime){
                    Log.e("策略发生变化","当前时间不可用");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }











}
