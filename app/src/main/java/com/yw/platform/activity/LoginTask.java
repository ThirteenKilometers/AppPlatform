package com.yw.platform.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yw.platform.R;
import com.yw.platform.download.DownloadManager;
import com.yw.platform.download.DownloadService;
import com.yw.platform.global.Constants;
import com.yw.platform.global.MyApplication;
import com.yw.platform.model.AppInfo;
import com.yw.platform.utils.AESEncryptor;
import com.yw.platform.utils.DeviceUtil;
import com.yw.platform.utils.FileUtil;
import com.yw.platform.utils.HttpClient;
import com.yw.platform.utils.LogSendUtils;
import com.yw.platform.view.CustomProgressDialog;

import net.ttxc.L4Proxy.L4ProxyArd;

import org.apache.http.cookie.Cookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class LoginTask {
    private final int APK_APP = 0;//0表示apk应用
    private final int NATIVE_APP = 1;//1表示自带原生应用
    private final int VIRTUAL_APP = 2;//2表示虚拟应用
    private final int PHONEGAP_APP = 3;//3表示自带cordova应用(类似代办公文等)
    private final int WEB_APP = 4;//4.表示webApp
    private boolean showUcWeb;
    private boolean veryfyDevice;
    private Handler handler;
    private String DownLoadURL;
    private CustomProgressDialog progressDialog;
    private LoginBack back;

    private Context context;
    private String mAccount;
    private String mPassword;

    public LoginTask(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 11://更新进度
                        setUpateMessage(msg.getData().getString("text"));
                        break;
                    case 12://成功
                        progressDialog.dismiss();
                        if (back != null) {
                            back.onSuccess();
                        }
                        break;
                    case 2:
                        if (Constants.isVPN) {
                            progressDialog.setMessage("证书认证中...");
                            new Thread(new Runnable() {
                                public void run() {
                                    verify();
                                }
                            }).start();
                        } else {
                            progressDialog.setMessage("登录认证中...");
                            new Thread(new Runnable() {
                                public void run() {
                                    verifyWithoutVPN();
                                }
                            }).start();
                        }
                        break;
                    case 5:
                        // 客户端版本需要更新
                        showUpdateDialog("提示", "有可更新版本，是否马上更新？");
                        break;
                    case 6://失败报错
                        progressDialog.dismiss();
                        if (back != null) {
                            back.onFail(msg.getData().getString("text"));
                        }
                        break;
                    case 222:
                        //注册消息推送
                        progressDialog.dismiss();
                        new Thread(new Runnable() {
                            public void run() {
                                logSend();
                            }
                        }).start();
                        break;
                    default:
                        setUpateMessage("未知");
                }
            }
        };
    }

    public void attemptLogin(String company, String account, String password, LoginBack back) {
        this.back = back;
        this.mAccount = company + "_" + account;
        this.mPassword = password;
        if (Constants.isVPN) {
            setUpateMessage("VPN连接中...");
        }
        new Thread() {
            public void run() {
                Looper.prepare();
                if (Constants.isVPN) {
                    vpnConnect(Constants.vpn_ip, Constants.rootDir, Integer.parseInt(Constants.vpn_port));
                } else {
                    //开启ssl加密连接服务
                    L4ProxyArd.getInstance().StartOpenSSL();
                    getAdressConfig();
                }
                Looper.loop();
            }
        }.start();
    }

    /**
     * 更新进度显示
     */
    private void setUpateMessage(String message) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.getInstance(context);
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
    }

    private void sendHandler(int what, String coment) {
        Message msg1 = new Message();
        msg1.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("text", coment);  //往Bundle中存放数据
        msg1.setData(bundle);//mes利用Bundle传递数据
        handler.sendMessage(msg1);//用activity中的handler发送消息
    }

    private void sendFailHandler(int what, String coment) {
        Message msg1 = new Message();
        msg1.what = 6;
        Bundle bundle = new Bundle();
        bundle.putString("text", coment);  //往Bundle中存放数据
        msg1.setData(bundle);//mes利用Bundle传递数据
        handler.sendMessage(msg1);//用activity中的handler发送消息
    }

    private void showUpdateDialog(final String title, final String text) {
        // TODO Auto-generated method stub
        Dialog altertDialog = new AlertDialog.Builder(context)
                .setTitle(title).setMessage(text).setPositiveButton(
                        "更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                downLoadMiddleWare();
                            }
                        }).setNegativeButton("暂不更新",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (veryfyDevice) {
                                    new Thread() {
                                        public void run() {
                                            deviceRegist();
                                        }
                                    }.start();
                                } else {
                                    handler.sendEmptyMessage(2);
                                }
                            }
                        }).create();
        altertDialog.setCanceledOnTouchOutside(false);
        altertDialog.show();
    }

    public void downLoadMiddleWare() {
        sendHandler(11, "客户端下载中...");
//		new PlatformDownloaderThread(downLoadHandler, DownLoadURL, FileUtil
//				.checkAndMkFile(FileUtil.setMkdir(LoginActivity.this) + File.separator),
//				"platform.apk", "").start();
        DownloadManager downloadManager = DownloadService.getDownloadManager(context);
        try {
            downloadManager.addNewDownload(DownLoadURL, "com.yw.platform",
                    Constants.downloadFile + "platform.apk", false, false,
                    new UpdatePlatformCallBack(Constants.downloadFile + "platform.apk"));
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void verifyWithoutVPN() {
        String result = HttpClient.getInstance().executePost_verifyWithoutVPN(Constants.platIp, Constants.platPort, mAccount, mPassword, DeviceUtil.getAndroidId(context));
        Log.i("LoginActivity", "verify_returnJsons=" + result);
        if (result != null) {
            JSONObject jsonObject_total;
            try {
                jsonObject_total = new JSONObject(result);
                boolean state = jsonObject_total.getBoolean("state");
                if (!state) {
                    if (jsonObject_total.getString("message").contains("jdbc.password.invalid")) {
                        sendFailHandler(6, "认证失败！密码错误");
                    } else {
                        sendFailHandler(6, "认证失败！");
                    }
                    return;
                }
                JSONObject jsonObject_content = jsonObject_total.getJSONObject("content");
                JSONObject jsonObject_organization = jsonObject_content.getJSONObject("organization");
                Constants.organization = jsonObject_organization.getString("name");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            getAppList();
        } else {
            sendFailHandler(6, "认证失败！");
        }
    }

    /**
     * 证书认证
     */
    private void verify() {
        String result = HttpClient.getInstance().executePost_verify(Constants.platIp, Constants.platPort, mAccount, mPassword);
        Log.i("LoginActivity", "verify_returnJsons=" + result);
        List<Cookie> list = HttpClient.getInstance().getAllCookies();
        for (Cookie cookie : list) {
            if (cookie.getDomain().contains(Constants.platIp)) {
                Log.i("LoginActivity", "Domain=" + cookie.getDomain() + ",name=" + cookie.getName() + ",value=" + cookie.getValue());
                JSONObject job = new JSONObject();
                try {
                    job.put("vpnIp", Constants.vpn_ip);
                    job.put("vpnPort", Constants.vpn_port);
//					job.put("platIp", platform_ip);
//					job.put("platPort", platform_port);
                    job.put("platIp", Constants.platIp);
                    job.put("platPort", Constants.platPort);
                    job.put("cookie", cookie.getName() + "=" + cookie.getValue());
                    job.put("cerPass", AESEncryptor.encrypt(Constants.KEY, mPassword));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                FileUtil.writeIpToSdCard(job.toString());
            }
        }

        if (result != null) {
            JSONObject jsonObject_total;
            try {
                jsonObject_total = new JSONObject(result);
                boolean state = jsonObject_total.getBoolean("state");
                if (!state) {
                    sendFailHandler(6, "证书认证失败！");
                    return;
                }
                JSONObject jsonObject_content = jsonObject_total.getJSONObject("content");
                JSONObject jsonObject_organization = jsonObject_content.getJSONObject("organization");
                Constants.organization = jsonObject_organization.getString("name");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            getAppList();
        } else {
            sendFailHandler(6, "证书认证失败！");
        }
    }

    private void getAppList() {
        sendHandler(11, "应用列表获取中...");
        String result = HttpClient.getInstance().post_getAppList(mAccount, Constants.platIp, Constants.platPort);
        Log.i("LoginActivity", "getAppList_returnJsons=" + result);
        if (result != null) {
            try {
                JSONObject jsonObject_total = new JSONObject(result);
                boolean state = jsonObject_total.getBoolean("state");
                if (!state) {
                    sendFailHandler(6, "获取应用列表失败！");
                    return;
                }
                JSONArray jsonArray = jsonObject_total.getJSONArray("content");
                List<AppInfo> resList = new ArrayList<AppInfo>();
                AppInfo tmpInfo;
                for (int i = 0; i < jsonArray.length(); i++) {
                    tmpInfo = new AppInfo();
                    tmpInfo.appName = jsonArray.getJSONObject(i).getString("displayName");
                    tmpInfo.iconUrl = jsonArray.getJSONObject(i).getString("iconURL");
                    String type = jsonArray.getJSONObject(i).getString("intstallPackageType");
                    tmpInfo.version = jsonArray.getJSONObject(i).getString("version");
                    tmpInfo.appCode = jsonArray.getJSONObject(i).getString("applicationCode");
                    if ("APP_PAGE_ADAPTER".equals(type)) {
                        //适配的资源
                        tmpInfo.type = PHONEGAP_APP;
                        tmpInfo.native_pro = jsonArray.getJSONObject(i).getString("installPackageName");
                        tmpInfo.localFilePath = Constants.downloadFile + tmpInfo.native_pro + ".zip";
                        tmpInfo.downloadUrl = jsonArray.getJSONObject(i).getString("installPackageURL");

                        String cur_version = context.getSharedPreferences(Constants.SYSTEMPREFERENT, 0).getString(tmpInfo.appName + "_version", "0");
                        if (!cur_version.equals(tmpInfo.version)) {
                            tmpInfo.needUpdate = true;
                        }
                    } else if ("APP_VIRTUAL".equals(type)) {
                        tmpInfo.type = VIRTUAL_APP;
                        tmpInfo.virtualResourceName = jsonArray.getJSONObject(i).getString("virtualResourceName");
                    } else if ("APP_ANDROID_APK".equals(type)) {
                        tmpInfo.type = APK_APP;
                        tmpInfo.packageName = jsonArray.getJSONObject(i).getString("installPackageName");
                        tmpInfo.localFilePath = Constants.downloadFile + tmpInfo.packageName + ".apk";
                        tmpInfo.downloadUrl = jsonArray.getJSONObject(i).getString("installPackageURL");
                        if (checkAppIsNeedUpdate(tmpInfo.packageName, tmpInfo.version)) {
                            tmpInfo.needUpdate = true;
                        }
                    } else if ("APP_WEB".equals(type)) {
                        tmpInfo.type = WEB_APP;
                        tmpInfo.ssoState = jsonArray.getJSONObject(i).getString("ssoState").equals("YES");
                        tmpInfo.web_url = jsonArray.getJSONObject(i).getString("webURL");
                        if (tmpInfo.ssoState) {
                            String config = jsonArray.getJSONObject(i).getString("webConfigBase64");
//							JSONObject config_obj=new JSONObject(new String(Base64.decode(config)));
//							tmpInfo.excuteJs=config_obj.getString("excuteJs");
//							tmpInfo.needCount=config_obj.getString("needCount").equals("YES");
                        }

                    }
                    resList.add(tmpInfo);
                }
                if (showUcWeb) {
                    tmpInfo = new AppInfo();
                    tmpInfo.appName = "浏览器";
                    tmpInfo.className = UCAppActivity.class;
                    tmpInfo.type = NATIVE_APP;
                    tmpInfo.appIcon = R.drawable.email_icon;
                    resList.add(tmpInfo);
                }
                tmpInfo = new AppInfo();
                tmpInfo.appName = "设置";
                tmpInfo.className = SetActivity.class;
                tmpInfo.type = NATIVE_APP;
                tmpInfo.appIcon = R.drawable.ic_launcher;
                resList.add(tmpInfo);
                MyApplication.getInstance().setResList(resList);
                getWhiteAdress();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                sendFailHandler(6, "获取应用列表失败！");
            }
        } else {
            sendFailHandler(6, "获取应用列表失败！");
        }
    }

    private void getWhiteAdress() {
        sendHandler(11, "获取网络策略...");
        String result = HttpClient.getInstance().executeGet("http://" + Constants.platIp + ":" + Constants.platPort + "/platform/address?loginName=" + this.mAccount);
        Log.e("策略请求 结果：", "--" + result);
        if (result != null) {
            JSONObject jsonObject_total;
            try {
                jsonObject_total = new JSONObject(result);
                boolean state = jsonObject_total.getBoolean("state");
                if (!state) {
                    sendFailHandler(6, "网络策略获取失败！");
                    return;
                }
                JSONArray jsonArray_adress = jsonObject_total.getJSONArray("content");
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < jsonArray_adress.length(); i++) {
                    list.add(jsonArray_adress.get(i).toString());
                }
                MyApplication.getInstance().setAddressList(list);
                for (int i = 0; i < MyApplication.getInstance().getAddressList().size(); i++) {
                    Log.i("Main", "adress=" + MyApplication.getInstance().getAddressList().get(i));
                }
                sendHandler(12, "");
                logSend();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            sendFailHandler(6, "网络策略获取失败！");
        }
    }

    public void logSend() {
        String result = LogSendUtils.getInstance(context).sendLog("EMAP", mAccount, "PLATFORM", DeviceUtil.getVersionName(context), "LOGIN", Constants.platIp, Constants.platPort);
        Log.i("LoginActivity", "logSend_returnJsons=" + result);
    }

    private boolean checkAppIsNeedUpdate(String packageName, String newVersion) {
        if (newVersion.isEmpty() || packageName.isEmpty() || "null".equalsIgnoreCase(newVersion)) {
            return false;
        }
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean needUpdate = false;
        int newV = 3;
        try {
            newV = Integer.parseInt(newVersion);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (versionCode < newV) {
            needUpdate = true;
        } else {
            needUpdate = false;
        }
        return needUpdate;
    }

    private Boolean vpnConnect(String strIp, String certPath, int port) {
        int iResult;
        // 建立vpn连接
        iResult = L4ProxyArd.getInstance().L4ProxyShakeHandsWithVPN(strIp, port, certPath, "", mPassword);
        System.out.println("iResult1:" + iResult);
        if (iResult != 0) {
            String errnonum = String.valueOf(iResult);
            sendFailHandler(6, "VPN连接失败");
            return false;
        } else {
            Constants.cerPw = mPassword;
            getAdressConfig();
            return true;
        }
    }

    /**
     * 获取配置
     */
    private void getAdressConfig() {
        sendHandler(11, "策略获取中...");
        if (Constants.isVPN) {
            int port = L4ProxyArd.getInstance().L4ProxyServiceRun(Constants.platIp + ":" + Constants.platPort);
            Constants.platIp = "127.0.0.1";
            Constants.platPort = String.valueOf(port);
        }
        String result = HttpClient.getInstance().post_getAdressConfig(Constants.platIp, Constants.platPort);
        Log.i("LoginActivity", "getConfig_returnJsons=" + result);
        if (result != null) {
            try {
                JSONObject jsonObject_total = new JSONObject(result);
                boolean state = jsonObject_total.getBoolean("state");
                if (!state) {
                    sendFailHandler(6, "获取策略失败！");
                    return;
                }
                JSONObject jsonObject_content = jsonObject_total.getJSONObject("content");
                //获取证书认证方法
                String authenMethod = jsonObject_content.getString("authenMethod");
                //获取一些配置，如是否显示电子邮件等
                JSONObject jsonObject_config = jsonObject_content.getJSONObject("platformConfig");
//				boolean showConference = jsonObject_config.getBoolean("conference.enable");
//				showContacts = jsonObject_config.getBoolean("contacts.enable");
//				showEmail = jsonObject_config.getBoolean("email.enable");
                veryfyDevice = jsonObject_config.optBoolean("device.enable");
                showUcWeb = jsonObject_config.optBoolean("platform.app.common.browser");

                MyApplication.getInstance().setShowUcWeb(showUcWeb);
                //获取ip地址配置
                JSONArray jsonArray_adress = jsonObject_content.getJSONArray("servers");
                JSONObject job;
                for (int i = 0; i < jsonArray_adress.length(); i++) {
                    job = jsonArray_adress.getJSONObject(i);
                    if ("androidMessage_server".equals(job.getString("name"))) {
                        //消息推送服务器
                        Constants.message_ip = job.getString("ip");
                        Constants.message_port = job.getString("port");
                    } else if ("androidPhonePlatformAppUpgrade_server".equals(job.getString("name"))) {
                        //判断程序版本是否需要更新
                        DownLoadURL = job.getString("url");
                        String versionCode = job.getString("versionCode");
                        if (versionCode == null || versionCode.equalsIgnoreCase("")) {
                            handler.sendEmptyMessage(2);
                        } else {
                            int newVersionNum = Integer.parseInt(versionCode);
                            int curVersionNum = Integer.parseInt(DeviceUtil.getVersionCode(context));
                            if (curVersionNum >= newVersionNum) {
                                if (veryfyDevice) {
                                    deviceRegist();
                                } else {
                                    handler.sendEmptyMessage(2);
                                }
                            } else {
                                handler.sendEmptyMessage(5);
                            }
                        }
                    } else if ("middleware_server".equals(job.getString("name"))) {
                        //中间件服务器
                        Constants.m_ip = job.getString("ip");
                        Constants.mid_port = job.getString("port");
                        Constants.mid_port = String.valueOf(L4ProxyArd.getInstance().L4ProxyServiceRun(Constants.m_ip + ":" + Constants.mid_port));
                    } else if ("virtual_server".equals(job.getString("name"))) {
                        //虚拟化服务器
                        Constants.docIp = job.getString("ip");
                        Constants.docPort = job.getString("port");
//						Constants.vir_vpnIp=job.getString("ip");
//						Constants.vir_vpnPort=job.getString("port");
//						Constants.vir_vpnPort=String.valueOf(L4ProxyArd.getInstance().L4ProxyServiceRun(Constants.vir_vpnIp+":"+Constants.vir_vpnPort));
//						Constants.vir_vpnIp="127.0.0.1";
                    } else if ("midwareApi_server".equals(job.getString("name"))) {
                        //中间件端口
                        Constants.m_ip = job.getString("ip");
                        Constants.m_port = job.getString("port");
                        Constants.m_port = String.valueOf(L4ProxyArd.getInstance().L4ProxyServiceRun(Constants.m_ip + ":" + Constants.m_port));
                    }
                }
                Constants.m_ip = "127.0.0.1";
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                sendFailHandler(6, "获取策略失败！");
            }
        } else {
            sendFailHandler(6, "获取策略失败！");
        }
    }

    //设备注册
    private void deviceRegist() {
        sendHandler(11, "设备注册中...");
        String result = HttpClient.getInstance().executePost_Regist(Constants.platIp, Constants.platPort, mAccount, DeviceUtil.getAndroidId(context), android.os.Build.MODEL);
        //TODO 1.解析返回值，2，保存状态。
        if (result != null) {
            try {
                JSONObject jsonObject_total = new JSONObject(result);
                String message = jsonObject_total.getString("message");
                if (message.equalsIgnoreCase("success")) {
                    handler.sendEmptyMessage(2);
                    return;
                } else if (message.contains("DEVICE_ENROLL_ERROR_0X001")) {
                    sendFailHandler(6, "非法用户！");
                } else if (message.contains("DEVICE_ENROLL_ERROR_0X002")) {
                    sendFailHandler(6, "设备已被其他用户注册，请联系管理员解绑设备！");
                } else if (message.contains("DEVICE_ENROLL_ERROR_0X003")) {
                    sendFailHandler(6, "用户已注销，不能注册设备！");
                } else if (message.contains("DEVICE_ENROLL_ERROR_0X004")) {
                    sendFailHandler(6, "用户已冻结，暂不能注册设备！");
                } else if (message.contains("DEVICE_ENROLL_ERROR_0X005")) {
                    sendFailHandler(6, "用户数超限，暂不能注册设备！");
                } else if (message.contains("DEVICE_ENROLL_ERROR_0X006")) {
                    sendFailHandler(6, "设备已冻结，暂不能注册设备！");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                sendFailHandler(6, "设备注册失败！");
            }
        } else {
            sendFailHandler(6, "设备注册失败！");
        }
    }

    /**
     * 回调类
     */
    class UpdatePlatformCallBack extends RequestCallBack<File> {
        String path;
        DownloadManager downloadManager;

        UpdatePlatformCallBack(String path) {
            this.path = path;
        }

        @Override
        public void onStart() {
            // TODO Auto-generated method stub
            super.onStart();
            downloadManager = DownloadService.getDownloadManager(context);
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            // TODO Auto-generated method stub
            super.onLoading(total, current, isUploading);
            setUpateMessage("已经完成" + (current * 100 / total) + "%");
        }

        @Override
        public void onFailure(HttpException arg0, String arg1) {
            // TODO Auto-generated method stub
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            Toast.makeText(context, "更新失败！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(ResponseInfo<File> arg0) {
            // TODO Auto-generated method stub
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            try {
                if (null != downloadManager.getDownloadInfo("com.yw.platform")) {
                    downloadManager.removeDownload(downloadManager.getDownloadInfo("com.yw.platform"));
                }
            } catch (DbException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // // 设置目标应用安装包路径
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public interface LoginBack {
        void onSuccess();

        void onFail(String err);
    }
}