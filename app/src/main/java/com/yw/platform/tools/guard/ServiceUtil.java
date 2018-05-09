package com.yw.platform.tools.guard;

import android.content.Context;
import android.content.Intent;

/**
 * Created by cxb on 2018/4/16.
 */

public class ServiceUtil {

    public static void start(Context context){
        // 启动本地服务和远程服务
        context.startService(new Intent(context, LocalCastielService.class));
        context.startService(new Intent(context, RemoteCastielService.class));
    }

}
