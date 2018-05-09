package com.yw.platform.beans.base;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by cxb on 2018/4/26.
 */

public class ResponseModel<T> implements Serializable {
    private String requestId="";//响应请求id
    private NetUser sender;//发件人
    private NetUser recipient;//收件人
    private String method="";//响应方法
    private boolean success=true;
    private String message="";
    private T content;
    private String sign=""; //签名值，未启用
    private Object contentData;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public NetUser getSender() {
        return sender;
    }

    public void setSender(NetUser sender) {
        this.sender = sender;
    }

    public NetUser getRecipient() {
        return recipient;
    }

    public void setRecipient(NetUser recipient) {
        this.recipient = recipient;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Object getContentData() {
        return contentData;
    }

    public void setContentData(Object contentData) {
        this.contentData = contentData;
    }

    public void setContentClass(Class clazz){
        if(content instanceof String){
            this.contentData= JSON.parseObject((String)content, clazz);
        }else{
             throw new RuntimeException("content 为非String类型无法完成对对象转换");
        }
    }


}
