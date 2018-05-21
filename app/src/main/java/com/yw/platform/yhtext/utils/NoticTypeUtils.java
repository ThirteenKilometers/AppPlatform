package com.yw.platform.yhtext.utils;


import android.content.Intent;
import android.util.Log;

import com.yw.platform.global.MyApplication;
import com.yw.platform.ui.activity.MainNewActivity;
import com.yw.platform.yhtext.activity.DocumentListActivity;
import com.yw.platform.yhtext.activity.LoginActivity;
import com.yw.platform.yhtext.beans.MessageEvent;
import com.yw.platform.yhtext.netty.client.Const;

import lzhs.com.library.utils.ToastUtils;

import static com.yw.platform.global.MyApplication.getInstance;

public class NoticTypeUtils {
    /**
     * 获取后台的设备类型 移动端进而进行处理
     * @param noticeType
     * @param sendMsg
     */
     public static  void  setNoticType( String noticeType,MessageEvent sendMsg){
        if ("policyUpdata".equals(noticeType)||"policyDistribution".equals(noticeType)||"strategy".equals(noticeType)) {
            sendMsg.setCode(Const.NOTICE_POLICY_CHANGE);
            sendMsg.setMsg("策略");

        } else if ("deviceFreeze".equals(noticeType)) {
            ToastUtils.showShort("设备已冻结");
            Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
            MyApplication.getInstance().startActivity(intent);

        } else if ("deviceUnBind".equals(noticeType)) {

            ToastUtils.showShort("设备已解绑");
            Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
            MyApplication.getInstance().startActivity(intent);

        } else if ("appAdd".equals(noticeType)) {//应用新增
            sendMsg.setCode(Const.NOTICE_APP_ADD);
            sendMsg.setMsg("应用新增");
            Log.i("info", "=================应用增新");
            NoticeUtils.showNotic("应用新增消息已收到", new Intent(getInstance(), MainNewActivity.class));
        } else if ("appRemove".equals(noticeType)) {//应用移除
            sendMsg.setCode(Const.NOTICE_APP_REMOVE);
            sendMsg.setMsg("应用移除");
            NoticeUtils.showNotic("应用移除消息已收到", new Intent(getInstance(), MainNewActivity.class));

        } else if ("appUpdate".equals(noticeType)) {//应用更新
            sendMsg.setCode(Const.NOTICE_APP_UPDATE);
            sendMsg.setMsg("应用更新");
            NoticeUtils.showNotic("应用更新消息已收到", new Intent(getInstance(), MainNewActivity.class));
        } else if ("appDistribution".equals(noticeType)) {//应用分发
            sendMsg.setCode(Const.NOTICE_APP_UPDATE);
            sendMsg.setMsg("应用分发");
            NoticeUtils.showNotic("应用分发消息已收到", new Intent(getInstance(), MainNewActivity.class));

        } else if ("companyDataErasure".equals(noticeType)) {//企业应用数据擦除
            sendMsg.setCode(Const.CONTROL_COMPANYDATA_DCREAL);
            sendMsg.setMsg("应用清除");

        } else if ("allDataErasure".equals(noticeType)) {//全部数据擦除
            sendMsg.setCode(Const.CONTROL_ALLDATA_DCREAL);
            sendMsg.setMsg("设备清除");

        } else if ("screenLock".equals(noticeType)) {//屏幕锁定
            sendMsg.setCode(Const.CONTROL_SCREEN_LOCK);
            sendMsg.setMsg("屏幕锁屏");

        } else if ("screenUnLock".equals(noticeType)) {//屏幕解锁
            sendMsg.setCode(Const.CONTROL_SCREEN_UNLOCK);
            sendMsg.setMsg("屏幕解锁");

        } else if ("fileDistribution".equals(noticeType)) {//文档新增
            sendMsg.setCode(Const.METHER_PUSH_QUERYDOCUMENTLIST_CODE);
            sendMsg.setMsg("文档分发");
            NoticeUtils.showNotic("文档分发消息已收到", new Intent(getInstance(), DocumentListActivity.class));
            Log.i("info", "noticePushServerRequest " + "接收到文档分发消息");
        } else if ("fileRemove".equals(noticeType)) {
            sendMsg.setCode(Const.METHER_PUSH_QUERYDOCUMENTLIST_CODE);
            sendMsg.setMsg("文档移除");
            NoticeUtils.showNotic("文档移除消息已收到", new Intent(getInstance(), DocumentListActivity.class));
        } else if ("fileUpdate".equals(noticeType)) {//文档更新
            sendMsg.setCode(Const.METHER_PUSH_QUERYDOCUMENTLIST_CODE);
            sendMsg.setMsg("文档更新");
            NoticeUtils. showNotic("文档更新消息已收到", new Intent(getInstance(), DocumentListActivity.class));
        }
    }
}
