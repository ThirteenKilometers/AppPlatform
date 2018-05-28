package com.yw.platform.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.yw.platform.beans.base.ResponseModel;
import com.yw.platform.beans.recevice.PolicyBean;
import com.yw.platform.global.MyApplication;
import com.yw.platform.netApi.NetApi;
import com.yw.platform.tools.AppLockTools;
import com.yw.platform.tools.SetInfo;
import com.yw.platform.ui.activity.AppLockerActivity;
import com.yw.platform.yhtext.beans.MessageEvent;
import com.yw.platform.yhtext.netty.client.Const;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by cxb on 2018/4/25.
 */

public class LocalHandleService extends Service {
    private DeviceManager deviceManager;//设备管理器

    private HandlerThread mMonitorThread;
    private Handler mHandler;
    private KeyguardManager mKeyguardManager;

    @Override
    public void onCreate() {
        Log.i("info", "======onCreate:LocalHandleService ");
        super.onCreate();
        EventBus.getDefault().register(this);
        deviceManager = DeviceManager.getInstance(this);
        initHandlerThread();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    private String requestId = "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.i("info", "======LocalHandleService" + event.getCode());
        switch (event.getCode()) {
            case Const.CONTROL_COMPANYDATA_DCREAL://清除企业数据
                deviceManager.clearAppCache(this);
                break;
            case Const.CONTROL_ALLDATA_DCREAL://清除设备数据
                deviceManager.WipeData();
                break;
            case Const.CONTROL_SCREEN_LOCK://锁屏
                deviceManager.lockDesktop();
                deviceManager.setPsd("1234");
                break;
            case Const.CONTROL_SCREEN_UNLOCK://截屏
                deviceManager.setPsd("");
                break;
            case Const.NOTICE_POLICY_CHANGE://策略变化通知
                requestId = System.currentTimeMillis() + "_" + ((int) (Math.random() * 100));
                NetApi.requestPolicy(requestId);
                break;
            case Const.METHER_QUERYPOLICY_CODE:
                ResponseModel responseData = event.getResponseData();
                if (!responseData.getRequestId().equals(requestId)) break;
                if (responseData.isSuccess()) {
                    try {
                        responseData.setContentClass(PolicyBean.class);
                        PolicyBean policy = (PolicyBean) responseData.getContentData();
                        MyApplication.getInstance().setPolicy(policy);
                    } catch (Exception e) {
                        Log.e("策略信息查询", "策略异常");
                    }
                } else {
                    Toast.makeText(this, "策略获取失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case Const.GET_LOCATION://获取设备位置

                break;
            case Const.HANDEL_CODE_CHANGE:
                StateManger.getInstance().setChangeCode((Integer)event.getData());
                break;
        }
    }

    /**
     * 描述：应用锁屏程序
     */
    private void initHandlerThread() {
        mMonitorThread = new HandlerThread("AppLockerMonitorThread");
        mMonitorThread.start();
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        mHandler = new Handler(mMonitorThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
                            SetInfo.getInstance().resetUnlockStatus();
                            break;
                        }
                        String foregroundApp = AppLockTools.getForegroundApp(LocalHandleService.this);
                        if (foregroundApp != null && SetInfo.getInstance().AppLock(foregroundApp)) {
                            Intent intent = new Intent(LocalHandleService.this, AppLockerActivity.class);
                            intent.putExtra("APP_NAME", foregroundApp);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        break;
                }
                if (!deviceManager.isAdminActive()) {
                    deviceManager.applyAdmin(LocalHandleService.this);
                }
                mHandler.sendEmptyMessageDelayed(1, 300);
            }
        };
        mHandler.sendEmptyMessageDelayed(1, 300);
    }


}
