package com.yw.platform.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yw.platform.R;
import com.yw.platform.global.Constants;
import com.yw.platform.view.CustomProgressDialog;

@ContentView(R.layout.activity_vpnuserset)
public class VpnUserSetActivity extends BaseActivity {
    
    @ViewInject(R.id.vpnuser)
    private EditText vpnuser;
    @ViewInject(R.id.vpnpw)
    private EditText vpnpw;
    
    String vpnuser_s;
    String vpnpw_s;
    String new_pwsur;
    
    private SharedPreferences share;
    
    private CustomProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        share = this.getSharedPreferences(Constants.SYSTEMPREFERENT,0);
        vpnuser.setText(share.getString(Constants.DEFAULT_VPN_USER,Constants.vpnUser));
        vpnpw.setText(share.getString(Constants.DEFAULT_VPN_PW, Constants.vpnPw));
    }
    public void sure(View view){
        vpnuser_s = vpnuser.getText().toString().trim();
        vpnpw_s = vpnpw.getText().toString().trim();
        if("".equals(vpnuser_s)||"".equals(vpnpw_s)){
            Toast.makeText(this, "填写不完整！", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor editor=share.edit();
        editor.putString(Constants.DEFAULT_VPN_USER, vpnuser_s).commit();
        editor.putString(Constants.DEFAULT_VPN_PW, vpnpw_s).commit();
        editor.commit();
        this.finish();
    }
    
    public void onback(View view){
        onBackPressed();
    }
}
