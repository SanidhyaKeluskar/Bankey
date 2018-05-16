package com.bankey.bankeyclient.tasks;

import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.api.data.PhoneVerificationResponse;
import com.bankey.bankeyclient.api.data.UserData;

/**
 * Created by Dima on 01.03.2018.
 */

public class PhoneVerificationTask extends AbstractBackgroundTask<PhoneVerificationResponse> {

    private String mPhone;

    public PhoneVerificationTask(String phone) {
        mPhone = phone;
    }

    @Override
    protected PhoneVerificationResponse requestData() throws Exception {
        return new PhoneVerificationResponse(MainApplication.instance().getApi().phoneVerification(mPhone));
    }
}
