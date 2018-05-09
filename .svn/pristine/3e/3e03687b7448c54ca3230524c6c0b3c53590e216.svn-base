package com.yw.platform.utils.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yw.platform.R;

/**
 * 普通内容提示
 */
public class CommonHint {
    public static void bulidDialog(Context context, String content, final TwoOnClickListener onClickListener){
        try {
            View view= LayoutInflater.from(context).inflate(R.layout.layout_dialog_hint,null);
            TextView textView= (TextView) view.findViewById(R.id.showt);
            textView.setText(content);
            DialogBase.Builder customDialog= new DialogBase.Builder(context)
                    .setShowTitle(false)
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(onClickListener!=null)onClickListener.onCancel();
                            }
                        })
                        .setContent(view)
                        .setCanCelable(false)
                        .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(onClickListener!=null)onClickListener.onSure();
                            }
                        });
                customDialog.show();
            }catch (Exception e){

            }
    }
    public static void bulidDialog1(final Context context, String content, final TwoOnClickListener onClickListener){
        try {
            View view= LayoutInflater.from(context).inflate(R.layout.layout_dialog_hint_test,null);
            TextView textView= (TextView) view.findViewById(R.id.showt);
            textView.setText(content);
            final EditText input= (EditText) view.findViewById(R.id.input);
            DialogBase.Builder customDialog= new DialogBase.Builder(context)
                    .setShowTitle(false)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(onClickListener!=null)onClickListener.onCancel();
                        }
                    })
                    .setContent(view)
                    .setCanCelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(input.getText().toString().equals("恢复出厂设置")){
                                if(onClickListener!=null)onClickListener.onSure();
                            }else{
                                Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            customDialog.show();
        }catch (Exception e){

        }
    }
    public interface TwoOnClickListener {
        public void onCancel();

        public void onSure();
    }
}
