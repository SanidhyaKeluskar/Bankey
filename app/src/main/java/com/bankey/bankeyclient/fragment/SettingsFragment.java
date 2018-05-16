package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.model.SettingsModel;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Dima on 25.02.2018.
 */

public class SettingsFragment extends AbstractFragment<SettingsModel> implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private View mBackView;
    private View mLogoutView;

    private ImageView mImageView;
    private TextView mNameView;
    
    private View mEditProfileView;
    private View mShareView;
    private View mServiceFeeView;
    private View mTurnKeyServiceView;
    private CheckBox mTurnKeyServiceCheckBox;
    private View mBecomeKeyView;
    private View mChangeCurrencyView;
    private View mGpsView;
    private CheckBox mGpsCheckBox;
    private View mLinkBankView;
    private View mTACView;
    private View mPrivacyPolicyView;

    @Override
    SettingsModel onCreateModel() {
        return new SettingsModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBackView = view.findViewById(R.id.settings_back);
        mLogoutView = view.findViewById(R.id.settings_logout);

        mImageView = view.findViewById(R.id.settings_image);
        mNameView = view.findViewById(R.id.settings_name);

        mEditProfileView = view.findViewById(R.id.settings_edit_profile);
        mShareView = view.findViewById(R.id.settings_share_friends);
        mServiceFeeView = view.findViewById(R.id.settings_service_fee);
        mTurnKeyServiceView = view.findViewById(R.id.settings_turn_key_service);
        mTurnKeyServiceCheckBox = view.findViewById(R.id.settings_key_service_checkbox);
        mBecomeKeyView = view.findViewById(R.id.settings_become_key);
        mChangeCurrencyView = view.findViewById(R.id.settings_change_currency);
        mGpsView = view.findViewById(R.id.settings_gps);
        mGpsCheckBox = view.findViewById(R.id.settings_gps_checkbox);
        mLinkBankView = view.findViewById(R.id.settings_link_bank);
        mTACView = view.findViewById(R.id.settings_tac);
        mPrivacyPolicyView = view.findViewById(R.id.settings_privacy_policy);

        // Show/Hide key service views
        boolean isKeyServiceAdded = UserSession.instance().getUserData().isTeller();
        // Hide not key vies
        mBecomeKeyView.setVisibility(isKeyServiceAdded ? View.GONE : View.VISIBLE);
        mChangeCurrencyView.setVisibility(isKeyServiceAdded ? View.GONE : View.VISIBLE);
        mGpsView.setVisibility(isKeyServiceAdded ? View.GONE : View.VISIBLE);
        // Show key views
        mServiceFeeView.setVisibility(isKeyServiceAdded ? View.VISIBLE : View.GONE);
        mTurnKeyServiceView.setVisibility(isKeyServiceAdded ? View.VISIBLE : View.GONE);

        mLinkBankView.setVisibility(UserSession.instance().getUserData().isBankAccount ? View.GONE : View.VISIBLE);

        mBackView.setOnClickListener(this);
        mLogoutView.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mEditProfileView.setOnClickListener(this);
        mShareView.setOnClickListener(this);
        mServiceFeeView.setOnClickListener(this);
        mTurnKeyServiceView.setOnClickListener(this);
        mBecomeKeyView.setOnClickListener(this);
        mChangeCurrencyView.setOnClickListener(this);
        mGpsView.setOnClickListener(this);
        mLinkBankView.setOnClickListener(this);
        mTACView.setOnClickListener(this);
        mPrivacyPolicyView.setOnClickListener(this);

        mGpsCheckBox.setOnCheckedChangeListener(this);
        mTurnKeyServiceCheckBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == mBackView.getId()) {
            mModel.onBackButtonClicked();
        } else if (view.getId() == mLogoutView.getId()) {
            mModel.onLogoutClicked();
        } else if (view.getId() == mEditProfileView.getId()) {
            mModel.onEditProfileClicked();
        } else if (view.getId() == mShareView.getId()) {
            mModel.onShareFriendsClicked();
        } else if (view.getId() == mBecomeKeyView.getId()) {
            mModel.onBecomeKeyClicked();
        } else if (view.getId() == mServiceFeeView.getId()) {
            mModel.onUpdateServiceFeeClicked();
        } else if (view.getId() == mLinkBankView.getId()) {
            mModel.onLinkBankAccountClick();
        }

        else if (view.getId() == mGpsView.getId()) {
            mGpsCheckBox.toggle();
        } else if (view.getId() == mTurnKeyServiceView.getId()) {
            mTurnKeyServiceCheckBox.toggle();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == mGpsCheckBox.getId()) {
            mModel.onGpsChecked(b);
        } else if (compoundButton.getId() == mTurnKeyServiceCheckBox.getId()) {
            mModel.onKeyServiceChecked(b);
        }
    }

    public void setImage(String imageUrl) {
        ImageLoader.getInstance().displayImage(imageUrl, mImageView);
    }

    public void setName(String name) {
        mNameView.setText(name);
    }

    public void setGpsChecked(boolean checked) {
        mGpsCheckBox.setOnCheckedChangeListener(null);
        mGpsCheckBox.setChecked(checked);
        mGpsCheckBox.setOnCheckedChangeListener(this);
    }

    public void setKeyServiceChecked(boolean checked) {
        mTurnKeyServiceCheckBox.setOnCheckedChangeListener(null);
        mTurnKeyServiceCheckBox.setChecked(checked);
        mTurnKeyServiceCheckBox.setOnCheckedChangeListener(this);
    }
}
