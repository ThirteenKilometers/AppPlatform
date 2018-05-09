package com.yw.platform.beans.recevice;

import java.io.Serializable;
import java.util.List;

/**
 * 接收服务端发送过来的消息实体
 * Created by cxb on 2018/4/26.
 */
public class ServiceNotice implements Serializable{
    private String notification;
    private List<Notice>  notices;

    public class Notice{
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

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public void setNotices(List<Notice> notices) {
        this.notices = notices;
    }
}
