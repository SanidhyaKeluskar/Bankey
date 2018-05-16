package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bankey.bankeyclient.AppLocationManager;
import com.bankey.bankeyclient.MainActivity;
import com.bankey.bankeyclient.Navigation;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.model.FragmentModel;

/**
 * Created by Dima on 24.02.2018.
 */

public abstract class AbstractFragment<M extends FragmentModel> extends Fragment {

    Navigation mNavigation;
    AppLocationManager mLocationManager;
    View mRootView;
    M mModel;

    View mProgressDialog;

    private boolean wasViewCreated;

    abstract M onCreateModel();

    public final boolean onActivityBackPressed() {
        return mModel.onActivityBackClicked();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigation = ((MainActivity)getActivity()).getNavigation();
        mLocationManager = ((MainActivity)getActivity()).getLocationManager();
        mModel = onCreateModel();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootView = view;

        View back = view.findViewById(R.id.header_back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNavigation.navigateBack();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!wasViewCreated) {
            mModel.onViewCreated(this);
            wasViewCreated = true;
        }
        mModel.onViewBound();
    }

    @Override
    public void onStop() {
        super.onStop();
        mModel.onViewUnbound();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mModel.onViewDestroyed();
    }

    @Override
    public void onDetach() {
        ((MainActivity)getActivity()).onDetachFragment(this);
        super.onDetach();
    }

    public Navigation getNavigation() {
        return mNavigation;
    }

    public AppLocationManager getLocationManager() {
        return mLocationManager;
    }

    public void showProgress() {
        if (mProgressDialog == null) {
            ViewGroup container = (ViewGroup) mRootView;
            mProgressDialog = LayoutInflater.from(getActivity()).inflate(R.layout.progress_dialog, container, false);
            container.addView(mProgressDialog);
        }
        mProgressDialog.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.setVisibility(View.GONE);
        }
    }

}
