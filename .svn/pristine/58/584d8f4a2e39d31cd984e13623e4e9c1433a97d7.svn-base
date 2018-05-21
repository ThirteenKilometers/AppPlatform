package com.yw.platform.yhtext.utils;


import android.content.Context;
import android.content.Intent;

import com.yw.platform.global.MyApplication;
import com.yw.platform.yhtext.activity.LoginActivity;

import lzhs.com.library.utils.ToastUtils;

import static lzhs.com.library.utils.ActivityUtils.startActivity;

public class CodeUtils {
    /**
     * 错误编码code
     * @param code
     */
     public  static void initCode(Context context, int code) {
        switch (code) {
            case 10002:
            case 10003:
                ToastUtils.showShort("登录过期,请重新登录");
              Intent intent= new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                startActivity(intent);
                break;
            case 10007:
                ToastUtils.showShort("当前时间禁止访问");
                startActivity(new Intent(context, LoginActivity.class));
                break;
            case 20004:
                ToastUtils.showShort("账号已在其他设备上登录,请重新登录");
                startActivity(new Intent(MyApplication.getInstance(), LoginActivity.class));
                break;

        }
    }

}
