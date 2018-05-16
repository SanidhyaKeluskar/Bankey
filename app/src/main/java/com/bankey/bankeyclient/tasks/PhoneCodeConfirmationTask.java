package com.bankey.bankeyclient.tasks;

import com.bankey.bankeyclient.MainApplication;

/**
 * Created by Dima on 01.03.2018.
 */

public class PhoneCodeConfirmationTask extends AbstractBackgroundTask<Boolean> {

    public enum Type {
        signup,
        forgot_password
    }

    private final String mPhone;
    private final String mCode;
    private final Type mType;

    public PhoneCodeConfirmationTask(String phone, String code, Type type) {
        mPhone = phone;
        mCode = code;
        mType = type;
    }

    @Override
    protected Boolean requestData() throws Exception {
        MainApplication.instance().getApi().phoneCodeVerification(mPhone, mCode, mType.toString());
        return false;
    }
}
