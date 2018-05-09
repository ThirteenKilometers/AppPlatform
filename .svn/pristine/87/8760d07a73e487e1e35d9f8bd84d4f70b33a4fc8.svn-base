package com.yw.platform.net;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.yw.platform.beans.ModelManger;
import com.yw.platform.beans.base.RequestModel;
import com.yw.platform.net.service.NettyService;
import com.yw.platform.tools.nettyn.NetInterface;
import com.yw.platform.tools.nettyn.SendBack;
import com.yw.platform.yhtext.beans.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxb on 2018/4/24.
 */

public class YwConnect {
    private static YwConnect instance=new YwConnect();
    private List<BackId> backs;
    private NetInterface myBinder;

    private boolean isBind=false;
    private long outTime=1000*60*2;//请求超时时间

    /**
     *服务连接器
     */
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind=true;
            myBinder = NetInterface.Stub.asInterface(binder);
            try {
                myBinder.setMsgHandle(new ReceiveHandle());//为通讯体配消息处理类和网络状态监听类
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    private YwConnect() {
        backs=new ArrayList<>();
    }

    public static YwConnect getInstance() {
        return instance;
    }
    public void  init(Context context){
        //启动Service
        Intent intentOne = new Intent(context, NettyService.class);
        context.startService(intentOne);
        Intent intent = new Intent(context, NettyService.class);
        context.bindService(intent, conn, context.BIND_AUTO_CREATE);
    }

    /**
     *
     * 描述消息发送
     * @param model
     * @param msgBack
     */
    public void send(RequestModel model, MsgBack msgBack){
        if(isBind){
            try {
                String requestId=System.currentTimeMillis()+"_"+((int)(Math.random()*100));
                model.setRequestId(requestId);
                backs.add(new BackId(requestId,msgBack));
                Gson gson=new Gson();
                String sendContent=gson.toJson(model);
                YwLog.e("net-处理发送","内容"+sendContent);
                myBinder.send(sendContent,new FastSendBack(msgBack) );
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 向服务器发送消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendMsgToService(MessageEvent event) {/* Do something */
        if (event.getCode() == com.yw.platform.yhtext.netty.client.Const.SEND_CODE)
            try {
                myBinder.send((String) event.getData(),new FastSendBack(null));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    /**
     * 描述：处理请求响应
     */
    private boolean handleResponse(String requestId,JSONObject contentJson){
            if(requestId==null||"".equals(requestId)){
                YwLog.e("net-处理请求响应","响应id为空");
                return false;
            }
            for (BackId back:backs) {
                if(requestId.equals(back.getRequestId())){
                    backs.remove(back);
                    String content=contentJson.toString();
                    back.getMsgBack().onSusser(content);
                    return true;
                }else if(System.currentTimeMillis()-back.getRequestTime()>outTime){
                    YwLog.e("net-处理请求响应",back.getRequestId()+"请求响应超时");
                    backs.remove(back);
                    back.getMsgBack().onFail(1);
                }
            }
            YwLog.e("net-处理请求响应",requestId+"为非法响应超时无匹配处理返回");

        return false;
    }

    public RequestModel getDefaultSendModel(){
        RequestModel<Object> model=new RequestModel<Object>();
        model.setSender(ModelManger.getDefaultSendUser("test"));
        model.setRecipients(ModelManger.getDefaultRptUsers());
        return model;
    }

    /**
     * 接收消息处理类
     */
    class ReceiveHandle extends MsgHandle.Stub{

        @Override
        public boolean handle(String msg) throws RemoteException {
            try {
                JSONObject jsonObject=new JSONObject(msg);
                JSONObject jsonContentObj=jsonObject.optJSONObject("content");
                String notification=jsonContentObj.optString("notification","");
                if("RESPONES".equals(notification)){//请求响应消息
                    String  requestId=jsonObject.optString("requestId");
                    boolean ishanel=handleResponse(requestId,jsonObject);
                }else if("REQUEST".equals(notification)){
                    YwLog.e("net-接收消息","接收一条服务器消息");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
        public void onNetChange(int state){

        }
        public void  onHeart(){

        }
    }
    class FastSendBack extends SendBack.Stub{//消息发送状态响应类
        // 快速响应nett处理类
        private MsgBack msgBack;

        public FastSendBack(MsgBack msgBack) {
            this.msgBack=msgBack;
        }
        @Override
        public void onSusser() throws RemoteException {//消息体正常处理发送
            YwLog.e("net-发送状态","成功");
        }

        @Override
        public void onFail(int code) throws RemoteException {//消息体处理发送失败
            YwLog.e("net-发送状态","失败");
            if(msgBack!=null){
                backs.remove(msgBack);
                msgBack.onFail(2);
            }
        }

        @Override
        public void onBack(String msg) throws RemoteException {
            YwLog.e("net-发送状态","返回");
        }
    }
    class BackId{
        private String requestId;
        private MsgBack msgBack;
        private Long requestTime;
        public BackId(String requestId, MsgBack msgBack) {
            this.requestId = requestId;
            this.msgBack = msgBack;
            this.requestTime=System.currentTimeMillis();
        }

        public String getRequestId() {
            return requestId;
        }

        public MsgBack getMsgBack() {
            return msgBack;
        }

        public Long getRequestTime() {
            return requestTime;
        }
    }
}
