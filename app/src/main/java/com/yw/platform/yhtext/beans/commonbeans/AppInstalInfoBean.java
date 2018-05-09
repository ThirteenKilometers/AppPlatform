package com.yw.platform.yhtext.beans.commonbeans;


import java.io.Serializable;

/**
 * AppInstalInfo信息
 */
public class AppInstalInfoBean  implements Serializable{
    String appTypeId;//安装包ID
    String installType;//安装类型：current 已安装最新版，history已安装历史版, none 未安装
    String version;//当前安装版本号，未安装不传

    public String getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(String appTypeId) {
        this.appTypeId = appTypeId;
    }

    public String getInstallType() {
        return installType;
    }

    public void setInstallType(String installType) {
        this.installType = installType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
