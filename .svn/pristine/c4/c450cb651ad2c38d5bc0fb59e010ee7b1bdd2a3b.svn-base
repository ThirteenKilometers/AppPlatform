package com.yw.platform.yhtext.beans.send_bean.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <br/>
 * 作者：LZHS<br/>
 * 时间： 2018/4/21 12:51<br/>
 * 邮箱：1050629507@qq.com
 */
public class BaseSendMsgBean implements Serializable {


    /**
     * sender : {"userCode":"testUserCode","client":"ANDROIDPHONE","ict":"SOCKET"}
     * recipients : [{"userCode":"INTERFACE","client":"INTERFACE","ict":"SOCKET"}]
     * method : login
     * content : {"userCode":"testUserCode","password":"yw123456","notification":"REQUEST"}
     */

    private BaseUserBean sender = new BaseUserBean();
    private String method = "";
    private Object content = new Object();
    private List<BaseUserBean> recipients = new ArrayList<>();
    private String requestId="";

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getSender() {
        return sender;
    }

    public void setSender(BaseUserBean sender) {
        this.sender = sender;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public List<BaseUserBean> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<BaseUserBean> recipients) {
        this.recipients = recipients;
    }
}
