package com.yw.platform.yhtext.utils;

import android.support.annotation.NonNull;

import com.yw.platform.global.AppUser;
import com.yw.platform.global.MyApplication;
import com.yw.platform.yhtext.beans.MessageEvent;
import com.yw.platform.yhtext.beans.send_bean.base.BaseSendMsgBean;
import com.yw.platform.yhtext.beans.send_bean.base.BaseUserBean;
import com.yw.platform.yhtext.netty.client.Const;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求公共部分
 */
public class BaseUtils {
    /**
     * BaseSendMsgBean
     * @return
     */
    @NonNull
    public static BaseSendMsgBean createDefaultSendBean() {
        BaseSendMsgBean sendMsgBean = new BaseSendMsgBean();
        sendMsgBean.setSender(createDefault("", "ANDROIDPHONE"));
        List<BaseUserBean> recipients = new ArrayList<>();
        recipients.add(createDefault("INTERFACE", ""));
        sendMsgBean.setRecipients(recipients);
        return sendMsgBean;
    }

    /**
     * BaseUserBean 发送者和接收者公共部分
     * @param code
     * @param client
     * @return
     */
    private static BaseUserBean createDefault(String code, String client) {
        BaseUserBean userBean = new BaseUserBean();
        userBean.setClient(client);
        userBean.setClientVersion(PhoneMessage.getVersionCode(MyApplication.getInstance()));
        userBean.setIct("SOCKET");//可以不传
        userBean.setUserCode(code);

        return userBean;
    }

    /**
     * EventBus发送消息方法
     * @param data
     */
    public static void sendMessage(String data) {
        MessageEvent event = new MessageEvent<String>();
        event.setCode(Const.SEND_CODE);
        event.setMsg("正在向服务器发送消息");
        event.setData(data);
        EventBus.getDefault().post(event);

    }

    /**
     * //当前登录账号getUserCode()
     * @return
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
