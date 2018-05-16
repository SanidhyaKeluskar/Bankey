package com.bankey.bankeyclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dima on 02.03.2018.
 */

public class AnimationUtils {

    private static final float PARALAX_PAGE_RATIO = 0.3f;
    private static final int PAGE_ANIM_DURATION = 300;

    public static final int PAGE_SHADOW_WIDTH = MainApplication.instance().getResources()
            .getDimensionPixelSize(R.dimen.page_shadow_width);

    private static boolean isPageAnimated = false;

    private static Animator.AnimatorListener mLockAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            isPageAnimated = true;
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            isPageAnimated = false;
        }
    };

    public static Animator getPageEnterAnimator(View pageView, @Nullable View previousView, @Nullable Animator.AnimatorListener listener) {
        final View shadowView = pageView.findViewById(R.id.page_shadow);

        AnimatorSet animator = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        animators.add(ObjectAnimator.ofFloat(pageView, "translationX", pageView.getWidth(), 0));
        if (previousView != null) {
            animators.add(ObjectAnimator.ofFloat(previousView, "translationX",
                    0, -previousView.getWidth() * PARALAX_PAGE_RATIO));
        }
        if (shadowView != null) {
            animators.add(ObjectAnimator.ofFloat(shadowView, "translationX", 0, -PAGE_SHADOW_WIDTH));
        }
        animator.playTogether(animators);
        animator.setDuration(PAGE_ANIM_DURATION);
        animator.addListener(mLockAnimatorListener);
        if (listener != null) {
            animator.addListener(listener);
        }
        return animator;
    }

    public static Animator getPageExitAnimator(View pageView, @Nullable View previousView, @Nullable Animator.AnimatorListener listener) {
        final View shadowView = pageView.findViewById(R.id.page_shadow);
        final View previousShadow = previousView == null ? null : previousView.findViewById(R.id.page_shadow);
        if (previousShadow != null) {
            previousShadow.setTranslationX(-PAGE_SHADOW_WIDTH);
        }

        AnimatorSet animator = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        animators.add(ObjectAnimator.ofFloat(pageView, "translationX", 0, pageView.getWidth()));
        if (previousView != null) {
            animators.add(ObjectAnimator.ofFloat(previousView, "translationX",
                    -previousView.getWidth() * PARALAX_PAGE_RATIO, 0));
        }
        if (shadowView != null) {
            animators.add(ObjectAnimator.ofFloat(shadowView, "translationX", -PAGE_SHADOW_WIDTH, 0));
        }
        animator.playTogether(animators);
        animator.setDuration(PAGE_ANIM_DURATION);
        animator.addListener(mLockAnimatorListener);
        if (listener != null) {
            animator.addListener(listener);
        }
        return animator;
    }

    public static boolean isPageAnimated() {
        return isPageAnimated;
    }

}
