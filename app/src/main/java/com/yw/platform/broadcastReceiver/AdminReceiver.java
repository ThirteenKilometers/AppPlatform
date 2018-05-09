package com.yw.platform.broadcastReceiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by cxb on 2018/3/15.
 */
public class AdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, "设备管理器：已激活", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(final Context context, Intent intent) {
        super.onDisabled(context, intent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                // 指定需要激活的组件
                intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(context.getApplicationContext(), AdminReceiver.class));
                intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "(使用前请激活窗口中的描述信息)");
                context.getApplicationContext().startActivity(intent1);
            }
        }, 3000);
    }


}