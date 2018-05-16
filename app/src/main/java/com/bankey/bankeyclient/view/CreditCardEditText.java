package com.bankey.bankeyclient.view;

import android.content.Context;
import android.util.AttributeSet;

import com.bankey.bankeyclient.R;

import java.util.regex.Pattern;

public class CreditCardEditText extends android.support.v7.widget.AppCompatEditText {

    private static final int INDEX_NOT_FOUND = -1;
    public static final String SEPARATOR = "-";

    private String mPreviousText;

    private boolean isLocked;

    public CreditCardEditText(Context context) {
        super(context);
    }

    public CreditCardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CreditCardEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isLocked) return;

        String text = s.toString();
        if (mPreviousText != null && text.length() > mPreviousText.length()) {
            String difference = difference(text, mPreviousText);
            if (!difference.equals(SEPARATOR)) {
                addSeparatorToText();
            }
        }
        mPreviousText = text;
    }

    private void addSeparatorToText() {
        String text = getText().toString();
        text = text.replace(SEPARATOR, "");
        if (text.length() >= 16) {
            return;
        }
        int interval = 4;
        char separator = SEPARATOR.charAt(0);

        StringBuilder stringBuilder = new StringBuilder(text);
        for (int i = 0; i < text.length() / interval; i++) {
            stringBuilder.insert(((i + 1) * interval) + i, separator);
        }
        isLocked = true;
        setText(stringBuilder.toString());
        setSelection(getText().length());
        isLocked = false;
    }

    private static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        int at = indexOfDifference(str1, str2);
        if (at == INDEX_NOT_FOUND) {
            return "";
        }
        return str2.substring(at);
    }

    private static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return INDEX_NOT_FOUND;
        }
        if (cs1 == null || cs2 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                break;
            }
        }
        if (i < cs2.length() || i < cs1.length()) {
            return i;
        }
        return INDEX_NOT_FOUND;
    }

    public enum CardType {

        UNKNOWN,
        VISA("^4[0-9]{12}(?:[0-9]{3}){0,2}$", R.drawable.visa),
        MASTERCARD("^(?:5[1-5]|2(?!2([01]|20)|7(2[1-9]|3))[2-7])\\d{14}$", R.drawable.mastercard),
        AMERICAN_EXPRESS("^3[47][0-9]{13}$", R.drawable.amex);

        private final Pattern pattern;
        public final int imageRes;

        CardType() {
            this.pattern = null;
            this.imageRes = R.drawable.creditcard;
        }

        CardType(String pattern, int imageRes) {
            this.pattern = Pattern.compile(pattern);
            this.imageRes = imageRes;
        }

        public static CardType detect(String cardNumber) {

            for (CardType cardType : CardType.values()) {
                if (null == cardType.pattern) continue;
                if (cardType.pattern.matcher(cardNumber).matches()) return cardType;
            }

            return UNKNOWN;
        }

    }
}