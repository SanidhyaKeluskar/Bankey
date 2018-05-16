package com.bankey.bankeyclient.model;

import android.content.Context;

import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.Prefs;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.AddressInputFragment;
import com.bankey.bankeyclient.fragment.BankAccountFragment;
import com.bankey.bankeyclient.fragment.LoginFragment;
import com.bankey.bankeyclient.fragment.TutorialFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dima on 25.02.2018.
 */

public class TutorialModel extends FragmentModel<TutorialFragment> {

    private static final Context ctx = MainApplication.instance();
    private static final List<PageData> sSignupTutorials = new ArrayList<>();
    private static final List<PageData> sBecomeKeyTutorials = new ArrayList<>();

    static {
        sSignupTutorials.add(new PageData(R.drawable.tutorial_signup_4, ctx.getString(R.string.tutorial_signup_1_title),
                ctx.getString(R.string.tutorial_signup_1_subtitle)));
        sSignupTutorials.add(new PageData(R.drawable.tutorial_signup_4, ctx.getString(R.string.tutorial_signup_2_title),
                ctx.getString(R.string.tutorial_signup_2_subtitle)));
        sSignupTutorials.add(new PageData(R.drawable.tutorial_signup_4, ctx.getString(R.string.tutorial_signup_3_title),
                ctx.getString(R.string.tutorial_signup_3_subtitle)));
        sSignupTutorials.add(new PageData(R.drawable.tutorial_signup_4, ctx.getString(R.string.tutorial_signup_4_title),
                ctx.getString(R.string.tutorial_signup_4_subtitle)));


        sBecomeKeyTutorials.add(new PageData(R.drawable.tutorial_signup_4, ctx.getString(R.string.tutorial_become_key_1_title),
                ctx.getString(R.string.tutorial_become_key_1_subtitle)));
        sBecomeKeyTutorials.add(new PageData(R.drawable.tutorial_signup_4, ctx.getString(R.string.tutorial_become_key_2_title),
                ctx.getString(R.string.tutorial_become_key_2_subtitle)));
        sBecomeKeyTutorials.add(new PageData(R.drawable.tutorial_signup_4, ctx.getString(R.string.tutorial_become_key_3_title),
                ctx.getString(R.string.tutorial_become_key_3_subtitle)));
    }

    public enum Type {
        SIGN_UP,
        BECOME_KEY
    }

    public static class PageData {
        public final int imageRes;
        public final String title;
        public final String desc;
        public PageData(int imageRes, String title, String desc) {
            this.imageRes = imageRes;
            this.title = title;
            this.desc = desc;
        }
    }

    @Override
    public void onViewCreated(TutorialFragment view) {
        super.onViewCreated(view);

        switch (view.getType()) {
            case SIGN_UP:
                view.showPages(sSignupTutorials);
                break;
            case BECOME_KEY:
                view.showPages(sBecomeKeyTutorials);
                break;
        }
    }

    public void onPassed() {
        switch (mView.getType()) {
            case SIGN_UP:
                Prefs.applyBoolean(mView.getActivity(), Prefs.KEY_SIGNUP_TUTORIAL, true);
                mView.getNavigation().openNextFragment(new LoginFragment(), true);
                break;
            case BECOME_KEY:
                if (UserSession.instance().getUserData().isBankAccount) {
                    mView.getNavigation().openNextFragment(AddressInputFragment.instantiate("", "", ""), true);
                } else {
                    mView.getNavigation().openNextFragment(BankAccountFragment.instantiate(BankAccountModel.Initiator.BECOME_KEY), true);
                }
                break;
        }
        mView.getNavigation().removeFromHistory(TutorialFragment.class);
    }

}
