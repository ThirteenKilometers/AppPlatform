package com.yw.platform.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yw.platform.R;
import com.yw.platform.global.Constants;
import com.yw.platform.global.MyApplication;
import com.yw.platform.model.AccountInfo;
import com.yw.platform.utils.LocationUtils;
import com.yw.platform.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends AdminActivity implements Animation.AnimationListener {

    private static final String TAG = "WelcomeActivity";
    @ViewInject(R.id.imageView4)
    private ImageView imageView;
    SharedPreferences oaPrefer;
    private String company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        AccountInfo info = getAccountInfo();
        if (info == null) {
            Toast.makeText(this, "请从官网下载合法安装包", Toast.LENGTH_SHORT).show();
            SystemClock.sleep(3000);
            finish();
            return;
        }
        MyApplication.getInstance().setCompany(company = info.company);
        Constants.platIp = info.ip;
        Constants.platPort = info.port;
        Constants.platIpRegister = info.rIp;
        Constants.platPortRegister = info.rPort;
        if (TextUtils.isEmpty(company)) {
            finish();
        } else {
            Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);
            alphaAnimation.setFillEnabled(true);
            alphaAnimation.setFillAfter(true);
            imageView.setAnimation(alphaAnimation);
            alphaAnimation.setAnimationListener(this);
        }
    }

/*    private void showErrorDialog(String content) {
        CommonHint.bulidDialog(this, content, new CommonHint.TwoOnClickListener() {
            @Override
            public void onCancel() {
                finish();
            }

            @Override
            public void onSure() {
                login();
            }
        });
    }

    private void login() {
        LoginTask util = new LoginTask(this);
        util.attemptLogin(company, "yw123456", new LoginTask.LoginBack() {
            @Override
            public void onSuccess() {
                MyApplication.getInstance().setCompany(company);
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("password", "yw123456");
                startActivity(intent);
                MyApplication.getInstance().setPassWord("yw123456");
                finish();
            }

            @Override
            public void onFail(String err) {
                showErrorDialog(err);
            }
        });
    }*/

    @Override
    public void onAnimationStart(Animation animation) {
        //checkOaVersion();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("company", company);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void checkOaVersion() {
        oaPrefer = this.getSharedPreferences("OA_vision", 0);
        String currentVision = oaPrefer.getString("oa_vision", "0");
        Log.i(TAG, "原来的oa包版本是：" + currentVision);
        int curVersionNum = Integer.parseInt(currentVision);
        int firstVision = Integer.parseInt(Constants.INIT_OA_VERSION);
        Log.i(TAG, "现在的oa包版本是：" + firstVision);

        if (curVersionNum < firstVision) {
            new CopyAssetsAsyncTask().execute(1);
        }
    }

    private class CopyAssetsAsyncTask extends AsyncTask<Integer, Bitmap, Object> {

        @Override
        protected Object doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            Log.i(TAG, "将程序中的OA包拷贝到设备中");
            LocationUtils.copyAsset(WelcomeActivity.this);
            oaPrefer.edit().putString(
                    "oa_vision",
                    Constants.INIT_OA_VERSION).apply();
            return null;
        }

        protected void onPostExecute(Object result) {
//			loadUrl("file://" + FileUtil.setMkdir(context)
//					+ File.separator + "app/oa/login.html");
        }
    }

    public AccountInfo getAssetInfo() {
        AccountInfo info = new AccountInfo();
        try {
            InputStream inStream = getAssets().open("cominfo");
            if (inStream == null) return null;
            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
            String temp;
            StringBuilder sb = new StringBuilder();
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            info.company = jsonObject.optString("company", "");
            info.ip = jsonObject.optString("ip", "");
            info.port = jsonObject.optString("port", "");
            info.rIp = jsonObject.optString("rIp", "");
            info.rPort = jsonObject.optString("rPort", "");
            if ("".equals(info.company) || "".equals(info.ip) || "".equals(info.port) || "".equals(info.rIp) || "".equals(info.rPort)) {
                return null;
            } else {
                return info;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private AccountInfo getAccountInfo() {
        //删除更新安装包防止用户点击
        File file = new File(Constants.downloadFile + "platform.apk");
        if (file.exists()) file.delete();
        AccountInfo info = getAssetInfo();
        if (info != null) {
            SharedPreferencesUtils.setParam("companyInfo", "company", info.company);
            SharedPreferencesUtils.setParam("companyInfo", "ip", info.ip);
            SharedPreferencesUtils.setParam("companyInfo", "port", info.port);
            SharedPreferencesUtils.setParam("companyInfo", "rIp", info.rIp);
            SharedPreferencesUtils.setParam("companyInfo", "rPort", info.rPort);
            return info;
        }
        String company = (String) SharedPreferencesUtils.getParam("companyInfo", "company", "");
        String ip = (String) SharedPreferencesUtils.getParam("companyInfo", "ip", "");
        String port = (String) SharedPreferencesUtils.getParam("companyInfo", "port", "");
        String rIp = (String) SharedPreferencesUtils.getParam("companyInfo", "rIp", "");
        String rPort = (String) SharedPreferencesUtils.getParam("companyInfo", "rPort", "");
        if ("".equals(company) || "".equals(ip) || "".equals(port) || "".equals(rIp) || "".equals(rPort)) {
            return null;
        } else {
            info = new AccountInfo();
            info.company = company;
            info.ip = ip;
            info.port = port;
            info.rIp = rIp;
            info.rPort = rPort;
            return info;
        }
    }

}
