package com.bankey.bankeyclient.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankey.bankeyclient.AnimationUtils;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.model.BankAccountModel;
import com.bankey.bankeyclient.view.CreditCardDateEditText;
import com.bankey.bankeyclient.view.CreditCardEditText;
import com.bankey.bankeyclient.view.PhoneCodePageBinder;

/**
 * Created by DLutskov on 3/23/2018.
 */

public class BankAccountFragment extends AbstractFragment<BankAccountModel> {

    public static final String INITIATOR_KEY = "initiator_key";
    public static final String OPERATION_KEY = "operation_key";
    public static final String AMOUNT_KEY = "amount_key";

    private static final String EXTRA_CARD_NUMBER = "extra_card";

    private View mButtonNext;
    private View mButtonBack;

    private View mCard1View;
    private EditText mCardNumberInput;

    private View mCard2View;
    private ImageView mCardTypeImage;
    private EditText mCardExpireView;
    private EditText mCardCVVView;

    private PhoneCodePageBinder mPhoneCodeBinder;

    @Override
    BankAccountModel onCreateModel() {
        BankAccountModel.Initiator initiator = BankAccountModel.Initiator.values()[getArguments().getInt(INITIATOR_KEY, 0)];
        int operationIndex = getArguments().getInt(OPERATION_KEY, -1);
        return new BankAccountModel(initiator, operationIndex == -1 ? null : UserSession.OperationType.values()[operationIndex],
                getArguments().getFloat(AMOUNT_KEY));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Card 1
        mCard1View = view.findViewById(R.id.bank_account_card_1_container);
        mCardNumberInput = view.findViewById(R.id.bank_account_card_number);
        mCardNumberInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (getActivity() == null) {
                    return false;
                }
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        || i == EditorInfo.IME_ACTION_DONE) {
                    mModel.onNextButtonClicked();
                }
                return true;
            }
        });
        mCardNumberInput.setText(getArguments().getString(EXTRA_CARD_NUMBER, ""));

        // Card 2
        mCard2View = view.findViewById(R.id.bank_account_card_2_container);
        mCardTypeImage = view.findViewById(R.id.bank_account_card_image);
        mCardExpireView = view.findViewById(R.id.bank_account_card_date);
        mCardCVVView = view.findViewById(R.id.bank_account_card_cvv);
        mCardCVVView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (getActivity() == null) {
                    return false;
                }
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        || i == EditorInfo.IME_ACTION_DONE) {
                    mModel.onNextButtonClicked();
                }
                return true;
            }
        });

        String phone = UserSession.instance().getUserData().getPhone();
        mPhoneCodeBinder = new PhoneCodePageBinder(view.findViewById(R.id.bank_account_phone_code), phone, mModel);
        mPhoneCodeBinder.getView().findViewById(R.id.header_arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigation.navigateBack();
            }
        });

        // Buttons
        mButtonBack = view.findViewById(R.id.header_arrow_back);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModel.onBackButtonClicked();
            }
        });

        mButtonNext = view.findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModel.onNextButtonClicked();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        Bundle args = getArguments();
        args.putString(EXTRA_CARD_NUMBER, mCardNumberInput.getText().toString());
    }

    public String getCardNumber() {
        return mCardNumberInput.getText().toString().replaceAll(CreditCardEditText.SEPARATOR, "");
    }

    public String getExpirationDate() {
        return mCardExpireView.getText().toString().replaceAll(CreditCardDateEditText.SEPARATOR, "");
    }

    public String getCVV() {
        return mCardCVVView.getText().toString();
    }

    public void updateCardImage(@DrawableRes int imageRes) {
        mCardTypeImage.setImageResource(imageRes);
    }

    public void switchToCard1() {
        mCard1View.setVisibility(View.VISIBLE);
        AnimatorSet set1 = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_in);
        set1.setTarget(mCard1View);
        AnimatorSet set2 = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_out);
        set2.setTarget(mCard2View);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(set1, set2);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCardNumberInput.requestFocus();
                mCard2View.setVisibility(View.INVISIBLE);
            }
        });
        set.start();
    }

    public void switchToCard2() {
        mCard2View.setVisibility(View.VISIBLE);
        AnimatorSet set1 = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_in);
        set1.setTarget(mCard2View);
        AnimatorSet set2 = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_out);
        set2.setTarget(mCard1View);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(set1, set2);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCardExpireView.requestFocus();
                mCard1View.setVisibility(View.INVISIBLE);
            }
        });
        set.start();
    }

    public void updatePhoneCodeVisibility(boolean visible) {
        final View pageView = mPhoneCodeBinder.getView();
        pageView.setVisibility(View.VISIBLE);
        if (visible) {
            AnimationUtils.getPageEnterAnimator(mPhoneCodeBinder.getView(), null, null).start();
            mPhoneCodeBinder.requestFocus();
        } else {
            AnimationUtils.getPageExitAnimator(mPhoneCodeBinder.getView(), null, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mPhoneCodeBinder.clearInput();
                    mCardCVVView.requestFocus();
                    pageView.setVisibility(View.INVISIBLE);
                }
            }).start();
        }
    }

    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mCardNumberInput.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mCardCVVView.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mCardNumberInput.getWindowToken(), 0);
            imm.hideSoftInputFromInputMethod(mPhoneCodeBinder.getInput().getWindowToken(), 0);
        }
    }

    public static BankAccountFragment instantiate(BankAccountModel.Initiator initiator) {
        return instantiate(initiator, null, 0);
    }

    public static BankAccountFragment instantiate(BankAccountModel.Initiator initiator, @Nullable UserSession.OperationType operation, float amount) {
        BankAccountFragment f = new BankAccountFragment();
        Bundle args = new Bundle();
        args.putInt(INITIATOR_KEY, initiator.ordinal());
        if (operation != null) {
            args.putInt(OPERATION_KEY, operation.ordinal());
        }
        args.putFloat(AMOUNT_KEY, amount);
        f.setArguments(args);
        return f;
    }

}
