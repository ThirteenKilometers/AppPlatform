package com.yw.platform.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.yw.platform.broadcastReceiver.ScreenBroadcastReceiver;
import com.yw.platform.global.MyApplication;

/**
 * Created by cxb on 2017/7/10.
 */

public class BaseActivity extends AdminActivity {
    private ScreenBroadcastReceiver mScreenReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().setLastLTime(-1);
        mScreenReceiver = new ScreenBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        long lastTime = MyApplication.getInstance().getLastLTime();
        if (lastTime > 0 && System.currentTimeMillis() - lastTime > (getTime() * 1000)) {
            Intent intent = new Intent(this, ValidateActivity.class);
            //startActivity(intent);
        }
        registerListener();
    }

    public int getTime() {
        return getSharedPreferences("clock", Context.MODE_PRIVATE).getInt("time", 1000);
    }

    public void setTime(int time) {
        getSharedPreferences("clock", Context.MODE_PRIVATE).edit().putInt("time", time).apply();
    }

    /**
     * 启动screen状态广播接收器
     */
    private void registerListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.getInstance().setLastLTime(System.currentTimeMillis());
        Log.e("锁屏时间-", "" + System.currentTimeMillis());
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mScreenReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void toast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }
}
