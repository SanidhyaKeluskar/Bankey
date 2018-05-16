package com.bankey.bankeyclient.tasks;

import com.bankey.bankeyclient.MainApplication;

/**
 * Created by Dima on 02.03.2018.
 */

public class ForgetPasswordChangeTask extends AbstractBackgroundTask<Boolean> {

    private String mPhoneNumber;
    private String mNewPasscode;

    public ForgetPasswordChangeTask(String phoneNumber, String newPasscode) {
        mPhoneNumber = phoneNumber;
        mNewPasscode = newPasscode;
    }

    @Override
    protected Boolean requestData() throws Exception {
        MainApplication.instance().getApi().forgotPasswordChange(mPhoneNumber, mNewPasscode);
        return true;
    }
}
