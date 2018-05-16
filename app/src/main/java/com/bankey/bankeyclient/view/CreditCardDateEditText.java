package com.bankey.bankeyclient.view;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * Created by DLutskov on 3/23/2018.
 */

public class CreditCardDateEditText extends android.support.v7.widget.AppCompatEditText {

    public static final String SEPARATOR = "/";

    private boolean isLocked;

    public CreditCardDateEditText(Context context) {
        super(context);
    }

    public CreditCardDateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CreditCardDateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (isLocked) return;

        String textString = text.toString();

        // Add '0' sign before first digit of month if it's more than 0 or 1
        if (lengthBefore < lengthAfter && text.length() == 1 && !(textString.charAt(0) == '0' || textString.charAt(0) == '1')) {
            isLocked = true;
            setText("0" + textString);
            setSelection(2);
            isLocked = false;
            return;
        }

        // Month is invalid
        if (lengthBefore < lengthAfter && text.length() == 2) {
            if ((text.charAt(0) == '0' && text.charAt(1) == '0')
                    || (text.charAt(0) == '1' && !(text.charAt(1) == '0' || text.charAt(1) == '1' || text.charAt(1) == '2'))) {
                isLocked = true;
                setText(text.subSequence(0, 1));
                setSelection(1);
                isLocked = false;
                return;
            }
        }

        // Append separator
        if (text.length() == 2 && lengthBefore < lengthAfter) {
            isLocked = true;
            setText(text + SEPARATOR);
            setSelection(3);
            isLocked = false;
        }

        // Skip separator and remove second digit
        if (lengthBefore > lengthAfter && text.length() == 2) {
            isLocked = true;
            setText(text.subSequence(0, 1));
            setSelection(1);
            isLocked = false;
        }

        String yearString = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 2000);
        int yearDigit1 = Integer.valueOf(yearString.substring(0,1));
        int yearDigit2 = Integer.valueOf(yearString.substring(1,2));

        // First digit year
        if (text.length() == 4 && Integer.parseInt(textString.substring(3,4)) < yearDigit1) {
            isLocked = true;
            setText(text.subSequence(0, 3));
            setSelection(3);
            isLocked = false;
        }

        // Second digit year
        if (text.length() == 5 && Integer.parseInt(textString.substring(3,4)) == yearDigit1 && Integer.parseInt(textString.substring(4,5)) < yearDigit2) {
            isLocked = true;
            setText(text.subSequence(0, 4));
            setSelection(4);
            isLocked = false;
        }

    }
}
