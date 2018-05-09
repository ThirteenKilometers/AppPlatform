package com.yw.platform.yhtext.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yw.platform.R;

public class WebViewActivity extends AppCompatActivity {
    WebView mWebView;
    private ProgressDialog dialog;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        init();
    }

    private void init() {
        mWebView = findViewById(R.id.webView);
        Intent intent=getIntent();
        if (intent != null) {
             value=intent.getStringExtra("key");
        }
        mWebView.loadUrl(value);
        //判断页面加载过程
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    closeDialog();//页面加载完成
                } else {
                    openDialog(newProgress);//网页正在加载，打开dialog
                }
            }
        });
        //覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebView中打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候是控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器打开
                view.loadUrl(url);
                return true;
            }
            //WebViewClient帮助WebView去处理一些页面控制和请求通知
        });
    }

    public  void onBack(View view){
        this.finish();
    }


    private void openDialog(int newProgress) {
        if (dialog == null) {
            dialog = new ProgressDialog(WebViewActivity.this);
            dialog.setTitle("正在加载");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(newProgress);
            dialog.setCancelable(true);
            dialog.show();
        } else {
            dialog.setProgress(newProgress);
        }
    }

    private void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
