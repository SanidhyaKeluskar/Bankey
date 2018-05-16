package com.bankey.bankeyclient.api.data.parser;

import com.bankey.bankeyclient.api.data.CountryData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dima on 09.03.2018.
 */

public class CountryListParser extends BaseParser<List<CountryData>> {

    @Override
    protected List<CountryData> parse(JsonParser parser) throws IOException {
        List<CountryData> result = new ArrayList<>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            result.add(new CountryData(parser));
        }
        return result;
    }
}
