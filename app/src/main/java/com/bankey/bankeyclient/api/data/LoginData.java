package com.bankey.bankeyclient.api.data;

import com.bankey.bankeyclient.api.data.parser.JacksonParser;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 * Created by Dima on 12.03.2018.
 */

public class LoginData {
    private long count;
    private long userId;
    private String token;

    private LoginData() {}

    public LoginData(JsonParser parser) throws IOException {
        JacksonParser.parseObject(parser, new JacksonParser.ValueReader("count") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                count = parser.getValueAsLong();
            }
        }, new JacksonParser.ValueReader("user_id") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                userId = parser.getValueAsLong();
            }
        }, new JacksonParser.ValueReader("auth_token") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                token = parser.getValueAsString();
            }
        });
    }

    public long getCount() {
        return count;
    }

    public long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public static LoginData test() {
        LoginData data = new LoginData();
        data.count = 1;
        data.userId = 1;
        data.token = "123";
        return data;
    }
}
