package com.yw.platform.beans.base;

import java.io.Serializable;

/**
 * Created by cxb on 2018/4/26.
 */

public class ResponseBean implements Serializable {
    private String message;//响应时返回：描述收件人响应信息
    private boolean success=true;//
    private String notification="RESPONES";
    private int code=0;
    private String noticeId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }
}
