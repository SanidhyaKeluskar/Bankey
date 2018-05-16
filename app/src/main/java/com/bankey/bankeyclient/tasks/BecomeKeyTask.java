package com.bankey.bankeyclient.tasks;

import com.bankey.bankeyclient.data.UserSession;

/**
 * Created by Dima on 04.03.2018.
 */

public class BecomeKeyTask extends AbstractBackgroundTask<Boolean> {

    @Override
    protected Boolean requestData() throws Exception {
        Thread.sleep(2000);
        UserSession.instance().getUserData().setTellerTest(); // TODO
        UserSession.instance().getUserData().setBankAccountTest(); // TODO
//        MainApplication.instance().getApi().becomeTeller(.0f, .0f);
        return true;
    }
}
