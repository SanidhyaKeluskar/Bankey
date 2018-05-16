package com.bankey.bankeyclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.model.InputAmountModel;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class InputAmountFragment extends AbstractFragment<InputAmountModel> {

    private static final String OPERATION_KEY = "operation";

    private TextView mTitle;
    private EditText mAmount;
    private TextView mBalance;

    private Watcher mWatcher = new Watcher();

    @Override
    InputAmountModel onCreateModel() {
        UserSession.OperationType operationType = UserSession.OperationType.values()[getArguments().getInt(OPERATION_KEY)];
        return new InputAmountModel(operationType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_money, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle = view.findViewById(R.id.header_title);
        mAmount = view.findViewById(R.id.add_money_amount);
        mBalance = view.findViewById(R.id.add_money_balance);

        mAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (getActivity() == null) {
                    return false;
                }
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        || i == EditorInfo.IME_ACTION_DONE) {
                    mModel.onNextButtonClicked();
                }
                return true;
            }
        });
        mAmount.setCursorVisible(false);
        mAmount.setLongClickable(false);
        mAmount.setBackground(null);
        mAmount.setEms(8);
        mAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        mAmount.requestFocus();
        mAmount.addTextChangedListener(mWatcher);
        mAmount.setSelection(mAmount.getText().length());

        view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.onNextButtonClicked();
            }
        });
    }

    public void setTitle(int tite) {
        mTitle.setText(tite);
    }

    public void setCurrentBalance(String balance) {
        mBalance.setText(String.format(getString(R.string.add_money_your_balance), balance));
    }

    public String getEnteredAmount() {
        return mAmount.getText().toString();
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(mAmount, 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        InputMethodManager imm = (InputMethodManager) mAmount.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mAmount.getWindowToken(), 0);
        }
    }

    private class Watcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String digits = s.toString().replaceAll("\\.", "");
            if (digits.length() < 4) {
                digits = "0" + digits;
            }
            StringBuilder result = new StringBuilder("");
            for (int i = digits.length() - 1; i >= 0; i--) {
                if (i == digits.length() - 3) {
                    result.append(".");
                }
                result.append(digits.charAt(i));
            }
            // remove unused zeros
            int substring = result.length();
            for (int i = result.length() - 1; i >= 5; i--) {
                if (result.charAt(i) == '0') {
                    substring = i;
                } else {
                    break;
                }
            }
            result = new StringBuilder(result.substring(0, substring));

            mAmount.removeTextChangedListener(this);
            mAmount.setText(result.reverse());
            mAmount.addTextChangedListener(this);
            mAmount.setSelection(result.length());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }

    public static InputAmountFragment instance(UserSession.OperationType type) {
        InputAmountFragment f = new InputAmountFragment();
        Bundle args = new Bundle();
        args.putInt(OPERATION_KEY, type.ordinal());
        f.setArguments(args);
        return f;
    }
}