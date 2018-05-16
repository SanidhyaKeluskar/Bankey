package com.bankey.bankeyclient.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.bankey.bankeyclient.R;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by DLutskov on 2/28/2018.
 */

public class PickupImageUtils {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_GALLERY = 102;

    public interface Listener {
        void onImageSelected(Uri imageUri);
    }

    private static List<Listener> sListeners = new ArrayList<>();

    public static void showAddImageDialog(final Activity activity) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(activity.getString(R.string.dialog_add_image_options));
        dialog.setMessage(activity.getString(R.string.dialog_select_image_source));
        dialog.setPositiveButton(activity.getString(R.string.gallery), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                dispatchPickPhotoGalleryIntent(activity);
            }
        });
        dialog.setNegativeButton(activity.getString(R.string.camera), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                dispatchTakePhotoIntent(activity);
            }
        });
        dialog.show();
    }

    private static void dispatchTakePhotoIntent(Activity activity) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(takeVideoIntent, REQUEST_IMAGE_CAPTURE);
    }

    private static void dispatchPickPhotoGalleryIntent(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_IMAGE_GALLERY);
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Uri imageUri = null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    imageUri = intent.getData();
                    break;
                case REQUEST_IMAGE_GALLERY:
                    imageUri = intent.getData();
                    break;
            }
        }

        if (imageUri != null) {
            for (Listener listener : sListeners) {
                listener.onImageSelected(imageUri);
            }
        }
    }

    public static void addListener(Listener listener) {
        sListeners.add(listener);
    }

    public static void removeListener(Listener listener) {
        sListeners.remove(listener);
    }

}
