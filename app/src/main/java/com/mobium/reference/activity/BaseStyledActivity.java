package com.mobium.reference.activity;


import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import com.mobium.client.ShopDataStorage;
import com.mobium.new_api.Api;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.reference.utils.NetworkStateReceiver;


/**
 *  on 20.06.15.
 * http://mobiumapps.com/
 */
public class BaseStyledActivity extends  android.support.v7.app.AppCompatActivity  {

    protected NetworkStateReceiver networkStateReceiver;

    protected Toolbar toolbar;
    protected ActionBar actionBar;
    protected FrameLayout container;

    private ButtonState state;
    protected ReferenceApplication application;
    protected ShopDataStorage storage;
    protected Api api;

    private IntentFilter intentFilter = new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
    public enum ButtonState {
        back, menu, gone;
        public static Drawable backIcon;
        public static Drawable menuIcon;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SimpleTheme);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        setContentView(R.layout.activity_action_bar);
        container = (FrameLayout) findViewById(R.id.activity_view);

        toolbar = (Toolbar) findViewById(R.id.activityToolBar);

        application = (ReferenceApplication) getApplication();
        storage = application.getShopData();
        api = Api.getInstance();

        setUpToolBar();

        networkStateReceiver = new NetworkStateReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkStateReceiver, intentFilter);
    }


    private void setUpToolBar() {
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        toolbar.setLogo(R.drawable.logo);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        ButtonState.backIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.back, null);
        ButtonState.menuIcon =  ResourcesCompat.getDrawable(getResources(), R.drawable.menu, null);
        setMenuButtonState(ButtonState.back);

        ViewCompat.setElevation(toolbar, ImageUtils.convertToPx(this, 8));

    }

    protected final void setMenuButtonState(ButtonState state) {
        this.state = state;
        switch (state) {
            case back:
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(ButtonState.backIcon);
                break;
            case menu:
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(ButtonState.menuIcon);
                break;
            case gone:
                actionBar.setDisplayHomeAsUpEnabled(false);
                break;
        }
    }

    public void setTitle(String title) {
        actionBar.setTitle(title);
    }


    public ButtonState getState() {
        return state;
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkStateReceiver);
    }
}
