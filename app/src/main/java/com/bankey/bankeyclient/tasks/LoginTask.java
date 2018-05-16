package com.bankey.bankeyclient.tasks;

import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.api.data.LoginData;
import com.bankey.bankeyclient.data.UserSession;

/**
 * Created by Dima on 02.03.2018.
 */

public class LoginTask extends AbstractBackgroundTask<LoginData> {

    private String mPhonenNumber;
    private String mPasscode;

    public LoginTask(String phoneNumber, String passCode) {
        mPhonenNumber = phoneNumber;
        mPasscode = passCode;
    }

    @Override
    protected LoginData requestData() throws Exception {
//        LoginData response = MainApplication.instance().getApi().login(mPhonenNumber, mPasscode);
        LoginData response = LoginData.test();
        UserSession.instance().getUserData().setSessionData(response);
        return response;
    }
}
