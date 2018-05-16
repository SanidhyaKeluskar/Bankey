package com.bankey.bankeyclient.tasks;

import android.net.Uri;

import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.api.data.UserData;
import com.bankey.bankeyclient.data.UserSession;

/**
 * Created by Dima on 01.03.2018.
 */

public class SignUpTask extends AbstractBackgroundTask<UserData> {

    private String mPhoneNumber;
    private String mPasscode;
    private String mName;
    private String mSurname;
    private String mDOB;
    private String mGender;
    private Uri mImage;

    public SignUpTask(String phone, String pass, String name, String surname, String dob, String gender, Uri image) {
        mPhoneNumber = phone;
        mPasscode = pass;
        mName = name;
        mSurname = surname;
        mDOB = dob;
        mGender = gender;
        mImage = image;
    }

    @Override
    protected UserData requestData() throws Exception {
        UserData result = MainApplication.instance().getApi().signup(mPhoneNumber, mPasscode, mName, mSurname, mDOB, mGender);
        // TODO Upload image request
        UserSession.instance().setUserData(result);
        return result;
    }
}
