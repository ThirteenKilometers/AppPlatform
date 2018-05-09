package com.yw.platform.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yw.platform.service.DeviceManager;
import com.yw.platform.global.MyApplication;

/**
 * Created by cxb on 2018/3/15.
 */

public class AdminActivity extends Activity {
    public DeviceManager deviceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceManager=DeviceManager.getInstance(this);
    }
    /**
     * 描述：判断是都具有管理员权限
     * @return 具有管理员权限返true，否则false
     */
    private boolean isAdminActive() {
        return deviceManager.isAdminActive();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAdminActive()){
            deviceManager.applyAdmin(this,9527);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9527) {
            if (resultCode == Activity.RESULT_CANCELED) {
                deviceManager.applyAdmin(this,9527);
            }
        }
    }
}
