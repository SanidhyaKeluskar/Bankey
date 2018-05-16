package com.bankey.bankeyclient.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.model.TutorialModel;
import com.bankey.bankeyclient.view.PagesIndicator;
import com.bankey.bankeyclient.view.TutorialRecycler;

import java.util.List;

/**
 * Created by Dima on 24.02.2018.
 */

public class TutorialFragment extends AbstractFragment<TutorialModel> {

    public static final String TYPE_EXTRA = "type_extra";

    private TutorialRecycler mRecyclerView;
    private RecyclerAdapter mAdapter;
    private PagesIndicator mIndicator;
    private View mStartButton;

    private TutorialModel.Type mType;

    @Override
    TutorialModel onCreateModel() {
        return new TutorialModel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = TutorialModel.Type.values()[getArguments().getInt(TYPE_EXTRA)];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.tutorial_recycler);
        mIndicator = view.findViewById(R.id.tutorial_pages_indicator);
        mRecyclerView.attachPagesIndicator(mIndicator);

        ((TextView)view.findViewById(R.id.button_next_text)).setText(R.string.start);
        mStartButton = view.findViewById(R.id.button_next);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModel.onPassed();
            }
        });
    }

    public void showPages(List<TutorialModel.PageData> pageDataList) {
        mAdapter = new RecyclerAdapter(pageDataList);
        mIndicator.update(pageDataList.size());
        mRecyclerView.setAdapter(mAdapter);
    }

    public TutorialModel.Type getType() {
        return mType;
    }

    private static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

        private List<TutorialModel.PageData> mDataList;

        RecyclerAdapter(List<TutorialModel.PageData> dataList) {
            mDataList = dataList;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tutorial_page, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            TutorialModel.PageData data = mDataList.get(position);
            holder.title.setText(data.title);
            holder.subtitle.setText(data.desc);
            holder.image.setImageResource(data.imageRes);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        static class Holder extends RecyclerView.ViewHolder {
            final ImageView image;
            final TextView title;
            final TextView subtitle;
            Holder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.tutorial_page_image);
                title = itemView.findViewById(R.id.tutorial_page_title);
                subtitle = itemView.findViewById(R.id.tutorial_page_subtitle);
            }
        }
    }

    public static TutorialFragment instantiate(TutorialModel.Type type) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_EXTRA, type.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

}
