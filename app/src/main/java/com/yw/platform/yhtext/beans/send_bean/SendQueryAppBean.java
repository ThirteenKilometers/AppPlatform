package com.yw.platform.yhtext.beans.send_bean;

import java.io.Serializable;

/**
* <br/>
* 作者：LZHS<br/>
* 时间： 2018/4/27 9:31<br/>
* 邮箱：1050629507@qq.com
 */

public class SendQueryAppBean  implements Serializable{
    String notification;
    String deviceSystem;
    String userCode;

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getDeviceSystem() {
        return deviceSystem;
    }

    public void setDeviceSystem(String deviceSystem) {
        this.deviceSystem = deviceSystem;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
