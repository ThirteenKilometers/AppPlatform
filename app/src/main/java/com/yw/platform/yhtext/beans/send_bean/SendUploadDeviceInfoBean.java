package com.yw.platform.yhtext.beans.send_bean;


import com.yw.platform.yhtext.beans.commonbeans.AppInstalInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备信息上传接口：method=”uploadDeviceInfo”
 * 请求Entity 信息
 * <br/>
 * 作者：LZHS<br/>
 * 时间： 2018/4/25 00:47<br/>
 * 邮箱：1050629507@qq.com
 */
public class SendUploadDeviceInfoBean {
    String notification;
    private Object deviceInfo = new Object();
    private List<AppInstalInfoBean>appInstalInfos=new ArrayList<>();
    private  String deviceCode;//设备号

    public List<AppInstalInfoBean> getAppInstalInfos() {
        return appInstalInfos;
    }

    public void setAppInstalInfos(List<AppInstalInfoBean> appInstalInfos) {
        this.appInstalInfos = appInstalInfos;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Object getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(Object deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
