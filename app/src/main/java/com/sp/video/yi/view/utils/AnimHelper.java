package com.sp.video.yi.view.utils;

import android.widget.ProgressBar;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import java.lang.ref.WeakReference;

/**
 * @author Yangz
 * @version 14-6-25
 */
public class AnimHelper {

    public static final int ANIMATION_DURATION = 1000;

    public static void animProgress(ProgressBar bar, final int progress) {
        if (bar.getAnimation() != null) {
            bar.getAnimation().cancel();
        }

        final WeakReference<ProgressBar> barRef = new WeakReference<ProgressBar>(bar);
        int startProgress = bar.getProgress();

        if (startProgress > progress) {
            startProgress = 0;
        }

        ValueAnimator animator = ValueAnimator.ofInt(startProgress, progress);
        animator.setDuration(ANIMATION_DURATION);
        animator.setEvaluator(new ArgbEvaluator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ProgressBar pb = barRef.get();
                if (pb != null) {
                    pb.setProgress((Integer) animation.getAnimatedValue());
                }
            }
        });
        animator.start();
    }

}