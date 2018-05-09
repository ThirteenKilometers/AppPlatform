package com.yw.platform.tools;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 设备信息
 * Created by cxb on 2018/3/29.
 */
public class DeviceInfo {
    // / 没有连接
    public static final int NETWORN_NONE = 0;
    // / wifi连接
    public static final int NETWORN_WIFI = 1;
    // / 手机网络数据连接
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;

    /**
     * 描述：获得可用的内存
     *
     * @param mContext
     * @return
     */
    public static long getmem_UNUSED(Context mContext) {
        long MEM_UNUSED;
        // 得到ActivityManager
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 创建ActivityManager.MemoryInfo对象
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // 取得剩余的内存空间
        MEM_UNUSED = mi.availMem / 1024;
        return MEM_UNUSED;
    }

    /**
     * 描述：获得总内存
     *
     * @return
     */
    public static long getmem_TOLAL() {
        long mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息
        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content);
        return mTotal;
    }

    /**
     * 描述：获取cup使用率
     * @return 使用率
     */
    public static double  getCpuUsage(){
        long[] last=getTotalCpuTime();
        long[] now=getTotalCpuTime();
        long lusage=((now[0]-now[1])-(last[0]-last[1]))*1000/(now[0]-last[0]);
        return lusage/1000.0;
    }
    /**
     * 描述：获取系统总CPU使用时间
     * @return cup使用情况
     */
    public static long [] getTotalCpuTime() { //
        long [] rea=new long[2];
        int user, nice, sys, idle, iowait, irq, softirq;
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new FileReader("/proc/stat"));
            String strTemp = null;
            strTemp = bReader.readLine();
            String[] listStrings = strTemp.split(" ");
            user = Integer.parseInt(listStrings[2]);
            nice = Integer.parseInt(listStrings[3]);
            sys = Integer.parseInt(listStrings[4]);
            idle = Integer.parseInt(listStrings[5]);// 空闲时间
            iowait = Integer.parseInt(listStrings[6]);
            irq = Integer.parseInt(listStrings[7]);
            softirq = Integer.parseInt(listStrings[8]);
            long all1 = 0l+user + nice + sys + idle + iowait + irq + softirq;
            long idle1 = idle;
            rea[0]=all1;
            rea[1]=idle1;
            bReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
        return rea;
    }
    /**
     * 获取ＧＰＳ当前状态
     * @param context
     * @return true-可用 false-不可用
     */
    public static boolean getGPSState(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean on = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return on;
    }
    /**
     * 获取Wifi状态
     * @param context
     * @return true-可用 false-不可用
     */
    private int getWifiState(Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null){
            int wifiState = wifiManager.getWifiState();
            return wifiState;
        }
        return WifiManager.WIFI_STATE_DISABLED;
//        wifiManager.WIFI_STATE_DISABLED (1)
//        wifiManager..WIFI_STATE_ENABLED (3)
//        wifiManager..WIFI_STATE_DISABLING (0)
//        wifiManager..WIFI_STATE_ENABLING  (2)
    }
    /**
     *
     * 返回当前网络连接类型
     *
     * @param context  上下文
     *
     * @return
     *
     */
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connManager) {
            return NETWORN_NONE;
        }
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORN_NONE;
        }
        // Wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORN_WIFI;
                }
            }
        }
        // 网络
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return  getMobileType(activeNetInfo.getSubtype(),networkInfo.getSubtypeName());
                }
            }
        }
        return NETWORN_NONE;
    }
    public static int getMobileType(int subType,String subTypeName){
        switch (subType) {
            case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
            case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
            case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN://api<8 : replace by 11
                return NETWORN_2G;
            case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
            case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
            case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                return NETWORN_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:  //api<11 : replace by 13
                return NETWORN_4G;
            default:// 有机型返
                // 中国移动 联通 电信 三种3G制式
                if (subTypeName.equalsIgnoreCase("TD-SCDMA") || subTypeName.equalsIgnoreCase("WCDMA")
                        || subTypeName.equalsIgnoreCase("CDMA2000")) {
                    return NETWORN_3G;
                } else {
                    return NETWORN_MOBILE;
                }
        }
    }

    /**
     * 描述：获取接入点信息
     * @param context
     */
    public void getApnInfo(Context context){
            String netStr=null;
            ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = con.getActiveNetworkInfo();
            if (info!=null){
                //获取网络接入点，这里一般为cmwap和cmnet
                netStr = info.getExtraInfo();
            }
    }

    /**
     * 描述：获取运营商
      * @param context
     * @return 0-未知 1-移动 2-联通 3-电信
     */
    public static int getMobileType(Context context) {
        TelephonyManager iPhoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String iNumeric = iPhoneManager.getSimOperator();
        if (iNumeric.length() > 0) {
            if (iNumeric.equals("46000") || iNumeric.equals("46002")) {// 中国移动
                return 1;
            } else if (iNumeric.equals("46001")) { // 中国联通
                return 2;
            } else if (iNumeric.equals("46003")) {// 中国电信
                return 3;
            }
        }
        return 0;
    }

    public final static String SHA1 = "SHA1";

    /**
     * 返回一个签名的对应类型的字符串
     *
     * @param context
     * @param packageName
     * @param type
     *
     * @return
     */
    public static String getSingInfo(Context context, String packageName, String type) {
        String tmp = null;
        Signature[] signs = getSignatures(context, packageName);
        for (Signature sig : signs) {
            if (SHA1.equals(type)) {
                tmp = getSignatureString(sig, SHA1);
                break;
            }
        }
        return tmp;
    }

    /**
     * 返回对应包的签名信息
     *
     * @param context
     * @param packageName
     *
     * @return
     */
    public static Signature[] getSignatures(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return packageInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取相应的类型的字符串（把签名的byte[]信息转换成16进制）
     *
     * @param sig
     * @param type
     *
     * @return
     */
    public static String getSignatureString(Signature sig, String type) {
        byte[] hexBytes = sig.toByteArray();
        String fingerprint = "error!";
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            if (digest != null) {
                byte[] digestBytes = digest.digest(hexBytes);
                StringBuilder sb = new StringBuilder();
                for (byte digestByte : digestBytes) {
                    sb.append((Integer.toHexString((digestByte & 0xFF) | 0x100)).substring(1, 3));
                }
                fingerprint = sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return fingerprint;
    }



    /**
     * 得到当前的手机蜂窝网络信号强度
     * 获取LTE网络和3G/2G网络的信号强度的方式有一点不同，
     * LTE网络强度是通过解析字符串获取的，
     * 3G/2G网络信号强度是通过API接口函数完成的。
     * asu 与 dbm 之间的换算关系是 dbm=-113 + 2*asu
     */
    public static void getCurrentNetDBM(Context context, SignalBack back) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneState phoneState=new PhoneState(context,tm,back);
        //开始监听
        tm.listen(phoneState, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    static class PhoneState extends PhoneStateListener{
        private Context context;
        private TelephonyManager tm;
        private SignalBack back;

        public PhoneState(Context context, TelephonyManager tm, SignalBack back) {
            this.context = context;
            this.tm = tm;
            this.back = back;
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            String signalInfo = signalStrength.toString();
            String[] params = signalInfo.split(" ");
            int type=getMobileType(context);
            if(tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE){//4G网络 最佳范围   >-90dBm 越大越好
                int itedbm = Integer.parseInt(params[9]);
                back.onChange(type,itedbm);
            }else if(tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                    tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                    tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                    tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS){
                //3G网络最佳范围  >-90dBm  越大越好  ps:中国移动3G获取不到  返回的无效dbm值是正数（85dbm）
                //在这个范围的已经确定是3G，但不同运营商的3G有不同的获取方法，故在此需做判断 判断运营商与网络类型的工具类在最下方
                if (type==1) {//中国移动3G不可获取，故在此返回0
                    back.onChange(type,0);
                }else if (type==2) {
                    back.onChange(type,signalStrength.getCdmaDbm());
                }else if (type==3) {
                    back.onChange(type,signalStrength.getEvdoDbm());
                }

            }else{
                //2G网络最佳范围>-90dBm 越大越好
                int asu = signalStrength.getGsmSignalStrength();
                int dbm = -113 + 2*asu;
                back.onChange(type,dbm);
            }
        }
    }
    public interface SignalBack{
        void onChange(int netType,int strength);
    }

}
