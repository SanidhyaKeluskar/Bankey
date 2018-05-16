package com.bankey.bankeyclient.model;

import com.bankey.bankeyclient.DialogUtils;
import com.bankey.bankeyclient.fragment.AbstractFragment;
import com.bankey.bankeyclient.tasks.AbstractBackgroundTask;

/**
 * Created by Dima on 25.02.2018.
 */

public class FragmentModel<V extends AbstractFragment> {

    V mView;
    boolean isViewBound;

    private boolean isRequestExecuting;

    public void onViewCreated(V view) {
        mView = view;
    }

    public void onViewBound() {
        isViewBound = true;
    }

    public void onViewUnbound() {
        isViewBound = false;
    }

    public void onViewDestroyed() {
        mView = null;
    }

    public boolean onActivityBackClicked() {
        // Prevent back if request executing
        if (isRequestExecuting) {
            return true;
        }
        return false;
    }

    public void onBackButtonClicked() {
        mView.getNavigation().navigateBack();
    }

    void runOnUIThread(Runnable task) {
        if (mView == null || mView.getActivity() == null) {
            return;
        }
        mView.getActivity().runOnUiThread(task);
    }

    void performRequestTask(AbstractBackgroundTask task, final Runnable calback) {
        mView.showProgress();
        isRequestExecuting = true;
        task.setListener(new AbstractBackgroundTask.Listener() {
            @Override
            public void onTaskFinished(AbstractBackgroundTask task) {
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        isRequestExecuting = false;
                        mView.hideProgress();
                        calback.run();
                    }
                });
            }
        }).start();
    }

}
