package com.bankey.bankeyclient.tasks;

/**
 * Created by DLutskov on 4/6/2018.
 */

public class FindKeysTask extends AbstractBackgroundTask {

    @Override
    protected Object requestData() throws Exception {
        Thread.sleep(2000);
        return true;
    }
}
