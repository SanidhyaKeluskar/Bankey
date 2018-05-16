package com.bankey.bankeyclient.api;

import android.support.annotation.Nullable;

import com.bankey.bankeyclient.api.data.CountryData;
import com.bankey.bankeyclient.api.data.LoginData;
import com.bankey.bankeyclient.api.data.UserAddress;
import com.bankey.bankeyclient.api.data.UserData;
import com.bankey.bankeyclient.api.http.RequestException;

import java.util.List;

/**
 * Created by Dima on 24.02.2018.
 */

public interface BankeyApi {

	String HOST = "http://54.91.82.248/";
	String HOST_USER = HOST + "user/api/";

    String URL_COUNTRIES = "listcountries/";
	String URL_PHONE_VERIFICATION = "verificationrequest/";
	String URL_PHONE_CODE_VERIFICATION = "phoneverification/";
	String URL_SIGNUP = "signup/";
	String URL_LOGIN = "login/";
	String URL_LOGOUT = "logout/";
	String URL_USER_DETAILS = "userdetails/";
	String URL_UPLOAD_PHOTO = "uploadphoto/";
	String URL_TELLER = "teller/";
	String URL_KEY_ACTIVATION_MODE = "activationmode/";
	String URL_CHANGE_FEE = "teller/fee/";
	String URL_FORGET_PASSWORD_RESET = "forgotpasswordrequest/";
	String URL_FORGET_PASSWORD_CHANGE = "forgotpassword/";

    String USA_FLAG = "http://54.91.82.248/media/country_flags/USA_flag_AitOEfn.jpeg";

    List<CountryData> getCountryList() throws RequestException;

	/**
	 * Is user exists - UserData will be returned. If no - null will be returned and verification code will be sent to the phone.
	 */
	@Nullable UserData phoneVerification(String phone) throws RequestException;
	void phoneCodeVerification(String phone, String code, String process) throws RequestException;
	UserData signup(String phone, String pass, String name, String surname, String birth, String gender) throws RequestException;
	LoginData login(String phone, String passcode) throws RequestException;
	void logout() throws RequestException;
	UserData getUserProfile(String userId) throws RequestException;
	UserData updateUserProfile(String userId, UserData dataToUpdate) throws RequestException;
	void uploadUserPhoto(String phone, String base64image) throws RequestException;
	boolean isTeller() throws RequestException;
	void becomeTeller(float fee, float minValue) throws RequestException;
	boolean isKeyActivationMode() throws RequestException;
	void changeKeyActivationMode(boolean mode) throws RequestException;
	void changeKeyUserFee(float fee) throws RequestException;
	void forgotPasswordReset(String phone) throws RequestException;
	void forgotPasswordChange(String phone, String passcode) throws RequestException;
}
