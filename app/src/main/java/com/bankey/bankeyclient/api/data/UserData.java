package com.bankey.bankeyclient.api.data;

import android.support.annotation.Nullable;

import com.bankey.bankeyclient.api.BankeyApi;
import com.bankey.bankeyclient.api.data.parser.JacksonParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Dima on 12.03.2018.
 */

public class UserData {
    String phone;
    String name;
    String surname;
    String email;
    UserAddress address;
    String photo;
    String birthday;
    String gender;

    long userId;
    long count;

    String token;

    boolean isTeller;

    public boolean isBankAccount; // TODO
    public BigDecimal mBalance = BigDecimal.ZERO; // TODO

    private UserData() {}

    public UserData(JsonParser parser) throws IOException {
        JacksonParser.parseObject(parser, new JacksonParser.ValueReader("phone") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                phone = parser.getValueAsString();
            }
        }, new JacksonParser.ValueReader("name") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                name = parser.getValueAsString();
            }
        }, new JacksonParser.ValueReader("surname") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                surname = parser.getValueAsString();
            }
        }, new JacksonParser.ValueReader("gender") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                gender = parser.getValueAsString();
            }
        },  new JacksonParser.ValueReader("email") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                email = parser.getValueAsString();
            }
        }, new JacksonParser.ValueReader("address") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                JsonNode node = parser.readValueAsTree();
                address = new UserAddress(node);
            }
        }, new JacksonParser.ValueReader("photo") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                photo = BankeyApi.HOST + parser.getValueAsString();
            }
        }, new JacksonParser.ValueReader("user_id") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                userId = parser.getValueAsLong();
            }
        }, new JacksonParser.ValueReader("count") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                count = parser.getValueAsLong();
            }
        }, new JacksonParser.ValueReader("birth_date") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                birthday = parser.getValueAsString();
            }
        }, new JacksonParser.ValueReader("auth_token") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                token = parser.getValueAsString();
            }
        });
    }

    public String getToken() {
        return token;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public UserAddress getAddress() {
        return address;
    }

    public String getPhoto() {
        return photo;
    }

    public long getUserId() {
        return userId;
    }

    public long getCount() {
        return count;
    }

    public String getBirthday() {
        return birthday;
    }

    public boolean isTeller() {
        return isTeller;
    }

    public BigDecimal getBalance() {
        return mBalance;
    }

    public String getBalanceString() {
        return new DecimalFormat("#00.00").format(mBalance.floatValue());
    }

    public void setSessionData(@Nullable LoginData loginData) {
        count = loginData == null ? 0 : loginData.getCount();
        userId = loginData == null ? 0 : loginData.getUserId();
        token = loginData == null ? null : loginData.getToken();
    }

    public JsonNode toJsonNode() {
//        try {
            return null;
//        } catch (IOException e) {
//            return null;
//        }
    }

    public static UserData test() {
        UserData userData = new UserData();
        userData.name = "Name";
        userData.surname = "Surname";
        userData.birthday = "22.02.1992";
        userData.gender = "M";

        userData.token = "121231231";
        return userData;
    }

    public void setTellerTest() {
        isTeller = true;

    }

    public void setBankAccountTest() {
        isBankAccount = true;
    }
}
