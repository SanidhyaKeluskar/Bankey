package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.model.TransactionPendingModel;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class TransactionPendingFragment extends AbstractFragment<TransactionPendingModel> {

    private static final String AMOUNT_KEY = "amount";
    private static final String NAME_KEY = "name";
    private static final String OPERATION_KEY = "operation";

    private TextView mTitle;
    private TextView mAmountAdded;
    private TextView mServiceCharge;
    private TextView mAddNote;

    @Override
    TransactionPendingModel onCreateModel() {
        UserSession.OperationType type = UserSession.OperationType.values()[getArguments().getInt(OPERATION_KEY)];
        return new TransactionPendingModel(getArguments().getFloat(AMOUNT_KEY), getArguments().getString(NAME_KEY), type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle = view.findViewById(R.id.transaction_pending_title);
        mAmountAdded = view.findViewById(R.id.transaction_pending_amount_added);
        mServiceCharge = view.findViewById(R.id.transaction_pending_service_charge);
        mAddNote = view.findViewById(R.id.transaction_pending_add_note);

        mServiceCharge.setText(Html.fromHtml(getString(R.string.service_charge) + MainApplication.KEY_SERVICE_FEE_HTML));

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

    public void setTitle(String text) {
        mTitle.setText(Html.fromHtml(text));
    }

    public void setAmountText(String text) {
        mAmountAdded.setText(Html.fromHtml(text));
    }

    public static TransactionPendingFragment instance(float amount, String keyName, UserSession.OperationType operationType) {
        TransactionPendingFragment f = new TransactionPendingFragment();
        Bundle args = new Bundle();
        args.putFloat(AMOUNT_KEY, amount);
        args.putString(NAME_KEY, keyName);
        args.putInt(OPERATION_KEY, operationType.ordinal());
        f.setArguments(args);
        return f;
    }
}
