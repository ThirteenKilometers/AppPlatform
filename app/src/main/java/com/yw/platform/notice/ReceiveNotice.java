package com.yw.platform.notice;

import com.alibaba.fastjson.JSON;
import com.yw.platform.beans.ModelManger;
import com.yw.platform.beans.base.ResponseBean;
import com.yw.platform.beans.base.ResponseModel;
import com.yw.platform.beans.recevice.ServiceNotice;
import com.yw.platform.global.AppUser;
import com.yw.platform.global.MyApplication;
import com.yw.platform.netApi.SuperSendApi;
import com.yw.platform.yhtext.beans.MessageEvent;
import com.yw.platform.yhtext.netty.client.Const;
import com.yw.platform.yhtext.utils.LocationUtils;
import com.yw.platform.yhtext.utils.NoticTypeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import lzhs.com.library.utils.log.LogUtils;

import static com.yw.platform.global.MyApplication.getInstance;

/**
 * Created by cxb on 2018/4/25.
 */

public class ReceiveNotice {

    private static ReceiveNotice receiveNotice;
    String notlocation="4.9E-324";
    private ReceiveNotice() {
        LocationUtils.initLocation(MyApplication.getInstance());
    }

    public static synchronized ReceiveNotice getinReceiveNotice() {
        if (receiveNotice == null) {
            receiveNotice = new ReceiveNotice();
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
                LogUtils.json("noticePushServerRequest", JSON.toJSONString(contentData));
                List<ServiceNotice.Notice> notices = contentData.getNotices();
                for (ServiceNotice.Notice notice : notices) {
                    handeItem(responseData, notice);
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
                            //responseBean.setLongitude(getInstance().longitude);
                            // responseBean.setLatitude(getInstance().latitude);
                            if (LocationUtils.longitude.equals(notlocation)||LocationUtils.latitude.equals(notlocation)){
                                responseBean.setLongitude("0");
                                responseBean.setLatitude("0");
                            }else {
                                responseBean.setLongitude(LocationUtils.longitude);
                                responseBean.setLatitude(LocationUtils.latitude);
                            }

                            LogUtils.i("精度:" + LocationUtils.longitude + "纬度：" + LocationUtils.latitude);
                        } else {
                            responseBean.setLongitude("0");
                            responseBean.setLatitude("0");
                        }
                        break;
                    }
                }
                SuperSendApi.responseMsg("响应服务请求", requestId, responseBean, Const.NOTICE_PATH, responseData);
                LocationUtils.stopLocation();

                LogUtils.json("noticePushClientResponce", JSON.toJSONString(responseBean));
                break;
        }
    }

    private void handeItem(ResponseModel responseData, ServiceNotice.Notice notice) {

        MessageEvent sendMsg = new MessageEvent<String>();
        sendMsg.setCode(-1);
        String noticeType = notice.getNoticeType();
        NoticTypeUtils.setNoticType(noticeType,sendMsg);
       /* String responsemsg = null;
        if ("policyUpdata".equals(noticeType)||"policyDistribution".equals(noticeType)||"strategy".equals(noticeType)) {
            sendMsg.setCode(Const.NOTICE_POLICY_CHANGE);
            sendMsg.setMsg("策略");
            responsemsg = "策略更新消息已收到";
        } else if ("deviceFreeze".equals(noticeType)) {
            ToastUtils.showShort("设备已冻结");
            Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
            MyApplication.getInstance().startActivity(intent);
           NoticeUtils.showNotic(responsemsg, new Intent(getInstance(), LoginActivity.class));
        } else if ("deviceUnBind".equals(noticeType)) {
            responsemsg = "设备解绑消息已收到";
            ToastUtils.showShort("设备已解绑");
            Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
            MyApplication.getInstance().startActivity(intent);
            // showNotic(responsemsg, new Intent(getInstance(), LoginActivity.class));
        } else if ("appAdd".equals(noticeType)) {//应用新增
            sendMsg.setCode(Const.NOTICE_APP_ADD);
            sendMsg.setMsg("应用新增");
            Log.i("info", "=================应用增新");
            responsemsg = "应用新增消息已收到";
            NoticeUtils.showNotic(responsemsg, new Intent(getInstance(), MainNewActivity.class));
        } else if ("appRemove".equals(noticeType)) {//应用移除
            sendMsg.setCode(Const.NOTICE_APP_REMOVE);
            sendMsg.setMsg("应用移除");
            responsemsg = "应用移除消息已收到";
           NoticeUtils.showNotic(responsemsg, new Intent(getInstance(), MainNewActivity.class));

        } else if ("appUpdate".equals(noticeType)) {//应用更新
            sendMsg.setCode(Const.NOTICE_APP_UPDATE);
            sendMsg.setMsg("应用更新");
            responsemsg = "应用更新消息已收到";
            NoticeUtilsshowNotic(responsemsg, new Intent(getInstance(), MainNewActivity.class));
        } else if ("appDistribution".equals(noticeType)) {//应用分发
            sendMsg.setCode(Const.NOTICE_APP_UPDATE);
            sendMsg.setMsg("应用分发");
            responsemsg = "应用分发消息已收到";
            showNotic(responsemsg, new Intent(getInstance(), MainNewActivity.class));

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
        } else if ("fileDistribution".equals(noticeType)) {//文档新增
            sendMsg.setCode(Const.METHER_PUSH_QUERYDOCUMENTLIST_CODE);
            sendMsg.setMsg("文档分发");
            responsemsg = "文档分发消息已收到";
            showNotic(responsemsg, new Intent(getInstance(), DocumentListActivity.class));
            Log.i("info", "noticePushServerRequest " + "接收到文档分发消息");
        } else if ("fileRemove".equals(noticeType)) {
            sendMsg.setCode(Const.METHER_PUSH_QUERYDOCUMENTLIST_CODE);
            sendMsg.setMsg("文档移除");
            responsemsg = "文档移除消息已收到";
            showNotic(responsemsg, new Intent(getInstance(), DocumentListActivity.class));
        } else if ("fileUpdate".equals(noticeType)) {//文档更新
            sendMsg.setCode(Const.METHER_PUSH_QUERYDOCUMENTLIST_CODE);
            sendMsg.setMsg("文档更新");
            responsemsg = "文档更新消息已收到";
            showNotic(responsemsg, new Intent(getInstance(), DocumentListActivity.class));
        }*/
        if (sendMsg.getCode() > 0) {
            EventBus.getDefault().post(sendMsg);
        }
    }


    /**
     * 登录账号
     */
    public static String getUserCode() {
        AppUser appUser = getInstance().getAppUser();
        if (appUser == null) {
            return "";
        } else {
            return appUser.getUserCode();
        }
    }
}
