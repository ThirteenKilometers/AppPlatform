/**  
 * GalleryAdapter.java
 * @version 1.0
 * @author Haven
 * @createTime 2011-12-9 下午05:04:34
 */
package com.yw.platform.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import com.yw.platform.view.MyImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

public class GalleryAdapter extends BaseAdapter {

	private Context context;
	private Map<Integer, Bitmap> bitmapCache;// 用于图片缓存
	private int currnetPos = 0;

	private String strPicUrl;

	public GalleryAdapter(Context context, String strPicUrl) {
		this.context = context;
		bitmapCache = new HashMap<Integer, Bitmap>();
		this.strPicUrl = strPicUrl;
	}

	@Override
	public int getCount() {
		return 1;
		// return strPicUrl.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public int getCurrentPos() {
		return currnetPos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("gallery", "getview");
		currnetPos = position;
		// Bitmap bmp =
		// BitmapFactory.decodeResource(context.getResources(),R.drawable.logo);
		Bitmap bmp = getLoacalBitmap(strPicUrl);
		MyImageView view = new MyImageView(context);
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		view.setImageBitmap(bmp);
		// if (bitmapCache.containsKey(position)
		// && bitmapCache.get(position) != null) {
		// view.setImageBitmap(bitmapCache.get(position));
		// }
		// if (!bitmapCache.containsKey(position) && (position) <
		// strPicUrl.length) {
		// Log.i("gallery", "加载当前图片");
		// bitmapCache.put(position, null);
		// new MyAsyncTask(position).execute(1);
		// }
		// // 预加载3张图片
		// if (!bitmapCache.containsKey(position + 1)
		// && (position + 1) < strPicUrl.length) {
		// Log.i("gallery", "预加载第" + (position + 1) + "张");
		// bitmapCache.put(position + 1, null);
		// new MyAsyncTask(position + 1).execute(1);
		// }
		// if (!bitmapCache.containsKey(position + 2)
		// && (position + 2) < strPicUrl.length) {
		// Log.i("gallery", "预加载第" + (position + 2) + "张");
		// bitmapCache.put(position + 2, null);
		// new MyAsyncTask(position + 2).execute(1);
		// }
		// if (!bitmapCache.containsKey(position + 3)
		// && (position + 3) < strPicUrl.length) {
		// Log.i("gallery", "预加载第" + (position + 3) + "张");
		// bitmapCache.put(position + 3, null);
		// new MyAsyncTask(position + 3).execute(1);
		// }

		return view;
	}

	/*
	 * class MyAsyncTask extends AsyncTask<Integer, Bitmap, Object> { private
	 * int index; private Bitmap bm;
	 * 
	 * public MyAsyncTask(int index) { this.index = index; }
	 * 
	 * protected void onPreExecute() {
	 * 
	 * }
	 * 
	 * // ...另启线程 protected Object doInBackground(Integer... params) { //
	 * 可以一次传多个参数，构成一个数组 // 异步读取图片 try { URL url = new URL(strPicUrl[index]); //
	 * 得到流 URLConnection openConnection = url.openConnection(); List<Cookie>
	 * cookies=HttpClient.getInstance().getAllCookies(); String cookieString =
	 * cookies.get(0).getName() + "=" + cookies.get(0).getValue();
	 * openConnection.setRequestProperty("Cookie", cookieString); InputStream is
	 * = openConnection.getInputStream(); // 读取Bitmap类型的文件 bm =
	 * BitmapFactory.decodeStream(is); } catch (Exception e) {
	 * e.printStackTrace(); } return null; }
	 * 
	 * // 在doInBackground(Object... params)方法中 调用publishProgress(values) //
	 * 后系统会自动调用此方法 protected void onProgressUpdate(Bitmap... values) {
	 * 
	 * }
	 * 
	 * protected void onPostExecute(Object result) { if (bm != null) { if (index
	 * == currnetPos) { GalleryAdapter.this.notifyDataSetChanged(); } if
	 * ((bitmapCache.containsKey(index) && bitmapCache.get(index) == null) ||
	 * !bitmapCache.containsKey(index)) { bitmapCache.put(index, bm); } } }
	 * 
	 * }
	 */
	public Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
