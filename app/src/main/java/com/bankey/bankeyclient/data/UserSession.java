package com.bankey.bankeyclient.data;

import com.bankey.bankeyclient.api.data.UserData;

/**
 * Created by Dima on 01.03.2018.
 */

public class UserSession {

    private static UserSession instance = new UserSession();

    public static UserSession instance() {
        return instance;
    }

    private UserData mUserData;

    //////////////

    private UserSession() {}

    public void setUserData(UserData userData) {
        mUserData = userData;
    }

    public UserData getUserData() {
        return mUserData;
    }

    public boolean isLoggedIn() {
        return mUserData != null && mUserData.getToken() != null;
    }






    public void test() {
        mUserData = UserData.test();
    }






    // Key request temp fields
    public float keyRequestAmount;
    public int keyRequestType; // 1 - pending, 2 - confirmed by key 3 - confirmed by you 4 - complete
    public OperationType keyRequestOperationType; // 0 - add, 1 - withdraw
    public String generatedCode;

    public enum OperationType {
        ADD,
        WITHDRAW
    }

}
