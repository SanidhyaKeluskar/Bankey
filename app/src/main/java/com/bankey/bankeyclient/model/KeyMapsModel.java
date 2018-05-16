package com.bankey.bankeyclient.model;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.bankey.bankeyclient.AppLocationManager;
import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.KeyDetailsFragment;
import com.bankey.bankeyclient.fragment.KeyMapsFragment;
import com.bankey.bankeyclient.fragment.KeyRequestConfirmFragment;
import com.bankey.bankeyclient.model.KeyRequestConfirmModel.Type;
import com.bankey.bankeyclient.tasks.AbstractBackgroundTask;
import com.bankey.bankeyclient.tasks.FindKeysTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class KeyMapsModel extends FragmentModel<KeyMapsFragment> implements AppLocationManager.Listener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {

    private final float mAmount;
    private final UserSession.OperationType mOperationType;

    private GoogleMap mMap;
    private AppLocationManager mLocationManager;

    private Map<Integer, Marker> mKeyMarkers = new HashMap<>();

    private boolean myLocationReceived;

    public KeyMapsModel(float amount, UserSession.OperationType operationType) {
        mAmount = amount;
        mOperationType = operationType;
    }

    @Override
    public void onViewCreated(KeyMapsFragment view) {
        super.onViewCreated(view);

        view.setLoadingKeysVisibility(View.VISIBLE);
        view.setSearchContainerVisibility(View.GONE);

        mLocationManager = view.getLocationManager();
        if (mLocationManager.getCurrentLocation() != null) {
            onMyLocationChanged(mLocationManager.getCurrentLocation());
        }
        mLocationManager.requestLocationUpdates();
    }

    @Override
    public void onViewBound() {
        super.onViewBound();
        mLocationManager.addListener(this);
    }

    @Override
    public void onViewUnbound() {
        super.onViewUnbound();
        mLocationManager.removeListener(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;
        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);

        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setInfoWindowAdapter(mView.getMarkerAdapter());

        if (myLocationReceived) {
            Location location = mLocationManager.getCurrentLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            updateCameraLocation(latLng);
            if (ActivityCompat.checkSelfPermission(mView.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mView.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onLocationRequestChanged(boolean started) {}

    @Override
    public void onMyLocationChanged(Location location) {
        if (myLocationReceived) return; // ignore next location updates

        myLocationReceived = true;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            updateCameraLocation(latLng);
        }
        loadKeys(latLng);
    }

    private void loadKeys(final LatLng currentLocation) {
        AbstractBackgroundTask task = new FindKeysTask().setListener(new AbstractBackgroundTask.Listener() {
            @Override
            public void onTaskFinished(final AbstractBackgroundTask task) {
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mView == null && mView.getActivity() == null) {
                            return;
                        }
                        mView.setLoadingKeysVisibility(View.GONE);
                        mView.setSearchContainerVisibility(View.VISIBLE);
                        if (task.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                            updateKeysOnMap();
                        } else {
                            DialogUtils.showError(mView.getActivity(), task.getException().getMessage());
                        }
                    }
                });
            }
        }).start();
    }

    private void updateCameraLocation(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
    }

    private void updateKeysOnMap() {
        Location location = mLocationManager.getCurrentLocation();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        Marker m1 = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Key 1")
                .snippet("Key 1 description")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        Marker m2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latLng.latitude - 0.0008, latLng.longitude + 0.0001))
                .title("Key 2")
                .snippet("Key 1 description")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        Marker m3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latLng.latitude - 0.00034, latLng.longitude + 0.0002))
                .title("Key 1")
                .snippet("Key 1 description")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        m1.showInfoWindow();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        mView.getNavigation().openNextFragment(KeyDetailsFragment.instance(mAmount, mOperationType), true);
    }

    public void onNextClicked() {
        mView.getNavigation().openNextFragment(KeyRequestConfirmFragment.instance(Type.REQUEST, mAmount, "Faddi Abdallah", mOperationType), true);
    }

}
