package com.yw.platform.yhtext.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yw.platform.R;
import com.yw.platform.activity.BaseActivity;
import com.yw.platform.beans.base.ResponseModel;
import com.yw.platform.beans.recevice.PolicyBean;
import com.yw.platform.global.AppUser;
import com.yw.platform.global.MyApplication;
import com.yw.platform.model.AppInfo;
import com.yw.platform.netApi.NetApi;
import com.yw.platform.service.DeviceManager;
import com.yw.platform.service.LocalHandleService;
import com.yw.platform.test.TestActivity;
import com.yw.platform.tools.AppInfoTools;
import com.yw.platform.ui.activity.MainNewActivity;
import com.yw.platform.view.CustomProgressDialog;
import com.yw.platform.yhtext.beans.MessageEvent;
import com.yw.platform.yhtext.beans.accept_bean.AcceptLoginBean;
import com.yw.platform.yhtext.beans.accept_bean.AcceptQueryAppBean;
import com.yw.platform.yhtext.beans.send_bean.SendLoginBean;
import com.yw.platform.yhtext.beans.send_bean.base.BaseSendMsgBean;
import com.yw.platform.yhtext.beans.send_bean.base.BaseUserBean;
import com.yw.platform.yhtext.netty.client.Const;
import com.yw.platform.yhtext.utils.MD5Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import lzhs.com.library.Utils;
import lzhs.com.library.utils.PhoneUtils;
import lzhs.com.library.wedgit.permission.PermissionUtils;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    Button logincommit;
    EditText editAccount, editPassword;
    String account, password;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        EventBus.getDefault().register(this);
        logincommit = (Button) findViewById(R.id.button);
        editAccount = (EditText) findViewById(R.id.edit_account);
        editPassword = (EditText) findViewById(R.id.edit_password);
        findViewById(R.id.button_test).setOnClickListener(LoginActivity.this);
        Utils.init(this);
        initpermissions();

    }
    private void initpermissions() {
        String[] permissions = {
                Manifest.permission_group.LOCATION,
                Manifest.permission_group.STORAGE
                ,Manifest.permission_group.PHONE
        };
        PermissionUtils.permission(permissions)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(ShouldRequest shouldRequest) {
                        // 处理 设置拒绝权限后再次请求的回调接口
                        Toast.makeText(LoginActivity.this, "授权才能登陆", Toast.LENGTH_SHORT).show();
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        //处理 请求成功逻辑
                        logincommit.setOnClickListener(LoginActivity.this);

                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        //处理 请求失败逻辑
                    }
                })
                .request();


    }
    private String requestId;
    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptMsg(MessageEvent event) {
        switch (event.getCode()) {
            case Const.ACCEPT_CODE:
                String str = (String) event.getDataContent();
                break;
            case Const.METHER_LOGIN_CODE:  //登录LOGIN操作
                AcceptLoginBean loginBean = JSON.parseObject((String) event.getDataContent(), AcceptLoginBean.class);
                if (loginBean.isSuccess()){
                    AppUser appUser=new AppUser();
                    appUser.setUserCode(loginBean.getUserCode());
                    appUser.setName("吴名氏");
                    MyApplication.getInstance().setAppUser(appUser);
                    setUpateMessage("设备信息验证中...");
                    requestId=System.currentTimeMillis()+"_"+((int)(Math.random()*100));
                    NetApi.requestPolicy(requestId);
                }else {
                    dialogDismiss();
                    Toast.makeText(this, loginBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case Const.METHER_QUERYPOLICY_CODE:
                ResponseModel responseData= event.getResponseData();
                if(!responseData.getRequestId().equals(requestId)) break;
                if (responseData.isSuccess()){
                    try {
                        responseData.setContentClass(PolicyBean.class);
                        PolicyBean policy= (PolicyBean) responseData.getContentData();
                        // TODO: 2018/5/8 获取策略接口
                         String camera = policy.getCamera();//相机
                        if (TextUtils.equals(camera,"yes")) DeviceManager.getInstance(this).setCameraDisabled(true);
                        else  DeviceManager.getInstance(this).setCameraDisabled(false);

                      /* String wifi=policy.getBluetooth();//蓝牙
                        RroadcastUtil.addBluetoothSate(this);//注册广播
                        if (TextUtils.equals(wifi,"yes")) SetInfo.getInstance().setBluetoothEable(true);
                        else  SetInfo.getInstance().setBluetoothEable(false);
*/
                        MyApplication.getInstance().setPolicy(policy);
                        requestId=System.currentTimeMillis()+"_"+((int)(Math.random()*100));
                        NetApi.queryApp(requestId);
                        setUpateMessage("获取应用例表");
                    }catch (Exception e){
                        dialogDismiss();
                        Toast.makeText(this, "策略信息异常", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    dialogDismiss();
                    Toast.makeText(this,"策略获取失败", Toast.LENGTH_SHORT).show();
                }
                break;
            //请求数据返回
            case Const.METHER_METHER_QUERYAPP_CODE:
                ResponseModel responseData1= event.getResponseData();
                if(!responseData1.getRequestId().equals(requestId)) break;
                if (responseData1.isSuccess()){
                    responseData1.setContentClass(AcceptQueryAppBean.class);
                    AcceptQueryAppBean appBean= (AcceptQueryAppBean) responseData1.getContentData();
                    List<AppInfo> apps= AppInfoTools.appBean2Appinfo(appBean.getApps());
                    MyApplication.getInstance().setResList(apps);
                    dialogDismiss();
                    Intent intent =new Intent();
                    intent.setClass(this, MainNewActivity.class);
                    startActivity(intent);
                    finish();
                }else{

                }
                break;
        }
    }
    /**
     * 更新进度显示
     */
    private void setUpateMessage(String message) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.getInstance(this);
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }
    public void dialogDismiss(){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @SuppressLint("MissingPermission")
    private String createLogingBean() {
        BaseSendMsgBean sendMsgBean = createDefaultSendBean();
        SendLoginBean loginBean = new SendLoginBean();
        loginBean.setDeviceCode(PhoneUtils.getDeviceId() + "");
        loginBean.setNotification("REQUEST");
        loginBean.setDeviceType("mi");
        loginBean.setDeviceSystem("android");//操作系统
        loginBean.setPassword(MD5Util.encrypt(password));//"登录密码，传MD5加密后的值"
        loginBean.setUserCode(account);//"登录账号"
        sendMsgBean.setContent(loginBean);
        sendMsgBean.setRequestId(Const.METHER_LOGIN_CODE+"");
        sendMsgBean.setMethod(Const.METHER_LOGIN);
        return JSON.toJSONString(sendMsgBean);
    }

    @NonNull
    private BaseSendMsgBean createDefaultSendBean() {
        BaseSendMsgBean sendMsgBean = new BaseSendMsgBean();
        sendMsgBean.setSender(createDefault("","ANDROIDPHONE"));
        List<BaseUserBean> recipients = new ArrayList<>();
        recipients.add(createDefault("INTERFACE",""));
        sendMsgBean.setRecipients(recipients);
        return sendMsgBean;
    }

    private BaseUserBean createDefault(String code,String client) {
        BaseUserBean userBean = new BaseUserBean();
        userBean.setClient(client);
        userBean.setClientVersion("客户端版本，发件人必须传，收件人可以不传");
        userBean.setIct("SOCKET");//可以不传
        userBean.setUserCode(code);

        return userBean;
    }

    public void sendMessage(String data) {
        MessageEvent event = new MessageEvent<String>();
        event.setCode(Const.SEND_CODE);
        event.setMsg("正在向服务器发送登录消息");
        event.setData(data);
        EventBus.getDefault().post(event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_test:
                Intent intent1 =new Intent();
                intent1.setClass(this, TestActivity.class);
                startActivity(intent1);
                break;
            case R.id.button:
                account = editAccount.getText().toString().trim();
                password = editPassword.getText().toString().trim();
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, R.string.accountorpsdIsEmpty, Toast.LENGTH_SHORT).show();
                }else {
                    String data = createLogingBean();
                    setUpateMessage("登陆中...");
                    sendMessage(data);
                }
                break;
        }
    }
  /*  public void setText(String text) {
        StringBuffer stringBuffer = new StringBuffer(mTextShow.getText());
        stringBuffer.append(text + "\n\r");
        mTextShow.setText(stringBuffer.toString());
    }*/
}
