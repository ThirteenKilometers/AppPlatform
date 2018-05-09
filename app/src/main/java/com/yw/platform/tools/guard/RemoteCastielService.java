package com.yw.platform.tools.guard;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

/**
 *
 * @ClassName: RemoteCastielService
 * @Description: 远程服务
 * @author 猴子搬来的救兵 http://blog.csdn.net/mynameishuangshuai
 */
public class RemoteCastielService extends Service {
    MyBinder myBinder;
    private PendingIntent pintent;
    MyServiceConnection myServiceConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("服务测试", "远程服务被创建");
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        myServiceConnection = new MyServiceConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this,LocalCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
        Log.e("服务测试", "远程服务被启动");
//        Notification notification = new Notification(R.drawable.ic_launcher, "猴子服务启动中", System.currentTimeMillis());
//        pintent=PendingIntent.getService(this, 0, intent, 0);
//       // notification.setLatestEventInfo(this, "猴子服务","防止被杀掉！", pintent);
//        //设置service为前台进程，避免手机休眠时系统自动杀掉该服务
//        startForeground(startId, notification);
        return START_REDELIVER_INTENT;
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            Log.e("castiel", "本地服务连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // 连接出现了异常断开了，LocalCastielService被杀死了
            Toast.makeText(RemoteCastielService.this, "本地服务Local被干掉", Toast.LENGTH_LONG).show();
            // 启动LocalCastielService
            RemoteCastielService.this.startService(new Intent(RemoteCastielService.this,LocalCastielService.class));
            RemoteCastielService.this.bindService(new Intent(RemoteCastielService.this,LocalCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
        }

    }

    class MyBinder extends CastielProgressConnection.Stub {
        @Override
        public String getProName() throws RemoteException {
            return "Remote猴子搬来的救兵 http://blog.csdn.net/mynameishuangshuai";
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return myBinder;
    }

}