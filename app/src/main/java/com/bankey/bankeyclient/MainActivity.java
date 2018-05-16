package com.bankey.bankeyclient;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.bankey.bankeyclient.api.data.UserData;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.AccountFragment;
import com.bankey.bankeyclient.fragment.KeyMapsFragment;
import com.bankey.bankeyclient.fragment.LoginFragment;
import com.bankey.bankeyclient.fragment.TutorialFragment;
import com.bankey.bankeyclient.model.PickupImageUtils;
import com.bankey.bankeyclient.model.TutorialModel;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_LOCATION = 1201;

    private Navigation mNavigation;
    private AppLocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mNavigation = new Navigation(this);
        mLocationManager = new AppLocationManager(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Open account if user is logged in
        if (UserSession.instance().isLoggedIn()) {
            mNavigation.openNextFragment(new AccountFragment(), false);
            return;
        }

        if (!Prefs.readBoolean(this, Prefs.KEY_SIGNUP_TUTORIAL, false)) {
            // Show tutorial on first app start
            TutorialFragment tutorialFragment = TutorialFragment.instantiate(TutorialModel.Type.SIGN_UP);
            mNavigation.openNextFragment(tutorialFragment, false);
        } else {
            // Open login page
            mNavigation.openNextFragment(new LoginFragment(), false);
        }
    }

    @Override
    public void onBackPressed() {
        if (!AnimationUtils.isPageAnimated() && !mNavigation.navigateBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !AnimationUtils.isPageAnimated() && super.dispatchTouchEvent(ev);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    public void onDetachFragment(Fragment fragment) {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PickupImageUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PERMISSION_LOCATION:
                if (isPermissionGranted(grantResults)) {
                    mLocationManager.requestLocationUpdates();
                }
                break;
        }
    }

    public Navigation getNavigation() {
        return mNavigation;
    }

    public AppLocationManager getLocationManager() {
        return mLocationManager;
    }

    private static boolean isPermissionGranted(int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

}