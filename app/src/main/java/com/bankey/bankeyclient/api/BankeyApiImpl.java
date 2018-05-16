package com.bankey.bankeyclient.api;

import android.support.annotation.Nullable;

import com.bankey.bankeyclient.api.data.CountryData;
import com.bankey.bankeyclient.api.data.LoginData;
import com.bankey.bankeyclient.api.data.UserData;
import com.bankey.bankeyclient.api.data.parser.BaseParser;
import com.bankey.bankeyclient.api.data.parser.CountryListParser;
import com.bankey.bankeyclient.api.data.parser.JacksonParser;
import com.bankey.bankeyclient.api.data.parser.EmptyResponseParser;
import com.bankey.bankeyclient.api.http.HttpResponse;
import com.bankey.bankeyclient.api.http.OkHttpClientImpl;
import com.bankey.bankeyclient.api.http.RequestException;
import com.bankey.bankeyclient.data.UserSession;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dima on 09.03.2018.
 */

public class BankeyApiImpl implements BankeyApi {

    private final OkHttpClientImpl mClient;

    public BankeyApiImpl() {
        mClient = new OkHttpClientImpl();
    }

    @Override
    public List<CountryData> getCountryList() throws RequestException {
        String url = HOST_USER + URL_COUNTRIES;
        CountryListParser parser = new CountryListParser();
        mClient.performGET(url, getHeaders(), parser);
        return parser.getResult();
    }
	
	@Override
	@Nullable
	public UserData phoneVerification(final String phone) throws RequestException {
		String url = HOST_USER + URL_PHONE_VERIFICATION;

		final JsonNode requestJson = JacksonParser.createJsonObject(new JacksonParser.ValuesWriter() {
			@Override
			public void writeValues(TokenBuffer buffer) throws IOException {
				buffer.writeFieldName("mobile_number");
				buffer.writeString(phone);
			}
		});

		BaseParser<UserData> parser = new BaseParser<UserData>() {
			@Override
			protected UserData parse(JsonParser parser) throws IOException {
				UserData userData = new UserData(parser);
				return userData.getName() == null ? null : userData; // TODO
			}
		};

		mClient.performPOST(url, OkHttpClientImpl.CONTENT_TYPE_JSON, requestJson.toString(), getHeaders(), parser);
		return parser.getResult();
	}

	@Override
	public void phoneCodeVerification(final String phone, final String code, final String process) throws RequestException {
		String url = HOST_USER + URL_PHONE_CODE_VERIFICATION;

		final JsonNode requestJson = JacksonParser.createJsonObject(new JacksonParser.ValuesWriter() {
			@Override
			public void writeValues(TokenBuffer buffer) throws IOException {
				buffer.writeFieldName("mobile_number");
				buffer.writeString(phone);
				buffer.writeFieldName("verification_code");
				buffer.writeNumber(code);
				buffer.writeFieldName("process");
				buffer.writeString(process);
			}
		});

		mClient.performPOST(url, OkHttpClientImpl.CONTENT_TYPE_JSON, requestJson.toString(), getHeaders(), new EmptyResponseParser());
	}

	@Override
	public UserData signup(final String phone, final String pass, final String name, final String surname,
						   final String birth, final String gender) throws RequestException {
		String url = HOST_USER + URL_SIGNUP;

		final JsonNode requestJson = JacksonParser.createJsonObject(new JacksonParser.ValuesWriter() {
			@Override
			public void writeValues(TokenBuffer buffer) throws IOException {
				buffer.writeFieldName("phone_no");
				buffer.writeString(phone);
				buffer.writeFieldName("password");
				buffer.writeString(pass);
				buffer.writeFieldName("name");
				buffer.writeString(name);
				buffer.writeFieldName("surname");
				buffer.writeString(surname);
				buffer.writeFieldName("gender");
				buffer.writeString(gender);
				buffer.writeFieldName("birth_date");
				buffer.writeString(birth);
			}
		});

		BaseParser<UserData> parser = new BaseParser<UserData>() {
			@Override
			protected UserData parse(JsonParser parser) throws IOException {
				return new UserData(parser);
			}
		};

		mClient.performPOST(url, OkHttpClientImpl.CONTENT_TYPE_JSON, requestJson.toString(), getHeaders(), parser);
		return parser.getResult();
	}
	
	@Override
	public LoginData login(final String phone, final String passcode) throws RequestException {
		String url = HOST_USER + URL_LOGIN;

		final JsonNode requestJson = JacksonParser.createJsonObject(new JacksonParser.ValuesWriter() {
			@Override
			public void writeValues(TokenBuffer buffer) throws IOException {
				buffer.writeFieldName("mobile_number");
				buffer.writeString(phone);
				buffer.writeFieldName("password");
				buffer.writeString(passcode);
			}
		});

		BaseParser<LoginData> parser = new BaseParser<LoginData>() {
			@Override
			protected LoginData parse(JsonParser parser) throws IOException {
				return new LoginData(parser);
			}
		};

		mClient.performPOST(url, OkHttpClientImpl.CONTENT_TYPE_JSON, requestJson.toString(), getHeaders(), parser);
		return parser.getResult();
	}
	
	@Override
	public void logout() throws RequestException {
		String url = HOST_USER + URL_LOGOUT;

		mClient.performGET(url, getHeaders());
	}
	
	@Override
	public UserData getUserProfile(String userId) throws RequestException {
		String url = HOST_USER + URL_USER_DETAILS + userId + "/";

		BaseParser<UserData> parser = new BaseParser<UserData>() {
			@Override
			protected UserData parse(JsonParser parser) throws IOException {
				return new UserData(parser);
			}
		};

		mClient.performGET(url, getHeaders(), parser);
		return parser.getResult();
	}
	
	@Override
	public UserData updateUserProfile(String userId, UserData dataToUpdate) throws RequestException {
		String url = HOST_USER + URL_USER_DETAILS + userId + "/";

		BaseParser<UserData> parser = new BaseParser<UserData>() {
			@Override
			protected UserData parse(JsonParser parser) throws IOException {
				return new UserData(parser);
			}
		};

		mClient.performPUT(url, OkHttpClientImpl.CONTENT_TYPE_JSON, dataToUpdate.toJsonNode().toString(), getHeaders(), parser);
		return parser.getResult();
	}
	
	@Override
	public void uploadUserPhoto(final String phone, final String base64image) throws RequestException {
		String url = HOST_USER + URL_UPLOAD_PHOTO;

		final JsonNode requestJson = JacksonParser.createJsonObject(new JacksonParser.ValuesWriter() {
			@Override
			public void writeValues(TokenBuffer buffer) throws IOException {
				buffer.writeFieldName("phone_no");
				buffer.writeString(phone);
				buffer.writeFieldName("photo");
				buffer.writeString(base64image);
			}
		});

		mClient.performPUT(url, OkHttpClientImpl.CONTENT_TYPE_JSON, requestJson.toString(), getHeaders(), null);
	}
	
	@Override
	public boolean isTeller() throws RequestException {
		String url = HOST_USER + URL_TELLER;

		BaseParser<Boolean> parser = new BaseParser<Boolean>() {
			@Override
			protected Boolean parse(JsonParser parser) throws IOException {
				boolean result = false;
				while (parser.nextToken() != JsonToken.END_OBJECT) {
					String fieldname = parser.getCurrentName();
					parser.nextToken();
					if (fieldname.equals("is_teller")) {
						result = parser.getValueAsBoolean();
					} else {
						parser.skipChildren();
					}
				}
				return result;
			}
		};

		mClient.performGET(url, getHeaders(), parser);
		return parser.getResult();
	}
	
	@Override
	public void becomeTeller(float fee, float minValue) throws RequestException {
		String url = HOST_USER + URL_TELLER;
		// TODO
		throw new RequestException(new HttpResponse(url), new UnsupportedOperationException("Request is not supported yet!"));
	}
	
	@Override
	public boolean isKeyActivationMode() throws RequestException {
		String url = HOST_USER + URL_KEY_ACTIVATION_MODE;

		BaseParser<Boolean> parser = new BaseParser<Boolean>() {
			@Override
			protected Boolean parse(JsonParser parser) throws IOException {
				boolean result = false;
				while (parser.nextToken() != JsonToken.END_OBJECT) {
					String fieldname = parser.getCurrentName();
					parser.nextToken();
					if (fieldname.equals("activation_mode")) {
						result = parser.getValueAsBoolean();
					} else {
						parser.skipChildren();
					}
				}
				return result;
			}
		};

		mClient.performGET(url, getHeaders(), parser);
		return parser.getResult();
	}
	
	@Override
	public void changeKeyActivationMode(final boolean mode) throws RequestException {
		String url = HOST_USER + URL_KEY_ACTIVATION_MODE;

		final JsonNode requestJson = JacksonParser.createJsonObject(new JacksonParser.ValuesWriter() {
			@Override
			public void writeValues(TokenBuffer buffer) throws IOException {
				buffer.writeFieldName("service_activation");
				buffer.writeBoolean(mode);
			}
		});

		mClient.performPUT(url, OkHttpClientImpl.CONTENT_TYPE_JSON, requestJson.toString(), getHeaders(), null);
	}

	@Override
	public void changeKeyUserFee(final float fee) throws RequestException {
		String url = HOST_USER + URL_KEY_ACTIVATION_MODE;

		final JsonNode requestJson = JacksonParser.createJsonObject(new JacksonParser.ValuesWriter() {
			@Override
			public void writeValues(TokenBuffer buffer) throws IOException {
				buffer.writeFieldName("fee");
				buffer.writeNumber(fee);
			}
		});

		mClient.performPUT(url, OkHttpClientImpl.CONTENT_TYPE_JSON, requestJson.toString(), getHeaders(), null);
	}

	@Override
	public void forgotPasswordReset(final String phone) throws RequestException {
		String url = HOST_USER + URL_FORGET_PASSWORD_RESET;

		final JsonNode requestJson = JacksonParser.createJsonObject(new JacksonParser.ValuesWriter() {
			@Override
			public void writeValues(TokenBuffer buffer) throws IOException {
				buffer.writeFieldName("mobile_number");
				buffer.writeString(phone);
			}
		});

		mClient.performPOST(url, OkHttpClientImpl.CONTENT_TYPE_JSON, requestJson.toString(), getHeaders());
	}
	
	@Override
	public void forgotPasswordChange(final String phone, final String passcode) throws RequestException {
		String url = HOST_USER + URL_FORGET_PASSWORD_CHANGE;

		final JsonNode requestJson = JacksonParser.createJsonObject(new JacksonParser.ValuesWriter() {
			@Override
			public void writeValues(TokenBuffer buffer) throws IOException {
				buffer.writeFieldName("mobile_number");
				buffer.writeString(phone);
				buffer.writeFieldName("password");
				buffer.writeString(passcode);
			}
		});

		mClient.performPOST(url, OkHttpClientImpl.CONTENT_TYPE_JSON, requestJson.toString(), getHeaders());
	}

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        if (UserSession.instance().isLoggedIn()) {
        	headers.put("Authorization", UserSession.instance().getUserData().getToken());
		}
        return headers;
    }
}
