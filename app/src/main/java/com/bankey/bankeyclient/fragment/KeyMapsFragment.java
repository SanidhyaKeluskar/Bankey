package com.bankey.bankeyclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.model.KeyMapsModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class KeyMapsFragment extends AbstractFragment<KeyMapsModel> {

    private static final String AMOUNT_KEY = "amount_key";
    private static final String OPERATION_KEY = "operation_key";

    private MapView mapView;
    private View mKeyLoadingView;
    private View mLocationLoadingView;
    private View mSearchContainer;

    private MarkerInfoAdapter mMarkerAdapter;

    @Override
    KeyMapsModel onCreateModel() {
        UserSession.OperationType type = UserSession.OperationType.values()[getArguments().getInt(OPERATION_KEY)];
        return new KeyMapsModel(getArguments().getFloat(AMOUNT_KEY), type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_key_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mKeyLoadingView = view.findViewById(R.id.key_map_searching_container);
        mLocationLoadingView = view.findViewById(R.id.key_map_my_location);
        mSearchContainer = view.findViewById(R.id.key_map_search_container);

        initMap(view, savedInstanceState);

        view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.onNextClicked();
            }
        });
    }

    private void initMap(View view, Bundle savedInstanceState) {
        // Gets the MapView from the XML layout and creates it
        mapView = view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Delay used for faster fragment loading
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.setVisibility(View.VISIBLE);
                // Gets to GoogleMap from the MapView and does initialization stuff
                mapView.getMapAsync(mModel);
            }
        }, 400);

        mMarkerAdapter = new MarkerInfoAdapter(getActivity(), mModel);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void setLoadingKeysVisibility(int visibility) {
        mKeyLoadingView.setVisibility(visibility);
    }

    public void setSearchContainerVisibility(int visibility) {
        mSearchContainer.setVisibility(visibility);
    }

    public void setLocationContainerVisibility(int visibility) {
        mLocationLoadingView.setVisibility(visibility);
    }

    public MarkerInfoAdapter getMarkerAdapter() {
        return mMarkerAdapter;
    }

    static class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mView;
        private TextView mName;
        private TextView mService;
        private TextView mDistanceTime;
        private ImageView mDistanceImage;
        private TextView mRate;
        private SimpleRatingBar mStars;

        private KeyMapsModel mModel;

        MarkerInfoAdapter(Context ctx, KeyMapsModel model) {
            mModel = model;
            mView = LayoutInflater.from(ctx).inflate(R.layout.layout_map_key, null);

            mName = mView.findViewById(R.id.key_maps_name);
            mService = mView.findViewById(R.id.key_maps_service);
            mDistanceTime = mView.findViewById(R.id.key_maps_distance_time);
            mDistanceImage = mView.findViewById(R.id.key_maps_distance_image);
            mRate = mView.findViewById(R.id.key_maps_rate);
            mStars = mView.findViewById(R.id.key_maps_stars);
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            Context ctx = mView.getContext();
            mName.setText("Fadi Adballah");
            mService.setText(Html.fromHtml(ctx.getString(R.string.key_maps_service) + "<b> 4 L.L</b>"));
            mDistanceTime.setText(Html.fromHtml("<b>3 min </b>" + ctx.getString(R.string.away)));
            return mView;
        }

    }

    public static KeyMapsFragment instance(float amount, UserSession.OperationType operationType) {
        KeyMapsFragment f = new KeyMapsFragment();
        Bundle args = new Bundle();
        args.putFloat(AMOUNT_KEY, amount);
        args.putInt(OPERATION_KEY, operationType.ordinal());
        f.setArguments(args);
        return f;
    }
}
