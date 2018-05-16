package com.bankey.bankeyclient.model;

import android.content.DialogInterface;

import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.TransactionPendingFragment;

import java.text.DecimalFormat;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class TransactionPendingModel extends FragmentModel<TransactionPendingFragment> {

    private static final String NAME_HTML = "<font color='#4786E4'><b>%s</b></font>";

    private final float mAmount;
    private final String mName;
    private final UserSession.OperationType mOperationType;

    public TransactionPendingModel(float amount, String name, UserSession.OperationType operationType) {
        mAmount = amount;
        mName = name;
        mOperationType = operationType;
    }

    @Override
    public void onViewCreated(TransactionPendingFragment view) {
        super.onViewCreated(view);

        view.setTitle(String.format(view.getString(R.string.transaction_pending_title), String.format(NAME_HTML, mName)));
        view.setAmountText(mView.getString(mOperationType == UserSession.OperationType.WITHDRAW ? R.string.transaction_pending_amount_withdrawn : R.string.transaction_pending_amount_added) + "<b> $" +
                new DecimalFormat("#0.00").format(mAmount) + "</b>");
    }

    public void onCancelClicked() {
        DialogUtils.showConfirmation(mView.getActivity(), mView.getString(R.string.dialog_cancel_request),
                mView.getString(R.string.dialog_cancel_request_desc), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO cancel request
                        UserSession.instance().keyRequestType = 0;

                        mView.getNavigation().navigateBack();
                    }
                });
    }

}
