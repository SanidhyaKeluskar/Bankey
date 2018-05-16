package com.bankey.bankeyclient.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankey.bankeyclient.AnimationUtils;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.model.LoginModel;
import com.bankey.bankeyclient.view.PassCodeInput;
import com.bankey.bankeyclient.view.PersonalDetailsPageBinder;
import com.bankey.bankeyclient.view.PhoneCodePageBinder;
import com.nostra13.universalimageloader.core.ImageLoader;

import static com.bankey.bankeyclient.model.LoginModel.PageType.FORGET_PASS_CODE_CONFIRMATION;
import static com.bankey.bankeyclient.model.LoginModel.PageType.SIGN_UP_PASS_CODE_CONFIRMATION;

/**
 * Created by Dima on 24.02.2018.
 */

public class LoginFragment extends AbstractFragment<LoginModel> {

    private ViewGroup mRootView;

    @Override
    LoginModel onCreateModel() {
        return new LoginModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = new FrameLayout(getActivity());
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRootView.setClipChildren(false);
        mRootView.setClipToPadding(false);
        return mRootView;
    }

    public PhonePage createPhonePage(LoginModel.PageType type) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_login_phone, mRootView, false);
        return new PhonePage(view, mModel, type);
    }

    public VerificationCodePage createVerificationCodePage(LoginModel.PageType type, String phoneNumber) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_login_enter_code, mRootView, false);
        return new VerificationCodePage(view, mModel, type, phoneNumber);
    }

    public PasscodePage createPassCodePage(LoginModel.PageType pageType) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_login_passcode, mRootView, false);
        return new PasscodePage(view, mModel, pageType);
    }

    public PersonalDetailsPage createPersonalDetailsPage() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_personal_details, mRootView, false);
        return new PersonalDetailsPage(view, mModel);
    }

    public WelcomePage createWelcomePage(String userName) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_login_welcome, mRootView, false);
        return new WelcomePage(view, mModel, userName);
    }

    public SignInPage createSignInPage(String username, @Nullable String imageUrl) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_login_sign_in, mRootView, false);
        return new SignInPage(view, mModel, username, imageUrl);
    }

    public void openPage(Page page, boolean animate, @Nullable final Animator.AnimatorListener listener) {
        final View currentView = mRootView.getChildCount() == 0 ? null : mRootView.getChildAt(mRootView.getChildCount() - 1);
        final View pageView = page.mView;
        View shadowView = pageView.findViewById(R.id.page_shadow);

        mRootView.addView(pageView);
        if (animate) {
            mRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    mRootView.removeOnLayoutChangeListener(this);
                    Animator animator = AnimationUtils.getPageEnterAnimator(pageView, currentView, listener);
                    animator.start();
                }
            });
        } else {
            pageView.setTranslationX(-AnimationUtils.PAGE_SHADOW_WIDTH);
            if (shadowView != null) shadowView.setTranslationX(-AnimationUtils.PAGE_SHADOW_WIDTH);
        }
    }

    public void closeCurrentPage(boolean animate, @Nullable Animator.AnimatorListener listener) {
        final View previousView = mRootView.getChildCount() == 1 ? null : mRootView.getChildAt(mRootView.getChildCount() - 2);
        final View pageView = mRootView.getChildAt(mRootView.getChildCount() - 1);
        final View shadowView = pageView.findViewById(R.id.page_shadow);

        if (animate) {
            Animator animator = AnimationUtils.getPageExitAnimator(pageView, previousView, listener);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mRootView.removeView(pageView);
                }
            });
            animator.start();
        } else {
            mRootView.removeView(pageView);
            if (previousView != null) {
                previousView.setTranslationX(-AnimationUtils.PAGE_SHADOW_WIDTH);
            }
        }
    }

    public void removePage(Page page) {
        mRootView.removeView(page.mView);
    }

    ////////////////////// INTERNAL PAGES ////////////////////////
    public static abstract class Page {

        final View mView;
        final LoginModel mModel;

        Page(View view, LoginModel model) {
            mView = view;
            mModel = model;
            View backArrow = view.findViewById(R.id.header_arrow_back);
            if (backArrow != null) {
                backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mModel.onBackButtonClicked();
                    }
                });
            }
        }

        public abstract LoginModel.PageType getType();
        public abstract void requestFocus();
    }

    public class PhonePage extends Page implements View.OnClickListener {
        
        private View mNextButton;
        private View mCountryContainer;
        private ImageView mCountryFlag;
        private TextView mCountryCodeTitle;
        private TextView mCountryCodeValue;
        private EditText mPhoneNumberEdit;
        private TextView mTACText;

        private LoginModel.PageType mType;

        PhonePage(View rootView, LoginModel model, LoginModel.PageType type) {
            super(rootView, model);
            mType = type;

            mNextButton = rootView.findViewById(R.id.button_next);
            mCountryContainer = rootView.findViewById(R.id.login_signup_phone_code_container);
            mCountryFlag = rootView.findViewById(R.id.login_signup_flag);
            mCountryCodeTitle = rootView.findViewById(R.id.login_signup_code_title);
            mCountryCodeValue = rootView.findViewById(R.id.login_signup_phone_code);
            mPhoneNumberEdit = rootView.findViewById(R.id.login_signup_edit_phone);
            mTACText = rootView.findViewById(R.id.login_signup_tac);

            // TAC Bottom
            String tacText = getString(R.string.login_sign_up_terms);
            String tac = getString(R.string.terms_ans_conditions);
            mTACText.setText(String.format(tacText, tac));
            // TODO tac click

            mPhoneNumberEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (getActivity() == null) {
                        return false;
                    }
                    if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                            || i == EditorInfo.IME_ACTION_DONE) {
                        mModel.onSignUpNextClicked(PhonePage.this);
                    }
                    return true;
                }
            });

            mNextButton.setOnClickListener(this);
            mTACText.setOnClickListener(this);
            mCountryContainer.setOnClickListener(this);
        }

        public void showKeyboard() {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(mPhoneNumberEdit, 0);
            }
        }

        public void setFlag(String flag) {
            ImageLoader.getInstance().displayImage(flag, mCountryFlag);
        }

        public void setPhoneCode(String phoneCode) {
            mCountryCodeValue.setText(phoneCode);
        }

        public String getPhoneNumber() {
            return mPhoneNumberEdit.getText().toString();
        }

        @Override
        public void requestFocus() {
            mPhoneNumberEdit.requestFocus();
        }

        @Override
        public LoginModel.PageType getType() {
            return mType;
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == mNextButton.getId()) {
                mModel.onSignUpNextClicked(this);
            } else if (view.getId() == mCountryContainer.getId()) {
                mModel.onSelectCountryClicked(this);
            }
        }
    }

    public class VerificationCodePage extends Page {

        private PhoneCodePageBinder mBinder;

        private LoginModel.PageType mType;

        VerificationCodePage(View rootView, LoginModel model, LoginModel.PageType type, String phoneNumber) {
            super(rootView, model);
            mType = type;

            mBinder = new PhoneCodePageBinder(rootView, phoneNumber, new PhoneCodePageBinder.Listener() {
                @Override
                public void onVerificationInput(String verificationCode) {
                    mModel.onVerificationCodeInputed(VerificationCodePage.this, verificationCode);
                }
                @Override
                public void onResendCodeClicked() {
                    mModel.onResendVerificationCodeClicked();
                }
            });

        }

        public void clearInput() {
            mBinder.clearInput();
        }

        @Override
        public void requestFocus() {
            mBinder.requestFocus();
        }

        @Override
        public LoginModel.PageType getType() {
            return mType;
        }

    }

    public class PasscodePage extends Page {

        // True if passcode was entered and this page for confirmation same code
        private final LoginModel.PageType mPageType;

        private TextView mTitle;
        private PassCodeInput mInput;

        PasscodePage(View rootView, LoginModel model, LoginModel.PageType pageType) {
            super(rootView, model);
            mPageType = pageType;
            mTitle = rootView.findViewById(R.id.login_passcode_title);
            mInput = rootView.findViewById(R.id.login_passcode_input);

            mTitle.setText(pageType == SIGN_UP_PASS_CODE_CONFIRMATION || pageType == FORGET_PASS_CODE_CONFIRMATION
                    ? R.string.login_passcode_reenter : pageType == LoginModel.PageType.FORGET_PASS_CODE
                    ? R.string.login_passcode_forget_create : R.string.login_passcode_create);

            mInput.setListener(new PassCodeInput.Listener() {
                @Override
                public void onPasscodeInput(String passcode) {
                    mModel.onPassCodeInputed(PasscodePage.this, passcode);
                }
            });
        }

        public void clearInput() {
            mInput.setText("");
        }

        public void closeKeyboard() {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
            }
        }

        @Override
        public void requestFocus() {
            mInput.requestFocus();
        }

        @Override
        public LoginModel.PageType getType() {
            return mPageType;
        }
    }

    public class PersonalDetailsPage extends Page implements PersonalDetailsPageBinder.Listener {

        private PersonalDetailsPageBinder mBinder;

        PersonalDetailsPage(View rootView, LoginModel model) {
            super(rootView, model);
            mBinder = new PersonalDetailsPageBinder(rootView, PersonalDetailsPageBinder.Type.SIGN_UP, this);
        }

        public void closeKeyboard() {
            mBinder.closeKeyboard();
        }

        @Override
        public void requestFocus() {
            mBinder.requestFocus();
        }

        @Override
        public LoginModel.PageType getType() {
            return LoginModel.PageType.PERSONAL_DETAILS;
        }

        @Override
        public void onNextClick() {
            mModel.onPersonalDetailsNextClick(this);
        }

        @Override
        public void onPhotoClick() {
            mModel.onPersonalDetailsImageClick();
        }

        public PersonalDetailsPageBinder getBinder() {
            return mBinder;
        }
    }

    public class WelcomePage extends Page implements View.OnClickListener {
        private TextView mNameView;
        private TextView mListNumberView;
        private View mStartButton;
        private View mShareFacebook;
        private View mShareTwitter;
        private View mShareWhatsapp;

        WelcomePage(View rootView, LoginModel model, String username) {
            super(rootView, model);
            mNameView = rootView.findViewById(R.id.login_welcome_name);
            mListNumberView = rootView.findViewById(R.id.login_welcome_number);
            mStartButton = rootView.findViewById(R.id.button_next);
            ((TextView)mStartButton.findViewById(R.id.button_next_text)).setText(R.string.start);
            mShareFacebook = rootView.findViewById(R.id.login_welcome_share_facebook);
            mShareTwitter = rootView.findViewById(R.id.login_welcome_share_twitter);
            mShareWhatsapp = rootView.findViewById(R.id.login_welcome_share_whatsapp);

            mNameView.setText(getString(R.string.welcome) + " " + username);

            mStartButton.setOnClickListener(this);
            mShareFacebook.setOnClickListener(this);
            mShareTwitter.setOnClickListener(this);
            mShareWhatsapp.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == mStartButton.getId()) {
                mModel.onWelcomeStartClicked();
            } else if (view.getId() == mShareFacebook.getId()) {
                mModel.onFacebookClick();
            } else if (view.getId() == mShareTwitter.getId()) {
                mModel.onTwitterClick();
            } else if (view.getId() == mShareWhatsapp.getId()) {
                mModel.onWhatsappClick();
            }
        }

        @Override
        public void requestFocus() {}

        @Override
        public LoginModel.PageType getType() {
            return LoginModel.PageType.WELCOME;
        }

    }

    public class SignInPage extends Page {

        private ImageView mImage;
        private TextView mName;
        private View mSettings;
        private PassCodeInput mInput;
        private View mForgetPassView;

        SignInPage(View rootView, LoginModel model, String username, @Nullable String imageUrl) {
            super(rootView, model);
            mImage = rootView.findViewById(R.id.account_image);
            mName = rootView.findViewById(R.id.account_name);
            mSettings = rootView.findViewById(R.id.account_settings);
            mInput = rootView.findViewById(R.id.login_passcode_input);
            mForgetPassView = rootView.findViewById(R.id.login_forget_passcode);

            TextView title = rootView.findViewById(R.id.my_account_title);
            title.setText("");

            mName.setText(username);
            if (imageUrl != null) {
                ImageLoader.getInstance().displayImage(imageUrl, mImage);
            }
            mForgetPassView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mModel.onForgetPasswordClick();
                }
            });
            mInput.setListener(new PassCodeInput.Listener() {
                @Override
                public void onPasscodeInput(String passcode) {
                    mModel.onLoginPassCodeInputed(SignInPage.this, passcode);
                }
            });
        }

        public void clearInput() {
            mInput.setText("");
        }

        public void closeKeyboard() {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
            }
        }

        @Override
        public void requestFocus() {
            mInput.requestFocus();
        }

        @Override
        public LoginModel.PageType getType() {
            return LoginModel.PageType.SING_IN;
        }
    }

}
