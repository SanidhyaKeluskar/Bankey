package com.bankey.bankeyclient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages device location and notify listeners when location is changed, or location
 * provider became enabled/disabled
 */
public class AppLocationManager implements LocationListener {

    public static final String ENABLED = "enabled";

    private static final long MIN_DISTANCE = 5; // Min distance for location updates in Meters
    private static final long MIN_TIME = 500; // Min time for location in Milliseconds

    public interface Listener {
        void onLocationRequestChanged(boolean started);
        void onMyLocationChanged(Location location);
    }

    /**
     * Singleton instance
     */
    private List<Listener> mListeners = new ArrayList<>();

    private final MainActivity mContext;
    private final LocationManager mLocationManager;
    protected Location mCurrentLocation;

    protected AppLocationManager(MainActivity context) {
        mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // TODO test
        mCurrentLocation = new Location("gps");
        mCurrentLocation.setLongitude(0);
        mCurrentLocation.setLatitude(0);
    }

    public boolean requestLocationUpdates() {
        // Try to get last known location
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MainActivity.PERMISSION_LOCATION);
            return false;
        }

//        mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if (mCurrentLocation == null) {
//            mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        }
        List<String> enabledProviders = mLocationManager.getProviders(true);
        for (String provider : enabledProviders) {
            mLocationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);
        }
        for (Listener l : mListeners) {
            l.onLocationRequestChanged(true);
        }

        // Check gps availability
        if (!isGpsLocationEnabled()) {
            tryEnableGPS();
        }

        return true;
    }

    public void stopLocationUpdates() {
        mLocationManager.removeUpdates(this);
        for (Listener l : mListeners) {
            l.onLocationRequestChanged(false);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String log = location.getProvider() + " " + location.getLatitude() + " " + location.getLongitude();
        mCurrentLocation = location;
        for (Listener l : mListeners) {
            l.onMyLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Nullable
    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public void addListener(Listener locationListener) {
        mListeners.add(locationListener);
    }

    public void removeListener(Listener locationListener) {
        mListeners.remove(locationListener);
    }

    private void checkGPSAvailability() {
        if (!isGpsLocationEnabled()) {
//            Toast.makeText(mContext, mContext.getString(R.string.gps_turned_off_please_turn_gps_on), Toast.LENGTH_LONG).show();
            tryEnableGPS();
        }
    }

    private boolean isGpsLocationEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void tryEnableGPS() {
        try {
            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra(ENABLED, true);
            mContext.sendBroadcast(intent);

            String provider = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                mContext.sendBroadcast(poke);
            }
        } catch (SecurityException ex) {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

}
