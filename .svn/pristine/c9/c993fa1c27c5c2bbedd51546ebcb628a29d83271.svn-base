package com.yw.platform.test;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yw.platform.R;
import com.yw.platform.activity.AdminActivity;
import com.yw.platform.tools.SetInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxb on 2018/3/22.
 */

public class AppsetActvitiy extends AdminActivity {
    private GridView gvApps;
    private List<AppItem> appItems;
    private AppAdapter adapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, AppsetActvitiy.class);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setapp);
        initView();
        initListener();
        initData();
    }
    private void initView(){
        gvApps= (GridView) findViewById(R.id.lv_apps);
        appItems=new ArrayList<>();
        adapter=new AppAdapter(this,appItems);
        gvApps.setAdapter(adapter);
    }
    private void initListener(){
    }
    private void initData() {
        appItems.clear();
        appItems.addAll(getAppList());
        adapter.notifyDataSetChanged();
        AppLockerService.startService(this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    private List<AppItem> getAppList() {
        List<AppItem> appItems=new ArrayList<>();
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        List<String> lockApps= SetInfo.getInstance().getAllLockApp();
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==  0) {
                AppItem appItem=new AppItem();
                //获取包名
                appItem.pageName=packageInfo.applicationInfo.packageName;
                appItem.name=packageInfo.applicationInfo.name;
                if (lockApps.contains(appItem.pageName)){
                    appItem.lock=true;
                }else{
                    appItem.lock=false;
                }
                appItems.add(appItem);
            }
        }
        return appItems;
    }

    private class AppAdapter extends BaseAdapter implements View.OnClickListener {
        private Context context;
        private List<AppItem> appItems;

        public AppAdapter(Context context, List<AppItem> appItems) {
            this.context = context;
            this.appItems = appItems;
        }

        @Override
        public int getCount() {
            return appItems.size();
        }

        @Override
        public Object getItem(int position) {
            return appItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view= View.inflate(context,R.layout.list_item_app,null);
            ImageView ivAppicon= (ImageView) view.findViewById(R.id.ic_appicon);
            ImageView ivAppLock= (ImageView) view.findViewById(R.id.ic_lock);
            TextView tvName= (TextView) view.findViewById(R.id.tvAppName);
            AppItem appItem=appItems.get(position);
            ivAppicon.setImageDrawable(getIcon(appItem.pageName));
            ivAppLock.setImageResource(appItem.lock?R.drawable.ic_lock:R.drawable.ic_unlock);
            tvName.setText(appItem.name);
            view.findViewById(R.id.item).setTag(position);
            view.findViewById(R.id.item).setOnClickListener(this);
            return view;
        }
        public Drawable getIcon(String packageName) {
            Drawable icon = null;
            try {
                icon = context.getPackageManager().getApplicationIcon(packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return icon;
        }

        @Override
        public void onClick(View v) {
            int postion= (Integer) v.getTag();
            AppItem appItem=appItems.get(postion);
            appItem.lock=!appItem.lock;
            SetInfo.getInstance().setApplock(appItem.pageName,appItem.lock);
            notifyDataSetChanged();
        }
    }
    class AppItem{
        public String name;
        public String pageName;
        public boolean lock=false;
    }
}
