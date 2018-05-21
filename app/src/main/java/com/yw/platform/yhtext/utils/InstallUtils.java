package com.yw.platform.yhtext.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;

public class InstallUtils {
    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    public static void install(Context mContext, String apkPath) {

        lzhs.com.library.utils.log.LogUtils.d("通过隐式意图调用系统安装程序安装APK" + apkPath);// storage/emulated/0/emap_platform/download/_1.apk
        lzhs.com.library.utils.log.LogUtils.d("系统目录" + Environment.getExternalStorageDirectory());//   /storage/emulated/0
        lzhs.com.library.utils.log.LogUtils.d("file_path 里面的路径");
        //   File file = new File(Environment.getExternalStorageDirectory(), apkPath);
        File file = new File(apkPath);

        Intent mIntent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.yw.platform.provider", file);

            mContext.grantUriPermission("com.android.camera", apkUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else
            mIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            mContext.startActivity(mIntent);
    }
}
