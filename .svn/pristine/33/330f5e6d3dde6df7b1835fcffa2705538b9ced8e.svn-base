package com.yw.platform.yhtext.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * <p>
 * 动画控制类
 */

public class Anim {

    //显示动画
    public static TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
    //隐藏动画
    public static TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f);

    public static void title_btn_animV(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 180f);
        animator.setDuration(500);
        animator.start();
    }

    public static void title_btn_animH(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 180f, 0f);
        animator.setDuration(500);
        animator.start();
    }


    public static void cleanAnim(ImageView animView) {
        if (animView == null)
            return;
        animView.setImageResource(0);
        animView.clearAnimation();
        animView.setVisibility(View.GONE);
    }

    public static ObjectAnimator round(ImageView iv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv, "rotation", 0f, 360f);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        return animator;
    }


}