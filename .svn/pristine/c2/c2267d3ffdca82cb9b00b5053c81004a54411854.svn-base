package com.yw.platform.netApi;

import android.util.Log;

import com.google.gson.Gson;
import com.yw.platform.beans.ModelManger;
import com.yw.platform.beans.base.ResponseModel;
import com.yw.platform.global.AppUser;
import com.yw.platform.global.MyApplication;
import com.yw.platform.yhtext.beans.MessageEvent;
import com.yw.platform.yhtext.netty.client.Const;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cxb on 2018/4/25.
 */

public class SuperSendApi {

//    /**
//     * 描述：请求
//     * @param hint
//     * @param content
//     */
//    public  static void sendMsg(String hint,Object content) {
//        RequestModel rModel= ModelManger.getDefaultReqestModel();
//        rModel.setContent(content);
//        Gson gson=new Gson();
//        String data=gson.toJson(rModel);
//        Log.e("消息发送"+hint,data);
//        MessageEvent event = new MessageEvent<String>();
//        event.setCode(Const.SEND_CODE);
//        event.setMsg("正在向服务器发送："+hint);
//        event.setData(data);
//        EventBus.getDefault().post(event);
//    }
    /**
     * 描述：响应请求
     * @param hint
     * @param content
     */
    public  static void responseMsg(String hint,String requestId,Object content,String meoth,ResponseModel responseData) {
        ResponseModel rModel= ModelManger.getDefaultResponseModel(getUserCode());
        rModel.setRequestId(requestId);
        rModel.setContent(content);
        rModel.setMethod(meoth);
        rModel.setRecipient(responseData.getSender());
        Gson gson=new Gson();
        String data=gson.toJson(rModel);
        Log.e("消息发送"+hint,data);
        MessageEvent event = new MessageEvent<String>();
        event.setCode(Const.SEND_CODE);
        event.setMsg("正在向服务器发送："+hint);
        event.setData(data);
        EventBus.getDefault().post(event);
    }
    public static String getUserCode() {
        AppUser appUser= MyApplication.getInstance().getAppUser();
        if(appUser==null){
            return "";
        }else{
            return appUser.getUserCode();
        }
    }


}
