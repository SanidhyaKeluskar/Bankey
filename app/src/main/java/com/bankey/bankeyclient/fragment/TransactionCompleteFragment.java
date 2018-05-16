package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.model.TransactionCompleteModel;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class TransactionCompleteFragment extends AbstractFragment<TransactionCompleteModel> {

    private static final String AMOUNT_KEY = "amount";

    private TextView mAmountAdded;
    private TextView mTotalBalance;
    private TextView mTransactionCode;

    @Override
    TransactionCompleteModel onCreateModel() {
        return new TransactionCompleteModel(getArguments().getFloat(AMOUNT_KEY));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_complete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAmountAdded = view.findViewById(R.id.transaction_complete_amount_added);
        mTotalBalance = view.findViewById(R.id.transaction_complete_total_balance);
        mTransactionCode = view.findViewById(R.id.transaction_complete_transaction_code);

        View next = view.findViewById(R.id.button_next);
        next.findViewById(R.id.button_next_cotainer).setBackgroundResource(R.drawable.round_button_account);
        next.findViewById(R.id.button_next_arrow).setVisibility(View.GONE);
        ((TextView)next.findViewById(R.id.button_next_text)).setText(R.string.done);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.onDoneClicked();
            }
        });
    }

    public void setAddedAmountText(String text) {
        mAmountAdded.setText(Html.fromHtml(text));
    }

    public void setTotalBalanceText(String text) {
        mTotalBalance.setText(Html.fromHtml(text));
    }

    public static TransactionCompleteFragment instance(float amount) {
        TransactionCompleteFragment f = new TransactionCompleteFragment();
        Bundle args = new Bundle();
        args.putFloat(AMOUNT_KEY, amount);
        f.setArguments(args);
        return f;
    }
}
