package com.bankey.bankeyclient.api.data.parser;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 * Common parser for responses which have no any data, success true is enough
 * If success is false - ResponseException will be thrown anyway
 */

public class EmptyResponseParser extends BaseParser<Boolean> {

    @Override
    protected Boolean parse(JsonParser parser) throws IOException {
        parser.skipChildren();
        return true;
    }
}
