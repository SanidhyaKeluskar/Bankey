package com.bankey.bankeyclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bankey.bankeyclient.fragment.AbstractFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dima on 24.02.2018.
 */

public class Navigation {

    private MainActivity mActivity;
    private List<AbstractFragment> mFragmentsHistory = new ArrayList<>();

    Navigation(MainActivity activity) {
        mActivity = activity;
    }

    public AbstractFragment getCurrentFragment() {
        return mFragmentsHistory.isEmpty() ? null : mFragmentsHistory.get(mFragmentsHistory.size() - 1);
    }

    public AbstractFragment getPreviousFragmnet() {
        return mFragmentsHistory.size() < 2 ? null : mFragmentsHistory.get(mFragmentsHistory.size() - 2);
    }

    public void removeFromHistory(Class<? extends AbstractFragment> clazz) {
        for (int i = mFragmentsHistory.size() - 1; i >= 0; i--) {
            if (mFragmentsHistory.get(i).getClass().equals(clazz)) {
                mFragmentsHistory.remove(i);
            }
        }
    }

    public void clearHistory() {
        Fragment current = getCurrentFragment();
        mFragmentsHistory.clear();
        mFragmentsHistory.add((AbstractFragment) current);
    }

    public void openNextFragment(final AbstractFragment fragment, boolean animate) {
        final Fragment current = getCurrentFragment();

        mActivity.getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_fragment_container, fragment)
                .commitAllowingStateLoss();
        mActivity.getSupportFragmentManager().executePendingTransactions();
        if (animate) {
            fragment.getView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    fragment.getView().removeOnLayoutChangeListener(this);

                    View currentView = current == null ? null : current.getView();
                    Animator animator = AnimationUtils.getPageEnterAnimator(fragment.getView(), currentView, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (current != null) {
                                mActivity.getSupportFragmentManager().beginTransaction().remove(current).commitAllowingStateLoss();
                            }
                        }
                    });
                    animator.start();
                }
            });
        } else {
            if (current != null) {
                mActivity.getSupportFragmentManager().beginTransaction().remove(current).commitAllowingStateLoss();
            }
        }
        mFragmentsHistory.add(fragment);
    }

    public boolean navigateBack() {
        AbstractFragment current = getCurrentFragment();
        if (current == null) {
            return false;
        }
        if (current.onActivityBackPressed()) {
            return true;
        }
        AbstractFragment previousFragment = getPreviousFragmnet();
        if (previousFragment != null) {
            mFragmentsHistory.remove(mFragmentsHistory.size() - 1);
            Fragment previousInstance = Fragment.instantiate(mActivity, previousFragment.getClass().getName(), previousFragment.getArguments());

            // Replace instance in stack
            mFragmentsHistory.remove(mFragmentsHistory.size() - 1);
            mFragmentsHistory.add((AbstractFragment) previousInstance);

            openPreviousFragment(current, (AbstractFragment) previousInstance, true);
            return true;
        }
        return false;
    }

    public void openPreviousFragment(final AbstractFragment current, final AbstractFragment previous, boolean animate) {
        if (!animate) {
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_fragment_container, previous)
                    .commitAllowingStateLoss();
            mActivity.getSupportFragmentManager().executePendingTransactions();
            return;
        }

        mActivity.getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_fragment_container, previous)
                .commitAllowingStateLoss();
        mActivity.getSupportFragmentManager().executePendingTransactions();

        current.getView().bringToFront();

        previous.getView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                previous.getView().removeOnLayoutChangeListener(this);
                Animator exitAnimator = AnimationUtils.getPageExitAnimator(current.getView(), previous.getView(), new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mActivity.getSupportFragmentManager().beginTransaction().remove(current).commitAllowingStateLoss();
                    }
                });
                exitAnimator.start();
            }
        });
    }

}
