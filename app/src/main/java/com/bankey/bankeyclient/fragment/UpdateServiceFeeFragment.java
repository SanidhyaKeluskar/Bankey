package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.model.UpdateServiceFeeModel;

import java.math.BigDecimal;

/**
 * Created by Dima on 02.03.2018.
 */

public class UpdateServiceFeeFragment extends AbstractFragment<UpdateServiceFeeModel> {

    public static final String BECOME_KEY_FLOW = "become_key_flow";

    private static final BigDecimal MIN_VALUE = new BigDecimal("0.01");
    private static final BigDecimal MAX_VALUE = new BigDecimal("3.0");

    private TextView mValueView;
    private View mPlusView;
    private View mMinusView;
    private View[] mMinValuesView;
    private View mButtonBack;
    private View mButtonNext;

    private BigDecimal mCurrentValue = new BigDecimal("0.01");
    private View mSelectedMinValueView;

    private boolean mBecomeKeyFlow;

    @Override
    UpdateServiceFeeModel onCreateModel() {
        return new UpdateServiceFeeModel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBecomeKeyFlow = getArguments().getBoolean(BECOME_KEY_FLOW);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service_fee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mValueView = view.findViewById(R.id.service_fee_value);
//        mCurrentValue = UserSession.instance().serviceFee; // TODO
        mValueView.setText(String.format("%.02f", mCurrentValue.floatValue()));

        initMinValues(view);
        initArrows(view);

        mButtonBack = view.findViewById(R.id.header_arrow_back);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModel.onBackButtonClick();
            }
        });

        mButtonNext = view.findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModel.onNextButtonClick();
            }
        });

    }

    private void initArrows(View view) {
        // Up Down arrows
        mPlusView = view.findViewById(R.id.service_fee_plus);
        mMinusView = view.findViewById(R.id.service_fee_minus);

        mPlusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentValue.equals(MAX_VALUE)) return;
                mCurrentValue = mCurrentValue.add(new BigDecimal("0.01"));
                mValueView.setText(String.format("%.02f", mCurrentValue.floatValue()));
            }
        });
        mMinusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentValue.equals(MIN_VALUE)) return;
                mCurrentValue = mCurrentValue.subtract(new BigDecimal("0.01"));
                mValueView.setText(String.format("%.02f", mCurrentValue.floatValue()));
            }
        });
    }

    private void initMinValues(View view) {
        // Init min values view
        ViewGroup minValuesContainer = view.findViewById(R.id.service_fee_min_container);
        mMinValuesView = new View[minValuesContainer.getChildCount()];
        for (int i = 0; i < minValuesContainer.getChildCount(); i++) {
            mMinValuesView[i] = ((ViewGroup)minValuesContainer.getChildAt(i)).getChildAt(0);
            mMinValuesView[i].setTag(i + 1);
            mMinValuesView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMinValueViewSelected(view);
                }
            });
            float minAmount = 3f; // TODO
            if (minAmount == (int)mMinValuesView[i].getTag()) {
                mMinValuesView[i].setSelected(true);
                mSelectedMinValueView = mMinValuesView[i];
            }
        }
    }

    private void onMinValueViewSelected(View selectedView) {
        if (selectedView == mSelectedMinValueView) {
            return;
        }
        mSelectedMinValueView.setSelected(false);
        mSelectedMinValueView.requestLayout();
        selectedView.setSelected(true);
        selectedView.requestLayout();

        mSelectedMinValueView = selectedView;
    }

    public int getSelectedMinValue() {
        return (int)mSelectedMinValueView.getTag();
    }

    public float getFeeValue() {
        return mCurrentValue.floatValue();
    }

    public boolean isBecomeKeyFlow() {
        return mBecomeKeyFlow;
    }

    public static UpdateServiceFeeFragment instantiate(boolean becomeKeyFlow) {
        Bundle args = new Bundle();
        args.putBoolean(BECOME_KEY_FLOW, becomeKeyFlow);
        UpdateServiceFeeFragment fragment = new UpdateServiceFeeFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
