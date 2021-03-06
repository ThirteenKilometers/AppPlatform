package com.yw.platform.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yw.platform.R;
import com.yw.platform.global.Constants;
import com.yw.platform.ui.activity.BaseActivity;

/**
 * Created by panda on 15-1-9.
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.platip_et)
    private EditText platip_et;

    @ViewInject(R.id.platport_et)
    private EditText platport_et;

    @ViewInject(R.id.vpnip_et)
    private EditText vpnip_et;

    @ViewInject(R.id.vpnport_et)
    private EditText vpnport_et;

    @ViewInject(R.id.btnback)
    private Button btnback;

    @OnClick(R.id.btnback)
    public void btnBackClick(View view){
        this.finish();
    }

    @OnClick(R.id.btnok)
    public void btnOkClick(View view){
        String plat_ip=platip_et.getText().toString();
        String plat_port=platport_et.getText().toString();
        String vpn_ip=vpnip_et.getText().toString();
        String vpn_port=vpnport_et.getText().toString();
        if(plat_ip.isEmpty()||plat_port.isEmpty()
                ||vpn_ip.isEmpty()||vpn_port.isEmpty()
                ){
            Toast.makeText(this, R.string.cannotbenull, Toast.LENGTH_SHORT).show();
        }else{
            SharedPreferences.Editor editor=share.edit();
            editor.putString(Constants.DEFAULT_PLAT_IP, plat_ip).commit();
            editor.putString(Constants.DEFAULT_PLAT_PORT, plat_port).commit();
            editor.putString(Constants.DEFAULT_VPN_IP, vpn_ip).commit();
            editor.putString(Constants.DEFAULT_VPN_PORT, vpn_port).commit();
            editor.commit();
            this.finish();
        }
    }

    @ViewInject(R.id.btnok)
    private Button btnok;

    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        share = this.getSharedPreferences(Constants.SYSTEMPREFERENT,0);

        platip_et.setText(share.getString(Constants.DEFAULT_PLAT_IP,Constants.platIpf));
        platport_et.setText(share.getString(Constants.DEFAULT_PLAT_PORT, Constants.platPortf));
        vpnip_et.setText(share.getString(Constants.DEFAULT_VPN_IP,Constants.vpnIp));
        vpnport_et.setText(share.getString(Constants.DEFAULT_VPN_PORT, Constants.vpnPort));
    }
}
