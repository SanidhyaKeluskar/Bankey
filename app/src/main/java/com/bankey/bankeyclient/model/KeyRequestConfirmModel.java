package com.bankey.bankeyclient.model;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.fragment.AccountFragment;
import com.bankey.bankeyclient.fragment.KeyRequestConfirmFragment;
import com.bankey.bankeyclient.fragment.KeyRequestConfirmationCodeFragment;

import java.text.DecimalFormat;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class KeyRequestConfirmModel extends FragmentModel<KeyRequestConfirmFragment> {

    public enum Type {
        REQUEST, // Confirm request to key to exchange amount
        RESPONSE, // Key - confirmed request
    }

    private static final String NAME_HTML = "<font color='#4786E4'><b>%s</b></font>";

    private final Type mType;
    private final float mAmount;
    private final String mName;
    private final UserSession.OperationType mOperationType;

    public KeyRequestConfirmModel(Type type, float amount, String name, UserSession.OperationType operationType) {
        mType = type;
        mAmount = amount;
        mName = name;
        mOperationType = operationType;
    }

    @Override
    public void onViewCreated(KeyRequestConfirmFragment view) {
        super.onViewCreated(view);

        String title = String.format(view.getString(mType == Type.REQUEST ? R.string.key_request_confirm_request : R.string.key_request_confirm_request_accepted),
                String.format(NAME_HTML, mName)) + "<b> $" +
                new DecimalFormat("#0.00").format(mAmount) + "</b>";
        view.setTitle(title);
        view.setButtonText(mType == Type.REQUEST ? R.string.accept : R.string.confirm);
    }

    public void onAcceptClicked() {
        if (mType == Type.REQUEST) {
            // TODO save
            UserSession.instance().keyRequestType = 1;
            UserSession.instance().keyRequestAmount = mAmount;
            UserSession.instance().keyRequestOperationType = mOperationType;

            mView.getNavigation().clearHistory();
            mView.getNavigation().openNextFragment(new AccountFragment(), true);
        } else {
            String code = "A2c45694"; // TODO
            UserSession.instance().generatedCode = code;
            UserSession.instance().keyRequestType = 3;
            mView.getNavigation().openNextFragment(KeyRequestConfirmationCodeFragment.instance(code), true);
            mView.getNavigation().removeFromHistory(KeyRequestConfirmFragment.class);
        }

    }
}
