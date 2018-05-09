package com.yw.platform.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;

import com.yw.platform.broadcastReceiver.AdminReceiver;

import java.io.File;

/**
 * Created by cxb on 2018/4/25.
 */

public class DeviceManager {

    private static DeviceManager instance;//设备管理
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    private DeviceManager(Context context){
        /**
         * 设备安全管理服务，2.2之前需要通过反射技术获取
         */
        this.devicePolicyManager=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //DeviceReceiver 继承自 DeviceAdminReceiver
        this.componentName=new ComponentName(context, AdminReceiver.class);
    }
    public static DeviceManager getInstance(Context context) {
        if (instance==null) {
            synchronized (DeviceManager.class) {
                if (instance==null) {
                    instance=new DeviceManager(context);
                }
            }
        }
        return instance;
    }
    /**
     * 描述：判断是都具有管理员权限
     * @return 具有管理员权限返true，否则false
     */
    public boolean isAdminActive() {
        return devicePolicyManager.isAdminActive(componentName);
    }

    /**
     * 描述：申请管理员权限
     */
    public  void applyAdmin(Activity context, int requestCode){
        // 打备管理器的激活窗口
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        // 指定需要激活的组件
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "(使用前请激活窗口中的描述信息)");
        context.startActivityForResult(intent,requestCode);
    }
    /**
     * 描述：申请管理员权限
     */
    public  void applyAdmin(Service service){
        // 打备管理器的激活窗口
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        // 指定需要激活的组件
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "(使用前请激活窗口中的描述信息)");
        service.startService(intent);
    }
    /**
     * 描述：清除锁屏密码
     */
    public  void clearPsd(){
        devicePolicyManager.resetPassword("", 2);
    }

    /**
     * 描述：设置锁屏密码
     */
    public  void setPsd(String psd){
        devicePolicyManager.resetPassword(psd, 2);
    }

    /**
     * 描述：锁屏
     *
     */
    public  void lockDesktop(){
        devicePolicyManager.lockNow();
    }
    /**
     * 描述：恢复出厂设置
     */
    public  void WipeData() {
        if(1>2){
            devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        }
        //双清存储数据（包括外置sd卡），wipeData后重启
        //WIPE_EXTERNAL_STORAGE = 0x0001;
        // Toast.makeText(context, "代码屏蔽", Toast.LENGTH_SHORT).show();
    }
    /**
     * 清除app缓存
     */
    public void clearAppCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int size = 0;
            if (children != null) {
                size = children.length;
                for (int i = 0; i < size; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        if (dir == null) {
            return true;
        } else {
            return dir.delete();
        }
    }

    /**
     * 相机设置
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public  void setCameraDisabled(boolean disable) {
        devicePolicyManager.setCameraDisabled(componentName,disable);
    }

    /**
     * 设置储存数据是否加密
     * @param encrypt
     */
    public  void setStorageEncryption(boolean encrypt) {
        devicePolicyManager.setStorageEncryption(componentName,encrypt);
    }
}
