package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.model.UpdateProfileModel;
import com.bankey.bankeyclient.view.PersonalDetailsPageBinder;

/**
 * Created by Dima on 10.03.2018.
 */

public class UpdateProfileFragment extends AbstractFragment<UpdateProfileModel> {

    private PersonalDetailsPageBinder mBinder;

    @Override
    UpdateProfileModel onCreateModel() {
        return new UpdateProfileModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_personal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinder = new PersonalDetailsPageBinder(view, PersonalDetailsPageBinder.Type.UPDATE, mModel);

        ImageView headerArrow = (ImageView)view.findViewById(R.id.header_arrow_back);
        headerArrow.setImageResource(R.drawable.icon_arrow_back_green);
        headerArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModel.onBackButtonClicked();
            }
        });
    }

    public PersonalDetailsPageBinder getBinder() {
        return mBinder;
    }

}
