package com.mobium.reference.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 *   25.03.15.
 * http://mobiumapps.com/
 * фрагмент, сохраняющий свое состояние
 *  определить следующие методы:
 *
 *  onSaveState
 *  onFirstTimeLaunched
 *  onRestoreState
 */

public class BasicSaveStateFragment extends Fragment {
    Bundle savedState;

    public BasicSaveStateFragment() {
        super();
        if (getArguments() == null)
            setArguments(new Bundle());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            onRestoreState(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null)
             onRestoreState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore State Here
        if (!restoreStateFromArguments()) {
            // First Time, Initialize something here
            onFirstTimeLaunched();
        }
    }

    /**
     * Called when the fragment is launched for the first time.
     * In the other words, fragment is now recreated.
     */
    protected void onFirstTimeLaunched() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            onRestoreState(getArguments());
        if (savedInstanceState != null)
            onRestoreState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Save State Here
        saveStateToArguments();
    }

    ////////////////////
    // Don't Touch !!
    ////////////////////
    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b != null)
                b.putBundle("internalSavedViewState8954201239547", savedState);
        }
    }

    ////////////////////
// Don't Touch !!
////////////////////
    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b != null) {
            savedState = b.getBundle("internalSavedViewState8954201239547");
            if (savedState != null) {
                restoreState();
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////
// Restore Instance State Here
/////////////////////////////////
    private void restoreState() {
        if (savedState != null) {
// For Example
//tv1.setText(savedState.getString("text"));
            onRestoreState(savedState);
        }
    }

    /**
     * Called when the fragment's activity has been
     * fragment's view hierarchy instantiated. It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state. This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-
     *                           a previous saved state, this is the state.
     */
    protected void onRestoreState(Bundle savedInstanceState) {

    }

    //////////////////////////////
    // Save Instance State Here
    //////////////////////////////
    private Bundle saveState() {
        Bundle state = new Bundle();
        // For Example
        //state.putString("text", tv1.getText().toString());
        onSaveState(state);
        return state;
    }


    protected void onSaveState(Bundle outState) {

    }


}
