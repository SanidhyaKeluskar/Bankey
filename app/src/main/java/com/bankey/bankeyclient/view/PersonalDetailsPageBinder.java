package com.bankey.bankeyclient.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.api.data.UserData;
import com.bankey.bankeyclient.data.UserSession;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Dima on 01.03.2018.
 */

public class PersonalDetailsPageBinder implements View.OnClickListener {

    public interface Listener {
        void onNextClick();
        void onPhotoClick();
    }

    public enum Type {
        SIGN_UP,
        UPDATE
    }

    private ViewGroup mContainer;
    private ImageView mPhoto;
    private EditText mInputName;
    private EditText mInputSurname;
    private EditText mInputDOB;
    private EditText mInputEmail;
    private RadioGroup mInputGender;

    private EditText mInputCountry;
    private EditText mInputAddress;
    private EditText mInputCity;

    private View mButtonNext;

    private Calendar mCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDOB(new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(mCalendar.getTime()));
            mInputEmail.requestFocus();
        }
    };

    private final Listener mListener;
    private Type mType;

    private Uri mImageUri;

    public PersonalDetailsPageBinder(View rootView, Type type, Listener listener) {
        mListener = listener;
        mType = type;

        mContainer = rootView.findViewById(R.id.login_personal_details_container);
        mPhoto = rootView.findViewById(R.id.login_personal_details_photo);
        mInputName = rootView.findViewById(R.id.login_personal_details_input_name);
        mInputSurname = rootView.findViewById(R.id.login_personal_details_input_surname);
        mInputDOB = rootView.findViewById(R.id.login_personal_details_input_birthday);
        mInputGender = rootView.findViewById(R.id.login_personal_details_gender);

        mInputEmail = rootView.findViewById(R.id.login_personal_details_input_email);
        mInputCountry = rootView.findViewById(R.id.input_address_country);
        mInputAddress = rootView.findViewById(R.id.input_address_line);
        mInputCity = rootView.findViewById(R.id.input_address_city);

        mButtonNext = rootView.findViewById(R.id.button_next);
        TextView btnText = rootView.findViewById(R.id.button_next_text);
        btnText.setText(type == Type.SIGN_UP ? R.string.next : R.string.save);

        initFieldsAvailability(type);
        initEditors(type);

        if (type == Type.UPDATE) {
            initStaticFields();
            mInputEmail.requestFocus();
        }

        mButtonNext.setOnClickListener(this);
        mPhoto.setOnClickListener(this);
        mInputDOB.setOnClickListener(this);
    }

    private void initFieldsAvailability(Type type) {
        mInputName.setEnabled(type == Type.SIGN_UP);
        mInputSurname.setEnabled(type == Type.SIGN_UP);
        mInputDOB.setEnabled(type == Type.SIGN_UP);
        mInputEmail.setEnabled(type == Type.SIGN_UP);
        mInputGender.getChildAt(0).setEnabled(type == Type.SIGN_UP);
        mInputGender.getChildAt(1).setEnabled(type == Type.SIGN_UP);

        boolean showKeyFields = type == Type.UPDATE && UserSession.instance().getUserData().isTeller();
        mInputCountry.setVisibility(showKeyFields ? View.VISIBLE : View.GONE);
        mInputAddress.setVisibility(showKeyFields ? View.VISIBLE : View.GONE);
        mInputCity.setVisibility(showKeyFields ? View.VISIBLE : View.GONE);
    }

    private void initEditors(Type type) {
        if (type == Type.SIGN_UP) {
            mInputSurname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT
                            || i == EditorInfo.IME_ACTION_NEXT) {
                        openDatePickerDialog();
                    }
                    return true;
                }
            });
        }
        mInputCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        || i == EditorInfo.IME_ACTION_DONE) {
                    mListener.onNextClick();
                }
                return true;
            }
        });
    }

    private void initStaticFields() {
        UserData userData = UserSession.instance().getUserData();
        mInputName.setText(userData.getName());
        mInputSurname.setText(userData.getSurname());
        mInputDOB.setText(userData.getBirthday());
        mInputEmail.setText(userData.getEmail());
        // TODO Gender
        mInputGender.check("M".equals(UserSession.instance().getUserData().getBirthday())
                ? R.id.login_personal_details_male : R.id.login_personal_details_female);


        mInputCountry.setText(userData.getAddress().getCountry());
        mInputAddress.setText(userData.getAddress().getLines());
        mInputCity.setText(userData.getAddress().getCity());
    }

    public String getName() {
        return mInputName.getText().toString();
    }

    public String getSurname() {
        return mInputSurname.getText().toString();
    }

    public String getBirthday() {
        return mInputDOB.getText().toString();
    }

    public String getGender() {
        return mInputGender.getCheckedRadioButtonId() == R.id.login_personal_details_male ? "M" : "F"; // TODO
    }

    public String getEmail() {
        return mInputEmail.getText().toString();
    }

    public String getCountry() {
        return mInputCountry.getText().toString();
    }

    public String getAddress() {
        return mInputAddress.getText().toString();
    }

    public String getCity() {
        return mInputCity.getText().toString();
    }

    public Uri getImageUri() {
        return mImageUri;
    }

    public boolean hasEmptyFields() {
        boolean hasEmptyMainFields = getName().isEmpty() || getBirthday().isEmpty() || mInputGender.getCheckedRadioButtonId() == -1;
        boolean hasEmptySecFields = getCountry().isEmpty() || getAddress().isEmpty() || getCity().isEmpty();
        boolean showKeyFields = mType == Type.UPDATE && UserSession.instance().getUserData().isTeller();
        return showKeyFields ? hasEmptyMainFields && hasEmptySecFields : hasEmptyMainFields;
    }

    public void displayImage(Uri imageUri) {
        mImageUri = imageUri;
        ImageLoader.getInstance().displayImage(imageUri.toString(), mPhoto);
    }

    public void updateDOB(String text) {
        mInputDOB.setText(text);
    }

    public void requestFocus() {
        mInputName.requestFocus();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == mPhoto.getId()) {
            mListener.onPhotoClick();
        } else if (view.getId() == mButtonNext.getId()) {
            mListener.onNextClick();
        } else if (view.getId() == mInputDOB.getId()) {
            openDatePickerDialog();
        }
    }

    public void closeKeyboard() {
        View focusedView = mInputCity;
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            View child = mContainer.getChildAt(i);
            if (child.isFocused()) {
                focusedView = child;
                break;
            }
        }
        InputMethodManager imm = (InputMethodManager) mContainer.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    private void openDatePickerDialog() {
        new DatePickerDialog(mInputDOB.getContext(),
                AlertDialog.THEME_HOLO_LIGHT,
                mDatePickerListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}
