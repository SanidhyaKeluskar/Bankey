package com.bankey.bankeyclient.model;

import android.app.Activity;
import android.content.DialogInterface;

import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.AddressInputFragment;
import com.bankey.bankeyclient.fragment.UpdateServiceFeeFragment;

/**
 * Created by Dima on 03.03.2018.
 */

public class AddressInputModel extends FragmentModel<AddressInputFragment> {

    private boolean exitConfirmed;

    @Override
    public boolean onActivityBackClicked() {
        if (!UserSession.instance().getUserData().isBankAccount) {
            // There is previous page in history (bank account)
            return false;
        }

        // It's first page of become key flow - need confirmation message
        if (exitConfirmed) {
            return false;
        }
        handleBack();
        return true;
    }

    public void onBackButtonClick() {
        handleBack();
    }

    private void handleBack() {
        Activity ctx = mView.getActivity();
        DialogUtils.showConfirmation(ctx, ctx.getString(R.string.warning),
                ctx.getString(R.string.process_cancel_confirmation), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exitConfirmed = true;
                        mView.getNavigation().navigateBack();
                    }
                });
    }

    public void onNextButtonClicked() {
        if (mView.hasEmptyFields()) {
            DialogUtils.showError(mView.getActivity(), R.string.dialog_empty_fields_error);
            return;
        }

        // Open service fee page
        mView.getNavigation().openNextFragment(UpdateServiceFeeFragment.instantiate(true), true);
    }


}
