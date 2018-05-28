package com.yw.platform.yhtext.utils;

public class NetworkConverviewUtils {
public static String  network = "";
    public static  String getNetworkState(int status){

        switch (status){
            case 0:
                network= "无连接";
                break;
            case 1:
                network= "wifi";
                break;
            case 2:
                network= "2G";
                break;
            case 3:
                network= "3G";
                break;
            case 4:
                network= "4G";
                break;
                default:
                    network="无连接";
                    break;


        }
        return  network;
    }
}
