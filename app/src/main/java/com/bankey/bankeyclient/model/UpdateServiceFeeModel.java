package com.bankey.bankeyclient.model;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.InputAmountFragment;
import com.bankey.bankeyclient.fragment.AddressInputFragment;
import com.bankey.bankeyclient.fragment.BankAccountFragment;
import com.bankey.bankeyclient.fragment.UpdateServiceFeeFragment;
import com.bankey.bankeyclient.tasks.AbstractBackgroundTask;
import com.bankey.bankeyclient.tasks.BecomeKeyTask;
import com.bankey.bankeyclient.tasks.UpdateServiceFeeTask;

/**
 * Created by Dima on 02.03.2018.
 */

public class UpdateServiceFeeModel extends FragmentModel<UpdateServiceFeeFragment> {

    public void onBackButtonClick() {
        mView.getNavigation().navigateBack();
    }

    public void onNextButtonClick() {
        if (mView.isBecomeKeyFlow()) {
            handleNextForBecomeKeyFlow();
        } else {
            final UpdateServiceFeeTask task = new UpdateServiceFeeTask();
            performRequestTask(task, new Runnable() {
                @Override
                public void run() {
                    if (task.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                        // Save fields TODO
                        // Move back
                        mView.getNavigation().navigateBack();
                    } else {
                        DialogUtils.showError(mView.getActivity(), task.getException().getMessage());
                    }
                }
            });
        }
    }

    // Need to add balance
    private void handleNextForBecomeKeyFlow() {
        Activity ctx = mView.getActivity();
        performBecomeKeyRequest();
    }

    private void performBecomeKeyRequest() {
        final AbstractBackgroundTask task = new BecomeKeyTask();
        performRequestTask(task, new Runnable() {
            @Override
            public void run() {
                if (task.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                    // Save fields TODO

                    FragmentActivity ctx = mView.getActivity();
                    if (UserSession.instance().getUserData().getBalance().compareTo(MainApplication.MIN_BALANCE_FOR_KEY) < 0) {
                        DialogUtils.showConfirmation(ctx, ctx.getString(R.string.dialog_add_balance_title),
                                ctx.getString(R.string.dialog_add_balance_description),
                                ctx.getString(R.string.yes),
                                ctx.getString(R.string.later),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mView.getNavigation().openNextFragment(InputAmountFragment.instance(UserSession.OperationType.ADD), true);
                                        mView.getNavigation().removeFromHistory(UpdateServiceFeeFragment.class);
                                        mView.getNavigation().removeFromHistory(BankAccountFragment.class);
                                        mView.getNavigation().removeFromHistory(AddressInputFragment.class);
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mView.getNavigation().removeFromHistory(BankAccountFragment.class);
                                        mView.getNavigation().removeFromHistory(AddressInputFragment.class);
                                        mView.getNavigation().navigateBack();
                                    }
                                });
                    }
                } else {
                    DialogUtils.showError(mView.getActivity(), task.getException().getMessage());
                }
            }
        });
    }

}
