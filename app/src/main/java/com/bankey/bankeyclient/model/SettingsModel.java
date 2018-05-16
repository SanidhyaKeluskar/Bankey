package com.bankey.bankeyclient.model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;

import com.bankey.bankeyclient.AppLocationManager;
import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.MainApplication;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.InputAmountFragment;
import com.bankey.bankeyclient.fragment.BankAccountFragment;
import com.bankey.bankeyclient.fragment.LoginFragment;
import com.bankey.bankeyclient.fragment.SettingsFragment;
import com.bankey.bankeyclient.fragment.TutorialFragment;
import com.bankey.bankeyclient.fragment.UpdateProfileFragment;
import com.bankey.bankeyclient.fragment.UpdateServiceFeeFragment;
import com.bankey.bankeyclient.view.ShareFriendsDialog;

import java.math.BigDecimal;

/**
 * Created by Dima on 25.02.2018.
 */

public class SettingsModel extends FragmentModel<SettingsFragment> implements AppLocationManager.Listener {

    @Override
    public void onViewCreated(SettingsFragment view) {
        super.onViewCreated(view);

        view.setImage(UserSession.instance().getUserData().getPhoto());
        view.setName(UserSession.instance().getUserData().getName());
    }

    @Override
    public void onViewBound() {
        super.onViewBound();
        mView.getLocationManager().addListener(this);
    }

    @Override
    public void onViewUnbound() {
        super.onViewUnbound();
        mView.getLocationManager().removeListener(this);
    }

    public void onLogoutClicked() {
        Context context = mView.getActivity();
        DialogUtils.showConfirmation(mView.getActivity(), context.getString(R.string.settings_logout_title),
                context.getString(R.string.settings_logout_desc), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        UserSession.instance().getUserData().setSessionData(null);
                        // TODO do logout
                        mView.getNavigation().openNextFragment(new LoginFragment(), true);
                        mView.getNavigation().clearHistory();
                    }
                });
    }

    public void onEditProfileClicked() {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
        mView.getNavigation().openNextFragment(fragment, true);
    }

    public void onShareFriendsClicked() {
        new ShareFriendsDialog().show(mView.getActivity().getSupportFragmentManager(),
                ShareFriendsDialog.class.getName());
    }

    public void onBecomeKeyClicked() {
        TutorialFragment tutorialFragment = TutorialFragment.instantiate(TutorialModel.Type.BECOME_KEY);
        mView.getNavigation().openNextFragment(tutorialFragment, true);
    }

    public void onUpdateServiceFeeClicked() {
        mView.getNavigation().openNextFragment(UpdateServiceFeeFragment.instantiate(false), true);
    }

    public void onLinkBankAccountClick() {
        mView.getNavigation().openNextFragment(BankAccountFragment.instantiate(BankAccountModel.Initiator.DEFAULT), true);
    }

    public void onGpsChecked(boolean checked) {
        AppLocationManager locationManager = mView.getLocationManager();
        if (checked) {
            mView.setGpsChecked(locationManager.requestLocationUpdates());
        } else {
            locationManager.stopLocationUpdates();
        }
    }

    public void onKeyServiceChecked(boolean checked) {
        BigDecimal balance = UserSession.instance().getUserData().getBalance();
        if (checked && balance.compareTo(MainApplication.MIN_BALANCE_FOR_KEY) < 0) {
            // Show minimum balance dialog
            Activity ctx = mView.getActivity();
            DialogUtils.showConfirmation(ctx, ctx.getString(R.string.dialog_add_balance_title),
                    ctx.getString(R.string.dialog_add_balance_description),
                    ctx.getString(R.string.yes),
                    ctx.getString(R.string.later),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mView.getNavigation().openNextFragment(InputAmountFragment.instance(UserSession.OperationType.ADD), true);
                            mView.setKeyServiceChecked(false);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mView.setKeyServiceChecked(false);
                        }
                    });
        }

    }

    @Override
    public void onLocationRequestChanged(boolean started) {
        if (started) {
            // TODO key service
            mView.setGpsChecked(true);
        }
    }

    @Override
    public void onMyLocationChanged(Location location) {

    }
}
