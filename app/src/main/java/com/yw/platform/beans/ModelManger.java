package com.yw.platform.beans;

import com.yw.platform.beans.base.NetUser;
import com.yw.platform.beans.base.RequestModel;
import com.yw.platform.beans.base.ResponseBean;
import com.yw.platform.beans.base.ResponseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxb on 2018/4/26.
 */

public class ModelManger {
    /**
     * 获取默认
     * @return
     */
    public static RequestModel getDefaultReqestModel(String userCode){
        RequestModel<Object> model=new RequestModel<Object>();
        model.setSender(getDefaultSendUser(userCode));
        model.setRecipients(getDefaultRptUsers());
        return model;
    }

    /**
     * 描述：返回本地默认账号
     * @return
     */
    public static NetUser getDefaultSendUser(String userCode){
        NetUser user=new NetUser();
        if(userCode==null||"".equals(userCode)){
            user.setUserCode("");
        }else{
            user.setUserCode(userCode);
        }
        return user;
    }

    /**
     * 获取默认接收人
     * @return
     */
    public static NetUser getDefaultRptUser(){
        NetUser user=new NetUser();
        user.setUserCode("INTERFACE");
        user.setClient("INTERFACE");
        user.setIct("SOCKET");
        return user;
    }
    /**
     * 获取默认接收人(通讯服务器)
     * @return
     */
    public static NetUser getCommRptUser(){
        NetUser user=new NetUser();
        user.setUserCode("COMMUNICATION");
        user.setClient("COMMUNICATION");
        user.setIct("SOCKET");
        return user;
    }
    /**
     * 获取默认接收人(通讯服务器)
     * @return
     */
    public static List<NetUser> getCommRptUsers(){
        List<NetUser> users=new ArrayList<>();
        users.add(getCommRptUser());
        return users;
    }
    /**
     * 获取默认接收人
     * @return
     */
    public static List<NetUser> getDefaultRptUsers(){
        List<NetUser> users=new ArrayList<>();
        users.add(getDefaultRptUser());
        return users;
    }

    /**
     * 描述：默认对服务端的响应实体
     * @return
     */
     public static ResponseModel getDefaultResponseModel(String userCode){
         ResponseModel responseModel=new ResponseModel();
         responseModel.setRecipient(getDefaultRptUser());
         responseModel.setSender(getDefaultSendUser(userCode));
         return responseModel;
     }
    /**
     * 描述：默认对服务端的响应实体
     * @return
     */
    /*public static ResponseBean getDefaultResponseBean(String noticeId,String msg){
        ResponseBean responseBean=new ResponseBean();
        responseBean.setNoticeId(noticeId);
        responseBean.setMessage(msg);
        return responseBean;
    }*/
    public static ResponseBean getDefaultResponseBean(List<String> noticeId, String msg){
        ResponseBean responseBean=new ResponseBean();
        responseBean.setNoticeIds(noticeId);
        responseBean.setMessage(msg);
        return responseBean;
    }
}
