package com.yw.platform.tools;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentFilter;

import com.yw.platform.broadcastReceiver.manger.BluetoothBroadcastReceiver;

/**
 * Created by cxb on 2018/3/20.
 */

public class RroadcastUtil {

    /**
     * 描述：蓝牙设备状态监听
     * 广播接收处理类：BluetoothBroadcastReceiver
     * @param context
     */
    public static void addBluetoothSate(Context context){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(new BluetoothBroadcastReceiver(), filter);
    }







}
