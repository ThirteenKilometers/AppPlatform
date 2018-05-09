package com.yw.platform.utils;


import org.json.JSONException;
import org.json.JSONObject;

import com.yw.platform.global.Constants;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
public class LogSendUtils {
	
	private Context context;
	private static LogSendUtils logSendUtils;
	private String username;

	private LogSendUtils(Context context){
		this.context=context;
	}
	
	public static LogSendUtils getInstance(Context context) {
		if (logSendUtils == null) {
			logSendUtils = new LogSendUtils(context);
		}
		return logSendUtils;
	}

	public String sendLog(String name,String username,String Apptype,String version,String operationType,String ip,String port){
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);  
//		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);     
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 
        String longitude_str="";
        String latitude_str="";
        if("".equals(username)){
            username = this.username;
        }else{
            this.username=username;
        }
        if(location != null){     
            double latitude = location.getLatitude(); //经度     
            double longitude = location.getLongitude(); //纬度  
            longitude_str=String.valueOf(longitude);
            latitude_str=String.valueOf(latitude);
        }
		
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("logonName",username);
//			jsonObj.put("certDn", Certification.getInstance(context).getDN());
			jsonObj.put("ip", "");
			jsonObj.put("longitude",longitude_str);
			jsonObj.put("latitude", latitude_str);
			jsonObj.put("operationObject",name);
			jsonObj.put("operationObjectType",Apptype);
			jsonObj.put("operationObjectVersion",version);
			jsonObj.put("operationType",operationType);
			jsonObj.put("deviceudid",DeviceUtil.getAndroidId(context));
			jsonObj.put("deviceType","ANDROIDPHONE");
			jsonObj.put("deviceModel",Build.MODEL);
			jsonObj.put("deviceOsType","ANDROID");
			jsonObj.put("deviceOsVersion",Build.VERSION.RELEASE);
			jsonObj.put("orgCode",Constants.organization);
			jsonObj.put("description", "");
			jsonObj.put("result", "1");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return HttpClient.getInstance().executePost_logSend(jsonObj, ip, port);
	}
	
	
}
