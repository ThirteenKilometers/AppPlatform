package com.yw.platform.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
/***
 * 
 * @author plf 
 *
 */
public class LocationGPS {

	public static boolean isOpen(Context context){
		LocationManager alm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		}else{
			return false;
		}
	}

	public static void openGpsSeting(Activity context, int request){
		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		context.startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
	}

	public static void openGPSSettings(Context context) {
		LocationManager alm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(context, "GPS模块正常 ", Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(context, "请开启GPS！", Toast.LENGTH_SHORT).show();

	}
	
	
	/*
	 *获取Gps位置
	 */
	public static void getLocation(Context context) {
		// 获取位置管理服务
		LocationManager locationManager;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 查找到服务信息
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
		criteria.setAltitudeRequired(false);//无海拔要求
		criteria.setBearingRequired(false);//无方位要求
		criteria.setCostAllowed(true);//允许产生资费
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
		String provider = locationManager.getBestProvider(criteria, true); // 获取最佳服务对象GPS信息

		Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
		
		updateToNewLocation(location);


		// 设置监听，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
		locationManager.requestLocationUpdates(provider, 100 * 1000, 500, new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}
			@Override
			public void onProviderEnabled(String provider) {

			}
			@Override
			public void onProviderDisabled(String provider) {

				
			}
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public static void updateToNewLocation(Location location) {
        TextView tv1=null;
        if (location != null) {
          double latitude = location.getLatitude();
          double longitude=location.getLongitude();
          tv1.setText( "维度：" + latitude+ "\n经度 "+longitude);
        } else {
          tv1.setText( "无法获取地理信息 ");
        }
      }

}
