package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.model.AddressInputModel;

/**
 * Created by Dima on 03.03.2018.
 */

public class AddressInputFragment extends AbstractFragment<AddressInputModel> {

    private static final String EXTRA_COUNTRY = "extra_country";
    private static final String EXTRA_ADDRESS = "extra_address";
    private static final String EXTRA_CITY = "extra_city";

    private EditText mCountryInput;
    private EditText mAddressInput;
    private EditText mCityInput;
    private View mBackButton;
    private View mNextButton;

    @Override
    AddressInputModel onCreateModel() {
        return new AddressInputModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCountryInput = view.findViewById(R.id.input_address_country);
        mAddressInput = view.findViewById(R.id.input_address_line);
        mCityInput = view.findViewById(R.id.input_address_city);
        mBackButton = view.findViewById(R.id.header_arrow_back);
        mNextButton = view.findViewById(R.id.button_next);

        mCountryInput.setText(getArguments().getString(EXTRA_COUNTRY, ""));
        mAddressInput.setText(getArguments().getString(EXTRA_ADDRESS, ""));
        mCityInput.setText(getArguments().getString(EXTRA_CITY, ""));

        mCityInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModel.onBackButtonClick();
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModel.onNextButtonClicked();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        Bundle args = getArguments();
        args.putString(EXTRA_COUNTRY, mCountryInput.getText().toString());
        args.putString(EXTRA_ADDRESS, mAddressInput.getText().toString());
        args.putString(EXTRA_CITY, mCityInput.getText().toString());
    }

    public String getCountry() {
        return mCountryInput.getText().toString();
    }

    public String getAddress() {
        return mAddressInput.getText().toString();
    }

    public String getCity() {
        return mCityInput.getText().toString();
    }

    public boolean hasEmptyFields() {
        return getCountry().isEmpty() || getAddress().isEmpty() || getCity().isEmpty();
    }

    public static AddressInputFragment instantiate(String country, String address, String city) {
        Bundle args = new Bundle();
        args.putString(EXTRA_COUNTRY, country);
        args.putString(EXTRA_ADDRESS, address);
        args.putString(EXTRA_CITY, city);
        AddressInputFragment fragment = new AddressInputFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
