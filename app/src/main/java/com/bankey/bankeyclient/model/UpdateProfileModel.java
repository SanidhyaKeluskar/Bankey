package com.bankey.bankeyclient.model;

import android.net.Uri;

import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.R;
import com.bankey.bankeyclient.fragment.UpdateProfileFragment;
import com.bankey.bankeyclient.tasks.AbstractBackgroundTask;
import com.bankey.bankeyclient.tasks.UpdateProfileTask;
import com.bankey.bankeyclient.view.PersonalDetailsPageBinder;

/**
 * Created by Dima on 10.03.2018.
 */

public class UpdateProfileModel extends FragmentModel<UpdateProfileFragment>
        implements PersonalDetailsPageBinder.Listener, PickupImageUtils.Listener {

    @Override
    public void onViewCreated(UpdateProfileFragment view) {
        super.onViewCreated(view);
        PickupImageUtils.addListener(this);
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        PickupImageUtils.removeListener(this);
    }

    @Override
    public void onImageSelected(Uri imageUri) {
        mView.getBinder().displayImage(imageUri);
    }

    @Override
    public void onNextClick() {
        if (mView.getBinder().hasEmptyFields()) {
            DialogUtils.showError(mView.getActivity(), R.string.dialog_empty_fields_error);
            return;
        }

        PersonalDetailsPageBinder binder = mView.getBinder();
        binder.closeKeyboard();
        final AbstractBackgroundTask task = new UpdateProfileTask(binder.getImageUri(), binder.getCountry(),
                binder.getAddress(), binder.getCity());
        performRequestTask(task, new Runnable() {
            @Override
            public void run() {
                if (task.getResultType() == AbstractBackgroundTask.ResultType.SUCCESS) {
                    mView.getNavigation().navigateBack();
                } else {
                    DialogUtils.showError(mView.getActivity(), task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void onPhotoClick() {
        PickupImageUtils.showAddImageDialog(mView.getActivity());
    }
}
