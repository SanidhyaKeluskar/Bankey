package com.bankey.bankeyclient.api.data;

import com.bankey.bankeyclient.api.BankeyApi;
import com.bankey.bankeyclient.api.data.parser.JacksonParser;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

public class CountryData {

    private String name;
    private String codeName;
    private String codeValue;
    private String flag;
    private String status;

    private CountryData() {}

    public CountryData(String name, String codeValue, String flag) {
        this.name = name;
        this.codeValue = codeValue;
        this.flag = flag;
    }

    public CountryData(JsonParser parser) throws IOException {
        JacksonParser.parseObject(parser, new JacksonParser.ValueReader("name") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                name = parser.getValueAsString();
            }
        }, new JacksonParser.ValueReader("code") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                codeName = parser.getValueAsString();
            }
        }, new JacksonParser.ValueReader("std_code") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                codeValue = parser.getValueAsString();
            }
        }, new JacksonParser.ValueReader("flag") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                flag = parser.getValueAsString();
            }
        }, new JacksonParser.ValueReader("status") {
            @Override
            public void parseValue(JsonParser parser) throws IOException {
                status = parser.getValueAsString();
            }
        });
    }

    public String getName() {
        return name;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public String getFlag() {
        return flag;
    }

    public String getStatus() {
        return status;
    }

}
