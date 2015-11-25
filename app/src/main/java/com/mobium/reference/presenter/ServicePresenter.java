package com.mobium.reference.presenter;

import android.view.View;
import com.mobium.reference.model.ServiceModel;
import com.mobium.reference.view.ServiceView;

/**
 *  on 25.07.15.
 * http://mobiumapps.com/
 */
public interface ServicePresenter {
    void onPhoneClick(String phone);
    void onButtonClick(View view);

    void showModel(ServiceModel model);
    ServicePresenter setScreen(ServiceView screen);

}
