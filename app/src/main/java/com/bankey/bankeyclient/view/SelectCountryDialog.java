package com.bankey.bankeyclient.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankey.bankeyclient.DataCache;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.api.data.CountryData;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Dima on 09.03.2018.
 */

public class SelectCountryDialog extends DialogFragment {

    private static final String SELECTED_COUNTRY_CODE = "selected_country_code";

    public interface Listener {
        void onCountrySelected(CountryData country);
    }

    private RecyclerView mRecycler;
    private String mSelectedCountryCode;
    private List<CountryData> mCountries;

    private Listener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCountries = DataCache.instance().getCountryList();
        mSelectedCountryCode = getArguments().getString(SELECTED_COUNTRY_CODE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecycler = new RecyclerView(getActivity());
        mRecycler.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return mRecycler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecycler.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new CountryListAdapter();
        adapter.setHasStableIds(true);
        mRecycler.setAdapter(adapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    private class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final CountryData data = mCountries.get(position);
            holder.name.setText(data.getName());
            holder.name.setTypeface(null, data.getCodeValue().equals(mSelectedCountryCode)
                    ? Typeface.BOLD : Typeface.NORMAL);
            ImageLoader.getInstance().displayImage(data.getFlag(), holder.flag);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onCountrySelected(data);
                    dismissAllowingStateLoss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCountries.size();
        }

        @Override
        public long getItemId(int position) {
            return mCountries.get(position).hashCode();
        }

        class Holder extends RecyclerView.ViewHolder {
            final ImageView flag;
            final TextView name;
            Holder(View itemView) {
                super(itemView);
                flag = itemView.findViewById(R.id.item_country_flag);
                name = itemView.findViewById(R.id.item_country_name);
            }
        }
    }

    public static SelectCountryDialog instantiate(String countryCode, Listener listener) {
        SelectCountryDialog dialog = new SelectCountryDialog();
        Bundle args = new Bundle();
        args.putString(SELECTED_COUNTRY_CODE, countryCode);
        dialog.setArguments(args);
        dialog.mListener = listener;
        return dialog;
    }
}
