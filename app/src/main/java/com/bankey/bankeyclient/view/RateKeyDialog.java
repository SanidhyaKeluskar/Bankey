package com.bankey.bankeyclient.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bankey.bankeyclient.R;

/**
 * Created by Dima on 09.03.2018.
 */

public class RateKeyDialog extends DialogFragment {

    private static final String NAME_HTML = "<font color='#4786E4'><b>%s</b></font>";

    private static final String KEY_NAME = "key_name";

    private String mName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mName = getArguments().getString(KEY_NAME);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_key_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.key_rate_text);
        String text = getString(R.string.key_rate_subtitle);
        String htmlText = String.format(NAME_HTML, mName);
        textView.setText(Html.fromHtml(String.format(text, htmlText)));

        view.findViewById(R.id.key_rate_later).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rate later
                dismissAllowingStateLoss();
            }
        });
        view.findViewById(R.id.key_rate_and_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rate and Review
                dismissAllowingStateLoss();
            }
        });
    }

    public static RateKeyDialog instantiate(String keyName) {
        RateKeyDialog dialog = new RateKeyDialog();
        Bundle args = new Bundle();
        args.putString(KEY_NAME, keyName);
        dialog.setArguments(args);
        return dialog;
    }
}