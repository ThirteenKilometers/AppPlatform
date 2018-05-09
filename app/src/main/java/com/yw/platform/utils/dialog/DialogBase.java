package com.yw.platform.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yw.platform.R;


/**
 * 对话框基础类
 * Created by cxb on 2017/3/10.
 * //<!-- 自定义loading dialog -->
 * //<style name="loading_dialog" parent="android:style/Theme.Dialog">
 * //<item name="android:windowFrame">@null</item>
 * //<item name="android:windowNoTitle">true</item>
 * //<item name="android:windowBackground">@android:color/transparent</item>
 * //<item name="android:windowIsFloating">true</item>
 * //<item name="android:windowContentOverlay">@null</item>
 * //</style>
 */

public final class DialogBase extends Dialog {

    public DialogBase(Context context) {
        super(context);
    }

    public DialogBase(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private int themeResId = R.style.loading_dialog;
        private boolean canCelable = true;
        //top
        private boolean isShowTitle = true;
        private String title;
        private int gravity;
        //content
        private View contentView;
        //bottom
        private String negativeText, positiveText;
        private OnClickListener positiveListener, negativeListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context, int themeResId) {
            this.context = context;
            this.themeResId = themeResId;
        }

        public Builder setTitle(String title, int gravity) {
            this.title = title;
            this.gravity = gravity;
            return this;
        }

        public Builder setShowTitle(boolean isShow) {
            this.isShowTitle = isShow;
            return this;
        }

        public Builder setContent(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setCanCelable(boolean canCelable) {
            this.canCelable = canCelable;
            return this;
        }

        public Builder setPositiveButton(String positiveText, OnClickListener listener) {
            this.positiveText = positiveText;
            this.positiveListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeText, OnClickListener listener) {
            this.negativeText = negativeText;
            this.negativeListener = listener;
            return this;
        }

        public DialogBase create1() {
            View baseView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_base, null);
            final DialogBase dialog = new DialogBase(context, themeResId);
            TextView tvTitle = (TextView) baseView.findViewById(R.id.c_title);
            TextView tvPositive = (TextView) baseView.findViewById(R.id.c_positiveButton);
            TextView tvNegative = (TextView) baseView.findViewById(R.id.c_negativeButton);
            LinearLayout contView = (LinearLayout) baseView.findViewById(R.id.c_content);
            ImageView ivClose = (ImageView) baseView.findViewById(R.id.iv_close);
            if (isShowTitle) {
                if (TextUtils.isEmpty(title)) {     //标题
                    tvTitle.setText("提示");
                } else {
                    tvTitle.setText(title);
                    tvTitle.setGravity(gravity);
                }
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (negativeListener != null) {
                            negativeListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    }
                });
            } else {
                baseView.findViewById(R.id.unit_top).setVisibility(View.GONE);
            }
            contView.addView(contentView);
            if (TextUtils.isEmpty(positiveText)) {  //确认
                tvPositive.setVisibility(View.GONE);
            } else {
                tvPositive.setText(positiveText);
                tvPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (positiveListener != null) {
                            positiveListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                        dialog.dismiss();
                    }
                });
            }
            if (TextUtils.isEmpty(negativeText)) {  //取消
                tvNegative.setVisibility(View.GONE);
            } else {
                tvNegative.setText(negativeText);
                tvNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (negativeListener != null) {
                            negativeListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    }
                });
            }
            dialog.setCancelable(canCelable);
            dialog.addContentView(baseView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(baseView);
            return dialog;
        }

        public DialogBase show() {
            DialogBase dialog = create1();
            dialog.show();
            return dialog;
        }
    }

    public static TextView createTextContent(Context context, String text) {
        final TextView defaultContent = new TextView(context);
        defaultContent.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics())));
        defaultContent.setGravity(Gravity.CENTER);
        defaultContent.setTextColor(Color.parseColor("#664747"));
        defaultContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        defaultContent.setText(text);
        return defaultContent;
    }
}



