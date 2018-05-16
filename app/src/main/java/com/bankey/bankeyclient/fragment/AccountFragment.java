package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.model.AccountModel;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Dima on 24.02.2018.
 */

public class AccountFragment extends AbstractFragment<AccountModel> implements View.OnClickListener {

    private View mBtnSettings;

    private TextView mNameView;
    private ImageView mImageView;

    private TextView mBalanceView;
    private TextView mCurrencyView;

    private View mMessagesView;
    private View mTransactionsView;

    private View mKeyRequestView;
    private View mKeyRequestLeft, mKeyRequestRight;
    private TextView mKeyRequestTitle;

    private View mBtnWithdraw;
    private View mBtnSend;
    private View mBtnAdd;

    @Override
    AccountModel onCreateModel() {
        return new AccountModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnSettings = view.findViewById(R.id.account_settings);

        mNameView = view.findViewById(R.id.account_name);
        mImageView = view.findViewById(R.id.account_image);

        mBalanceView = view.findViewById(R.id.account_balance);
        mCurrencyView = view.findViewById(R.id.account_currency_sign);

        mMessagesView = view.findViewById(R.id.account_messages);
        mTransactionsView = view.findViewById(R.id.account_transactions);

        mKeyRequestView = view.findViewById(R.id.account_key_request_container);
        mKeyRequestLeft = view.findViewById(R.id.account_key_request_container_left);
        mKeyRequestRight = view.findViewById(R.id.account_key_request_container_right);
        mKeyRequestTitle = view.findViewById(R.id.account_key_request_text);

        mBtnWithdraw = view.findViewById(R.id.account_withdraw);
        mBtnSend = view.findViewById(R.id.account_send);
        mBtnAdd = view.findViewById(R.id.account_add);

        mBtnSettings.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mMessagesView.setOnClickListener(this);
        mTransactionsView.setOnClickListener(this);
        mKeyRequestView.setOnClickListener(this);
        mBtnWithdraw.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnSettings) {
            mModel.onSettingsClick();
        } else if (view == mBtnAdd) {
            mModel.onAddClicked();
        } else if (view == mBtnWithdraw) {
            mModel.onWithdrawClicked();
        } else if (view == mBtnSend) {
            mModel.onSendClicked();
        } if (view == mKeyRequestView) {
            mModel.onKeyRequestClick();
        }
    }

    public void setName(String name) {
        mNameView.setText(name);
    }

    public void setBalance(String balance) {
        mBalanceView.setText(balance);
    }

    public void setImageUrl(String imageUrl) {
        if (imageUrl != null) {
            ImageLoader.getInstance().displayImage(imageUrl, mImageView);
        }
    }

    public void setKeyRequestButtonVisibility(int visibility) {
        mKeyRequestView.setVisibility(visibility);
    }

    public void setKeyRequestButtonColor(@ColorRes int color) {
        int colorInt = ContextCompat.getColor(getActivity(), color);
        mKeyRequestLeft.setBackgroundColor(colorInt);
        mKeyRequestRight.setBackgroundColor(colorInt);
    }

    public void setKeyRequestTitle(String title) {
        mKeyRequestTitle.setText(Html.fromHtml(title));
    }
}