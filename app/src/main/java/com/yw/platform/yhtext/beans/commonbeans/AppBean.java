package com.yw.platform.yhtext.beans.commonbeans;

import java.io.Serializable;

/**
 * <br/> App信息
 * 作者：LZHS<br/>
 * 时间： 2018/4/27 9:31<br/>
 * 邮箱：1050629507@qq.com
 */

public class AppBean implements Serializable {
    String  appTypeId ;
    String     appName ;
    String   appPkgType;
    String startClassName;
    String  describetion ;
    String   appUrl;
    String appVersion  ;
    String   icoPath;
    String   fieSize;
    String  mainPath ;
    String  mainType ;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPkgType() {
        return appPkgType;
    }

    public void setAppPkgType(String appPkgType) {
        this.appPkgType = appPkgType;
    }

    public String getDescribetion() {
        return describetion;
    }

    public void setDescribetion(String describetion) {
        this.describetion = describetion;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getIcoPath() {
        return icoPath;
    }

    public void setIcoPath(String icoPath) {
        this.icoPath = icoPath;
    }

    public String getFieSize() {
        return fieSize;
    }

    public void setFieSize(String fieSize) {
        this.fieSize = fieSize;
    }

    public String getMainPath() {
        return mainPath;
    }

    public void setMainPath(String mainPath) {
        this.mainPath = mainPath;
    }

    public String getMainType() {
        return mainType;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }



    public String getStartClassName() {
        return startClassName;
    }

    public void setStartClassName(String startClassName) {
        this.startClassName = startClassName;
    }

    public String getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(String appTypeId) {
        this.appTypeId = appTypeId;
    }
}
