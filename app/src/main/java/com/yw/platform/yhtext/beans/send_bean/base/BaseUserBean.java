package com.yw.platform.yhtext.beans.send_bean.base;

import java.io.Serializable;

/**
 * <br/>
 * 作者：LZHS<br/>
 * 时间： 2018/4/25 00:38<br/>
 * 邮箱：1050629507@qq.com
 */
public class BaseUserBean implements Serializable {
    /**
     * 用户账号， 发件人可以不传，收件人必须传
     */
    String userCode = "";
    /**
     * 客户端类型 IOSPHONE （IOS客户端）;
     * ANDROIDPHONE（ANDROID客户端）;
     * INTERFACE（接口服务器）;
     * WEBPAGE（网页）;
     * COMMUNICATION（通信服务器）；
     */
    String client = "ANDROIDPHONE";
    /**
     * 客户端版本，发件人必须传，收件人可以不传
     */
    String clientVersion = "v1.0";
    /**
     * 通信类型SOCKET; WEBSOCKET，可以不传
     */
    String ict = "SOCKET";


    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getIct() {
        return ict;
    }

    public void setIct(String ict) {
        this.ict = ict;
    }
}
