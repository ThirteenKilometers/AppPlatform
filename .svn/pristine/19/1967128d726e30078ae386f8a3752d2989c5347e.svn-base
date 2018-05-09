package com.yw.platform.tools;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import java.util.List;

/**
 * Created by cxb on 2018/4/16.
 */

public class AppLockTools {
    /**
     * 获取前台app包名
     * @return
     */
    public static String getForegroundApp(Context context){
        if (Build.VERSION.SDK_INT >= 21) {
            return getForegroundAppNew(context);
        } else {
            return getForegroundAppold(context);
        }
    }

    @TargetApi(21)
    private static String getForegroundAppNew(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
        long ts = System.currentTimeMillis();
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return null;
        }
        UsageStats recentStats = null;
        for (UsageStats usageStats : queryUsageStats) {
            if(recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()){
                recentStats = usageStats;
            }
        }
        return recentStats.getPackageName();
    }

    private static String getForegroundAppold(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
        if (null != appTasks && !appTasks.isEmpty()) {
            return appTasks.get(0).topActivity.getPackageName();
        }
        return null;
    }
}
