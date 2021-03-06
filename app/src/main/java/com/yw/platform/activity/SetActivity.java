package com.yw.platform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.yw.platform.R;
import com.yw.platform.ui.activity.BaseActivity;

@ContentView(R.layout.activity_set)
public class SetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        
    }
    public void exchangePassword(View view){
        Intent intent = new Intent();
        intent.setClass(SetActivity.this, ChangePwActivity.class);
        startActivity(intent);
    }
    
    public void setUser(View view){
        Intent intent = new Intent();
        intent.setClass(SetActivity.this, VpnUserSetActivity.class);
        startActivity(intent);
    }
    public void lockScreen(View view){
        Intent intent=new Intent(this,ValidateActivity.class);
        startActivity(intent);
        finish();
    }
    public void setLock(View view){
        Intent intent=new Intent(this,SetLockActivity.class);
        startActivity(intent);
    }
    public void onback(View view){
        onBackPressed();
    }
}
