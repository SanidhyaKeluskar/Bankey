package com.bankey.bankeyclient.api.data.parser;

import com.bankey.bankeyclient.api.data.UserData;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 * Created by DLutskov on 3/16/2018.
 */

public class PhoneVerificationParser extends BaseParser<UserData> {

    @Override
    protected UserData parse(JsonParser parser) throws IOException {
        return new UserData(parser);
    }
}
