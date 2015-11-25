package com.mobium.reference.fragments;

import android.os.AsyncTask;

import com.mobium.reference.anotations.NeedInternetAccess;
import com.mobium.reference.utils.executing.ExecutingException;


/**
 *
 *
 * Date: 13.12.12
 * Time: 2:35
 */
@NeedInternetAccess
public abstract class BasicLoadableFragment extends BasicContentFragment {
    private boolean isLoaded = false;
    private boolean isPrepared = false;
    private boolean isPendingCompleteLoading = false;
    private ExecutingException pendingException;
    private AsyncTask<Void, Void, Void> loadingTask;

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
    }

    protected boolean isSilentLoading() {
        return false;
    }

    protected void onSilentError(ExecutingException e) {

    }

    protected boolean needLoading() {
        return false;
    }

    private void completeExecution(ExecutingException exception) {
        if (exception != null) {
            if (isSilentLoading()) {
                onSilentError(exception);
            } else {
                onError(exception, this::performLoading, () -> {
                            hideProgress();
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                }
                );
            }
        } else {

            afterLoaded();
            afterPrepared();
            hideProgress();
            isPrepared = true;
            isLoaded = true;
        }
    }

    protected void performLoading() {
        if (!isResumed())
            return;

        loadingTask = new AsyncTask<Void, Void, Void>() {

            private ExecutingException executingException;

            @Override
            protected void onPreExecute() {
                showProgress();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadingTask = null;
                if (isAdded()) {
                    completeExecution(executingException);
                } else {
                    pendingException = executingException;
                    isPendingCompleteLoading = true;
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    loadInBackground();
                } catch (ExecutingException e) {
                    e.printStackTrace();
                    executingException = e;
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        isPrepared = false;

        boolean isLoadingNeeded = needLoading();
        if (!isLoadingNeeded && !isLoaded) {
            isPrepared = true;
            doesntNeedLoading();
            afterPrepared();
            hideProgress();
        }

        if (!isLoaded && isLoadingNeeded) {
            if (isPendingCompleteLoading) {
                completeExecution(pendingException);
                isPendingCompleteLoading = false;
                return;
            }

            if (loadingTask != null && loadingTask.getStatus() == AsyncTask.Status.RUNNING) {
                showProgress();
                return;
            }

            performLoading();
        }

        if (isLoaded) {
            isPrepared = true;
            alreadyLoaded();
            afterPrepared();
            hideProgress();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (loadingTask != null && loadingTask.getStatus() == AsyncTask.Status.RUNNING) {
                loadingTask.cancel(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    protected abstract void loadInBackground() throws ExecutingException;

    protected void afterLoaded() {
    }

    protected void alreadyLoaded() {
    }

    protected void doesntNeedLoading() {
    }

    protected void afterPrepared() {
    }
}