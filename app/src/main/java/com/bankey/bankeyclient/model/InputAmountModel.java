package com.bankey.bankeyclient.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.AbstractFragment;
import com.bankey.bankeyclient.fragment.BankAccountFragment;
import com.bankey.bankeyclient.fragment.InputAmountFragment;
import com.bankey.bankeyclient.fragment.KeyMapsFragment;

import java.math.BigDecimal;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class InputAmountModel extends FragmentModel<InputAmountFragment> {

    private UserSession.OperationType mOperation;

    public InputAmountModel(UserSession.OperationType operationType) {
        mOperation = operationType;
    }

    @Override
    public void onViewCreated(InputAmountFragment view) {
        super.onViewCreated(view);

        view.setCurrentBalance(UserSession.instance().getUserData().getBalanceString());
        view.showKeyboard();
        view.setTitle(mOperation == UserSession.OperationType.WITHDRAW ? R.string.withdraw_money : R.string.add_money);
    }

    public void onNextButtonClicked() {
        final BigDecimal amount = new BigDecimal(mView.getEnteredAmount());

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            DialogUtils.showError(mView.getActivity(), R.string.dialog_enter_valid_amount);
            return;
        }

        if (mOperation == UserSession.OperationType.WITHDRAW && amount.compareTo(UserSession.instance().getUserData().getBalance()) > 0) {
            DialogUtils.showError(mView.getActivity(), R.string.dialog_not_enough_balance);
            return;
        }

        showAmountMethodDialog(mView.getActivity(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (UserSession.instance().getUserData().isBankAccount) {
                    // TODO Perform add balance request
                    switch (mOperation) {
                        case ADD:
                            UserSession.instance().getUserData().mBalance = UserSession.instance().getUserData().mBalance.add(amount);
                            break;
                        case WITHDRAW:
                            UserSession.instance().getUserData().mBalance = UserSession.instance().getUserData().mBalance.subtract(amount);
                            break;
                    }
                    mView.getNavigation().navigateBack();
                } else {
                    // Link bank account cause it's not linked
                    mView.getNavigation().openNextFragment(BankAccountFragment.instantiate(BankAccountModel.Initiator.AMOUNT_INPUT, mOperation, amount.floatValue()), true);
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mView.getNavigation().openNextFragment(KeyMapsFragment.instance(amount.floatValue(), mOperation), true);
            }
        });
    }

    private void showAmountMethodDialog(FragmentActivity activity, final DialogInterface.OnClickListener bank, final DialogInterface.OnClickListener cash) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(activity.getString(R.string.warning));
        dialog.setMessage(activity.getString(R.string.dialog_money_select_method));
        dialog.setNegativeButton(R.string.dialog_money_bank, bank);
        dialog.setPositiveButton(R.string.dialog_money_cash, cash);
        dialog.setNeutralButton(R.string.cancel, null);
        dialog.show();
    }

}
