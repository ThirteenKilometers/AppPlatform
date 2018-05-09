package com.yw.platform.yhtext.beans.accept_bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <br/>
 * 作者：LZHS<br/>
 * 时间： 2018/4/28 10:22<br/>
 * 邮箱：1050629507@qq.com
 */

public class AcceptQueryNoticBean implements Serializable {
    /**
     * 响应时返回：描述收件人响应信息
     */
    String message;

    /**
     * true 成功  false 失败
     */
    boolean success;
    /**
     * 值：RESPONES
     */
    String notification;

    /**
     * 结果编号：参考错误编码表
     */
    int code;

    /**
     * Notice 数组
     */
    List<Notice> notices=new ArrayList<>();

    public class Notice {
        private String noticeType;
        private String noticeId;
        private String senderUserId;
        private String senderUserName;
        private String dataId;
        private String dataName;

        public String getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(String noticeType) {
            this.noticeType = noticeType;
        }

        public String getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(String noticeId) {
            this.noticeId = noticeId;
        }

        public String getSenderUserId() {
            return senderUserId;
        }

        public void setSenderUserId(String senderUserId) {
            this.senderUserId = senderUserId;
        }

        public String getSenderUserName() {
            return senderUserName;
        }

        public void setSenderUserName(String senderUserName) {
            this.senderUserName = senderUserName;
        }

        public String getDataId() {
            return dataId;
        }

        public void setDataId(String dataId) {
            this.dataId = dataId;
        }

        public String getDataName() {
            return dataName;
        }

        public void setDataName(String dataName) {
            this.dataName = dataName;
        }
    }


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

    public List<Notice> getNotices() {
        return notices;
    }

    public void setNotices(List<Notice> notices) {
        this.notices = notices;
    }
}

