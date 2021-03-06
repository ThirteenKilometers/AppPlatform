package com.yw.platform.beans.recevice;

/**
 * Created by cxb on 2018/4/28.
 */

public class PolicyBean {
    /**
     * policyName : test
     * passwordErrorNum : 0
     * wifiVisit : yes
     * deviceNum : 0
     * startTime : Jan 1, 1970 12:00:00 AM
     * endTime : Jan 1, 1970 5:55:06 PM
     * rememberPassword : no
     * sdWrite : yes
     * gps : yes
     * bluetooth : yes
     * camera : yes
     * message : success
     * success : true
     * code : 10000
     * notification : RESPONES
     */
    private String policyName;
    private int passwordErrorNum;
    private String wifiVisit;
    private int deviceNum;
    private String startTime;
    private String endTime;
    private String rememberPassword;
    private String sdWrite;
    private String gps;
    private String bluetooth;
    private String camera;
    private String message;
    private boolean success;
    private int code;
    private String notification;
    private String browser;//yes or no
    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public int getPasswordErrorNum() {
        return passwordErrorNum;
    }

    public void setPasswordErrorNum(int passwordErrorNum) {
        this.passwordErrorNum = passwordErrorNum;
    }

    public String getWifiVisit() {
        return wifiVisit;
    }

    public void setWifiVisit(String wifiVisit) {
        this.wifiVisit = wifiVisit;
    }

    public int getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(int deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRememberPassword() {
        return rememberPassword;
    }

    public void setRememberPassword(String rememberPassword) {
        this.rememberPassword = rememberPassword;
    }

    public String getSdWrite() {
        return sdWrite;
    }

    public void setSdWrite(String sdWrite) {
        this.sdWrite = sdWrite;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }
}
