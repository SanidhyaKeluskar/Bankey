package com.bankey.bankeyclient.model;

import android.view.View;

import com.bankey.bankeyclient.MainActivity;
import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.AccountFragment;
import com.bankey.bankeyclient.fragment.AccountListFragment;
import com.bankey.bankeyclient.fragment.InputAmountFragment;
import com.bankey.bankeyclient.fragment.KeyRequestConfirmFragment;
import com.bankey.bankeyclient.fragment.KeyRequestConfirmationCodeFragment;
import com.bankey.bankeyclient.fragment.SettingsFragment;
import com.bankey.bankeyclient.fragment.TransactionCompleteFragment;
import com.bankey.bankeyclient.fragment.TransactionPendingFragment;

import java.math.BigDecimal;

import static com.bankey.bankeyclient.model.KeyRequestConfirmModel.Type.RESPONSE;

/**
 * Created by Dima on 25.02.2018.
 */

public class AccountModel extends FragmentModel<AccountFragment> {

    private static final String KEY_REQUEST_PENDING_HTML = "<font color='#4786E4'><b>%s</b></font>";
    private static final String KEY_REQUEST_ACCEPTED_HTML = "<font color='#07EFCD'><b>%s</b></font>";

    @Override
    public void onViewCreated(AccountFragment view) {
        super.onViewCreated(view);

        view.setImageUrl(UserSession.instance().getUserData().getPhoto());
        view.setName(UserSession.instance().getUserData().getName() + " " + UserSession.instance().getUserData().getSurname());
        view.setBalance(UserSession.instance().getUserData().getBalanceString());

        updateKeyRequestState();
    }
    
    private void updateKeyRequestState() {
        int keyRequestType = UserSession.instance().keyRequestType;
        UserSession.OperationType keyRequestOperation = UserSession.instance().keyRequestOperationType;

        if (keyRequestType == 1) { // Pending
            mView.setKeyRequestButtonVisibility(View.VISIBLE);
            mView.setKeyRequestButtonColor(R.color.blue_dark);
            mView.setKeyRequestTitle(String.format(KEY_REQUEST_PENDING_HTML, mView.getString(R.string.pending))
                    + " " + mView.getString(keyRequestOperation == UserSession.OperationType.WITHDRAW ? R.string.account_key_request_withdraw : R.string.account_key_request_add) + " "
                    + String.format(KEY_REQUEST_PENDING_HTML, "Fadi Adbullah")); // TODO
        } else if (UserSession.instance().keyRequestType == 2 || UserSession.instance().keyRequestType == 3) {
            mView.setKeyRequestButtonVisibility(View.VISIBLE);
            mView.setKeyRequestButtonColor(R.color.blue_green);
            mView.setKeyRequestTitle(String.format(KEY_REQUEST_ACCEPTED_HTML, mView.getString(R.string.accepted))
                    + " " + mView.getString(keyRequestOperation == UserSession.OperationType.WITHDRAW ? R.string.account_key_request_withdraw : R.string.account_key_request_add) + " "
                    + String.format(KEY_REQUEST_PENDING_HTML, "Fadi Adbullah")); // TODO
        } else {
            mView.setKeyRequestButtonVisibility(View.GONE);
        }
    }

    public void onSettingsClick() {
        mView.getNavigation().openNextFragment(new SettingsFragment(), true);
    }

    public void onAddClicked() {
        mView.getNavigation().openNextFragment(InputAmountFragment.instance(UserSession.OperationType.ADD), true);
    }

    public void onWithdrawClicked() {
        mView.getNavigation().openNextFragment(InputAmountFragment.instance(UserSession.OperationType.WITHDRAW), true);
    }

    public void onSendClicked() {
        mView.getNavigation().openNextFragment(new AccountListFragment(), true);
    }

    public void onKeyRequestClick() {
        int requestType = UserSession.instance().keyRequestType;
        UserSession.OperationType operationType = UserSession.instance().keyRequestOperationType;

        switch (requestType) {
            case 1:
                mView.getNavigation().openNextFragment(TransactionPendingFragment.instance(UserSession.instance().keyRequestAmount, "Fadi Abdullah", operationType), true); // TODO
                break;
            case 2:
            case 3:
                String code = UserSession.instance().generatedCode;
                if (code != null) {
                    mView.getNavigation().openNextFragment(KeyRequestConfirmationCodeFragment.instance(code), true);
                } else {
                    mView.getNavigation().openNextFragment(KeyRequestConfirmFragment.instance(RESPONSE, UserSession.instance().keyRequestAmount, "Fadi Abdullah", operationType), true); // TODO
                }
                break;
        }
    }





    private MainActivity mainActivity;

    // TODO
    @Override
    public void onViewBound() {
        super.onViewBound();

        // Change status from pending to accepted
        if (UserSession.instance().keyRequestType == 1) {
            triggerPendingConfirmAction();
        }

        if (UserSession.instance().keyRequestType == 3) {
            triggerPendingComplete();
        }

        mainActivity = (MainActivity) mView.getActivity();
    }

    private void triggerPendingConfirmAction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                UserSession.instance().keyRequestType = 2;
                MainApplication.sendNotification("Accepted request from Fadi Abddalah"); // TODO

                if (mView != null && mView.getActivity() != null) {
                    mView.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateKeyRequestState();
                        }
                    });
                }
            }
        }).start();
    }


    private void triggerPendingComplete() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (UserSession.instance().keyRequestType == 0) {
                    return;
                }

                UserSession.instance().keyRequestType = 0;
//                MainApplication.sendNotification("Completed transaction with Fadi Abddalah"); // TODO
                UserSession.instance().getUserData().mBalance = UserSession.instance().getUserData().mBalance.add(
                        new BigDecimal(String.valueOf(UserSession.instance().keyRequestAmount)));

                if (!mainActivity.isFinishing()) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.getNavigation().openNextFragment(TransactionCompleteFragment.instance(UserSession.instance().keyRequestAmount), true);
                        }
                    });
                }
            }
        }).start();
    }

}
