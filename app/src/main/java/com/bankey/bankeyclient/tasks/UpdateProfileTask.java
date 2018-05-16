package com.bankey.bankeyclient.tasks;

import android.net.Uri;

/**
 * Created by Dima on 10.03.2018.
 */

public class UpdateProfileTask extends AbstractBackgroundTask<Boolean> {

    private Uri imageUri;
    private String country;
    private String address;
    private String city;

    public UpdateProfileTask(Uri imageUri, String country, String address, String city) {
        this.imageUri = imageUri;
        this.country = country;
        this.address = address;
        this.city = city;
    }

    @Override
    protected Boolean requestData() throws Exception {
        Thread.sleep(2000);
        return true;
    }
}
