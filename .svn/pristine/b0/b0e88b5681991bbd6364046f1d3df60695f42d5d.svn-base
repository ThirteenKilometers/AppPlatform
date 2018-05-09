package com.yw.platform.net.netty;

import android.util.Log;

import com.yw.platform.net.netty.inter.NettyListener;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 * Created by LiuSaibao on 11/23/2016.
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private NettyListener listener;

    public NettyClientHandler(NettyListener listener) {
        this.listener = listener;
    }
    /**
     * 客户端读取服务端信息
     */
    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
        ByteBuf buf = (ByteBuf) arg1;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String data=new String(bytes);
        Log.e("channel", "channelRead=" + data);
        listener.onMessageResponse(data);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        Log.e("channel", "messageReceived=" +msg.toString());
        listener.onMessageResponse(msg.toString());
    }


    /**
     * 客户端连接到服务端后进行
     * 覆盖channelActive 方法在channel被启用的时候触发（在建立连接的时候）
     * 覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端传给服务端");
        //NettyClient.getInstance().setConnectStatus(true);
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_SUCCESS);
        Log.e("channel", "channelActive=");
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.e("channel", "channelInactive=");
		//NettyClient.getInstance().setConnectStatus(false);
		listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_CLOSED);
		NettyClient.getInstance().reconnect();
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        Log.e("channel", "channelRegistered=");
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        Log.e("channel", "channelReadComplete=");
    }
    /*
     * (non-Javadoc)
     * 覆盖了 handlerAdded() 事件处理方法。
     * 每当从服务端收到新的客户端连接时
     */
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        Log.e("channel", "handlerAdded=");
    }

    /*
     * (non-Javadoc)
     * .覆盖了 handlerRemoved() 事件处理方法。
     * 每当从服务端收到客户端断开时
     */
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        Log.e("channel", "handlerRemoved=");
        //NettyClient.getInstance().setConnectStatus(false);
		listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_CLOSED);
		NettyClient.getInstance().reconnect();
    }

    /*
     * exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，
     * 即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时。
     * 在大部分情况下，捕获的异常应该被记录下来并且把关联的 channel 给关闭掉。
     * 然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现，
     * 比如你可能想在关闭连接之前发送一个错误码的响应消息。
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        super.exceptionCaught(ctx, cause);
        Log.e("channel", "exceptionCaught="+cause.getMessage());
        ThrowException(cause);
    }
    public void ThrowException( Throwable cause) {
        // 调试打印堆栈而不退出
        Log.e("异常", Log.getStackTraceString(cause));

        // 创建异常打印堆栈
        Exception e = new Exception("this is a log");
        e.printStackTrace();

        // 获取当前线程的堆栈
        for (StackTraceElement i : Thread.currentThread().getStackTrace()) {
            Log.e("异常", i.toString());
        }
        RuntimeException re = new RuntimeException();
        re.fillInStackTrace();
        Log.e("异常", "stackTrace", re);
    }

}
