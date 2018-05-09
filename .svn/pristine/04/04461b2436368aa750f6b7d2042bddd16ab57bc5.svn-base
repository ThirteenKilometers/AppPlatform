package com.yw.platform.activity;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.artifex.mupdf.MuPDFActivity;
import com.yw.platform.R;
import com.yw.platform.adapter.ZipAdapter;
import com.yw.platform.global.Constants;
import com.yw.platform.global.MyApplication;
import com.yw.platform.model.ZipContentItem;
import com.yw.platform.utils.DeviceUtil;
import com.yw.platform.utils.FileUtil;
import com.yw.platform.utils.HttpClient;
import com.yw.platform.view.CustomProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowZipActivity extends BaseActivity {
	private String TAG = "ShowZipActivity";
	private ArrayList<ZipContentItem> list;
	private String getAttachMessage;
	private String ShowingName;// 正在显示的文件的名字
	private CustomProgressDialog loadingDialog;

	private TextView mFilenameView;
	private ImageButton mBackButton;
	private String zipName;
	private String ShowingType;// 正在显示的文件的类型，如果不支持就不在连接网络，直接报错；

	private String m_ip, m_port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(savedInstanceState!=null){
			Intent it = new Intent(ShowZipActivity.this, MainActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(it);
			return;
		}
		setContentView(R.layout.showzip);

		mBackButton = (ImageButton) findViewById(R.id.backButton);
		mFilenameView = (TextView) findViewById(R.id.docNameText);
		mBackButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.activity_right_in,
						R.anim.activity_left_out);
			}
		});

//		if (((MyApp) getApplication()).getVpnType() == 0) {
//			m_ip = ((MyApp) getApplication()).getIpass_M_ip();
//			m_port = ((MyApp) getApplication()).getiMidUI_port();
//		} else {
//			m_ip = ((MyApp) getApplication()).getM_IP();
//			m_port = ((MyApp) getApplication()).getM_Port();
//		}
		
		m_ip=Constants.m_ip;
		m_port=Constants.mid_port;

		String json = getIntent().getExtras().getString("list");
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray fileList = jsonObject.getJSONArray("fileList");
			zipName = jsonObject.getString("fileName");
			list = new ArrayList<ZipContentItem>();
			ZipContentItem con;
			for (int i = 0; i < fileList.length(); i++) {
				JSONObject listObject = (JSONObject) fileList.opt(i);
				con = new ZipContentItem();
				con.type = listObject.getString("fileType");
				con.name = listObject.getString("fileName");
				con.size = listObject.getString("fileSize");
				con.url = listObject.getString("fileUrl");
				list.add(con);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mFilenameView.setText(zipName);
		ListView zipLv = (ListView) findViewById(R.id.ziplist);
		zipLv.setAdapter(new ZipAdapter(this, list));
		zipLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				showLoadingDialog();
//				if (((MyApplication) getApplication()).getVpnType() == 0) {
//					list.get(position).url = FileUtil.changeUrl(list
//							.get(position).url, m_ip + ":" + m_port);
//				}
				getAttachMessage = list.get(position).url
						+ "&client=android:phone:"
						+ DeviceUtil.getAndroidId(ShowZipActivity.this) + ":"
						+ DeviceUtil.getScreenPixel(ShowZipActivity.this);
				Log.i(TAG, "getAttachMessage=" + getAttachMessage);
				ShowingName = list.get(position).name;
				ShowingType = list.get(position).type;
				new GetJsonThread().start();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.activity_right_in,
				R.anim.activity_left_out);
		super.onBackPressed();
	}

	Handler hideDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (loadingDialog != null) {
					loadingDialog.hide();
				}
				break;
			}
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 获取数据错误
			case 0:
				if (loadingDialog != null) {
					loadingDialog.hide();
				}
				// callbackcon.error("数据获取错误！");
				String code = msg.getData().getString("code");
				String note = msg.getData().getString("note");
				showDialog(note + "\n错误码：" + code);
				break;
			// 数据为压缩包
			case 1:
				String progress = msg.arg1 + "%";
				loadingDialog.setMessage("已经完成" + progress);
				break;
			case 4:
				if (loadingDialog != null) {
					loadingDialog.hide();
				}
				break;
			case 3:
				hideDialogHandler.sendEmptyMessageDelayed(0, 2000);
				if (msg.getData().getString("type").equalsIgnoreCase("pdf")) {

					Log.i(TAG, msg.getData().getString("type"));
					Uri uri = Uri.parse(msg.getData().getString("fileUrl"));
					Intent intent = new Intent(ShowZipActivity.this,
							MuPDFActivity.class);
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(uri);
					intent.putExtra("name", ShowingName);
					startActivity(intent);
					overridePendingTransition(R.anim.activity_left_in,
							R.anim.activity_right_out);
				}  else if (msg.getData().getString("type").equalsIgnoreCase(
						"jpg")
						|| msg.getData().getString("type").equalsIgnoreCase(
								"png")) {
					Intent pic_intent = new Intent();
					pic_intent.putExtra("url", msg.getData().getString(
							"fileUrl"));
					pic_intent.putExtra("name", ShowingName);
					pic_intent.setClass(ShowZipActivity.this,
							ShowPicActivity.class);
					startActivity(pic_intent);
					overridePendingTransition(R.anim.activity_left_in,
							R.anim.activity_right_out);
				} else {
					Log.i(TAG, msg.getData().getString("type"));
					Intent intent = new Intent(ShowZipActivity.this,
							NewShowTxtActivity.class);
					intent.putExtra("name", ShowingName);
					// Intent intent = new
					// Intent(ShowZipActivity.this,ShowTxtActivity.class);
					intent.putExtra("url", msg.getData().getString("fileUrl"));
					startActivity(intent);
					overridePendingTransition(R.anim.activity_left_in,
							R.anim.activity_right_out);
				}
				break;
			case 2:
				// 下载失败
				showDialog("文件下载失败！");
				break;
			case 501:
				if (loadingDialog != null) {
					loadingDialog.hide();
				}
				showDialog("网络请求失败，请重试！\n" + "错误码：109000501");
				break;
			}
		}
	};

	class GetJsonThread extends Thread {
		public void run() {
			if (ShowingType == null || ShowingType.equalsIgnoreCase("")) {
				Message msg = new Message();
				Bundle bundle = new Bundle();
				msg.what = 0;
				bundle.putString("code", "109000513");
				bundle.putString("note", "不支持的文件格式");
				msg.setData(bundle);
				handler.sendMessage(msg);
				return;
			} else if (!ShowingType.equalsIgnoreCase("zip")
					&& !ShowingType.equalsIgnoreCase("rar")
					&& !ShowingType.equalsIgnoreCase("cebx")
					&& !ShowingType.equalsIgnoreCase("ceb")
					&& !ShowingType.equalsIgnoreCase("txt")
					&& !ShowingType.equalsIgnoreCase("log")
					&& !FileUtil.isImageType(ShowingType)
					&& !FileUtil.isPDFType(ShowingType)) {
				Message msg = new Message();
				Bundle bundle = new Bundle();
				msg.what = 0;
				bundle.putString("code", "109000513");
				bundle.putString("note", "不支持的文件格式");
				msg.setData(bundle);
				handler.sendMessage(msg);
				return;
			}
			
			String urls = "http://" + m_ip + ":" + m_port
					+ "/mw/do.proxy?client=android:phone:"
					+ DeviceUtil.getAndroidId(ShowZipActivity.this) + ":"
					+ DeviceUtil.getScreenPixel(ShowZipActivity.this) + "&type=session&app=OA";
			String results = HttpClient.getInstance().executeGet(urls);
			Log.i(TAG, "returnJsons=" + results);
			
			// String returnJSON = FileUtil.getStringJson(getAttachMessage);
			String returnJSON = HttpClient.getInstance().executeGet(
					getAttachMessage);
			Log.i(TAG, "得到的json字符串为=" + returnJSON);
			if (returnJSON != null && !"".endsWith(returnJSON)) {
				// 处理json数据
				// workedWithJson(returnJSON);
				FileUtil.attach_workedWithJson(returnJSON, getAttachMessage,
						handler, ShowZipActivity.this, ShowingName);
			} else {
				Message msg = new Message();
				Bundle bundle = new Bundle();
				msg.what = 0;
				bundle.putString("code", "109000501");
				bundle.putString("note", "网络请求失败，请重试。");
				msg.setData(bundle);
				handler.sendMessage(msg);
			}

		}

	}

	// 弹出框
	private void showDialog(String text) {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage(text).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).setCancelable(false).create();
		dialog.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		loadingDialog=null;
		super.onDestroy();
	}
	
	// 弹出进度框
	public void showLoadingDialog() {
		// TODO Auto-generated method stub
		loadingDialog = new CustomProgressDialog(this);
		loadingDialog.setMessage("正在获取数据，请稍候...");
		loadingDialog.show();
	}
}