package com.bankey.bankeyclient.model;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.InputAmountFragment;
import com.bankey.bankeyclient.fragment.AddressInputFragment;
import com.bankey.bankeyclient.fragment.BankAccountFragment;
import com.bankey.bankeyclient.view.CreditCardEditText;
import com.bankey.bankeyclient.view.PhoneCodePageBinder;

import java.math.BigDecimal;

/**
 * Created by DLutskov on 3/23/2018.
 */

public class BankAccountModel extends FragmentModel<BankAccountFragment> implements PhoneCodePageBinder.Listener {

    public static final int CARD_NUMBER_COUNT = 16;
    public static final int CARD_DATE_COUNT = 4;
    public static final int CARD_CVV_COUNT = 3;

    public enum Initiator {
        DEFAULT, // Opened from settings page
        BECOME_KEY, // Part of become a key flow
        AMOUNT_INPUT, // Bank was not linked when amount inputted. Add or withdraw will be perform once bank is linked
    }

    private enum Type {
        CARD_1, // Card number
        CARD_2, // CVV and expiration date
        CODE_CONFIRMATION // Phone code confirmation
    }

    private final Initiator mInitiator;

    private UserSession.OperationType mOperationType;
    private float mAmount;

    private Type mType = Type.CARD_1;

    private String mCardNumber;
    private String mDate;
    private String mCVV;

    private boolean exitConfirmed;

    public BankAccountModel(Initiator initiator, @Nullable UserSession.OperationType type, float amount) {
        mInitiator = initiator;
        mOperationType = type;
        mAmount = amount;
    }

    @Override
    public boolean onActivityBackClicked() {
        if (exitConfirmed) {
            mView.closeKeyboard();
            return super.onActivityBackClicked();
        }

        switch (mType) {
            case CARD_1:
                if (mInitiator == Initiator.BECOME_KEY) {
                    return handleBackForBecomeKeyFlow();
                }
                break;
            case CARD_2:
                mType = Type.CARD_1;
                mView.switchToCard1();
                return true;
            case CODE_CONFIRMATION:
                mType = Type.CARD_2;
                mView.updatePhoneCodeVisibility(false);
                return true;
        }

        mView.closeKeyboard();
        return super.onActivityBackClicked();
    }

    private boolean handleBackForBecomeKeyFlow() {
        handleBack();
        return true;
    }

    private void handleBack() {
        Activity ctx = mView.getActivity();
        DialogUtils.showConfirmation(ctx, ctx.getString(R.string.warning),
                ctx.getString(R.string.process_cancel_confirmation), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exitConfirmed = true;
                        mView.getNavigation().navigateBack();
                    }
                });
    }

    public void onNextButtonClicked() {
        switch (mType) {

            case CARD_1:
                mCardNumber = mView.getCardNumber();
                if (mCardNumber.length() != CARD_NUMBER_COUNT) {
                    DialogUtils.showError(mView.getActivity(), R.string.dialog_empty_fields_error);
                    return;
                }
                mType = Type.CARD_2;
                mView.updateCardImage(CreditCardEditText.CardType.detect(mCardNumber).imageRes);
                mView.switchToCard2();
                break;
            case CARD_2:
                mDate = mView.getExpirationDate();
                mCVV = mView.getCVV();
                if (mCVV.length() != CARD_CVV_COUNT || mDate.length() != CARD_DATE_COUNT) {
                    DialogUtils.showError(mView.getActivity(), R.string.dialog_empty_fields_error);
                    return;
                }

                mType = Type.CODE_CONFIRMATION;
                mView.updatePhoneCodeVisibility(true);
                break;
        }
    }

    @Override
    public void onResendCodeClicked() {
        // TODO
    }

    @Override
    public void onVerificationInput(String verificationCode) {
        // TODO perform request
        UserSession.instance().getUserData().setBankAccountTest(); // TODO
        mView.closeKeyboard();

        switch (mInitiator) {

            case DEFAULT:
//                exitConfirmed = true;
//               mView.getNavigation().navigateBack();
                mView.getNavigation().openNextFragment(InputAmountFragment.instance(UserSession.OperationType.ADD), true);
                mView.getNavigation().removeFromHistory(BankAccountFragment.class);
                break;
            case BECOME_KEY:
                mView.getNavigation().openNextFragment(AddressInputFragment.instantiate("", "", ""), true);
                mView.getNavigation().removeFromHistory(BankAccountFragment.class);
                break;
            case AMOUNT_INPUT:
                exitConfirmed = true;
                performAmountRequest();
                mView.getNavigation().removeFromHistory(InputAmountFragment.class);
                mView.getNavigation().navigateBack();
                break;
        }
    }

    private void performAmountRequest() { // TODO
        BigDecimal amount = new BigDecimal(String.valueOf(mAmount));
        switch (mOperationType) {
            case ADD:
                UserSession.instance().getUserData().mBalance = UserSession.instance().getUserData().mBalance.add(amount);
                break;
            case WITHDRAW:
                UserSession.instance().getUserData().mBalance = UserSession.instance().getUserData().mBalance.subtract(amount);
                break;
        }
    }

}
