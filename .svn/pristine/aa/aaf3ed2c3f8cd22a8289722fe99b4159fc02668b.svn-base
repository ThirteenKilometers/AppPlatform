package com.yw.platform.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yw.platform.global.MyApplication;

/**
 * 描述：锁屏监听广播类
 * Created by cxb on 2017/7/12.
 */
public class ScreenBroadcastReceiver extends BroadcastReceiver {
    private String action = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
       if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
           MyApplication.getInstance().setLastLTime(1000);
           Log.e("锁屏时间",""+System.currentTimeMillis());
        }
    }
}