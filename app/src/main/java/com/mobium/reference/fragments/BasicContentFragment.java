package com.mobium.reference.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.executing.ExecutingAsyncTask;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.views.VisibilityViewUtils;


/**
 *
 *
 * Date: 28.11.12
 * Time: 22:10
 */
public class BasicContentFragment extends BasicSaveStateFragment implements FragmentUtils.BackButtonHandler {


    private boolean isExecuting = false;

    private AsyncTask executingTask;
    private ExecutingAsyncTask runnableTask;

    private boolean isPendingCompleteExecuting = false;
    private ExecutingException pendingException;


    protected String getTitle() {
        return Config.get().design().defaultNavigationBar().getText(getContext());
    }

    public boolean onBackPressed() {

        if (isExecuting) {
            executingTask.cancel(true);
            isExecuting = false;
            return true;
        } else {
            return false;
        }

    }


    public void executeAsync(ExecutingAsyncTask task) {
        if (runnableTask != null) {
            throw new RuntimeException("Already executing a task");
        }
        runnableTask = task;
        performExecuting();
    }

    private void performExecuting() {
        executingTask = new AsyncTask<Void, Void, Void>() {

            private ExecutingException executingException;

            @Override
            protected void onPreExecute() {
                showProgress();
                runnableTask.beforeExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                executingTask = null;
                if (isAdded()) {
                    completeExecution(executingException);
                } else {
                    pendingException = executingException;
                    isPendingCompleteExecuting = true;
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    runnableTask.executeAsync();
                } catch (ExecutingException e) {
                    e.printStackTrace();
                    runnableTask.onError(e);
                    executingException = e;
                }
                return null;
            }
        }.execute();
    }

    private void completeExecution(ExecutingException exception) {
        if (exception != null) {
            hideProgress();
            onError(exception, this::performExecuting, () -> {
                        runnableTask.onCancel();
                        runnableTask = null;
                    }
            );
        } else {
            runnableTask.afterExecute();
            hideProgress();
            runnableTask = null;
        }
    }

    protected void onError(ExecutingException exception, final Runnable onRepeat, final Runnable onCancel) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        if (exception.isCanRepeat()) {
            builder.setMessage(getString(R.string.repeating_error_message, exception.getUserMessage()));
            builder.setPositiveButton(getString(R.string.repeating_error_positive), (dialogInterface, i) -> {
                onRepeat.run();
            });
            builder.setNegativeButton(getString(R.string.repeating_error_negative), (dialogInterface, i) -> {
                if (onCancel != null) {
                    onCancel.run();
                }
            });
        } else {
            builder.setMessage(exception.getUserMessage());
            builder.setNegativeButton(getString(R.string.not_repeating_error_negative), (dialogInterface, i) -> {
                if (onCancel != null) {
                    onCancel.run();
                }
            });
        }

        builder.setCancelable(true);
        builder.setOnCancelListener(dialogInterface -> {
            if (onCancel != null) {
                onCancel.run();
            }
        });

        builder.create().show();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isPendingCompleteExecuting) {
            completeExecution(pendingException);
            isPendingCompleteExecuting = false;
        }

        if (executingTask != null && executingTask.getStatus() == AsyncTask.Status.RUNNING) {
            showProgress();
        }

        if (runnableTask == null && !(this instanceof BasicLoadableFragment)) {
            hideProgress();
        }

//        getDashboardActivity().onPageView(getClass().getCanonicalName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (executingTask != null && executingTask.getStatus() == AsyncTask.Status.RUNNING) {
                executingTask.cancel(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //    RefWatcher refWatcher = getApplication().getRefWatcher();
        //    refWatcher.watch(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity activity = getActivity();

        if (activity instanceof FragmentUtils.ActionBarView) {
            String title = getTitle();
            if (title == null)
                title = getString(R.string.app_name);
            
            ((FragmentUtils.ActionBarView) activity).updateTitle(title);
            ((FragmentUtils.ActionBarView) activity).setGravity(FragmentUtils.ActionBarView.Gravity.left);
            ((FragmentUtils.ActionBarView) activity).setMode(FragmentUtils.ActionBarView.Mode.text);
//            ((FragmentUtils.ActionBarView) activity).setIconColor(R.color.color_accent);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    protected View getContentView() {
        return null;
    }

    protected View getProgressView() {
        return null;
    }


    protected void showProgress() {
        VisibilityViewUtils.hide(getContentView(), false);
        VisibilityViewUtils.show(getProgressView(), true);
    }

    protected void hideProgress() {
        VisibilityViewUtils.show(getContentView(), false);
        VisibilityViewUtils.hide(getProgressView(), true);
    }

    public ReferenceApplication getApplication() {
        return (ReferenceApplication) getActivity().getApplication();
    }


    public MainDashboardActivity getDashboardActivity() {
        return (MainDashboardActivity) getActivity();
    }

    public ReferenceApplication getReferenceApplication() {
        return (ReferenceApplication) getActivity().getApplication();
    }


    public void setupUI(final View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(view);
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }


    public void hideSoftKeyboard(View view) {
        Activity activity = this.getActivity();
        if (activity != null) {
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    protected boolean isHasSearch() {
        return false;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.top_filter).setVisible(false);
        menu.findItem(R.id.top_cart).setVisible(true);
        menu.findItem(R.id.top_share).setVisible(false);
        menu.findItem(R.id.top_points_list).setVisible(false);
        menu.findItem(R.id.top_map).setVisible(false);
        menu.findItem(R.id.top_search).setVisible(false);
    }


    public boolean isTopActionBarButtonMenu() {
        return false;
    }


    public boolean isFragmentUIActive() {
        return FragmentUtils.isUiFragmentActive(this);
    }

    public void setTitle(String title) {
        if (getActivity() instanceof FragmentUtils.ActionBarView)
            ((FragmentUtils.ActionBarView) getActivity()).updateTitle(title);
    }

}