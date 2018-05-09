package com.yw.platform.beans.base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cxb on 2018/4/24.
 */

public class RequestModel<T> implements Serializable {
    private String requestId;
    private NetUser sender;
    private List<NetUser> recipients;
    private String method;
    private  T content;

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

    public List<NetUser> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<NetUser> recipients) {
        this.recipients = recipients;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
