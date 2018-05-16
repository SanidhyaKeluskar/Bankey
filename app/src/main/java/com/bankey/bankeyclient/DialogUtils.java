package com.bankey.bankeyclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * Created by Dima on 26.02.2018.
 */

public class DialogUtils {

    public static void showAlert(Activity activity, String title, String subtitle) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title);
        dialog.setMessage(subtitle);
        dialog.setPositiveButton(activity.getString(R.string.ok), null);
        dialog.show();
    }

    public static void showAlert(Activity activity, int title, int subtitle) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title);
        dialog.setMessage(subtitle);
        dialog.setPositiveButton(activity.getString(R.string.ok), null);
        dialog.show();
    }

    public static void showError(Activity activity, int error) {
        showAlert(activity, R.string.error, error);
    }

    public static void showError(Activity activity, String error) {
        showAlert(activity, activity.getString(R.string.error), error);
    }

    public static void showSuccess(Activity activity, int success) {
        showAlert(activity, R.string.success, success);
    }

    public static void showSuccess(Activity activity, String success) {
        showAlert(activity, activity.getString(R.string.success), success);
    }

    public static void showConfirmation(Activity activity, String title, String subtitle,
                                        OnClickListener positiveListener) {
        showConfirmation(activity, title, subtitle, activity.getString(R.string.yes),
                activity.getString(R.string.no), positiveListener, null);
    }

    public static void showConfirmation(Activity activity, String title, String subtitle, String yes, String no,
                                        OnClickListener positiveListener, OnClickListener negativeListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title);
        dialog.setMessage(subtitle);
        dialog.setPositiveButton(yes, positiveListener);
        dialog.setNegativeButton(no, negativeListener);
        dialog.show();

    }



}
