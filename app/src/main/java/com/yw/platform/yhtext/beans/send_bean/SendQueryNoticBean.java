package com.yw.platform.yhtext.beans.send_bean;

import java.io.Serializable;

/**
 * <br/>
 * 作者：LZHS<br/>
 * 时间： 2018/4/28 9:31<br/>
 * 邮箱：1050629507@qq.com
 */

public class SendQueryNoticBean implements Serializable {
    String notification;
    String noticeType;
    String noticeId;
    String userCode;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
}
