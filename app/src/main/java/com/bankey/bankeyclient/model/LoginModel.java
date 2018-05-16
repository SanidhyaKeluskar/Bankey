package com.bankey.bankeyclient.model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.bankey.bankeyclient.DataCache;
import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.SharingUtils;
import com.bankey.bankeyclient.api.BankeyApi;
import com.bankey.bankeyclient.api.data.CountryData;
import com.bankey.bankeyclient.api.data.LoginData;
import com.bankey.bankeyclient.api.data.PhoneVerificationResponse;
import com.bankey.bankeyclient.api.data.UserData;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.AccountFragment;
import com.bankey.bankeyclient.fragment.LoginFragment;
import com.bankey.bankeyclient.tasks.AbstractBackgroundTask;
import com.bankey.bankeyclient.tasks.CountryListTask;
import com.bankey.bankeyclient.tasks.ForgetPasswordResetTask;
import com.bankey.bankeyclient.tasks.LoginTask;
import com.bankey.bankeyclient.tasks.ForgetPasswordChangeTask;
import com.bankey.bankeyclient.tasks.PhoneCodeConfirmationTask;
import com.bankey.bankeyclient.tasks.PhoneVerificationTask;
import com.bankey.bankeyclient.tasks.SignUpTask;
import com.bankey.bankeyclient.view.PersonalDetailsPageBinder;
import com.bankey.bankeyclient.view.SelectCountryDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dima on 24.02.2018.
 */

public class LoginModel extends FragmentModel<LoginFragment> implements PickupImageUtils.Listener {

    private static final int PHONE_NUMBER_MIN_DIGITS = 6;

    public enum PageType {
        PHONE,

        SING_IN,

        VERIFICATION_CODE_SIGN_UP,
        VERIFICATION_CODE_FORGET_PASSCODE,

        FORGET_PASS_CODE,
        FORGET_PASS_CODE_CONFIRMATION,

        SIGN_UP_PASS_CODE,
        SIGN_UP_PASS_CODE_CONFIRMATION,

        PERSONAL_DETAILS,
        WELCOME
    }

    private List<LoginFragment.Page> mPages = new ArrayList<>();

    private CountryData mSelectedCountry;
    private String mEnteredPhoneNumber;
    private String mEnteredPasscode;

    public LoginModel() {
        // TODO default country
        mSelectedCountry = new CountryData("United States Of America", "+1", BankeyApi.USA_FLAG);
    }

    @Override
    public void onViewCreated(LoginFragment view) {
        super.onViewCreated(view);

        PickupImageUtils.addListener(this);

        // Open phone input page
        LoginFragment.PhonePage phonePage = view.createPhonePage(PageType.PHONE);
        openPage(phonePage, false, null);
        phonePage.setPhoneCode(mSelectedCountry.getCodeValue());
        phonePage.setFlag(mSelectedCountry.getFlag());
        phonePage.showKeyboard();
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();

        PickupImageUtils.removeListener(this);
    }

    @Override
    public boolean onActivityBackClicked() {
        if (mPages.size() <= 1) {
            return false;
        }

        LoginFragment.Page currentPage = mPages.get(mPages.size() - 1);
        if (currentPage.getType() == PageType.SIGN_UP_PASS_CODE
                || currentPage.getType() == PageType.FORGET_PASS_CODE
                || currentPage.getType() == PageType.VERIFICATION_CODE_SIGN_UP
                || currentPage.getType() == PageType.VERIFICATION_CODE_FORGET_PASSCODE) {
            // Show confirmation that user wants to leave registration
            showCancelConfirmationDialog();
            return true;
        }

        mPages.remove(mPages.size() - 1);
        LoginFragment.Page previousPage = mPages.get(mPages.size() - 1);
        mView.closeCurrentPage(true, null);
        previousPage.requestFocus();

        return true;
    }

    private void showCancelConfirmationDialog() {
        Activity ctx = mView.getActivity();
        DialogUtils.showConfirmation(mView.getActivity(), ctx.getString(R.string.warning),
                ctx.getString(R.string.process_cancel_confirmation), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Return to signup
                        mPages.remove(mPages.size() - 1);
                        mView.closeCurrentPage(true, null);
                        mEnteredPasscode = null;
                    }
                });
    }

    private void openPage(LoginFragment.Page page, boolean animate, @Nullable Animator.AnimatorListener listener) {
        mPages.add(page);
        mView.openPage(page, animate, listener);
        page.requestFocus();
    }

    /////////////////// PHONE PAGE ///////////////////////
    public void onSelectCountryClicked(final LoginFragment.PhonePage page) {
        if (DataCache.instance().getCountryList() != null) {
            showSelectCountryDialog(page);
        } else {
            final AbstractBackgroundTask task = new CountryListTask();
            performRequestTask(task, new Runnable() {
                @Override
                public void run() {
                    if (task.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                        showSelectCountryDialog(page);
                    } else {
                        DialogUtils.showError(mView.getActivity(), task.getException().getMessage());
                    }
                }
            });
        }
    }

    private void showSelectCountryDialog(final LoginFragment.PhonePage page) {
        SelectCountryDialog dialog = SelectCountryDialog.instantiate(mSelectedCountry.getCodeValue(), new SelectCountryDialog.Listener() {
            @Override
            public void onCountrySelected(CountryData country) {
                mSelectedCountry = country;
                page.setPhoneCode(mSelectedCountry.getCodeValue());
                page.setFlag(mSelectedCountry.getFlag());
            }
        });
        dialog.show(mView.getActivity().getSupportFragmentManager(), SelectCountryDialog.class.getName());
    }

    public void onSignUpNextClicked(LoginFragment.PhonePage page) {
        if (page.getPhoneNumber().length() < PHONE_NUMBER_MIN_DIGITS) {
            DialogUtils.showError(mView.getActivity(), R.string.dialog_wrong_phone_number);
            return;
        }

        mEnteredPhoneNumber = mSelectedCountry.getCodeValue() + page.getPhoneNumber();

        switch (page.getType()) {
            case PHONE:
                performPhoneVerificationTask(mEnteredPhoneNumber);
                break;
        }
    }

    private void performPhoneVerificationTask(final String phoneNumber) {
        // Do phone verification request
        final AbstractBackgroundTask<PhoneVerificationResponse> task = new PhoneVerificationTask(mEnteredPhoneNumber);
        performRequestTask(task, new Runnable() {
            @Override
            public void run() {
                if (task.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                    UserData userdata = task.getResult().userdata;
                    UserSession.instance().setUserData(userdata);
                    boolean showLogin = userdata != null;
                    // Go to next page
                    // If such user is not registered - verify phone number
                    if (showLogin) {
                        openPage(mView.createSignInPage(userdata.getName() + " " + userdata.getSurname(), userdata.getPhoto()), true, null);
                    } else {
                        openPage(mView.createVerificationCodePage(PageType.VERIFICATION_CODE_SIGN_UP, phoneNumber), true, null);
                    }
                } else {
                    DialogUtils.showError(mView.getActivity(), task.getException().getMessage());
                }
            }
        });
    }

    ///////////////// PHONE VERIFICATION CODE /////////////////
    public void onVerificationCodeInputed(final LoginFragment.VerificationCodePage page, final String code) {
        // Do phone code verification request
        AbstractBackgroundTask task = null;
        PageType nextPageType = null;
        switch (page.getType()) {
            case VERIFICATION_CODE_SIGN_UP:
                task = new PhoneCodeConfirmationTask(mEnteredPhoneNumber, code, PhoneCodeConfirmationTask.Type.signup);
                nextPageType = PageType.SIGN_UP_PASS_CODE;
                break;
            case VERIFICATION_CODE_FORGET_PASSCODE:
                task = new PhoneCodeConfirmationTask(mEnteredPhoneNumber, code, PhoneCodeConfirmationTask.Type.forgot_password);
                nextPageType = PageType.FORGET_PASS_CODE;
                break;
        }

        final AbstractBackgroundTask finalTask = task;
        final PageType finalNextPageType = nextPageType;
        performRequestTask(finalTask, new Runnable() {
            @Override
            public void run() {
                if (finalTask.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                    // Go to next page - creating passcode for user
                    openPage(mView.createPassCodePage(finalNextPageType), true, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            // Remove verification code - back will lead to signup page
                            mPages.remove(page);
                            mView.removePage(page);
                        }
                    });
                } else {
                    page.clearInput();
                    DialogUtils.showError(mView.getActivity(), finalTask.getException().getMessage());
                }
            }
        });
    }

    public void onResendVerificationCodeClicked() {
        // TODO
    }

    ////////////////// PASS CODE ///////////////////
    public void onPassCodeInputed(final LoginFragment.PasscodePage page, String code) {
        switch (page.getType()) {
            case SIGN_UP_PASS_CODE:
            case FORGET_PASS_CODE:
                // Pass code was created need to confirm it entering it again
                mEnteredPasscode = code;
                PageType nextPageType = page.getType() == PageType.FORGET_PASS_CODE
                        ? PageType.FORGET_PASS_CODE_CONFIRMATION : PageType.SIGN_UP_PASS_CODE_CONFIRMATION;
                openPage(mView.createPassCodePage(nextPageType), true, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        page.clearInput();
                    }
                });
                break;
            case SIGN_UP_PASS_CODE_CONFIRMATION:
                if (!mEnteredPasscode.equals(code)) {
                    // Pass code is not equals
                    DialogUtils.showError(mView.getActivity(), R.string.wrong_passcode_confirmation);
                    page.clearInput();
                } else {
                    // Going to details page
                    openPage(mView.createPersonalDetailsPage(), true, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            // Remove passcode confirmation from history
                            mPages.remove(page);
                            mView.removePage(page);
                        }
                    });
                }
                break;
            case FORGET_PASS_CODE_CONFIRMATION:
                if (!mEnteredPasscode.equals(code)) {
                    // Pass code is not equals
                    DialogUtils.showError(mView.getActivity(), R.string.wrong_passcode_confirmation);
                    page.clearInput();
                } else {
                    // Do reset passcode request
                    String passcode = mEnteredPasscode;
                    final AbstractBackgroundTask task = new ForgetPasswordChangeTask(mEnteredPhoneNumber, passcode);
                    performRequestTask(task, new Runnable() {
                        @Override
                        public void run() {
                            if (task.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                                UserData userdata = UserSession.instance().getUserData();
                                // Open login page
                                openPage(mView.createSignInPage(userdata.getName() + " " + userdata.getSurname(), userdata.getPhoto()), true, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        // Remove all pages except initial and current
                                        for (int i = mPages.size() - 2; i >= 1; i--) {
                                            mView.removePage(mPages.remove(i));
                                        }
                                    }
                                });
                            } else {
                                DialogUtils.showError(mView.getActivity(), task.getException().getMessage());
                            }
                        }
                    });
                }
                break;
        }
    }

    //////////////// PERSONAL DETAILS //////////////////
    public void onPersonalDetailsImageClick() {
        PickupImageUtils.showAddImageDialog(mView.getActivity());
    }

    @Override
    public void onImageSelected(Uri imageUri) {
        LoginFragment.Page currentPage = mPages.get(mPages.size() - 1);
        if (currentPage.getType() == PageType.PERSONAL_DETAILS) {
            ((LoginFragment.PersonalDetailsPage)currentPage).getBinder().displayImage(imageUri);
        }
    }

    public void onPersonalDetailsNextClick(final LoginFragment.PersonalDetailsPage page) {
        if (page.getBinder().hasEmptyFields()) {
            DialogUtils.showError(mView.getActivity(), R.string.dialog_empty_fields_error);
            return;
        }

        page.closeKeyboard();

        // Do sing up request
        final PersonalDetailsPageBinder binder = page.getBinder();
        final AbstractBackgroundTask<UserData> task = new SignUpTask(mEnteredPhoneNumber, mEnteredPasscode, binder.getName(),
                binder.getSurname(), binder.getBirthday(), binder.getGender(), binder.getImageUri());
        performRequestTask(task, new Runnable() {
            @Override
            public void run() {
                if (task.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                    // TODO set data
                    // Open welcome page
                    final LoginFragment.Page welcomePage = mView.createWelcomePage(UserSession.instance().getUserData().getName());
                    openPage(welcomePage, true, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            // Clear all pages from history
                            for (int i = 0; i < mPages.size() - 2; i++) {
                                mView.removePage(mPages.get(i));
                            }
                            mPages.clear();
                            mPages.add(welcomePage);
                        }
                    });
                } else {
                    DialogUtils.showError(mView.getActivity(), task.getException().getMessage());
                }
            }
        });
    }

    //////////////////// WELCOME /////////////////////
    public void onFacebookClick() {
        SharingUtils.shareFacebook(mView.getActivity());
    }

    public void onTwitterClick() {
        SharingUtils.shareTweeter(mView.getActivity());
    }

    public void onWhatsappClick() {
        SharingUtils.shareWhatsapp(mView.getActivity());
    }

    public void onWelcomeStartClicked() {
        // Open my account page and clear login from history
        mView.getNavigation().openNextFragment(new AccountFragment(), true);
        mView.getNavigation().removeFromHistory(LoginFragment.class);
    }

    /////////////////////// SIGN IN ///////////////////////////
    public void onForgetPasswordClick() {
        Activity ctx = mView.getActivity();
        DialogUtils.showConfirmation(ctx, ctx.getString(R.string.login_forget_passcode_dialog_title),
                ctx.getString(R.string.login_forget_passcode_dialog_subtitle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final AbstractBackgroundTask task = new ForgetPasswordResetTask(mEnteredPhoneNumber);
                        performRequestTask(task, new Runnable() {
                            @Override
                            public void run() {
                                if (task.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                                    openPage(mView.createVerificationCodePage(PageType.VERIFICATION_CODE_FORGET_PASSCODE, mEnteredPhoneNumber), true, null);
                                } else {
                                    DialogUtils.showError(mView.getActivity(), task.getException().getMessage());
                                }
                            }
                        });
                    }
                });
    }

    public void onLoginPassCodeInputed(final LoginFragment.SignInPage page, final String passCode) {
        // Do login request
        final AbstractBackgroundTask<LoginData> task = new LoginTask(mEnteredPhoneNumber, passCode);
        performRequestTask(task, new Runnable() {
            @Override
            public void run() {
                if (task.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                    // Open my account page and clear login from history
                    page.closeKeyboard();
                    mView.getNavigation().openNextFragment(new AccountFragment(), true);
                    mView.getNavigation().removeFromHistory(LoginFragment.class);
                } else {
                    page.clearInput();
                    DialogUtils.showError(mView.getActivity(), task.getException().getMessage());
                }
            }
        });
    }

}
