package com.yw.platform.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;

import com.yw.platform.R;
import com.yw.platform.tools.SetInfo;

/**
 * 描述：应用锁输入界面
 */
public class AppLockerActivity extends Activity {
    private ImageView ivAppicon;
    private EditText etPsd;
    private String mAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applocker);
        ivAppicon = (ImageView) findViewById(R.id.ic_appicon);
        etPsd= (EditText) findViewById(R.id.et_input);
        mAppName = getIntent().getStringExtra("APP_NAME");
        if (mAppName != null) {
            try {
                Drawable icon = getPackageManager().getApplicationIcon(mAppName);
                ivAppicon.setImageDrawable(icon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        etPsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input=s.toString();
                if(input.length()>10){
                    etPsd.setText("");
                }else  if(input.length()>=4&&input.equals("123456")){
                    SetInfo.getInstance().unlockApp(mAppName);
                    //AppLockerService.startService(AppLockerActivity.this);
                    finish();
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(Intent.ACTION_MAIN,null);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                this.finish();
                return true;
            case KeyEvent.KEYCODE_MENU:
                finish();
                break;
            case KeyEvent.KEYCODE_HOME:
                // 收不到
                break;
            case KeyEvent.KEYCODE_APP_SWITCH:
                // 收不到
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
