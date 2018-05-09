package com.yw.platform.yhtext.beans.accept_bean;


import java.io.Serializable;

public class AcceptQueryDocumentPreviewUrl implements Serializable {
  String  message;
  boolean success;
  String notification;
  int code;
  String previewUrl; //文档在线预览路径

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

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
}
