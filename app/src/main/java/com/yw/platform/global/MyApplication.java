package com.yw.platform.global;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.ec.avirtualapp.controller.VpassSessionActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yw.platform.R;
import com.yw.platform.beans.ModelManger;
import com.yw.platform.beans.base.RequestModel;
import com.yw.platform.beans.recevice.PolicyBean;
import com.yw.platform.model.AppInfo;
import com.yw.platform.notice.ReceiveNotice;
import com.yw.platform.service.LocalHandleService;
import com.yw.platform.utils.LockPatternUtils;
import com.yw.platform.yhtext.netty.NettyService;

import net.ttxc.L4Proxy.L4ProxyArd;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyApplication extends Application {

    private static MyApplication mInstance=null;
    private List<AppInfo> resList;//应用列表
    private AppUser appUser;
    private PolicyBean policy;

    private RequestModel heartPack;//心跳包

    public static MyApplication getInstance() {
        return mInstance;

    }
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        mInstance = this;
        mLockPatternUtils = new LockPatternUtils(this);
        configImageLoader();
        //Utils.init(this);
        //开启ssl加密连接服务
        L4ProxyArd.getInstance().StartOpenSSL();
        init();
        // 程序创建的时候执行 by yh
        if (!isServiceRunning(this, NettyService.class.getName()))
            startService(getIntent());
    }
    private void init(){
        startService(new Intent(this, LocalHandleService.class));
        EventBus.getDefault().register(ReceiveNotice.getinReceiveNotice());
    }
    public void setResList(List<AppInfo> resList) {
        this.resList = resList;
    }
    public  List<AppInfo> getResList() {
        return this.resList;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
        this.heartPack=null;
    }

    public PolicyBean getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyBean policy) {
        this.policy = policy;
    }

    public RequestModel getHeartPack() {
        if (heartPack==null&&appUser!=null){
            heartPack=new RequestModel<Object>();
            heartPack.setMethod("heartbeat");
            heartPack.setSender(ModelManger.getDefaultSendUser("test"));
            heartPack.setRecipients(ModelManger.getCommRptUsers());
            HashMap<String,String> content=new HashMap<>();
            content.put("notification","REQUEST");//固定值
            content.put("userCode",appUser.getUserCode());
            content.put("deviceCode","223232");
            heartPack.setContent(content);
        }
        return heartPack;
    }
    public boolean getShowSafeUcWeb() {
        if(policy!=null&&"yes".equals(policy.getBrowser())){
            return true;
        }
        return false;
    }

    /**
     *
     *
     *
     *
     * 可能暂未使用（可能！！！！！！！！可能！！！！！！！）
     *
     */
    private LockPatternUtils mLockPatternUtils;
    private String passWord = "";
    private long lastLTime = -1;
    private String mCompany;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread #" + mCount.getAndIncrement());
        }
    };
    private static final BlockingQueue<Runnable> POOL_WORK_QUEUE =
            new LinkedBlockingQueue<Runnable>(128);

    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, POOL_WORK_QUEUE, THREAD_FACTORY);
    private String mAccount;

    public String getPassWord() {
        return passWord;
    }

    public void excuteNetwork(Runnable command){
        threadPool.execute(command);
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean getShowUcWeb() {
        return showUcWeb;
    }

    public void setShowUcWeb(boolean showUcWeb) {
        this.showUcWeb = showUcWeb;
    }

    public long getLastLTime() {
        return lastLTime;
    }

    public void setLastLTime(long lastLTime) {
        this.lastLTime = lastLTime;
    }

    private boolean showUcWeb;

    private List<Activity> activitiesContainer = new ArrayList<Activity>();


    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        this.mCompany = company;
    }

    private List<String> addressList = new ArrayList<String>();

    public List<String> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

    public static boolean MainActivityIsAlive;//消息推送时用来判断跳到哪一个页面





    @Override
    public void onLowMemory() {
        removeAllActivity();
        super.onLowMemory();
    }

    public LockPatternUtils getLockPatternUtils() {
        return mLockPatternUtils;
    }

    public void addActivity(Activity activity) {
        activitiesContainer.add(activity);
    }

    public void removeActivity(Activity activity) {
        activitiesContainer.remove(activity);
    }

    public void removeAllActivity() {
        for (Activity activity : activitiesContainer) {
            Log.i("MyApplication", "finish=" + activity.getClass().getName());
//            activity.finish();
        }
    }

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ett) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ett) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ett) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public String getAccount() {
        return mAccount;
    }

    public enum LAST_CONNCED_TYPE {
        LAST_CONNECT_NONE, LAST_CONNECT_APP, LAST_CONNECT_DESKTOP
    }

    public enum ServerType {
        serverTypeVpn, serverTypeVpass, serverTypeLocalMachine
    }

    public enum AuthType {
        authTypeUserAndCert, authTypeUser, authTypeCert;
    }

    public static LAST_CONNCED_TYPE lastConnectType = LAST_CONNCED_TYPE.LAST_CONNECT_NONE;
    public static String curServerTypeValue;
    private VpassSessionActivity vpass;

    public VpassSessionActivity getVpassActivity() {
        return vpass;
    }

    public void setVpassActivity(VpassSessionActivity context) {
        this.vpass = context;
    }
    /**
     * yh netty全局
     */
    public static final String TAG = "BaseApplication";

   // public static MyApplication mInstance=null;

   /* public static MyApplication getInstance() {
        return mInstance;
    }
*/







    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "onTerminate");
        super.onTerminate();
        stopService(getIntent());
    }

    /*@Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }*/

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @NonNull
    private Intent getIntent() {
        return new Intent(this,NettyService.class);
    }


    /**
     * 判断服务是否在运行中
     *
     * @param context     即为Context对象
     * @param serviceName 即为Service的全名
     * @return 是否在运行中
     */
    private boolean isServiceRunning(Context context, String serviceName) {
        if (!TextUtils.isEmpty(serviceName) && context != null) {
            ActivityManager activityManager
                    = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ArrayList<ActivityManager.RunningServiceInfo> runningServiceInfoList
                    = (ArrayList<ActivityManager.RunningServiceInfo>) activityManager.getRunningServices(100);
            for (Iterator<ActivityManager.RunningServiceInfo> iterator = runningServiceInfoList.iterator(); iterator.hasNext(); ) {
                ActivityManager.RunningServiceInfo runningServiceInfo = iterator.next();
                if (serviceName.equals(runningServiceInfo.service.getClassName().toString()))
                    return true;
            }
        }
        return false;
    }
}
