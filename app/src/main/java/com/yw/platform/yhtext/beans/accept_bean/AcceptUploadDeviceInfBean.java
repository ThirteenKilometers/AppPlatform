package com.yw.platform.yhtext.beans.accept_bean;

/**
 * <br/>
 * 作者：LZHS<br/>
 * 时间： 2018/4/25 01:00<br/>
 * 邮箱：1050629507@qq.com
 */
public class AcceptUploadDeviceInfBean {
    /**
     * 响应时返回：描述收件人响应信息
     */
    String message;
    /**
     * true 保存成功  false 保存失败
     */
    boolean success;
    /**
     * 值：RESPONES
     */
    String notification;

    /**
     * 结果编号
     */
    int code;

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
}
