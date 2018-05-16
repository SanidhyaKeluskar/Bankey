package com.bankey.bankeyclient.tasks;


import com.bankey.bankeyclient.MainApplication;

/**
 * Created by Dima on 04.03.2018.
 */

public class UpdateServiceFeeTask extends AbstractBackgroundTask<Boolean> {

    @Override
    protected Boolean requestData() throws Exception {
        MainApplication.instance().getApi().changeKeyUserFee(.0f); // TODO
        return true;
    }
}
