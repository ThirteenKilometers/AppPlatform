
package com.yw.platform.ui.activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yw.platform.R;
import com.yw.platform.activity.BaseActivity;
import com.yw.platform.activity.WebAppActivity;
import com.yw.platform.beans.base.ResponseModel;
import com.yw.platform.download.DownloadInfo;
import com.yw.platform.download.DownloadManager;
import com.yw.platform.download.DownloadService;
import com.yw.platform.global.AppUser;
import com.yw.platform.global.Constants;
import com.yw.platform.global.MyApplication;
import com.yw.platform.model.AppInfo;
import com.yw.platform.model.VirtualAppInfo;
import com.yw.platform.netApi.NetApi;
import com.yw.platform.tools.ApkCheckUtils;
import com.yw.platform.tools.AppInfoTools;
import com.yw.platform.tools.DeviceInfo;
import com.yw.platform.tools.SetInfo;
import com.yw.platform.utils.DeviceUtil;
import com.yw.platform.utils.FileUtil;
import com.yw.platform.utils.MyBase64;
import com.yw.platform.utils.SharedPreferencesUtils;
import com.yw.platform.utils.dialog.DialogBase;
import com.yw.platform.view.BaseViewHolder;
import com.yw.platform.view.CustomProgressDialog;
import com.yw.platform.view.PageControlView;
import com.yw.platform.view.RoundProgressBar;
import com.yw.platform.yhtext.beans.MessageEvent;
import com.yw.platform.yhtext.beans.accept_bean.AcceptQueryAppBean;
import com.yw.platform.yhtext.beans.accept_bean.AcceptQueryNoticBean;
import com.yw.platform.yhtext.beans.commonbeans.AppInstalInfoBean;
import com.yw.platform.yhtext.beans.commonbeans.DeviceInfoBean;
import com.yw.platform.yhtext.beans.send_bean.SendQueryNoticBean;
import com.yw.platform.yhtext.beans.send_bean.SendUploadDeviceInfoBean;
import com.yw.platform.yhtext.beans.send_bean.base.BaseSendMsgBean;
import com.yw.platform.yhtext.beans.send_bean.base.BaseUserBean;
import com.yw.platform.yhtext.netty.client.Const;
import com.yw.platform.yhtext.utils.PhoneMessage;

import net.ttxc.L4Proxy.L4ProxyArd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lzhs.com.library.utils.AppUtils;
import lzhs.com.library.utils.PhoneUtils;
import lzhs.com.library.utils.log.LogUtils;

/**
 * Created by panda on 15-1-12.
 */
@ContentView(R.layout.activity_main)
public class MainNewActivity extends BaseActivity implements View.OnClickListener {
    String data = "";
    int Singlestrength;
    double latitude = 0;//纬度
    double longitude = 0;//精度
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private static int COLUMN_NUM = 4;
    private static int LINE_NUM = 4;
    private static int PAGE_COUNT = COLUMN_NUM * LINE_NUM;
    private int screenHeight;
    @ViewInject(R.id.pageControl)
    private PageControlView mPageControl;
    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;
    private int pageNum;
    private int mCurPageNumIndex;
    private ArrayList<GridView> mAppGridViews = new ArrayList<GridView>();
    public static ArrayList<VirtualAppInfo> vir_resList;
    private List<AppInfo> resList = new ArrayList<AppInfo>();
    private int resCount;
    private List<AppGridViewAdapter> adapterList = new ArrayList<AppGridViewAdapter>();
    private DownloadManager downloadManager;
    private PackageManager packageManager;
    private static final int HANDEL_MSG_GO_SESSION_ACTIVITY = 1;
    private static final String APPSHELL_PATH = "c:\\Virtual Application Cloud Platform\\AppShell\\AppShell.exe ";
    private static final String LEFT_BRACKET = "[";
    private static final String RIGHT_BRACKET = "]";
    private static final String QUOTATION = "\"";
    public static String password;
    private CustomProgressDialog loadingDialog;
    public static MainNewActivity context;
    private byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};
    public String readyToOpenVirturl = "";
    private boolean canRefresh;
    private PagerAdapter mPagerAdapter;
    private AppInfo currenInfo;
    //private static BitmapUtils bitmapUtils;
    private String lastRequestId;
    private List<AppItem> appItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册广播,监听home键
        MyApplication.getInstance().addActivity(this);
        //MyApplication.getInstance().addActivity("MainActivity");
        context = this;
        ViewUtils.inject(this);
        initLocation();//百度定位
        EventBus.getDefault().register(this);
        initView();
        initData(MyApplication.getInstance().getResList());
        updateAppList();
        data = createUploadDeviceInfoMessage();
        sendMessage(data);
        sendMessage(createQueryNotice());
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        SetOption();
        mLocationClient.start();
    }

    private void SetOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    /**
     * 获取通知接口:  method=” queryNotice”
     */
    private String createQueryNotice() {
        BaseSendMsgBean sendMsgBean = createDefaultSendBean();
        SendQueryNoticBean sendQueryNoticBean = new SendQueryNoticBean();
        sendQueryNoticBean.setNotification("REQUEST");
        sendQueryNoticBean.setNoticeType("");//通知类型: 传null查所有类型的通知
        sendQueryNoticBean.setNoticeId("");//通知ID: 传null查所有类型的通知
        sendQueryNoticBean.setUserCode(getUserCode());//当前登录账号
        sendMsgBean.setContent(sendQueryNoticBean);

        sendMsgBean.setMethod(Const.METHER_QUERYNOTIC);
        sendMsgBean.setRequestId(Const.METHER_QUERYNOTIC + "");
        return JSON.toJSONString(sendMsgBean);
    }

    @SuppressLint("MissingPermission")
    private String createUploadDeviceInfoMessage() {
        BaseSendMsgBean sendMsgBean = createDefaultSendBean();
        SendUploadDeviceInfoBean uploadDeviceInfoBean = new SendUploadDeviceInfoBean();
        DeviceInfoBean deviceInfoBean = DeviceInfoEntith();
        List<AppInstalInfoBean> appInstalInfos = createDeviceInfos();
        uploadDeviceInfoBean.setDeviceInfo(deviceInfoBean);
        uploadDeviceInfoBean.setAppInstalInfos(appInstalInfos);
        uploadDeviceInfoBean.setNotification("REQUEST");
        uploadDeviceInfoBean.setDeviceCode(PhoneUtils.getDeviceId() + "");
        sendMsgBean.setContent(uploadDeviceInfoBean);
        sendMsgBean.setMethod(Const.METHER_UPLOAD_DEVICE_INFO);
        return JSON.toJSONString(sendMsgBean);
    }

    @NonNull
    private BaseSendMsgBean createDefaultSendBean() {
        BaseSendMsgBean sendMsgBean = new BaseSendMsgBean();
        sendMsgBean.setSender(createDefault("", "ANDROIDPHONE"));
        List<BaseUserBean> recipients = new ArrayList<>();
        recipients.add(createDefault("INTERFACE", ""));
        sendMsgBean.setRecipients(recipients);
        return sendMsgBean;
    }

    private BaseUserBean createDefault(String code, String client) {
        BaseUserBean userBean = new BaseUserBean();
        userBean.setClient(client);
        userBean.setClientVersion("客户端版本，发件人必须传，收件人可以不传");
        userBean.setIct("SOCKET");//可以不传
        userBean.setUserCode(code);

        return userBean;
    }

    @SuppressLint("MissingPermission")
    DeviceInfoBean DeviceInfoEntith() {
        DeviceInfoBean deviceInfoBean = new DeviceInfoBean();
        deviceInfoBean.setNotification("REQUEST");
        deviceInfoBean.setDeviceType(PhoneMessage.devicetype + "");//设备型号
        deviceInfoBean.setDeviceModel(PhoneMessage.deviceModel + "");//"设备制作商"
        deviceInfoBean.setDeviceSystem(PhoneMessage.deviceSysem + "");//"操作系统"
        deviceInfoBean.setDeviceSystemVersion(PhoneMessage.deviceSystemVersion + "");//系统版本
        deviceInfoBean.setMac(PhoneMessage.mac() + "");//"设备MAC地址"
        deviceInfoBean.setDeviceCode(PhoneUtils.getDeviceId() + "");//设备唯一标识
        deviceInfoBean.setImei("IMEI");
        deviceInfoBean.setEsn("ESN");
        deviceInfoBean.setCpuOccupy("");// TODO: 2018/5/2 有待测试类型 //cpu占用 String.valueOf(DeviceInfo.getCpuUsage())+

        long Occupy = (DeviceInfo.getmem_TOLAL() - DeviceInfo.getmem_UNUSED(MainNewActivity.this)) / DeviceInfo.getmem_TOLAL();
        deviceInfoBean.setRamOccupy(String.valueOf(Occupy) + "");//内存占用
        deviceInfoBean.setGpsState(DeviceInfo.getGPSState(MainNewActivity.this) + "");//GPS状态
        deviceInfoBean.setBluetoothState(SetInfo.getbluetoothEable() + "");//蓝牙状态
        deviceInfoBean.setNetworkState(DeviceInfo.getNetworkState(MainNewActivity.this));//网络状态
        deviceInfoBean.setElectricity("");
        DeviceInfo.getCurrentNetDBM(MainNewActivity.this, new DeviceInfo.SignalBack() {
            @Override
            public void onChange(int netType, int strength) {
                Singlestrength = strength;
            }
        });
        deviceInfoBean.setSignalIntensity(Singlestrength + "");//"信号强度"
        deviceInfoBean.setAccessInfo("");//接入点信息
        deviceInfoBean.setSimInfo("");//SIM卡信息
        //传经纬度
        deviceInfoBean.setLongitude(JSON.toJSONString(longitude));//经度（不能传空字符串）
        deviceInfoBean.setLatitude(JSON.toJSONString(latitude));//纬度
        deviceInfoBean.setStorageInfo("");//存储信息
        deviceInfoBean.setAppInfo("");//应用安装信息
        deviceInfoBean.setCertificateInfo("");//证书信息
        deviceInfoBean.setConfigInfo("");//配置信息
        return deviceInfoBean;
    }

    @NonNull
    private List<AppInstalInfoBean> createDeviceInfos() {
        List<AppInstalInfoBean> arrBean = new ArrayList<>();//需要向服务上传信息
        List<AppInfo> appInfoList = MyApplication.getInstance().getResList();//服务器历史数据
        List<AppUtils.AppInfo> locaApps = AppUtils.getAppsInfo();//获取本地应用信息
        for (AppInfo appInfo : appInfoList) {
            if (appInfo.type == 0) {
                boolean isInstall = false;//是否安装应用
                AppInstalInfoBean infoBean = new AppInstalInfoBean();
                infoBean.setAppTypeId(appInfo.appId);
                for (AppUtils.AppInfo locaApp : locaApps) {
                    if (TextUtils.equals(appInfo.packageName, locaApp.getPackageName())) {
                        // TODO: 2018/5/3  安装类型current 已安装最新版，history已安装历史版, none 未安装   已卸载应用
                        if (TextUtils.equals(appInfo.version, locaApp.getVersionCode() + ""))
                            infoBean.setInstallType("current");
                        else infoBean.setInstallType("history");
                        infoBean.setVersion("" + locaApp.getVersionCode());// TODO: 2018/5/3  当前安装版本号，未安装不传
                        isInstall = true;
                        continue;
                    }
                }
                if (!isInstall) {
                    infoBean.setInstallType("none");
                    infoBean.setVersion("");
                }
                arrBean.add(infoBean);

            }
        }
        LogUtils.json("arrBean", JSON.toJSONString(arrBean));
        return arrBean;
    }

    public void sendMessage(String data) {
        MessageEvent event = new MessageEvent<String>();
        event.setCode(Const.SEND_CODE);
        event.setMsg("正在向服务器发送登录消息");
        event.setData(data);
        EventBus.getDefault().post(event);

    }


    private void initView() {
        screenHeight = DeviceUtil.getScreenHeight(this);
        downloadManager = DownloadService.getDownloadManager(this);
        packageManager = getPackageManager();
        mPagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mAppGridViews.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(mAppGridViews.get(position));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(mAppGridViews.get(position));
                return mAppGridViews.get(position);
            }
        };
        mViewPager.setAdapter(mPagerAdapter);// 与ListView用法相同，设置重写的Adapter。这样就实现了ViewPager的滑动效果。
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int selectedPageIndex) {
                mCurPageNumIndex = selectedPageIndex;
                mPageControl.generatePageControl(mCurPageNumIndex);
                Log.d("", "mCurPageNum=" + mCurPageNumIndex);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initData(List<AppInfo> apps) {
        if (apps != null) {
            resList.clear();
            resList.addAll(apps);
        }
        resCount = resList.size();
        pageNum = (int) Math.ceil(resCount / PAGE_COUNT) + 1;
        if (resCount % PAGE_COUNT == 0) {
            pageNum = (int) Math.ceil(resCount / PAGE_COUNT);
        }
        adapterList.clear();
        mAppGridViews.clear();
        AppGridViewAdapter adapter = null;
        for (int i = 1; i <= pageNum; i++) {
            GridView appGridView = new GridView(this);
            appGridView.setSelector(new ColorDrawable(Color.argb(0x00, 0xff, 0xff, 0xff)));
            adapter = new AppGridViewAdapter(i, resList);
            appGridView.setAdapter(adapter);
            appGridView.setNumColumns(COLUMN_NUM);
            appGridView.setOnItemClickListener(new ItemClickListener(i));
            final int fI = i;
            appGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                    AppBean appInfo = resList.get(position + (fI - 1) * PAGE_COUNT);
//                    uninstallApp(appInfo.packageName); 2017-10-11 没有这个功能 自己加的
                    return true;
                }
            });
            appGridView.setVerticalSpacing(screenHeight / 20);
            adapterList.add(adapter);
            mAppGridViews.add(appGridView);
        }
        mPageControl.bindViewGroup(mAppGridViews.size());
        mPagerAdapter.notifyDataSetChanged();
    }

    private void updateAppList() {
        lastRequestId = NetApi.getReuestId();
        NetApi.queryApp(lastRequestId);
        Log.i("info", "---------------------updateAppList: ");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptMsg(MessageEvent event) {
        switch (event.getCode()) {
            //通知消息
            case Const.NOTICE_APP_ADD:
            case Const.NOTICE_APP_UPDATE:
            case Const.NOTICE_APP_REMOVE:
                updateAppList();
                break;
            //请求数据返回
            case Const.METHER_METHER_QUERYAPP_CODE:
                ResponseModel responseData = event.getResponseData();
                if (responseData.isSuccess()) {
                    responseData.setContentClass(AcceptQueryAppBean.class);
                    AcceptQueryAppBean appBean = (AcceptQueryAppBean) responseData.getContentData();
                    //获取到集合
                    // TODO: 2018/5/3    List<AppBean> app=appBean.getApps();
                    //resList=appBean.getApps();
                    List<AppInfo> apps = AppInfoTools.appBean2Appinfo(appBean.getApps());
                    initData(apps);
                }
                break;
            case Const.METHER_METHER_QUERYAPPURL_CODE:
                ResponseModel responseData2 = event.getResponseData();
                for (AppItem appItem : appItems) {
                    if (responseData2.getRequestId().equals(appItem.requestId)) {
                        appItems.remove(appItem);
                        if (responseData2.isSuccess()) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject((String) responseData2.getContent());
                                downloadManager.addNewDownload(jsonObject.optString("queryAppDownloadUrl"), appItem.appInfo.packageName,
                                        appItem.appInfo.localFilePath, false, false,
                                        new DownloadRequestCallBack(appItem.appInfo, appItem.view));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case Const.METHER_METHER_QUERYNOTIC_CODE://获取通知接口
                AcceptQueryNoticBean acceptQueryNoticBean =
                        JSON.parseObject((String) event.getDataContent(), AcceptQueryNoticBean.class);
                // LogUtils.json("querynotic", JSON.toJSONString(acceptQueryNoticBean));

                List<AcceptQueryNoticBean.Notice> notices = acceptQueryNoticBean.getNotices();

                for (int i = 0; i < notices.size(); i++) {
                    MessageEvent sendMsg = new MessageEvent<String>();
                    sendMsg.setCode(-1);
                    String noticeType = notices.get(i).getNoticeType();
                    if ("strategy".equals(noticeType)) {
                        sendMsg.setCode(-1);
                        sendMsg.setMsg("策略");
                    } else if ("appAdd".equals(noticeType)) {//应用新增
                        sendMsg.setCode(Const.NOTICE_APP_ADD);
                        sendMsg.setMsg("应用新增");
                    } else if ("appRemove".equals(noticeType)) {//应用移除
                        sendMsg.setCode(Const.NOTICE_APP_REMOVE);
                        sendMsg.setMsg("应用移除");
                    } else if ("appUpdate".equals(noticeType)) {//应用更新
                        sendMsg.setCode(Const.NOTICE_APP_UPDATE);
                        sendMsg.setMsg("应用更新");
                    } else if ("companyDataErasure".equals(noticeType)) {//企业应用数据擦除
                        sendMsg.setCode(Const.CONTROL_COMPANYDATA_DCREAL);
                        sendMsg.setMsg("应用清除");
                    } else if ("allDataErasure".equals(noticeType)) {//全部数据擦除
                        sendMsg.setCode(Const.CONTROL_ALLDATA_DCREAL);
                        sendMsg.setMsg("设备清除");
                    } else if ("screenLock".equals(noticeType)) {//屏幕锁定
                        sendMsg.setCode(Const.CONTROL_SCREEN_LOCK);
                        sendMsg.setMsg("屏幕锁屏");
                    } else if ("screenUnLock".equals(noticeType)) {//屏幕解锁
                        sendMsg.setCode(Const.CONTROL_SCREEN_UNLOCK);
                        sendMsg.setMsg("屏幕解锁");
                    } else if ("equipmentPositioning".equals(noticeType)) {//设备定位
                        sendMsg.setCode(-1);
                        sendMsg.setMsg("设备定位");
                    } else if ("fileAdd".equals(noticeType)) {//文档新增
                        sendMsg.setCode(-1);
                        sendMsg.setMsg("文档新增");
                    } else if ("fileRemove".equals(noticeType)) {
                        sendMsg.setCode(-1);
                        sendMsg.setMsg("文档移除");
                    } else if ("fileUpdate".equals(noticeType)) {
                        sendMsg.setCode(-1);
                        sendMsg.setMsg("文档更新");
                    }
                    if (sendMsg.getCode() > 0) {
                        EventBus.getDefault().post(sendMsg);
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.MainActivityIsAlive = false;
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.i("resultCode=" + resultCode);
        switch (resultCode) {
            case Constants.ACTIVITY_RESULT_STARTINSTALLAPK:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        DialogBase.Builder customDialog = new DialogBase.Builder(this)
                .setShowTitle(false)
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setContent(DialogBase.createTextContent(this, "确定要返回桌面吗？"))
                .setCanCelable(false)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        customDialog.show();
    }

    private class AppGridViewAdapter extends BaseAdapter {
        private int mPageIndex;
        private List<AppInfo> resList;

        public AppGridViewAdapter(int pageIndex, List<AppInfo> resList) {
            mPageIndex = pageIndex;
            this.resList = resList;
        }

        @Override
        public int getCount() {
            if (mPageIndex < pageNum) {
                return PAGE_COUNT;
            } else {
                return resList.size() - (mPageIndex - 1) * PAGE_COUNT;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppInfo appInfo = resList.get(position + (mPageIndex - 1) * PAGE_COUNT);
            if (convertView == null) {
                convertView = LayoutInflater.from(MainNewActivity.this).inflate(R.layout.app_item, parent, false);
            }
            TextView tv = BaseViewHolder.get(convertView, R.id.tvAppName);
            ImageView iv = BaseViewHolder.get(convertView, R.id.ivAppImg);
            if (appInfo.type == AppInfoTools.NATIVE_APP) {
                iv.setImageResource(appInfo.appIcon);
            } else {
                ImageLoader.getInstance().displayImage(appInfo.iconUrl, iv);
            }
            tv.setText(appInfo.appName);
            if (appInfo.type == AppInfoTools.APK_APP) {
                setState(convertView, appInfo);
            }
            return convertView;
        }
    }

    public void setState(View convertView, AppInfo appInfo) {
        if (downloadManager.getDownloadInfo(appInfo.packageName) != null) {
            DownloadInfo info = downloadManager.getDownloadInfo(appInfo.packageName);
            RoundProgressBar progressBar = BaseViewHolder.get(convertView, R.id.rpb);
            if (info.getState() == HttpHandler.State.LOADING) {
                if (info.getHandler() != null) {
                    info.getHandler().setRequestCallBack(new DownloadRequestCallBack(appInfo, convertView));
                } else {
                    try {
                        downloadManager.removeDownload(info);
                        return;
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setProgress((int) (info.getProgress() * 100 / info.getFileLength()));
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void showWebAppLogin(final AppInfo appInfo) {
        if (!appInfo.ssoState) {
            Intent intent = new Intent();
            intent.putExtra("url", appInfo.web_url);
            intent.putExtra("appName", appInfo.appName);
            intent.setClass(MainNewActivity.this, WebAppActivity.class);
            startActivity(intent);
            return;
        }
        final SharedPreferences sharepre = context.getSharedPreferences(appInfo.appName, 0);
        if (sharepre.contains("username")) {
            Intent intent = new Intent();
            intent.putExtra("url", appInfo.web_url);
            intent.putExtra("appName", appInfo.appName);
            intent.putExtra("isSso", appInfo.ssoState);
            intent.putExtra("excuteJs", appInfo.excuteJs);
            intent.setClass(MainNewActivity.this, WebAppActivity.class);
            startActivity(intent);
        } else {
            LayoutInflater settingInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View loginView = settingInflater.inflate(R.layout.webapp_login_dialog, null);
            final Dialog webAppDialog = new Dialog(context, R.style.Theme_NoBackground_NoTitle);
            webAppDialog.show();
            TextView textView = (TextView) loginView.findViewById(R.id.title);
            textView.setText(appInfo.appName);
            final EditText username_et = (EditText) loginView.findViewById(R.id.webappusername_et);
            final EditText password_et = (EditText) loginView.findViewById(R.id.webapppassword_et);
            ImageView login_cancel = (ImageView) loginView.findViewById(R.id.webapp_link_back);
            login_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webAppDialog.dismiss();
                }
            });
            Button login_btn = (Button) loginView.findViewById(R.id.webappsave_btn);
            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String username_str = username_et.getText().toString().trim();
                    String password_str = password_et.getText().toString().trim();
                    if ("".equals(username_str) || "".equals(password_str)) {
                        Toast.makeText(context, "各项都不能为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        webAppDialog.dismiss();
                        sharepre.edit().putString("username", username_str).commit();
                        sharepre.edit().putString("password", password_str).commit();
                        Intent intent = new Intent();
                        intent.putExtra("url", appInfo.web_url);
                        intent.putExtra("appName", appInfo.appName);
                        intent.putExtra("isSso", appInfo.ssoState);
                        intent.putExtra("excuteJs", appInfo.excuteJs);
                        intent.setClass(MainNewActivity.this, WebAppActivity.class);
                        startActivity(intent);
                    }
                }
            });
            Button cancel_btn = (Button) loginView.findViewById(R.id.webappcancel_btn);
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webAppDialog.dismiss();
                }
            });
            webAppDialog.setContentView(loginView);
        }
    }

    public String encryptDES(String encryptString, String encryptKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return MyBase64.encode(encryptedData);
    }

    class ItemClickListener implements OnItemClickListener {
        private int mPageIndex = 0;

        /**
         * @Description:TODO
         */
        public ItemClickListener(int num) {
            this.mPageIndex = num;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            AppInfo appInfo = resList.get(position + (mPageIndex - 1) * PAGE_COUNT);
            currenInfo = appInfo;
            if (appInfo.type == AppInfoTools.APK_APP) {
                if (appInfo.packageName.isEmpty()) {
                    Toast.makeText(MainNewActivity.this, "包名为空请联系管理员", Toast.LENGTH_LONG).show();
                    return;
                }
                if (appInfo.version.isEmpty()) {
                    Toast.makeText(MainNewActivity.this, "版本未知请联系管理员", Toast.LENGTH_LONG).show();
                    return;
                }
                if (appInfo.needUpdate) {
                    if (!AppInfoTools.checkAppIsNeedUpdate(appInfo.packageName, appInfo.version)) {
                        appInfo.needUpdate = false;
                    }
                }
                if (appInfo.needUpdate) { // 未安装或需要更新
                    //正在下载中
                    if (downloadManager.getDownloadInfo(appInfo.packageName) != null) {
                        // 说明正在下载
                        if (downloadManager.getDownloadInfo(appInfo.packageName).getState() == HttpHandler.State.LOADING) {
//                            // 正在下载
                            Toast.makeText(MainNewActivity.this, appInfo.appName + "取消下载", Toast.LENGTH_SHORT).show();
                            try {
                                downloadManager.removeDownload(downloadManager.getDownloadInfo(appInfo.packageName));
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        } else {
                            addToDownload(appInfo, view);
                        }
                    } else if (isApkExist(appInfo.localFilePath)) {//已下载
                        if (ApkCheckUtils.isLegalApk(appInfo.localFilePath)) {//合法apk
                            installApp(appInfo.localFilePath);
                        } else {
                            addToDownload(appInfo, view);
                        }
                    } else {
                        addToDownload(appInfo, view);
                    }
                } else {
                    try {
                        openAppByPackageName(appInfo.packageName);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Toast.makeText(MainNewActivity.this, "配置出错，请联系管理员！", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (appInfo.type == AppInfoTools.VIRTUAL_APP) {
                // 虚拟应用
                readyToOpenVirturl = appInfo.virtualResourceName;
                if (vir_resList == null) {
                    loadingDialog = CustomProgressDialog.getInstance(MainNewActivity.this);
                    loadingDialog.setMessage("正在登录，请稍候...");
                    loadingDialog.show();
                    new Thread() {
                        public void run() {
                            Looper.prepare();
                            new com.ec.avirtualapp.controller.LoginActivity(MainNewActivity.this, handler).login(password);
                            Looper.loop();
                        }
                    }.start();
                    return;
                }
            } else if (appInfo.type == AppInfoTools.WEB_APP) {
                if (appInfo.ssoState) {
                    if (appInfo.needCount) {

                    } else {
                        showWebAppLogin(appInfo);
                    }
                } else {
                    if (appInfo.appName.equals("内网邮件")) {
                        goVpn();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("url", appInfo.web_url);
                        intent.putExtra("appName", appInfo.appName);
                        intent.setClass(MainNewActivity.this, WebAppNewActivity.class);
                        startActivity(intent);
                    }
                }
            } else if (appInfo.type == AppInfoTools.NATIVE_APP) {
                Intent intent = new Intent();
                intent.putExtra("appName", appInfo.appName);
                intent.setClass(MainNewActivity.this, appInfo.className);
                startActivity(intent);
            }
        }

    }

    public void openAppByPackageName(String packageName) throws NameNotFoundException {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//重点是加这个
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                startActivity(intent);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addToDownload(AppInfo appInfo, View view) {
        AppItem appItem = new AppItem();
        appItem.requestId = NetApi.getReuestId();
        appItem.appInfo = appInfo;
        appItem.view = view;
        appItems.add(appItem);
        NetApi.queryAppDownUrl(appItem.requestId, appItem.appInfo.appId);
    }

    class AppItem {
        public String requestId;
        public AppInfo appInfo;
        public View view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDEL_MSG_GO_SESSION_ACTIVITY:
//                    goSessionActivity((ManualBookmark) msg.obj);
                    break;
                case 6:
                    //失败报错
                    loadingDialog.dismiss();
                    showErrorDialog(msg.getData().getString("text"));
                    break;
                case 7:
                    loadingDialog.dismiss();
                    int port = L4ProxyArd.getInstance().L4ProxyServiceRun(FileUtil.getIpanPort(currenInfo.web_url));
                    String ipPort = "127.0.0.1:" + port;
                    String url = FileUtil.changeUrl(currenInfo.web_url, ipPort);
                    Intent intent = new Intent();
                    intent.putExtra("url", url);
                    intent.putExtra("appName", currenInfo.appName);
                    intent.setClass(MainNewActivity.this, WebAppActivity.class);
                    startActivity(intent);
                    break;
                case 123:
                    // 刷新
                    for (AppGridViewAdapter adapter : adapterList) {
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 1234:
                    // 刷新
                    initData(MyApplication.getInstance().getResList());
                    break;
            }
        }
    };

    /**
     * 描述：判断apk是否存在
     *
     * @param path
     * @return
     */
    private boolean isApkExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    private void showErrorDialog(String string) {
        Dialog dialog = new Builder(this).setTitle("提示").setMessage(
                string).setPositiveButton("确定",
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).create();
        dialog.show();
    }

    //安装应用程序
    private void installApp(String filePath) {
        File apkfile = new File(filePath);
       /* if (!apkfile.exists()) {
            return;
        }*/
        Log.i("info", "installApp: " + filePath);
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String authority = MyApplication.getInstance().getPackageName() + ".provider";
            Uri contentUri = FileProvider.getUriForFile(MyApplication.getInstance(), authority, apkfile);

            i.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            i.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android" +
                    ".package-archive");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
       /* // TODO: 2018/5/14   FileProvider.getUriForFile(MyApplication.getInstance(),MyApplication.getInstance().getPackageName(),)
        // 设置目标应用安装包路径
        i.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
        MyApplication.getInstance().startActivity(i);
    }



    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    public static void install(Context mContext, String apkPath) {
        File file = new File(Environment.getExternalStorageDirectory(), apkPath);
        Intent mIntent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName(), file);
            mContext.grantUriPermission("com.android.camera", apkUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else
            mIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(mIntent);
    }

    //卸载应用程序
    private void uninstallApp(String packageName) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_DELETE);
        i.setData(Uri.parse("package:" + packageName));
        startActivity(i);
    }

    private PackageInfo getPackageInfo(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            return null;
        }
        return packageInfo;
    }

    public class DownloadRequestCallBack extends RequestCallBack<File> {
        private AppInfo info;
        private String packageName, localFilePath;
        private RoundProgressBar progressbar;
        private final int percent = 100;

        public DownloadRequestCallBack(AppInfo info, View parentView) {
            this.info = info;
            this.packageName = info.packageName;
            this.localFilePath = info.localFilePath;
            this.progressbar = (RoundProgressBar) parentView.findViewById(R.id.rpb);
        }

        @Override
        public void onStart() {
            super.onStart();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
            progressbar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(HttpException arg0, String arg1) {
            Toast.makeText(context, "下载失败，请检查网络!", Toast.LENGTH_SHORT).show();
            try {
                downloadManager.removeDownload(downloadManager.getDownloadInfo(packageName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            super.onLoading(total, current, isUploading);
            if (total > 0) {
                progressbar.setProgress((int) (current * percent / total));
                downloadManager.getDownloadInfo(packageName).setProgress(current);
                Log.i("Main", "下载进度：" + (int) (current * percent / total));
            } else {
                progressbar.setProgress(10);
                downloadManager.getDownloadInfo(packageName).setProgress(current);
            }

        }

        @Override
        public void onSuccess(ResponseInfo<File> arg0) {
            progressbar.setVisibility(View.GONE);
            try {
                if (null != downloadManager.getDownloadInfo(packageName)) {
                    downloadManager.removeDownload(downloadManager.getDownloadInfo(packageName));
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            //info.needUpdate ==== false;
            installApp(localFilePath);
        }
    }


    /* (non Javadoc)
     * @Description: TODO
     * @param v
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {

    }


    public void goVpn() {
        loadingDialog = CustomProgressDialog.getInstance(MainNewActivity.this);
        loadingDialog.setMessage("正在登录，请稍候...");
        loadingDialog.show();
        new Thread() {
            public void run() {
                Looper.prepare();
                String vpn_ip = (String) SharedPreferencesUtils.getParam(Constants.DEFAULT_VPN_IP, Constants.vpnIp);
                String vpn_port = (String) SharedPreferencesUtils.getParam(Constants.DEFAULT_VPN_PORT, Constants.vpnPort);
                String user = (String) SharedPreferencesUtils.getParam(Constants.DEFAULT_VPN_USER, Constants.vpnUser);
                String pw = (String) SharedPreferencesUtils.getParam(Constants.DEFAULT_VPN_PW, Constants.vpnPw);
                vpnConnect(vpn_ip, Integer.parseInt(vpn_port), user, pw);
                Looper.loop();
            }
        }.start();
    }

    private Boolean vpnConnect(String strIp, int port, String user, String pw) {
        int iResult = 0;
        // 建立vpn连接
        iResult = L4ProxyArd.getInstance().L4ProxyLoginWithPass(strIp, port, user, pw);

        System.out.println("iResult1:" + iResult);

        if (iResult != 0) {
            String errnonum = String.valueOf(iResult);
            Message msg1 = new Message();
            msg1.what = 6;
            Bundle bundle = new Bundle();
            bundle.putString("text", "VPN连接失败");  //往Bundle中存放数据
            msg1.setData(bundle);//mes利用Bundle传递数据
            handler.sendMessage(msg1);//用activity中的handler发送消息
            return false;
        } else {
            handler.sendEmptyMessage(7);
            return true;
        }
    }

    /**
     * 登录账号
     */
    public static String getUserCode() {
        AppUser appUser = MyApplication.getInstance().getAppUser();
        if (appUser == null) {
            return "";
        } else {
            return appUser.getUserCode();
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            //  button.setText(JSON.toJSONString(location));
            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        }
    }
}
