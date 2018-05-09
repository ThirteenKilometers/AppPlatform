package com.yw.platform.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.yw.platform.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewShowTxtActivity extends BaseActivity {
	private TextView textView;
	private String bookPath;// 记录读入书的路径
	private View mButtonsView;
	private TextView mFilenameView;
	private ImageButton mBackButton;
	private String fileName;
	private boolean mButtonsVisible = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(savedInstanceState!=null){
			Intent it = new Intent(NewShowTxtActivity.this, MainActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(it);
			return;
		}
		setContentView(R.layout.new_showtxt);

		mButtonsView = findViewById(R.id.topBar);
		mBackButton = (ImageButton) findViewById(R.id.backButton);
		mFilenameView = (TextView) findViewById(R.id.docNameText);

		fileName = getIntent().getExtras().getString("name");
		mFilenameView.setText(fileName);

		bookPath = getIntent().getExtras().getString("url");
		String fileContent = getStringFromFile("GB2312");
		textView = (TextView) findViewById(R.id.textview);
		textView.setText(fileContent);

		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("NewShowTxtActivity", "点击。。。");
				if (mButtonsVisible) {
					hideButtons();
				} else {
					showButtons();
				}
			}
		});
		mBackButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.activity_right_in,
						R.anim.activity_left_out);
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

	public String getStringFromFile(String code) {
		try {
			StringBuffer sBuffer = new StringBuffer();
			FileInputStream fInputStream = new FileInputStream(bookPath);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fInputStream, code);
			BufferedReader in = new BufferedReader(inputStreamReader);
			if (!new File(bookPath).exists()) {
				return null;
			}
			while (in.ready()) {
				sBuffer.append(in.readLine() + "\n");
			}
			in.close();
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	void hideButtons() {
		mButtonsVisible = false;
		Animation anim = new TranslateAnimation(0, 0, 0, -mButtonsView
				.getHeight());
		anim.setDuration(200);
		anim.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				mButtonsView.setVisibility(View.INVISIBLE);
			}
		});
		mButtonsView.startAnimation(anim);
	}

	void showButtons() {
		mButtonsVisible = true;

		Animation anim = new TranslateAnimation(0, 0,
				-mButtonsView.getHeight(), 0);
		anim.setDuration(200);
		anim.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
				mButtonsView.setVisibility(View.VISIBLE);
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
			}
		});
		mButtonsView.startAnimation(anim);
	}
}
