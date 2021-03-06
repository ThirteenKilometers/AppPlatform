package com.yw.platform.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yw.platform.R;
import com.yw.platform.global.MyApplication;

/**
 * 密码验证activity
 * Created by cxb on 2017/7/10.
 */
@ContentView(R.layout.activity_validate)
public class ValidateActivity extends Activity {
    @ViewInject(R.id.pw_et)
    private EditText input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 3) {
                    String passwoid = "111111";//MyApplication.getInstance().getPassWord();
                    String inputpassWors = input.getText().toString();
                    if (inputpassWors == null || inputpassWors.equals("")) {
                        input.setError("密码错误");
                        return;
                    }
                    if (inputpassWors.equals(passwoid)) {
                        MyApplication.getInstance().setLastLTime(System.currentTimeMillis());
                        hideInputMethod(input);
                        finish();
                    } else {
                        input.setError("密码错误");
                    }
                }
            }
        });
    }
    /**
     * 隐藏输入法
     */
    public void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void onClear(View view){
        input.setText("");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}