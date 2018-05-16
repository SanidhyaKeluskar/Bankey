package com.bankey.bankeyclient.model;

import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.KeyDetailsFragment;
import com.bankey.bankeyclient.fragment.KeyRequestConfirmFragment;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class KeyDetailsModel extends FragmentModel<KeyDetailsFragment> {

    private final float mAmount;
    private final UserSession.OperationType mOperationType;

    public KeyDetailsModel(float amount, UserSession.OperationType operationType) {
        mAmount = amount;
        mOperationType = operationType;
    }

    @Override
    public void onViewCreated(KeyDetailsFragment view) {
        super.onViewCreated(view);

        mView.setHeaderTitle("Fadi Abdallah");
    }

    public void onNextClicked() {
        mView.getNavigation().openNextFragment(KeyRequestConfirmFragment.instance(KeyRequestConfirmModel.Type.REQUEST, mAmount, "Faddi Abdallah", mOperationType), true);
    }
}
