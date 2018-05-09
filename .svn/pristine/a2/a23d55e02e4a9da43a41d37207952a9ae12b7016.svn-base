package com.yw.platform.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by cxb on 2018/4/9.
 */

public class NetworkSniffTask1 extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "nstask";

    private WeakReference<Context> mContextRef;

    public NetworkSniffTask1(Context context) {
        mContextRef = new WeakReference<Context>(context);
    }
    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "Let's sniff the network");

        try {
            Context context = mContextRef.get();

            if (context != null) {

                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                WifiInfo connectionInfo = wm.getConnectionInfo();
                int ipAddress = connectionInfo.getIpAddress();
                String ipString = Formatter.formatIpAddress(ipAddress);


                Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                Log.d(TAG, "ipString: " + String.valueOf(ipString));

                String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                Log.d(TAG, "prefix: " + prefix);

                long stime=System.currentTimeMillis();
                Log.e("time",stime+"");
                int icount=0;
                for (int i = 1; i < 255; i++) {
                    String testIp = prefix + String.valueOf(i);
                    InetAddress address = InetAddress.getByName(testIp);
                    boolean reachable = address.isReachable(50);
                    String hostName = address.getCanonicalHostName();
                    if (reachable){
                        Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                        icount++;
                    }else{

                    }
                }
                Log.e("icount",icount+"");
                Log.e("time",(System.currentTimeMillis()-stime)+"");
            }
        } catch (Throwable t) {
            Log.e(TAG, "Well that's not good.", t);
        }
        return null;
    }





     static class RunnableTask implements Runnable{
        private int start;
        private int end;
        private String sIpheard;
        private CheckBack back;
        private int icount=0;

        public RunnableTask( String sIpheard,int start, int end) {
            this.start = start;
            this.end = end;
            this.sIpheard = sIpheard;
        }
        public void setBack(CheckBack back){
            this.back = back;
        }
        @Override
        public void run() {
            for (int i = 1; i < 255; i++) {
                try {
                    String testIp = sIpheard + String.valueOf(i);
                    InetAddress address  = InetAddress.getByName(testIp);
                    boolean reachable = address.isReachable(50);
                    String hostName = address.getCanonicalHostName();
                    if (reachable){
                        Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                        icount++;
                    }else{

                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    interface CheckBack{
        void onReachable(int icount);
    }



    interface handeIp{
        String getTestIp();
        void onTestBack();
    }











}
