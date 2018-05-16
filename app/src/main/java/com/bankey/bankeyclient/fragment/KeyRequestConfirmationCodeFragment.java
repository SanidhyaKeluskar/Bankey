package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.model.KeyRequestConfirmationCodeModel;

/**
 * Created by DLutskov on 4/3/2018.
 */
public class KeyRequestConfirmationCodeFragment extends AbstractFragment<KeyRequestConfirmationCodeModel> {

    private static final String CODE_KEY = "code";

    private TextView mCodeValue;

    @Override
    KeyRequestConfirmationCodeModel onCreateModel() {
        return new KeyRequestConfirmationCodeModel(getArguments().getString(CODE_KEY));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_key_request_confirmation_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCodeValue = view.findViewById(R.id.key_request_confirmation_code_code_value);

        View next = view.findViewById(R.id.button_next);
        next.findViewById(R.id.button_next_cotainer).setBackgroundResource(R.drawable.round_button_account);
        next.findViewById(R.id.button_next_arrow).setVisibility(View.GONE);
        ((TextView)next.findViewById(R.id.button_next_text)).setText(R.string.cancel);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.onCancelClicked();
            }
        });
    }

    public static KeyRequestConfirmationCodeFragment instance(String code) {
        KeyRequestConfirmationCodeFragment f = new KeyRequestConfirmationCodeFragment();
        Bundle args = new Bundle();
        args.putString(CODE_KEY, code);
        f.setArguments(args);
        return f;
    }
}
