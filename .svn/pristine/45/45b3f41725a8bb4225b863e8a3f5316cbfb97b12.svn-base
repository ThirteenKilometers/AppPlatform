package com.yw.platform.broadcastReceiver.manger;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.yw.platform.beans.recevice.PolicyBean;
import com.yw.platform.global.MyApplication;
import com.yw.platform.tools.SetInfo;

/**
 * 描述：蓝牙状态改变
 */
public class BluetoothBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF://STATE_OFF 手机蓝牙关闭
                    Log.d("aaa", "");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF://手机蓝牙正在关闭
                    Log.d("aaa", "STATE_TURNING_OFF ");
                    break;
                case BluetoothAdapter.STATE_ON://手机蓝牙开启
                    Log.d("aaa", "STATE_ON 手机蓝牙开启");
                    handeBluetooth(context);
                    break;
                case BluetoothAdapter.STATE_TURNING_ON://手机蓝牙正在开启
                    Log.d("aaa", "STATE_TURNING_ON 手机蓝牙正在开启");
                    handeBluetooth(context);
                    break;
            }
    }
    /**
     * 描述：处理蓝牙设备
     */
    private void handeBluetooth(Context context){
        BluetoothAdapter  bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        PolicyBean policy= MyApplication.getInstance().getPolicy();
        if((bluetoothAdapter!=null)&& ("no".equals(policy.getBluetooth()))){
            Toast.makeText(context, "蓝牙设备已禁用", Toast.LENGTH_SHORT).show();
            bluetoothAdapter.disable();
        }
    }




}
