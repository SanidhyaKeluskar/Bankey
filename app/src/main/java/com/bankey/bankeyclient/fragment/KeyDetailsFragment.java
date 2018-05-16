package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.data.UserSession;
import com.bankey.bankeyclient.model.KeyDetailsModel;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

/**
 * Created by DLutskov on 4/3/2018.
 */

public class KeyDetailsFragment extends AbstractFragment<KeyDetailsModel> {

    private static final String AMOUNT_KEY = "amount";
    private static final String OPERATION_KEY = "operation";

    private TextView mHeaderTitle;
    private ImageView mFace;
    private TextView mName;
    private TextView mAddress;
    private SimpleRatingBar mStars;
    private View mDirections;
    private View mReviews;
    private View mContact;

    @Override
    KeyDetailsModel onCreateModel() {
        UserSession.OperationType operationType = UserSession.OperationType.values()[getArguments().getInt(OPERATION_KEY)];
        return new KeyDetailsModel(getArguments().getFloat(AMOUNT_KEY), operationType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_key_details, container, false);
    }
    ////////////////////////
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeaderTitle = view.findViewById(R.id.header_title);
        mFace  = view.findViewById(R.id.key_details_face);
        mName  = view.findViewById(R.id.key_details_name);
        mAddress  = view.findViewById(R.id.key_details_address);
        mStars  = view.findViewById(R.id.key_details_rate);
        mDirections  = view.findViewById(R.id.key_details_directions);
        mReviews  = view.findViewById(R.id.key_details_reviews);
        mContact  = view.findViewById(R.id.key_details_contact);

        view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.onNextClicked();
            }
        });
    }

    public void setHeaderTitle(String title) {
        mHeaderTitle.setText(title);
    }

    public static KeyDetailsFragment instance(float amount, UserSession.OperationType operationType) {
        KeyDetailsFragment f = new KeyDetailsFragment();
        Bundle args = new Bundle();
        args.putFloat(AMOUNT_KEY, amount);
        args.putInt(OPERATION_KEY, operationType.ordinal());
        f.setArguments(args);
        return f;
    }
}
