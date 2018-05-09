package com.yw.platform.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by cxb on 2018/4/10.
 */

public class TaskIp  {
    private Context context;
    private String prefix;
    private int icount=-1;
    private List<String> ips;
    private List<String> arpIps;
    public TaskIp(Context context) {
        this.context = context;
        init();
    }
    private void init(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wm.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        String ipString = Formatter.formatIpAddress(ipAddress);
        prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
        ips=new ArrayList<>();
    }
    private void readArp() {
        arpIps=new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line = "";
            String ip = "";
            String flag = "";
            String mac = "";
            while ((line = br.readLine()) != null) {
                try {
                    line = line.trim();
                    if (line.length() < 63) continue;
                    if (line.toUpperCase(Locale.US).contains("IP")) continue;
                    ip = line.substring(0, 17).trim();
                    flag = line.substring(29, 32).trim();
                    mac = line.substring(41, 63).trim();
                    if (mac.contains("00:00:00:00:00:00")) continue;
                    Log.e("scanner", "readArp: mac= "+mac+" ; ip= "+ip+" ;flag= "+flag);
                    arpIps.add(ip);
                } catch (Exception e) {

                }
            }
            br.close();
        } catch(Exception e) {
        }
    }
    public  void startTest(){
        readArp();
        for (int i = 0; i <10 ; i++) {
            new Thread(new RunnableTask(this)).start();
        }
    }

    public synchronized String getTestIp() {
        icount++;
        if(icount<arpIps.size()){
            //String testIp = prefix + String.valueOf(icount);

            return arpIps.get(icount);
        }else{
            return null;
        }

    }
    public  void onReachable(String ip) {
        ips.add(ip);
    }
    private int finshCount=0;
    public synchronized void onFinsh() {
        finshCount++;
        if (finshCount==10){
            Log.e("ips",""+ips.size());
        }
    }

    static class RunnableTask implements Runnable{
        private TaskIp handeIp;

        public RunnableTask(TaskIp handeIp) {
            this.handeIp = handeIp;
        }
        @Override
        public void run() {
            String testIp=handeIp.getTestIp();
            while(testIp!=null){
                try {
                    InetAddress address  = InetAddress.getByName(testIp);
                    boolean  reachable = address.isReachable(3000);
                    String hostName = address.getCanonicalHostName();
                    if (reachable) {
                        handeIp.onReachable(testIp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                testIp=handeIp.getTestIp();
            }
            handeIp.onFinsh();
        }
    }












}
