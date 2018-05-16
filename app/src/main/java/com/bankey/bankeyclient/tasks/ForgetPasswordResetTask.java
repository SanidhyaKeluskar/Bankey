package com.bankey.bankeyclient.tasks;

import com.bankey.bankeyclient.MainApplication;

/**
 * Created by Dima on 22.03.2018.
 */

public class ForgetPasswordResetTask extends AbstractBackgroundTask<Boolean> {

    private String mPhoneNumber;

    public ForgetPasswordResetTask(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @Override
    protected Boolean requestData() throws Exception {
        MainApplication.instance().getApi().forgotPasswordReset(mPhoneNumber);
        return true;
    }
}
