package com.bankey.bankeyclient.api.data.parser;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.bankey.bankeyclient.api.http.ResponseError;
import com.bankey.bankeyclient.api.http.StreamParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public abstract class JacksonParser<T> implements StreamParser {

    private T mResult;
    private JsonFactory mFactory;

    public JacksonParser() {
        mFactory = new JsonFactory();
        onCreateFactory(mFactory);
    }

    public final T parseStream(InputStream inputStream) throws ResponseError {
        try {
            JsonParser parser = mFactory.createParser(inputStream);
            mResult = parseJson(parser);
            parser.close();
        } catch (ResponseError responseError) {
            throw responseError;
        } catch (Exception e) {
            throw new ResponseError(e);
        }
        return mResult;
    }

    @CallSuper
    public void onCreateFactory(JsonFactory factory) {
        factory.setCodec(new ObjectMapper());
    }

    public final T getResult() {
        return mResult;
    }

    protected abstract T parseJson(JsonParser parser) throws IOException, ResponseError;

    /**
     * @param valueParsers list of parsers for each json fields, if there is no value parser for some json fieldName
     *                    ValueParser with null name will be used. Only one ValueParser with null name can be specified
     * @throws IOException
     */
    public static void parseObject(JsonParser parser, ValueReader... valueParsers) throws IOException {
        Map<String, ValueReader> valueParsersMap = new HashMap<>(valueParsers.length);
        ValueReader defaultValueParser = null;
        for (ValueReader valueParser : valueParsers) {
            if (valueParser.mName == null) {
                if (defaultValueParser != null) {
                    throw new IllegalArgumentException("There are more than 1 ValueReader with null key!");
                }
                defaultValueParser = valueParser;
            } else {
                valueParsersMap.put(valueParser.mName, valueParser);
            }
        }

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldname = parser.getCurrentName();
            parser.nextToken();

            ValueReader valueParser = valueParsersMap.get(fieldname);

            if (valueParser != null) {
                valueParser.parseValue(parser);
            } else if (defaultValueParser != null) {
                defaultValueParser.parseValue(parser);
            } else {
                parser.skipChildren();
            }
        }
    }

    public static @Nullable JsonNode createJsonObject(ValuesWriter valuesWriter) {
        TokenBuffer tokenBuffer = new TokenBuffer(new ObjectMapper(), false);
        try {
            tokenBuffer.writeStartObject();
            valuesWriter.writeValues(tokenBuffer);
            tokenBuffer.writeEndObject();
            JsonParser parser = tokenBuffer.asParser();
            tokenBuffer.close();
            return parser.readValueAsTree();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Handling reading value with specified name from JsonParser
     * If name is null ValueReader will be used for any fieldName when no other ValueReaders were not found for such fieldName
     */
    public static abstract class ValueReader {

        private final @Nullable String mName;

        public ValueReader(String name) {
            mName = name;
        }

        public abstract void parseValue(JsonParser parser) throws IOException;
    }

    public interface ValuesWriter {

        void writeValues(TokenBuffer buffer) throws IOException;
    }

}
