package com.bankey.bankeyclient;

import com.bankey.bankeyclient.api.data.CountryData;

import java.util.List;

/**
 * Created by Dima on 09.03.2018.
 */

public class DataCache {

    private static DataCache instance = new DataCache();

    public static DataCache instance() {
        return instance;
    }

    private DataCache() {}

    private List<CountryData> mCountryList;

    public List<CountryData> getCountryList() {
        return mCountryList;
    }

    public void setCountryList(List<CountryData> mCountryList) {
        this.mCountryList = mCountryList;
    }
}
