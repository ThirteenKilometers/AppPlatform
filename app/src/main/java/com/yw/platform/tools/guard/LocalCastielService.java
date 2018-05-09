package com.yw.platform.tools.guard;


import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.yw.platform.tools.AppLockTools;
import com.yw.platform.tools.SetInfo;
import com.yw.platform.ui.activity.AppLockerActivity;

/**
 * @ClassName: LocalCastielService
 * @Description: 本地服务
 * @author
 * @version
 */
public class LocalCastielService extends Service {

    MyBinder myBinder;
    private PendingIntent pintent;
    MyServiceConnection myServiceConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("服务测试", "本地服务被创建");
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        myServiceConnection = new MyServiceConnection();
        initHandlerThread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, RemoteCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
        Log.e("服务测试", "本地服务onStartCommend");
        //Notification notification = new Notification(R.drawable.ic_launcher, "猴子服务启动中", System.currentTimeMillis());
        //pintent = PendingIntent.getService(this, 0, intent, 0);
        //notification.(this, "猴子服务", "防止被杀掉！", pintent);
        // 设置service为前台进程，避免手机休眠时系统自动杀掉该服务
        mHandler.sendEmptyMessageDelayed(200, 1000);
        return START_REDELIVER_INTENT;
    }
    private HandlerThread mMonitorThread;
    private Handler mHandler;
    private KeyguardManager mKeyguardManager;
    private void initHandlerThread(){
        mMonitorThread = new HandlerThread("AppLockerMonitorThread");
        mMonitorThread.start();
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        mHandler = new Handler(mMonitorThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 200:
                        if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
                            SetInfo.getInstance().resetUnlockStatus();
                            break;
                        }
                        String foregroundApp = AppLockTools.getForegroundApp(LocalCastielService.this);
                        if (foregroundApp!=null&&SetInfo.getInstance().AppLock(foregroundApp)) {
                            Intent intent = new Intent(LocalCastielService.this, AppLockerActivity.class);
                            intent.putExtra("APP_NAME", foregroundApp);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        break;
                }
                mHandler.sendEmptyMessageDelayed(200, 1000);
            }
        };
    }
    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            Log.e("服务测试",  "远程服务连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // 连接出现了异常断开了，RemoteService被杀掉了
            Toast.makeText(LocalCastielService.this, "远程服务Remote被干掉", Toast.LENGTH_LONG).show();
            // 启动RemoteCastielService
            LocalCastielService.this.startService(new Intent(LocalCastielService.this, RemoteCastielService.class));
            LocalCastielService.this.bindService(new Intent(LocalCastielService.this, RemoteCastielService.class),
                    myServiceConnection, Context.BIND_IMPORTANT);
        }

    }

    class MyBinder extends CastielProgressConnection.Stub {

        @Override
        public String getProName() throws RemoteException {
            return "Local猴子搬来的救兵 http://blog.csdn.net/mynameishuangshuai";
        }

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return myBinder;
    }

}