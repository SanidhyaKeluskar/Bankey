package com.bankey.bankeyclient.api.data;

import com.bankey.bankeyclient.api.data.parser.JacksonParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;

/**
 * Created by Dima on 12.03.2018.
 */

public class UserAddress {
    private String country;
    private String line1;
    private String line2;
    private String city;

    public UserAddress(JsonNode node) throws IOException {
//        country = node.findValue("country").asText();
//        line1 = node.findValue("line1").asText();
//        line2 = node.findValue("line2").asText();
//        city = node.findValue("city").asText();
    }

    public JsonNode toJson() throws IOException {
        return JacksonParser.createJsonObject(new JacksonParser.ValuesWriter() {
            @Override
            public void writeValues(TokenBuffer buffer) throws IOException {
                buffer.writeFieldName("country");
                buffer.writeString(country);
                buffer.writeFieldName("line1");
                buffer.writeString(line1);
                buffer.writeFieldName("line2");
                buffer.writeString(line2);
                buffer.writeFieldName("city");
                buffer.writeString(city);
            }
        });
    }

    public String getCountry() {
        return country;
    }

    public String getLines() {
        return line1 += " " + line2;
    }

    public String getCity() {
        return city;
    }
}
