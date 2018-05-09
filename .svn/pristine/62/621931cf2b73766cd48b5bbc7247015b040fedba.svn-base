package com.yw.platform.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yw.platform.R;
import com.yw.platform.global.MyApplication;
import com.yw.platform.utils.HttpClient;
import com.yw.platform.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.yw.platform.utils.StringUtils.phoneNumberMatch;

/**
 * RegisterActivity
 *
 * @author joker
 * @date 2017/10/30
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends Activity {
    @ViewInject(R.id.text_company)
    private TextView textCompany;
    @ViewInject(R.id.edit_name)
    private EditText editName;
    @ViewInject(R.id.edit_phone)
    private EditText editPhone;
    @ViewInject(R.id.edit_auth_code)
    private EditText editAuthCode;
    @ViewInject(R.id.text_auth_code)
    private TextView textAuthCode;
    private final int DELAY_COUNT = 120;
    private final long HANDLER_DELAY_MILLIS = 1000;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int count = msg.what - 1;
            if (count > 0) {
                textAuthCode.setText(count + "s 后重新获取");
                mHandler.sendEmptyMessageDelayed(count, HANDLER_DELAY_MILLIS);
            } else if (count == 0) {
                textAuthCode.setClickable(true);
                textAuthCode.setText("获取验证码");
                textAuthCode.setTextColor(Color.parseColor("#fb636f"));
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        //Common Thread Pool
        String company = MyApplication.getInstance().getCompany();
        if (TextUtils.isEmpty(company)) {
            finish();
        }
        textCompany.setText(company);
    }

    @OnClick(R.id.image_back)
    private void onBack(View v) {
        onBackPressed();
    }

    @OnClick(R.id.text_auth_code)
    private void getAuthCode(View v) {
        final String phone = editPhone.getText().toString().trim();
        if (StringUtils.isEmpty2Toast(phone, R.string.phone_empty_tips) || !phoneNumberMatch(phone)) {
            return;
        }
        textAuthCode.setClickable(false);
        textAuthCode.setTextColor(Color.parseColor("#bfbfbf"));
        MyApplication.getInstance().excuteNetwork(new Runnable() {
            @Override
            public void run() {
                final String res = HttpClient.getInstance().getAuthCode(phone);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res != null) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(res);
                                if (!jsonObject.optBoolean("state")) {
                                    Toast.makeText(RegisterActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                mHandler.sendEmptyMessageDelayed(DELAY_COUNT, HANDLER_DELAY_MILLIS);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                textAuthCode.setClickable(true);
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "验证码获取失败！", Toast.LENGTH_SHORT).show();
                            textAuthCode.setClickable(true);
                            textAuthCode.setTextColor(Color.parseColor("#fb636f"));
                        }
                    }
                });

            }
        });
    }

    @OnClick(R.id.button_register)
    private void register(View v) {
        final String company = textCompany.getText().toString().trim();
        final String name = editName.getText().toString().trim();
        final String phone = editPhone.getText().toString().trim();
        final String authCode = editAuthCode.getText().toString().trim();
        //预留字段
        final String password = "yw123456";
        if (StringUtils.isEmpty2Toast(company, R.string.company_empty_tips) ||
                StringUtils.isEmpty2Toast(name, R.string.name_empty_tips) ||
                StringUtils.isEmpty2Toast(phone, R.string.phone_empty_tips) ||
                StringUtils.isEmpty2Toast(authCode, R.string.auth_code_empty_tips) ||
                StringUtils.isEmpty2Toast(password, R.string.password_empty_tips) ||
                !phoneNumberMatch(phone)) {
            return;
        }
        MyApplication.getInstance().excuteNetwork(new Runnable() {
            @Override
            public void run() {
                final String res = HttpClient.getInstance().registerAccount(company, phone, password, name, authCode);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res != null) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(res);
                                if (!jsonObject.optBoolean("state")) {
                                    Toast.makeText(RegisterActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                JSONObject content = jsonObject.getJSONObject("content");
                                if (content.optInt("code") == 4) {
                                    //注册成功
                                    //云平台3个产品授权
                                    JSONArray authDesc = content.optJSONArray("authDesc");
                                    if (authDesc != null) {
                                        for (int i = 0; i < authDesc.length(); i++) {
                                            JSONObject auth = authDesc.getJSONObject(i);
                                            int status = auth.optInt("status");
//                                            int productId = auth.optInt("productId");
//                                            String productName = auth.optString("productName");
//                                            String authResult = auth.optString("authResult");
//                                            if (status == 0) {
                                            //授权失败
//                                           Toast.makeText(RegisterActivity.this, auth.optString("authResult"), Toast.LENGTH_SHORT).show();
//                                            }
                                            MyApplication.getInstance().setAccount(phone);
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    }
                                    //多桌面等产品授权
                                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }
}
