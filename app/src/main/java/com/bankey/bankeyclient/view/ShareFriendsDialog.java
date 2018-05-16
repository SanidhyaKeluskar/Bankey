package com.bankey.bankeyclient.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.SharingUtils;

/**
 * Created by Dima on 07.03.2018.
 */

public class ShareFriendsDialog extends DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_share_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.dialog_share_facebook).setOnClickListener(this);
        view.findViewById(R.id.dialog_share_twitter).setOnClickListener(this);
        view.findViewById(R.id.dialog_share_whatsapp).setOnClickListener(this);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_share_facebook) {
            SharingUtils.shareFacebook(getActivity());
        } else if (view.getId() == R.id.dialog_share_twitter) {
            SharingUtils.shareTweeter(getActivity());
        } else if (view.getId() == R.id.dialog_share_whatsapp) {
            SharingUtils.shareWhatsapp(getActivity());
        }
        dismissAllowingStateLoss();
    }
}
