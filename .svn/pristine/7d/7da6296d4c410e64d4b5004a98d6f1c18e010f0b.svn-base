package com.yw.platform.net.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.yw.platform.net.MsgHandle;
import com.yw.platform.net.YwLog;
import com.yw.platform.net.netty.NettyClient;
import com.yw.platform.net.netty.crc.RequestUtil;
import com.yw.platform.net.netty.inter.NettyListener;
import com.yw.platform.tools.nettyn.NetInterface;
import com.yw.platform.tools.nettyn.SendBack;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 *
 * Created by LiuSaibao on 11/17/2016.
 */
public class NettyService extends Service implements NettyListener {

    private MyBinder myBinder;
    private NetworkReceiver receiver;
    private MsgHandle msgHandle;
    private ScheduledExecutorService mScheduledExecutorService;

    private void shutdown() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdown();
            mScheduledExecutorService = null;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        receiver = new NetworkReceiver();
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        // 自定义心跳，每隔20秒向服务器发送心跳包
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                byte[] requestBody = {(byte) 0xFE, (byte)0xED, (byte)0xFE, 5};
//                boolean state= NettyClient.getInstance().sendMsgToServer(requestBody, new ChannelFutureListener() {    //3
//                    @Override
//                    public void operationComplete(ChannelFuture future) {
//                        if (future.isSuccess()) {                //4
//                            Log.e("","心跳发送成功");
//                        } else {
//                            Log.e("","心跳发送失败");
//                        }
//                    }
//                });
//                if(!state)Log.e("","心跳发送失败");

            }
        }, 1, 10, TimeUnit.SECONDS);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NettyClient.getInstance().setListener(this);
        connect();
        return START_NOT_STICKY;
    }

    @Override
    public void onServiceStatusConnectChanged(int statusCode) {		//连接状态监听
        Log.e("connect status:", statusCode+"");
        if (statusCode == NettyListener.STATUS_CONNECT_SUCCESS) {

        } else {

        }
    }
    /**
     * 认证数据请求
     */
    private void authenticData(Object data) {
        byte[] content = RequestUtil.getEncryptBytes(data);
        byte[] requestHeader = RequestUtil.getRequestHeader(content, 1, 1001);
        byte[] requestBody = RequestUtil.getRequestBody(requestHeader, content);
        NettyClient.getInstance().sendMsgToServer(requestBody, new ChannelFutureListener() {    //3
            @Override
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {                //4
                    Log.e("Write auth successful","");
                } else {
                    Log.e("Write auth error","");
                }
            }
        });
    }

    @Override
    public void onMessageResponse(String data) {
        YwLog.e("net-通讯体内接收消：",data+"");
        boolean isHandle=false;
        if(this.msgHandle!=null){
            try {
                isHandle=this.msgHandle.handle(data);
            } catch (RemoteException e) {
                e.printStackTrace();
                isHandle=false;
            }
        }
        if(!isHandle){//消息未被处理进行推送通知
            YwLog.e("net-通讯体内","消息未正常处理");
        }
    }
    private void connect(){
        if (!NettyClient.getInstance().getConnectStatus()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!NettyClient.getInstance().getConnectStatus()){
                        NettyClient.getInstance().connect();//连接服务器
                    }
                }
            }).start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        shutdown();
        NettyClient.getInstance().setReconnectNum(0);
        NettyClient.getInstance().disconnect();
    }
    public class MyBinder extends NetInterface.Stub {

        @Override
        public void startWork() throws RemoteException {
            connect();
        }
        public void send(String data, final SendBack back) throws RemoteException {
            YwLog.e("net-处理发送服务线程","内容"+data);
            boolean state=NettyClient.getInstance().sendMsgToServer(data.getBytes(), new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {//4
                        back.onSusser();
                    } else {
                        back.onFail(1);
                    }
                }
            });
            if(state){
                back.onSusser();
            }else{
                back.onFail(1);
            }
        }

        @Override
        public int getSate() throws RemoteException {
            return NettyClient.getInstance().isConnect()?1:0;
        }

        @Override
        public void setMsgHandle(MsgHandle msgHandle) throws RemoteException {
            NettyService.this.msgHandle=msgHandle;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return myBinder;
    }
    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI|| activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    connect();
                }
            }else {


            }
        }
    }
}
