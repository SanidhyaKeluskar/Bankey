package com.bankey.bankeyclient.api.data.parser;

import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.api.http.ResponseError;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

/**
 * Created by DLutskov on 2/8/2018.
 */

public abstract class BaseParser<T> extends JacksonParser<T> {

    @Override
    protected final T parseJson(JsonParser parser) throws IOException, ResponseError {
        parser.nextToken();

        Boolean success = null;
        String message = MainApplication.instance().getString(R.string.unknown_server_error);
        T data = null;

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldname = parser.getCurrentName();
            parser.nextToken();
            switch (fieldname) {
                case "success":
                    success = parser.getValueAsBoolean();
                    break;
                case "message":
                    message = parser.getValueAsString();
                    break;
                case "data":
                    data = parse(parser);
                    break;

                default:
                    parser.skipChildren();
            }
        }

        if (success == null || !success) {
            throw new ResponseError(-1, message);
        }

        return data;
    }

    protected abstract T parse(JsonParser parser) throws IOException;
}
