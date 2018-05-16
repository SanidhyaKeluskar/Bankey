package com.bankey.bankeyclient.model;

import android.content.DialogInterface;

import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.KeyRequestConfirmationCodeFragment;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class KeyRequestConfirmationCodeModel  extends FragmentModel<KeyRequestConfirmationCodeFragment> {

    private final String mCode;

    public KeyRequestConfirmationCodeModel(String code) {
        mCode = code;
    }

    public void onCancelClicked() {
        DialogUtils.showConfirmation(mView.getActivity(), mView.getString(R.string.dialog_cancel_request),
                mView.getString(R.string.dialog_cancel_request_desc), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO cancel request
                        UserSession.instance().keyRequestType = 0;

                        mView.getNavigation().navigateBack();
                    }
                });
    }

}