package com.bankey.bankeyclient.tasks;


import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.bankey.bankeyclient.api.http.RequestException;

import java.util.concurrent.atomic.AtomicBoolean;


public abstract class AbstractBackgroundTask<T> implements Runnable {

    public interface Listener<T> {
        @WorkerThread
        void onTaskFinished(AbstractBackgroundTask<T> task);
    }

    public enum ResultType {
        SUCCESS,
        NO_DATA, // requestData returned null
        REQUEST_ERROR, // HttpClient throws RequestException exception
        TECHNICAL_ERROR, // Other exceptions thrown on client side
        CANCELLED, // Task was cancelled by client
        NONE, // Task was not started or finished
    }

    private final AtomicBoolean isRunning = new AtomicBoolean();

    /**
     * Reference to thread in which task was started when start() method called
     */
    private Thread mThread;

    private Listener<T> mListener;

    private ResultType mResultType = ResultType.NONE;

    private T mResult;

    private Exception mException;

    private void onCatchException(@NonNull final Exception e, @NonNull final ResultType type) {
        if (mResultType == ResultType.CANCELLED) {
            return;
        }

        mException = e;
        mResultType = type;

        if (mListener != null)
            mListener.onTaskFinished(this);
    }

    protected abstract T requestData() throws Exception;

    @Override
    public final void run() {
        isRunning.set(true);

        Exception exception = null;
        try {
            mResult = requestData();
            if (mResult == null) throw new NoDataException("requestData returns NULL, task: " + this.getClass().getSimpleName());
        } catch (NoDataException e) {
            onCatchException(e, ResultType.NO_DATA);
            exception = e;
        } catch (RequestException e) {
            onCatchException(e, ResultType.REQUEST_ERROR);
            exception = e;
        } catch (Exception e) {
            onCatchException(e, ResultType.TECHNICAL_ERROR);
            exception = e;
        } finally {
            if (exception == null && mResult != null && !Thread.interrupted() && mResultType != ResultType.CANCELLED) {
                mResultType = ResultType.SUCCESS;
                if (mListener != null) {
                    mListener.onTaskFinished(this);
                }
            }
            isRunning.set(false);
        }
    }

    /**
     * Id used to differ tasks (for using multiple running tasks with PeriodicTasks
     * @return
     */
    public String getId() {
        return getClass().getSimpleName();
    }

    public final AbstractBackgroundTask<T> setListener(Listener<T> listener) {
        mListener = listener;
        return this;
    }

    public final AbstractBackgroundTask<T> start() {
        mThread = new Thread(this);
        mThread.start();
        return this;
    }

    public final void stop() {
        if (mThread != null) {
            mThread.interrupt();
            mResultType = ResultType.CANCELLED;
            if (mListener != null)
                mException = new TaskCancelledException("stop() was called for this task!");
                mListener.onTaskFinished(this);
        }
    }

    public final @NonNull T getResult() {
        return mResult;
    }

    public final ResultType getResultType() {
        return mResultType;
    }

    public final Exception getException() {
        return mException;
    }

    public final boolean isRunning() {
        return isRunning.get();
    }

    private static class NoDataException extends Exception {

        NoDataException(String log) {
            super(log);
        }

    }

    private static class TaskCancelledException extends Exception {

        TaskCancelledException(String log) {
            super(log);
        }

    }
}
