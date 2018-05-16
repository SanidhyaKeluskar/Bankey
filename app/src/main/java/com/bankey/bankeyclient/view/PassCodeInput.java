package com.bankey.bankeyclient.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bankey.bankeyclient.R;

public class PassCodeInput extends android.support.v7.widget.AppCompatEditText {

    public interface Listener {
        void onPasscodeInput(String passcode);
    }

    private int mPinCount = 4;

    private Paint mPaint;
    private Paint mCurrentPaint;
    private Paint mFilledPaint;

    private int mIndicatorRadius;
    private int mIndicatorMargin;
    private int mStrokeWidth;

    private Listener mListener;

    public PassCodeInput(@NonNull Context context) {
        super(context);
        init();
    }

    public PassCodeInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PassCodeInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setCursorVisible(false);
        setLongClickable(false);
        setBackground(null);
        setEms(mPinCount);
        setInputType(InputType.TYPE_CLASS_NUMBER);

        mStrokeWidth = Math.round(getResources().getDisplayMetrics().density);
        mIndicatorRadius = getResources().getDimensionPixelSize(R.dimen.login_passcode_input_circle_radius);
        mIndicatorMargin = getResources().getDimensionPixelSize(R.dimen.login_passcode_input_circle_margin);

        int greyColor = ContextCompat.getColor(getContext(), R.color.grey);
        int filledColor = ContextCompat.getColor(getContext(), R.color.blue_green);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(greyColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mCurrentPaint = new Paint();
        mCurrentPaint.setAntiAlias(true);
        mCurrentPaint.setColor(filledColor);
        mCurrentPaint.setStyle(Paint.Style.STROKE);
        mCurrentPaint.setStrokeWidth(mStrokeWidth);

        mFilledPaint = new Paint();
        mFilledPaint.setAntiAlias(true);
        mFilledPaint.setColor(filledColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(params);
        }
        int width = mPinCount == 0 ? 0
                : getXPositionForIndicator(mPinCount - 1) + mIndicatorRadius + mStrokeWidth;
        int height = mIndicatorRadius * 2 + mStrokeWidth * 2;
        setMeasuredDimension(width, height);
    }

    private int getXPositionForIndicator(int indicatorPosition) {
        return mStrokeWidth + mIndicatorRadius + indicatorPosition * (mIndicatorRadius * 2 + mStrokeWidth + mIndicatorMargin);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mPinCount; i++) {
            int x = getXPositionForIndicator(i);
            int y = mStrokeWidth + mIndicatorRadius;

            int activeIndex = getText().length();
            canvas.drawCircle(x, y, mIndicatorRadius, activeIndex == i ? mCurrentPaint : i < activeIndex ? mFilledPaint : mPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() == mPinCount && mListener != null) {
            mListener.onPasscodeInput(text.toString());
        }
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        // we don't need arrow key, return null will also disable the copy/paste/cut pop-up menu.
        return null;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

}
