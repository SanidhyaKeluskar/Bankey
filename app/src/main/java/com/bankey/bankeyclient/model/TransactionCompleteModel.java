package com.bankey.bankeyclient.model;

import android.support.v4.app.FragmentActivity;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.TransactionCompleteFragment;
import com.bankey.bankeyclient.view.RateKeyDialog;

import java.text.DecimalFormat;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class TransactionCompleteModel extends FragmentModel<TransactionCompleteFragment> {

    private static final String NAME_HTML = "<font color='#4786E4'><b>%s</b></font>";

    private final float mAmount;

    public TransactionCompleteModel(float amount) {
        mAmount = amount;
    }

    @Override
    public void onViewCreated(TransactionCompleteFragment view) {
        super.onViewCreated(view);

        view.setAddedAmountText(mView.getString(R.string.transaction_complete_amount_added) + "<b> $" +
                new DecimalFormat("#0.00").format(mAmount) + "</b>");

        view.setTotalBalanceText(mView.getString(R.string.transaction_complete_total_balance) + "<b> $" +
                new DecimalFormat("#0.00").format(UserSession.instance().getUserData().mBalance.floatValue()) + "</b>");
    }

    @Override
    public void onViewDestroyed() {
        FragmentActivity activity = mView.getActivity();
        RateKeyDialog.instantiate("Fadi Abddalah").show(activity.getSupportFragmentManager(), RateKeyDialog.class.getName());

        super.onViewDestroyed();
    }

    public void onDoneClicked() {
        mView.getNavigation().navigateBack();
    }

}
