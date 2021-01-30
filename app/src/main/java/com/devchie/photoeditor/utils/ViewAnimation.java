package com.devchie.photoeditor.utils;

import android.animation.Animator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.TranslateAnimation;

public class ViewAnimation {
    public static void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) view.getHeight(), 0.0f);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
    }

    public static void slideDown(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) view.getHeight());
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
    }

    public static void animationView(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            int width = view.getWidth() / 2;
            int height = view.getHeight() / 2;
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(view, width, height, 0.0f, (float) Math.hypot((double) width, (double) height));
            view.setVisibility(View.VISIBLE);
            createCircularReveal.start();
            return;
        }
        view.setVisibility(View.VISIBLE);
    }
}
