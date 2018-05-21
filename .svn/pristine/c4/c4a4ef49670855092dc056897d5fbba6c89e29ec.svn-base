package com.yw.platform.yhtext.utils;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.yw.platform.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.yw.platform.global.MyApplication.getInstance;

/**
 * 通知栏
 */
public class NoticeUtils {

    //显示通知
    @SuppressLint("NewApi")
    public static void showNotic(String ContentText, Intent intent) {
        NotificationManager myManager = (NotificationManager) getInstance().getSystemService(NOTIFICATION_SERVICE);
        //3.定义一个PendingIntent，点击Notification后启动一个Activity
        PendingIntent pi = PendingIntent.getActivity(
                getInstance(),
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        Notification.Builder myBuilder = new Notification.Builder(getInstance());
        myBuilder.setContentTitle("移动安全平台系统通知")
                .setContentText(ContentText)
                .setTicker("您收到新的消息")
                //设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示
                .setSmallIcon(R.drawable.icon)
                // .setLargeIcon(R.drawable.icon)
                //设置默认声音和震动
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)//点击后取消
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setPriority(Notification.PRIORITY_HIGH)//高优先级
                .setVisibility(Notification.VISIBILITY_PRIVATE)
                //android5.0加入了一种新的模式Notification的显示等级，共有三种：
                //VISIBILITY_PUBLIC  只有在没有锁屏时会显示通知
                //VISIBILITY_PRIVATE 任何情况都会显示通知
                //VISIBILITY_SECRET  在安全锁和没有锁屏的情况下显示通知
                .setContentIntent(pi);  //3.关联PendingIntent
        Notification myNotification = myBuilder.build();
        //4.通过通知管理器来发起通知，ID区分通知，不要和其他应用发生冲突
        myManager.notify(0, myNotification);

    }
}
