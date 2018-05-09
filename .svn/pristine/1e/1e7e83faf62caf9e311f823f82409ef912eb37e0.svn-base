package com.yw.platform.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yw.platform.R;
import com.yw.platform.utils.dialog.DialogBase;

/**
 * Created by cxb on 2017/7/12.
 */
@ContentView(R.layout.activity_setclock)
public class SetLockActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tvTime)
    private TextView showTime;
    private LinearLayout all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        switch (getTime()){
            case 10:
                showTime.setText("10秒");
                break;
            case 30:
                showTime.setText("30秒");
                break;
            case 60:
                showTime.setText("1分钟");
                break;
            case 120:
                showTime.setText("2分钟");
                break;
        }
    }
    public void onback(View v){
        finish();
    }

    public void setTime(View view){
        bulidDialog(this);
    }
    public  synchronized void bulidDialog(Context context){
        try {
            all= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_dialog_setclock,null);
            handle(all);
            DialogBase.Builder customDialog= new DialogBase.Builder(context)
                    .setShowTitle(false)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setContent(all)
                    .setCanCelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < all.getChildCount(); i++) {
                                if(i%2==0){
                                    LinearLayout view= (LinearLayout) all.getChildAt(i);
                                    if(view.getChildAt(1).getVisibility()==View.VISIBLE){
                                        switch (i/2){
                                            case 0:
                                                setTime(10);
                                                showTime.setText("10秒");
                                                break;
                                            case 1:
                                                setTime(30);
                                                showTime.setText("30秒");
                                                break;
                                            case 2:
                                                setTime(60);
                                                showTime.setText("1分钟");
                                                break;
                                            case 3:
                                                setTime(120);
                                                showTime.setText("2分钟");
                                                break;
                                        }
                                        i=10;
                                    }
                                }
                            }
                        }
                    });
            customDialog.show();
        }catch (Exception e){

        }
    }


    private void handle(LinearLayout all){
        clear();
        int time=getTime();
        for (int i = 0; i < all.getChildCount(); i++) {
            if(i%2==0){
                LinearLayout view= (LinearLayout) all.getChildAt(i);
                view.setOnClickListener(this);
                if(time==i2count(i/2)){
                    view.getChildAt(1).setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private int count2i(int count){
        switch (count){
            case 10:
                return 0;
            case 30:
                return 1;
            case 60:
                return 2;
            case 120:
                return 3;
        }
        return 0;
    }
    private int i2count(int i){
        switch (i){
            case 0:
                return 10;
            case 1:
                return 30;
            case 2:
                return 60;
            case 3:
                return 120;
        }
        return 0;
    }

    private void clear(){
        for (int i = 0; i < all.getChildCount(); i++) {
            if(i%2==0){
                LinearLayout view= (LinearLayout) all.getChildAt(i);
                view.getChildAt(1).setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onClick(View v) {
        clear();
        ((LinearLayout)v).getChildAt(1).setVisibility(View.VISIBLE);
    }
}
