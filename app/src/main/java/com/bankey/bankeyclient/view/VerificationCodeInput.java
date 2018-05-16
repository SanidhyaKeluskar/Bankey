package com.bankey.bankeyclient.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bankey.bankeyclient.R;


public class VerificationCodeInput extends android.support.v7.widget.AppCompatEditText {

    public interface Listener {
        void onVerificationInput(String verificationCode);
    }

    private int mCodeCount = 6;

    private Paint mPaint;
    private Paint mFilledPaint;
    private Paint mTextPaint;

    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorMargin;
    private int mIndicatorMarginBottom;
    private int mTextSize;

    private Listener mListener;

    public VerificationCodeInput(@NonNull Context context) {
        super(context);
        init();
    }

    public VerificationCodeInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerificationCodeInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setCursorVisible(false);
        setLongClickable(false);
        setBackground(null);
        setEms(mCodeCount);
        setInputType(InputType.TYPE_CLASS_NUMBER);

        mIndicatorWidth = getResources().getDimensionPixelSize(R.dimen.login_verification_digit_width);
        mIndicatorHeight = getResources().getDimensionPixelOffset(R.dimen.login_verification_digit_height);
        mIndicatorMargin = getResources().getDimensionPixelSize(R.dimen.login_verification_digit_margin);
        mIndicatorMarginBottom = getResources().getDimensionPixelOffset(R.dimen.login_verification_margin_digit_bottom);
        mTextSize = getResources().getDimensionPixelSize(R.dimen.login_verification_text_size);

        int greyColor = ContextCompat.getColor(getContext(), R.color.grey);
        int filledColor = ContextCompat.getColor(getContext(), R.color.blue_dark);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(greyColor);

        mFilledPaint = new Paint();
        mFilledPaint.setAntiAlias(true);
        mFilledPaint.setColor(filledColor);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(Math.round(getResources().getDisplayMetrics().density) * 18);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(params);
        }
        int width = mIndicatorWidth * mCodeCount + mIndicatorMargin * (mCodeCount - 1);
        int height = mIndicatorHeight + mIndicatorMarginBottom + mTextSize;
        setMeasuredDimension(width, height);
    }

    private int getXPositionForIndicator(int indicatorPosition) {
        return mIndicatorWidth * indicatorPosition + mIndicatorMargin * indicatorPosition;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mCodeCount; i++) {
            int x = getXPositionForIndicator(i);
            int y = (int) (getHeight() * 0.5f - mIndicatorHeight * 0.5f);

            int activeIndex = getText().length();

            if (i >= activeIndex) {
                Paint paint = i <= activeIndex ? mFilledPaint : mPaint;
                canvas.drawRect(x, y, x + mIndicatorWidth, y + mIndicatorHeight, paint);
            }
            if (i < getText().length()) {
                int xText = (int) (x + mTextSize * 0.3f);
                int yText = (int) (getHeight() * 0.5f + mTextSize * 0.25f);
                canvas.drawText(String.valueOf(getText().charAt(i)), xText, yText, mTextPaint);
            }
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() == mCodeCount && mListener != null) {
            mListener.onVerificationInput(text.toString());
        }
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        // we don't need arrow key, return null will also disable the copy/paste/cut pop-up menu.
        return null;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        setSelection(this.length());
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

}