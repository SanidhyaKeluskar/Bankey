package com.bankey.bankeyclient.tasks;

import com.bankey.bankeyclient.DataCache;
import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.api.data.CountryData;

import java.util.List;

/**
 * Created by Dima on 09.03.2018.
 */

public class CountryListTask extends AbstractBackgroundTask<List<CountryData>> {

    @Override
    protected List<CountryData> requestData() throws Exception {
        List<CountryData> data = MainApplication.instance().getApi().getCountryList();

        // TODO temp
        data.add(new CountryData("Ukraine", "+380", "http://icons.veryicon.com/png/Flag/All%20Country%20Flag/Ukraine%20Flag.png"));

        DataCache.instance().setCountryList(data);
        return data;
    }
}
