package com.yw.platform.yhtext.netty.client;

public interface NettyListener {


    /**
     * 对消息的处理
     */
    void onMessageResponse(String data);

    /**
     * 当服务状态发生变化时触发
     */
    void onServiceStatusConnectChanged(int statusCode);
}
