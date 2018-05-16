package com.bankey.bankeyclient.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DLutskov on 12/15/2017.
 */

public class TutorialRecycler extends RecyclerView {

    private LinearLayoutManager mLayoutManager;

    private PagesIndicator mIndicator;

    private final RecyclerView.OnScrollListener mScoreboardRecyclerScroll = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (getChildCount() == 0) return;

            int firstVisiblePosition = mLayoutManager.findFirstVisibleItemPosition();
            if (firstVisiblePosition == RecyclerView.NO_POSITION) {
                return;
            }

            float offsetRatio = - (float) getChildAt(0).getLeft() / (float) getWidth();

            if (mIndicator != null) {
                mIndicator.onPageScrolled(firstVisiblePosition, offsetRatio);
            }
        }
    };

    public TutorialRecycler(Context context) {
        super(context);
        init();
    }

    public TutorialRecycler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TutorialRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // Scoreboard recycler
        setOnFlingListener(null);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(this);
        mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addOnScrollListener(mScoreboardRecyclerScroll);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeOnScrollListener(mScoreboardRecyclerScroll);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mIndicator != null) mIndicator.setVisibility(visibility);
    }

    public void attachPagesIndicator(PagesIndicator indicator) {
        mIndicator = indicator;
    }

}
