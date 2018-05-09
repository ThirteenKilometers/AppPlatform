package com.yw.platform.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.yw.platform.R;

public class CustomProgressDialog extends AlertDialog {

	public CustomProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	private String TAG = "CustomProgressDialog";
	private String message;
	private Context context;
	private static CustomProgressDialog pd;
	TextView tv_WaitingMessage;

	public static CustomProgressDialog getInstance(Context con) {
		if (pd == null) {
			pd = new CustomProgressDialog(con);
			Window win = pd.getWindow();
			LayoutParams params = new LayoutParams();
			params.gravity = Gravity.CENTER;
			win.setAttributes(params);
		}
		return pd;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.dialog_progress);
		setContentView(R.layout.dialog_waiting);
		tv_WaitingMessage = (TextView) findViewById(R.id.msg);
		tv_WaitingMessage.setText(message);
	}

	public void setMessage(String message) {
		this.message = message;
		if(tv_WaitingMessage!=null){
			tv_WaitingMessage.setText(message);
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		pd = null;
	}
}
