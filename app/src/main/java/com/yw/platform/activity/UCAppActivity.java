package com.yw.platform.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdf.MuPDFActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yw.platform.R;
import com.yw.platform.download.DownloadManager;
import com.yw.platform.download.DownloadService;
import com.yw.platform.global.Constants;
import com.yw.platform.global.MyApplication;
import com.yw.platform.ui.activity.BaseActivity;
import com.yw.platform.utils.FileUtil;
import com.yw.platform.utils.HttpClient;
import com.yw.platform.utils.dialog.DialogBase;
import com.yw.platform.view.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class UCAppActivity extends BaseActivity {
    private String appUrl = "";
    private String appName, username, password, excuteJs;
    private SharedPreferences share;
    private CustomProgressDialog progressDialog;
    private EditText editText;
    private String host, urls;
    WebView webview;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uc_web);
        TextView title = (TextView) findViewById(R.id.title);
        ImageView btn_back = (ImageView) findViewById(R.id.title_back);
        TextView btn_setting = (TextView) findViewById(R.id.title_setting);
        editText = (EditText) findViewById(R.id.et_url);
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hideInput(UCAppActivity.this, editText);
                showDialog("确定退出" + appName + "?");
            }
        });
        btn_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showSettingDialog();
            }
        });
        webview = (WebView) findViewById(R.id.webview);
        appName = getIntent().getStringExtra("appName");
        title.setText(appName);
        btn_setting.setVisibility(View.INVISIBLE);
        Log.i("WebAppNewActivity", "excuteJs=" + excuteJs);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);// 设定支持缩放
        webSettings.setBuiltInZoomControls(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setDownloadListener(new MyWebViewDownLoadListener());
        // 不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);// 关键点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("Main", "url=" + url);
                URL urlh = null;
                try {
                    urlh = new URL(url);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String host1;
                if (urlh != null) {
                    host1 = urlh.getHost();
                } else {
                    Toast.makeText(UCAppActivity.this, "网络禁止访问！", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (!isHost2Can(url) && !isAdressCan(host1)) {
                    Toast.makeText(UCAppActivity.this, "网络禁止访问！", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    view.loadUrl(url);
                    editText.setText(url);
                    progressDialog = CustomProgressDialog.getInstance(UCAppActivity.this);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                    progressDialog.setMessage("加载中...");
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                Log.i("onPageFinished", "url...=" + url);
                super.onPageFinished(view, url);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            /*
             * (non Javadoc)
             * @Description: TODO
             * @param view
             * @param errorCode
             * @param description
             * @param failingUrl
             * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView, int,
             * java.lang.String, java.lang.String)
             */
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(UCAppActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        // webview.loadUrl(appUrl);
    }

    public void goUrl(View view) {
        urls = editText.getText().toString().trim();
        if (!urls.startsWith("http")) {
            urls = "http://" + urls;
        }
        try {
            URL url = new URL(urls);
            host = url.getHost();
            handler.sendEmptyMessage(1234);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Message msg1 = new Message();
            msg1.what = 12345;
            Bundle bundle = new Bundle();
            bundle.putString("text", "不是正确的网址！"); // 往Bundle中存放数据
            msg1.setData(bundle);// mes利用Bundle传递数据
            handler.sendMessage(msg1);// 用activity中的handler发送消息
            return;
        }
    }

    /**
     * 强制隐藏输入法键盘
     */
    private void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //增加二级域名判断
    public boolean isHost2Can(String url) {
        List<String> list = MyApplication.getInstance().getAddressList();
        for (int i = 0; i < list.size(); i++) {
            Log.i("haha", "i=" + i);
            if (url.contains(list.get(i))) {
                return true;
            }
        }
        return false;
    }

    // 判断是否含有*
    public boolean isAdressCan(String host) {
        List<String> list = MyApplication.getInstance().getAddressList();
        // 主要判断白名单中是否有*
        if (host.contains(".")) {
            String[] arr = host.split("\\.");
            if (arr.length == 3) {
                host = arr[1];
            } else if (arr.length == 4) {
                host = arr[2];
            } else {
                return false;
            }
        }
        String host_s = "*." + host;
        for (int i = 0; i < list.size(); i++) {
            Log.i("haha", "i=" + i);
            if (list.get(i).contains(host_s)) {
                return true;
            }
        }
        return false;
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            progressDialog = CustomProgressDialog.getInstance(UCAppActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setMessage("文件下载中...");
            downloadFile(url);
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    progressDialog.dismiss();
                    Toast.makeText(UCAppActivity.this, "文件下载失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    progressDialog.dismiss();
                    Toast.makeText(UCAppActivity.this, "不支持的文件格式！", Toast.LENGTH_SHORT).show();
                    break;
                case 123:
                    // 刷新
                    String type = msg.getData().getString("type");
                    String url = msg.getData().getString("url");
                    DownloadManager downloadManager = DownloadService.getDownloadManager(UCAppActivity.this);
                    try {
                        downloadManager.addNewDownload(url, "com.yw.attach",
                                UCAppActivity.this.getCacheDir().getPath() + "/sdfqwer." + type, false, false,
                                new UpdatePlatformCallBack(
                                        UCAppActivity.this.getCacheDir().getPath() + "/sdfqwer." + type));
                    } catch (DbException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 1234:
                    if (!isHost2Can(urls + "/") && !isAdressCan(host)) {
                        Toast.makeText(UCAppActivity.this, "网络禁止访问！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        webview.loadUrl(urls);
                    }
                    break;
                case 12345:
                    // 失败报错
                    Toast.makeText(UCAppActivity.this, msg.getData().getString("text"), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    public void downloadFile(final String path) {
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(path); // 创建url对象
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 建立连接
                    conn.setRequestMethod("GET"); // 设置请求方法
                    conn.setReadTimeout(5000); // 设置响应超时时间
                    conn.setConnectTimeout(5000); // 设置连接超时时间
                    conn.connect(); // 发送请求
                    int responseCode = conn.getResponseCode(); // 获取响应码
                    if (responseCode == 200) { // 响应码是200(固定值)就是连接成功，否者就连接失败
                        String type = conn.getContentType();
                        Message message = new Message();
                        if ("application/pdf".equalsIgnoreCase(type)) {
                            type = "pdf";
                        } else if ("application/msword".equalsIgnoreCase(type)) {
                            type = "doc";
                        } else if ("application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                .equalsIgnoreCase(type)) {
                            type = "docx";
                        } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                                .equalsIgnoreCase(type)) {
                            type = "xlsx";
                        } else if ("application/vnd.ms-excel".equalsIgnoreCase(type)) {
                            type = "xls";
                        } else if ("application/vnd.ms-powerpoint".equalsIgnoreCase(type)) {
                            type = "ppt";
                        } else if ("application/vnd.openxmlformats-officedocument.presentationml.presentation"
                                .equalsIgnoreCase(type)) {
                            type = "pptx";
                        } else {
                            handler.sendEmptyMessage(1);
                            return;
                        }
                        Bundle data = new Bundle();
                        data.putString("type", type);
                        data.putString("url", path);
                        message.setData(data);
                        message.what = 123;
                        handler.sendMessage(message);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();
    }

    /**
     * 回调类
     */
    class UpdatePlatformCallBack extends RequestCallBack<File> {
        String path;
        DownloadManager downloadManager;

        UpdatePlatformCallBack(String path) {
            this.path = path;
            downloadManager = DownloadService.getDownloadManager(UCAppActivity.this);
        }

        @Override
        public void onStart() {
            // TODO Auto-generated method stub
            super.onStart();
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            // TODO Auto-generated method stub
            super.onLoading(total, current, isUploading);
            if (!(path.contains("pdf"))) {
                progressDialog.setMessage("文件下载中...");
                return;
            }
            progressDialog.setMessage("已经完成" + (current * 100 / total) + "%");
        }

        @Override
        public void onFailure(HttpException arg0, String arg1) {
            // TODO Auto-generated method stub
            if (progressDialog != null && !UCAppActivity.this.isFinishing() && UCAppActivity.this != null) {
                progressDialog.dismiss();
            }
            Toast.makeText(UCAppActivity.this, "文件下载失败！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(ResponseInfo<File> arg0) {
            // TODO Auto-generated method stub
            if (path.contains("pdf") && progressDialog != null
                    && !UCAppActivity.this.isFinishing() && UCAppActivity.this != null) {
                progressDialog.dismiss();
            }
            try {
                if (null != downloadManager.getDownloadInfo("com.yw.attach")) {
                    downloadManager.removeDownload(downloadManager.getDownloadInfo("com.yw.attach"));
                }
            } catch (DbException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (path.contains("pdf")) {
                Uri uri = Uri.parse(path);
                Intent intent = new Intent(UCAppActivity.this, MuPDFActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.putExtra("name", "加密文件");
                UCAppActivity.this.startActivity(intent);
                UCAppActivity.this.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            } else
//                if (path.contains("ppt") || path.contains("pptx")||path.contains("xls") || path.contains("xlsx")) 
            {
                // 查询服务端是否有已经转换的文件
                checkPPT(path);
            }
//            else {
//                WordReader fr = new WordReader(WebAppNewActivity.this, path);
//                webview.loadUrl(fr.returnPath);
//                new Handler().postDelayed(new Runnable(){    
//                    public void run() {    
//                    //execute the task    
//                        shuiyinTv.setVisibility(View.VISIBLE);
//                    }    
//                 }, 1000); 
//                File file = new File(path);
//                file.delete();
//                File file1 = new File(getCacheDir().getPath() + "/" + Constants.BASE_WORD_FILE);
//                deleteAllFiles(file1);
        }
//        }
    }

    private void checkPPT(final String path) {
        RequestParams params = new RequestParams();
        HttpUtils http = new HttpUtils();
        http.configCookieStore(HttpClient.getInstance().getCookieStore());
        http.configCurrentHttpCacheExpiry(50 * 1000);
        String url = "http://" + Constants.docIp + ":" + Constants.docPort + "/doconvert/document/query?md5="
                + FileUtil.getFileMD5(new File(path));
        http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> info) {
                JSONObject obj = null;
                String downUrl = "";
                try {
                    obj = new JSONObject(info.result);
                    downUrl = obj.optString("content");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (downUrl.length() < 5) {
                    //上传
                    xUtilsUpLoadFile(path);
                } else {
                    File file = new File(path);
                    file.delete();
                    //下载
                    Bundle data = new Bundle();
                    data.putString("type", "pdf");
                    data.putString("url", downUrl);
                    Message message = new Message();
                    message.setData(data);
                    message.what = 123;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                progressDialog.dismiss();
                File file = new File(path);
                file.delete();
                Toast.makeText(UCAppActivity.this, "文档下载失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * 这里是 xUtils的文件post上传， url是上传到网络的位置 userid是其他的参数 filePath是要上传的文件位置
     */
    public void xUtilsUpLoadFile(final String filePath) {
        String url = "http://" + Constants.docIp + ":" + Constants.docPort + "/doconvert/document/upload";
        // RequestParams对象是用来存放请求参数的
        RequestParams params = new RequestParams();
//        params.addBodyParameter("userid", userid);// 这里是一般的参数
        params.addBodyParameter("file", new File(filePath));// 这里才是重点上传文件的参数
        // HttpUtils网络请求
        HttpUtils http = new HttpUtils();
        http.configSoTimeout(200 * 1000);
        http.configCookieStore(HttpClient.getInstance().getCookieStore());
        // 发送请求
        /**
         * 第一个参数：请求方式 第二个参数：请求地址 第三个参数：请求携带的参数类 第四个参数：网络请求的监听
         */
        http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject obj = null;
                String downUrl = "";
                try {
                    obj = new JSONObject(responseInfo.result);
                    downUrl = obj.optString("content");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (downUrl.length() < 5) {
                    //上传
                    progressDialog.dismiss();
                    Toast.makeText(UCAppActivity.this, "文档下载失败！", Toast.LENGTH_SHORT).show();
                } else {
                    File file = new File(filePath);
                    file.delete();
                    //下载
                    Bundle data = new Bundle();
                    data.putString("type", "pdf");
                    data.putString("url", downUrl);
                    Message message = new Message();
                    message.setData(data);
                    message.what = 123;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                progressDialog.dismiss();
                File file = new File(filePath);
                file.delete();
                Toast.makeText(UCAppActivity.this, "文档下载失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    private void usernameLogin() {
        username = share.getString("username", "");
        password = share.getString("password", "");
        excuteJs = getIntent().getStringExtra("excuteJs");
        excuteJs = excuteJs.replace("ttxcuser", username);
        excuteJs = excuteJs.replace("ttxcpwd", password);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // showDialog("确定退出"+appName+"?");
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            showDialog("确定退出" + appName + "?");
        }
    }

    // 弹出框
    private void showDialog(String text) {
        DialogBase.Builder customDialog = new DialogBase.Builder(this)
                .setShowTitle(false)
                .setNegativeButton("取消", null)
                .setContent(DialogBase.createTextContent(this, text))
                .setCanCelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        webview.clearCache(true);
                        finish();
                    }
                });
        customDialog.show();
    }

    private void showSettingDialog() {
        final SharedPreferences sharepre = getSharedPreferences(appName, 0);
        LayoutInflater settingInflater = (LayoutInflater) getSystemService(
                android.content.Context.LAYOUT_INFLATER_SERVICE);
        View loginView = settingInflater.inflate(R.layout.webapp_login_dialog, null);
        final Dialog webAppDialog = new Dialog(this, R.style.Theme_NoBackground_NoTitle);
        webAppDialog.show();
        TextView textView = (TextView) loginView.findViewById(R.id.title);
        textView.setText(appName);
        final EditText username_et = (EditText) loginView.findViewById(R.id.webappusername_et);
        final EditText password_et = (EditText) loginView.findViewById(R.id.webapppassword_et);
        ImageView login_cancel = (ImageView) loginView.findViewById(R.id.webapp_link_back);
        login_cancel.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webAppDialog.dismiss();
            }
        });
        Button login_btn = (Button) loginView.findViewById(R.id.webappsave_btn);
        login_btn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username_str = username_et.getText().toString().trim();
                String password_str = password_et.getText().toString().trim();
                if ("".equals(username_str) || "".equals(password_str)) {
                    Toast.makeText(UCAppActivity.this, "各项都不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    webAppDialog.dismiss();
                    sharepre.edit().putString("username", username_str).commit();
                    sharepre.edit().putString("password", password_str).commit();
                    usernameLogin();
                    webview.loadUrl(appUrl);
                    progressDialog.show();
                }
            }
        });
        Button cancel_btn = (Button) loginView.findViewById(R.id.webappcancel_btn);
        cancel_btn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webAppDialog.dismiss();
            }
        });
        webAppDialog.setContentView(loginView);
    }

    /*
     * (non Javadoc)
     * @Description: TODO
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
