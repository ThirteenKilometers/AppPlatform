package com.yw.platform.yhtext.beans.accept_bean.base;

import com.yw.platform.yhtext.beans.send_bean.base.BaseUserBean;

import java.io.Serializable;
import java.util.List;


/**
 * <br/>
 * 作者：LZHS<br/>
 * 时间： 2018/4/25 01:03<br/>
 * 邮箱：1050629507@qq.com
 */
public class BaseAcceptMsgBean implements Serializable {

    /**
     * sender : {"userCode":"INTERFACE","client":"INTERFACE","ict":"SOCKET"}
     * recipients : [{"userCode":"testUserCode","client":"ANDROIDPHONE","ict":"SOCKET"}]
     * method : login
     * content : {"success":false,"message":"密码错误","notification":"RESPONES"}
     * success : true
     * message : sucess
     */
    private BaseUserBean sender;
    private String method;
    private String content;
    private boolean success;
    private String message;
    private List<BaseUserBean> recipients;

    public BaseUserBean getSender() {
        return sender;
    }

    public void setSender(BaseUserBean sender) {
        this.sender = sender;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BaseUserBean> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<BaseUserBean> recipients) {
        this.recipients = recipients;
    }

}
