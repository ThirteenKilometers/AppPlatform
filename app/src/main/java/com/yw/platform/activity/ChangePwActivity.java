package com.yw.platform.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yw.platform.R;
import com.yw.platform.global.Constants;
import com.yw.platform.global.MyApplication;
import com.yw.platform.ui.activity.BaseActivity;
import com.yw.platform.utils.AESEncryptor;
import com.yw.platform.utils.HttpClient;
import com.yw.platform.view.CustomProgressDialog;

@ContentView(R.layout.activity_changepw)
public class ChangePwActivity extends BaseActivity {
    
    @ViewInject(R.id.old_pw)
    private EditText old_pw_et;
    @ViewInject(R.id.new_pw)
    private EditText new_pw_et;
    @ViewInject(R.id.new_pwsure)
    private EditText new_pwsur_et;
    
    String old_pw;
    String new_pw;
    String new_pwsur;
    
    private CustomProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        
    }
    public void exchangePassword(View view){
        old_pw = old_pw_et.getText().toString().trim();
        new_pw = new_pw_et.getText().toString().trim();
        new_pwsur = new_pwsur_et.getText().toString().trim();
        if("".equals(old_pw)||"".equals(new_pw)||"".equals(new_pwsur)){
            Toast.makeText(this, "填写不完整！", Toast.LENGTH_SHORT).show();
            return;
        }else if(!(new_pw.equals(new_pwsur))){
            Toast.makeText(this, "两次新密码不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        submit();
    }
    public void submit(){
        progressDialog = CustomProgressDialog.getInstance(this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("请稍候...");
        
        
        RequestParams params = new RequestParams();
        params.addBodyParameter("logonName", MyApplication.getInstance().getCompany());
        params.addBodyParameter("oldPassword", old_pw);
        params.addBodyParameter("newPassword", new_pw);
        HttpUtils http = new HttpUtils();
        http.configCookieStore(HttpClient.getInstance().getCookieStore());
        http.configCurrentHttpCacheExpiry(50*1000);
        http.send(HttpMethod.POST, "http://"+Constants.platIp+":"+Constants.platPort+"/platform/account/modify/password", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> info) {
                progressDialog.dismiss();
                if(info.result.contains("MODIFY_PASSWORD_ERROR_0X001")){
                    Toast.makeText(ChangePwActivity.this, "原密码错误！", Toast.LENGTH_SHORT).show();
                    return;
                }else if(info.result.contains("MODIFY_PASSWORD_ERROR_0X0012")){
                    Toast.makeText(ChangePwActivity.this, "未知用户！", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences share = ChangePwActivity.this.getSharedPreferences(Constants.SYSTEMPREFERENT,0);
                boolean isRememberPassword=share.getBoolean(Constants.ISREMEMBERPASSWORD,false);
                if(isRememberPassword){
                    try {
                        String password_entryed= AESEncryptor.encrypt(Constants.KEY, new_pw);
                            share.edit().putString(MyApplication.getInstance().getCompany(),password_entryed).commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
                progressDialog.dismiss();
                Toast.makeText(ChangePwActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                progressDialog.dismiss();
                Toast.makeText(ChangePwActivity.this, "密码修改失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    public void onback(View view){
        onBackPressed();
    }
}
