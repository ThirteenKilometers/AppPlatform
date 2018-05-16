package com.yw.platform.notice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yw.platform.R;
import com.yw.platform.beans.ModelManger;
import com.yw.platform.beans.base.ResponseBean;
import com.yw.platform.beans.base.ResponseModel;
import com.yw.platform.beans.recevice.ServiceNotice;
import com.yw.platform.global.AppUser;
import com.yw.platform.global.MyApplication;
import com.yw.platform.netApi.SuperSendApi;
import com.yw.platform.ui.activity.MainNewActivity;
import com.yw.platform.yhtext.activity.DocumentListActivity;
import com.yw.platform.yhtext.beans.MessageEvent;
import com.yw.platform.yhtext.netty.client.Const;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import lzhs.com.library.utils.log.LogUtils;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by cxb on 2018/4/25.
 */

public class ReceiveNotice {
    String longitude = "0";
    String latitude = "0";
    private static ReceiveNotice receiveNotice;

    private ReceiveNotice() {

    }

    public ReceiveNotice(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static synchronized ReceiveNotice getinReceiveNotice() {
        if (receiveNotice == null) {
            receiveNotice = new ReceiveNotice();
        }
        return receiveNotice;
    }


    public static synchronized ReceiveNotice getinReceiveNotice(String longitude, String latitude) {

        if (receiveNotice == null) {
            receiveNotice = new ReceiveNotice(longitude, latitude);
            Log.i("info", "getinReceiveNotice: "+longitude);
            LogUtils.i("精度:" + longitude+"纬度："+latitude);
        }
        return receiveNotice;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getCode()) {
            case Const.METHER_NOTICE_PATH_CODE:
                ResponseModel responseData = event.getResponseData();
                responseData.setContentClass(ServiceNotice.class);
                String requestId = responseData.getRequestId();
                ServiceNotice contentData = (ServiceNotice) responseData.getContentData();
             //   LogUtils.json("noticePushServerRequest", JSON.toJSONString(contentData));
                List<ServiceNotice.Notice> notices = contentData.getNotices();
                for (ServiceNotice.Notice notice : notices) {
                    handeItem(notice);
                }
                List<String> noticeIds = new ArrayList<>();//传给服务器的noticIds String类型数组
                for (int i = 0; i < notices.size(); i++) {
                    noticeIds.add(notices.get(i).getNoticeId());
                }

                // ResponseBean responseBean = ModelManger.getDefaultResponseBean("通知消息已接收", "324235");
                ResponseBean responseBean = ModelManger.getDefaultResponseBean(noticeIds, "324235");

                responseBean.setNoticeIds(noticeIds);

                // TODO: 2018/5/11 noticId
                for (int i = 0; i < notices.size(); i++) {
                    //如果后台给的字段是要求定位的话
                    if ("equipmentPositioning".equals(notices.get(i).getNoticeType())) {
                        if (!getUserCode().equals("")) {//已经登录的话
                            // TODO: 2018/5/10 传已经定位的信息
                            responseBean.setLongitude(longitude);
                            responseBean.setLatitude(latitude);
                        } else {
                            responseBean.setLongitude("0");
                            responseBean.setLatitude("0");
                        }
                        break;
                    }
                }

                SuperSendApi.responseMsg("响应服务请求", requestId, responseBean, Const.NOTICE_PATH, responseData);
                LogUtils.json("noticePushClientResponce", JSON.toJSONString(responseBean));

                break;
        }
    }

    private void handeItem(ServiceNotice.Notice notice) {
        MessageEvent sendMsg = new MessageEvent<String>();
        sendMsg.setCode(-1);
        String noticeType = notice.getNoticeType();
        String responsemsg = null;
        if ("strategy".equals(noticeType)) {
            sendMsg.setCode(-1);
            sendMsg.setMsg("策略");
            responsemsg = "策略更新消息已收到";
        } else if ("appAdd".equals(noticeType)) {//应用新增
            sendMsg.setCode(Const.NOTICE_APP_ADD);
            sendMsg.setMsg("应用新增");
            responsemsg = "应用新增消息已收到";
            showNotic(responsemsg,new Intent(MyApplication.getInstance(), MainNewActivity.class));
        } else if ("appRemove".equals(noticeType)) {//应用移除
            sendMsg.setCode(Const.NOTICE_APP_REMOVE);
            sendMsg.setMsg("应用移除");
            responsemsg = "应用移除消息已收到";
            showNotic(responsemsg,new Intent(MyApplication.getInstance(), MainNewActivity.class));

        } else if ("appUpdate".equals(noticeType)) {//应用更新
            sendMsg.setCode(Const.NOTICE_APP_UPDATE);
            sendMsg.setMsg("应用更新");
            responsemsg = "应用更新消息已收到";
            showNotic(responsemsg,new Intent(MyApplication.getInstance(), MainNewActivity.class));

        } else if ("companyDataErasure".equals(noticeType)) {//企业应用数据擦除
            sendMsg.setCode(Const.CONTROL_COMPANYDATA_DCREAL);
            sendMsg.setMsg("应用清除");
            responsemsg = "应用清除消息已收到";
        } else if ("allDataErasure".equals(noticeType)) {//全部数据擦除
            sendMsg.setCode(Const.CONTROL_ALLDATA_DCREAL);
            sendMsg.setMsg("设备清除");
            responsemsg = "设备清除消息已收到";
        } else if ("screenLock".equals(noticeType)) {//屏幕锁定
            sendMsg.setCode(Const.CONTROL_SCREEN_LOCK);
            sendMsg.setMsg("屏幕锁屏");
            responsemsg = "锁屏消息已收到";
        } else if ("screenUnLock".equals(noticeType)) {//屏幕解锁
            sendMsg.setCode(Const.CONTROL_SCREEN_UNLOCK);
            sendMsg.setMsg("屏幕解锁");
            responsemsg = "解锁屏幕消息已收到";
        } /*else if ("equipmentPositioning".equals(noticeType)) {//设备定位
            sendMsg.setCode(-1);
            sendMsg.setMsg("设备定位");
        } */ else if ("fileDistribution".equals(noticeType)) {//文档新增
            sendMsg.setCode(Const.METHER_PUSH_QUERYDOCUMENTLIST_CODE);
            sendMsg.setMsg("文档分发");
            responsemsg = "文档分发消息已收到";
            showNotic(responsemsg,new Intent(MyApplication.getInstance(), DocumentListActivity.class));
            Log.i("info", "noticePushServerRequest "+"接收到文档分发消息");
        } /*else if ("fileRemove".equals(noticeType)) {
            sendMsg.setCode(Const.METHER_PUSH_QUERYDOCUMENTLIST_CODE);
            sendMsg.setMsg("文档移除");
            responsemsg = "文档移除消息已收到";
            showNotic(responsemsg,new Intent(MyApplication.getInstance(), DocumentListActivity.class));

        } else if ("fileUpdate".equals(noticeType)) {
            sendMsg.setCode(Const.METHER_PUSH_QUERYDOCUMENTLIST_CODE);
            sendMsg.setMsg("文档更新");
            responsemsg = "文档更新消息已收到";
            showNotic(responsemsg,new Intent(MyApplication.getInstance(), DocumentListActivity.class));
        }*/

        if (sendMsg.getCode() > 0) {
            EventBus.getDefault().post(sendMsg);
        }
    }
    //显示通知
    @SuppressLint("NewApi")
    private void showNotic(String ContentText, Intent intent) {
        NotificationManager myManager = (NotificationManager) MyApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);
        //3.定义一个PendingIntent，点击Notification后启动一个Activity
        PendingIntent pi = PendingIntent.getActivity(
                MyApplication.getInstance(),
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        Notification.Builder myBuilder = new Notification.Builder(MyApplication.getInstance());
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

    /**
     * 登录账号
     */
    public static String getUserCode() {
        AppUser appUser = MyApplication.getInstance().getAppUser();
        if (appUser == null) {
            return "";
        } else {
            return appUser.getUserCode();
        }
    }
}
