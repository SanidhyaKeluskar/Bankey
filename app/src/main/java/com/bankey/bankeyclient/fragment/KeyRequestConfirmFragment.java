package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.model.KeyRequestConfirmModel;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class KeyRequestConfirmFragment extends AbstractFragment<KeyRequestConfirmModel> {

    private static final String TYPE_KEY = "type";
    private static final String AMOUNT_KEY = "amount";
    private static final String NAME_KEY = "name";
    private static final String OPERTION_KEY = "operation";

    private TextView mRequestAccepted;
    private TextView mServiceCharge;
    private EditText mNotes;

    @Override
    KeyRequestConfirmModel onCreateModel() {
        KeyRequestConfirmModel.Type type = KeyRequestConfirmModel.Type.values()[getArguments().getInt(TYPE_KEY)];
        UserSession.OperationType operationType = UserSession.OperationType.values()[getArguments().getInt(OPERTION_KEY)];
        return new KeyRequestConfirmModel(type, getArguments().getFloat(AMOUNT_KEY), getArguments().getString(NAME_KEY), operationType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_key_request_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRequestAccepted = view.findViewById(R.id.key_request_confirm_request_accepted);
        mServiceCharge = view.findViewById(R.id.key_request_confirm_service_charge);
        mNotes = view.findViewById(R.id.key_request_confirm_notes);

        mServiceCharge.setText(Html.fromHtml(getString(R.string.service_charge) + MainApplication.KEY_SERVICE_FEE_HTML));

        View next = view.findViewById(R.id.button_next);
        next.findViewById(R.id.button_next_cotainer).setBackgroundResource(R.drawable.round_button_account);
        next.findViewById(R.id.button_next_arrow).setBackgroundResource(R.drawable.icon_arrow_white);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.onAcceptClicked();
            }
        });
    }

    public void setButtonText(int text) {
        ((TextView)mRootView.findViewById(R.id.button_next_text)).setText(text);
    }

    public void setTitle(String text) {
        mRequestAccepted.setText(Html.fromHtml(text));
    }

    public static KeyRequestConfirmFragment instance(KeyRequestConfirmModel.Type type, float amount, String keyName, UserSession.OperationType operationType) {
        KeyRequestConfirmFragment f = new KeyRequestConfirmFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_KEY, type.ordinal());
        args.putFloat(AMOUNT_KEY, amount);
        args.putString(NAME_KEY, keyName);
        args.putInt(OPERTION_KEY, operationType.ordinal());
        f.setArguments(args);
        return f;
    }
}
