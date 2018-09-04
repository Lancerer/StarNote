package com.example.lancer.starnote.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * author: Lancer
 * date：2018/9/4
 * des:
 * email:tyk790406977@126.com
 */

public class AnimUtils {
    /**
     * 旋转动画
     */
    public static Animation getRotateAnimation(float fromDegress, float toDegress, long time) {
        RotateAnimation animation = new RotateAnimation(fromDegress, toDegress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(time);
        animation.setFillAfter(true);
        return animation;

    }

    /**
     * 透明度 Alpha
     */
    public static Animation getAlphaAnimation(float fromAlpha, float toAlpha,
                                              long durationMillis) {
        AlphaAnimation alpha = new AlphaAnimation(fromAlpha, toAlpha);
        alpha.setDuration(durationMillis);
        alpha.setFillAfter(true);
        return alpha;
    }

    /**
     * 缩放 Scale
     */
    public static Animation getScaleAnimation(float scaleXY, long durationMillis) {
        ScaleAnimation scale = new ScaleAnimation(1.0f, scaleXY, 1.0f, scaleXY,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(durationMillis);
        return scale;
    }

    /**
     * 位移 Translate
     */
    public static Animation getTranslateAnimation(float fromXDelta,
                                                  float toXDelta, float fromYDelta, float toYDelta,
                                                  long durationMillis) {
        TranslateAnimation translate = new TranslateAnimation(fromXDelta,
                toXDelta, fromYDelta, toYDelta);
        translate.setDuration(durationMillis);
        translate.setFillAfter(true);
        return translate;
    }

    /**
     * @param relativeLayout 子菜单容器
     * @param img            菜单按钮
     * @param time           动画时长
     */
    public static void OpenAnimation(RelativeLayout relativeLayout, ImageView img, long time) {
        relativeLayout.setVisibility(View.VISIBLE);
        for (int i = 1; i < relativeLayout.getChildCount(); i++) {
            ImageView imageView = null;
            if (relativeLayout.getChildAt(i) instanceof ImageView) {
                imageView = (ImageView) relativeLayout.getChildAt(i);

            } else {
                continue;
            }
            int top = imageView.getTop();
            int left = imageView.getLeft();
            if (top == 0) {
                top = (img.getHeight() + 50) * i;
            }
            if (left == 0) {
                left = img.getLeft();
            }
            AnimationSet set = new AnimationSet(true);
            set.addAnimation(getRotateAnimation(-360, 0, time));
            set.addAnimation(getAlphaAnimation(0.5f, 1f, time));
            set.addAnimation(getTranslateAnimation(img.getLeft() - left, 0, img.getTop() - top + 30, 0, time));

            set.setFillAfter(true);
            set.setDuration(time);
            set.setInterpolator(new OvershootInterpolator(1f));
            imageView.startAnimation(set);
        }
    }

    public static void CloseAnimation(final RelativeLayout relativeLayout, ImageView img, long time) {
        for (int i = 1; i < relativeLayout.getChildCount(); i++) {
            ImageView imageView = null;
            if (relativeLayout.getChildAt(i) instanceof ImageView) {
                imageView = (ImageView) relativeLayout.getChildAt(i);
            } else {
                continue;
            }
            AnimationSet set = new AnimationSet(true);
            set.addAnimation(getRotateAnimation(0, -360, time));
            set.addAnimation(getAlphaAnimation(1.0f,0f,time));
            set.addAnimation(getTranslateAnimation(0,img.getLeft()-imageView.getLeft()
            ,0,img.getTop()-imageView.getTop()+30,time));

            set.setFillAfter(true);
            set.setDuration(time);
            set.setStartOffset(((relativeLayout.getChildCount() - i) * 100)
                    / (-1 + relativeLayout.getChildCount()));
            set.setInterpolator(new AnticipateInterpolator(1f));
            set.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    relativeLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            imageView.startAnimation(set);

        }
    }
}
