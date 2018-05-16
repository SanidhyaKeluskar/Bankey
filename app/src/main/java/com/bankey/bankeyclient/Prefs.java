package com.bankey.bankeyclient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dima on 26.02.2018.
 */

public class Prefs {

    public static final String KEY_SIGNUP_TUTORIAL = "signup_tutorial";

    public static void applyBoolean(Activity ctx, String key, boolean value) {
        SharedPreferences sharedPref = ctx.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean readBoolean(Activity ctx, String key, boolean defaultValue) {
        SharedPreferences sharedPref = ctx.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }

}
