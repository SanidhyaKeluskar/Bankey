package com.bankey.bankeyclient.view;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bankey.bankeyclient.R;

/**
 * Created by DLutskov on 3/23/2018.
 */

public class PhoneCodePageBinder {

    public interface Listener extends VerificationCodeInput.Listener {
        void onResendCodeClicked();
    }

    private View mRootView;
    private TextView mSubtitle;
    private TextView mBottomTitle;
    private VerificationCodeInput mInput;

    private Listener mListener;

    public PhoneCodePageBinder(View rootView, String phoneNumber, Listener listener) {
        Context ctx = rootView.getContext();
        mRootView = rootView;
        mListener = listener;
        mSubtitle = rootView.findViewById(R.id.login_enter_code_subtitle);
        mBottomTitle = rootView.findViewById(R.id.login_enter_code_bottom);
        mInput = rootView.findViewById(R.id.login_enter_code_input);

        // Subtitle
        mSubtitle.setText(String.format(ctx.getString(R.string.login_enter_code_subtitle), phoneNumber));
        // TODO Need time
        updateTimeString("00:40");

        mInput.setListener(mListener);
    }

    public View getView() {
        return mRootView;
    }

    public VerificationCodeInput getInput() {
        return mInput;
    }

    private void updateTimeString(String time) {
        // Bottom text
        String bottomText = mInput.getContext().getString(R.string.login_enter_code_bottom) + "<br>";
        mBottomTitle.setText(Html.fromHtml(bottomText +
                "<font color='#000000'>" + time + "</font>"));
    }

    public void clearInput() {
        mInput.setText("");
    }

    public void requestFocus() {
        mInput.requestFocus();
    }

}
