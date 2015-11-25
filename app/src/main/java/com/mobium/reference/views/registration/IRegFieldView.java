package com.mobium.reference.views.registration;

import android.util.Pair;
import android.view.ViewGroup;
import com.mobium.userProfile.ResponseParams.RegField;

/**
 *  on 19.07.15.
 * http://mobiumapps.com/
 */
public interface IRegFieldView {
    void showRegFieldView(ViewGroup where, RegField field);

    rx.Observable<Pair<String, RegField>> changeText();

    rx.Observable<Boolean> correct();
}
