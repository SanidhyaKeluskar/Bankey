package com.bankey.bankeyclient.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.bankey.bankeyclient.R;

/**
 * Created by DLutskov on 12/14/2017.
 */

public class PagesIndicator extends android.support.v7.widget.AppCompatTextView {

    private boolean showIndicatorForSinglePage;

    private Paint mPaint;
    private Paint mFilledPaint;
    private int mIndicatorRadius;
    private int mIndicatorMargin;
    private int mStrokeWidth;

    private int mPageCount;

    private SparseArray<Float> mSelectedPages = new SparseArray<>();

    public PagesIndicator(@NonNull Context context) {
        super(context);
        init();
    }

    public PagesIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagesIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mStrokeWidth = Math.round(getResources().getDisplayMetrics().density);
        mIndicatorRadius = getResources().getDimensionPixelSize(R.dimen.tutorial_indicator_radius);
        mIndicatorMargin = getResources().getDimensionPixelSize(R.dimen.tutorial_indicator_margin);

        int defaultColor = ContextCompat.getColor(getContext(), R.color.blue_green);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.grey));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mFilledPaint = new Paint();
        mFilledPaint.setAntiAlias(true);
        mFilledPaint.setColor(defaultColor);
    }

    public void update(int pageCount) {
        if (mPageCount != pageCount) {
            mPageCount = pageCount;
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = mIndicatorRadius * 2 + mStrokeWidth * 2;
            params.width = mPageCount == 0 ? 0
                    : getXPositionForIndicator(mPageCount - 1) + mIndicatorRadius + mStrokeWidth;
            requestLayout();
        }
    }

    public void onPageSelected(int pageIndex) {
        mSelectedPages.clear();
        mSelectedPages.put(pageIndex, 1f);
    }

    public void onPageScrolled(int leftPosition, float leftOffset) {
        mSelectedPages.clear();
        mSelectedPages.put(leftPosition, 1 - leftOffset);
        mSelectedPages.put(leftPosition == mPageCount - 1 ? 0 : leftPosition + 1, leftOffset);
        invalidate();
    }

    public void setColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    private int getXPositionForIndicator(int indicatorPosition) {
        return mStrokeWidth + mIndicatorRadius + indicatorPosition * (mIndicatorRadius * 2 + mStrokeWidth + mIndicatorMargin);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!showIndicatorForSinglePage && mPageCount == 1) {
            return;
        }

        for (int i = 0; i < mPageCount; i++) {
            int x = getXPositionForIndicator(i);
            int y = mStrokeWidth + mIndicatorRadius;
            canvas.drawCircle(x, y, mIndicatorRadius, mPaint);

            Float currentIndicatorAlpha = mSelectedPages.get(i);
            if (currentIndicatorAlpha != null) {
                mFilledPaint.setAlpha((int) (255 * currentIndicatorAlpha));
                canvas.drawCircle(x, y, mIndicatorRadius, mFilledPaint);
            }
        }
    }
}
