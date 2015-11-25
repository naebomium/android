package com.mobium.reference.activity;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Optional;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mobium.client.LogicUtils;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.api.ShopApiExecutor;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.ShopCategory;
import com.mobium.config.common.Config;
import com.mobium.config.prototype.ILeftMenu;
import com.mobium.config.prototype.INavigationBar;
import com.mobium.google_places_api.models.AutoCompleteResult;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.utills.RegionUtils;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.fragments.LaunchFragment;
import com.mobium.reference.fragments.goods.CartFragment;
import com.mobium.reference.leftmenu.CategoryObserver;
import com.mobium.reference.leftmenu.LeftMenuListView;
import com.mobium.reference.push_logic.PushUtils;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.reference.utils.KeyboardUtils;
import com.mobium.reference.utils.NetworkUtils;
import com.mobium.reference.utils.RateDialogUtils;
import com.mobium.reference.utils.RegisterAppUtils;
import com.mobium.reference.utils.executing.ExecutingAsyncTask;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.storage.AppIdProvider;
import com.mobium.userProfile.ProfileApi;

public class MainDashboardActivity extends BaseActivity<ReferenceApplication>
        implements
        FragmentUtils.HasMenu,
        CategoryObserver,
        FragmentActionHandler.CustomActionHandler,
        FragmentUtils.ActionBarView,
        LocationListener {
    //primary
    public static final String ACTION_CUSTOM_ACTION = "com.extradea.reference.ACTION";
    private static final String TAG = "MainDashboardActivity";
    private ShopDataStorage storage;


    //left menu
    private DrawerLayout mainContainer;
    private LeftMenuListView leftMenu;
    private View leftMenuWrapper;
    private View leftMenuProgressBar;
    private TextView changeLeftMenuCityTitle;
    private boolean isMenuEnabled = true;


    //листенры активити
    private LogicUtils.OnChangeCartListener cartListener;

    //счетчик товаров в экшенбаре
    private TextView productCounter;

    private android.support.v7.widget.Toolbar toolbar;

    private boolean doubleBackToExitPressedOnce = false;

    //support
    private ExecutingAsyncTask loadMenuTask;

    //location
    private Location lastKnownLocation;

    private boolean alreadyHaveInternetAccessEvent = false;

    private TextView actionBarText;
    private ImageView actionBarLogo;
    private LinearLayout actionBarLogoTitleContainer;
    private ImageView actionBarMenuIcon;


    @Override
    public void onHaveInternetAccess() {
        if (alreadyHaveInternetAccessEvent)
            return;

        PushUtils.registerForPushAsync(this, aBoolean -> {
            if (!aBoolean && !NetworkUtils.isOnline(this))
                return;
            alreadyHaveInternetAccessEvent = true;
            networkStateReceiver.removeListener(MainDashboardActivity.this);
        });

        final Runnable checkAnUpdateRegions = () -> {
            //если регионы с сервера не загружены
            if (application.regionNotSelect()) {
                getOptionalLocationUsingBestProvider().ifPresent(value -> lastKnownLocation = value);
                tryDetermineRegionByNetworkOrGps();
//                //загрузить асинхронно
//                LocationTasks.getRegionsAsync(this, regionsData -> {
//                    if (regionsData.getRegions() == null || regionsData.getRegions().size() == 0)
//                        //если регионы пришли пустые, вдупляем список с одним регионом
//                        storage.setDefaultRegionList();
//                    else {
//                        storage.onReceive(regionsData.getRegions());
//                        storage.setGooglePlacesEnabled(Region.PlacesService.google_places.equals(regionsData.getService()));
//                    }
//                    if (!storage.getCurrentRegion().isPresent()) {
//                        changeLeftMenuCityTitle.setText("Выбрать регион");
//                        tryDetermineRegionByNetworkOrGps();
//                    }
//                });
//                //иначе если регион не задан, поппытаться определить его по сети или координатам
//            } else if ()

            }
        };

        //если приложение не зарегестрированно, зарегистрировать асинхронно
        if (!AppIdProvider.getAppId(this).isPresent())
            RegisterAppUtils.registerAppAcync(
                    this,
                    appId -> {
                        AppIdProvider.putAppId(this, appId);
                        ProfileApi.getInstance().setAppId(appId);
                        //после регистрации проверить регионы
                        checkAnUpdateRegions.run();
                    },
                    ReferenceApplication.getInstance().RegAppAPI());
            //иначе проверить регионы
        else checkAnUpdateRegions.run();
    }

    @Override
    public void networkUnavailable() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setTheme(R.style.Theme_AppBaseTheme);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        storage = ShopDataStorage.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainContainer = (DrawerLayout) findViewById(R.id.main_layout_drawer);
        leftMenuWrapper = findViewById(R.id.leftMenuWrapper);
        leftMenu = (LeftMenuListView) findViewById(R.id.leftMenu);
        leftMenuProgressBar = findViewById(R.id.leftMenuProgressBar);

        setUpActionBar();
//        ImageView imageView = (ImageView) findViewById(R.drawable.logo);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mainContainer, null, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                leftMenu.initByDefValues(false);
                setLeftMenuItemsVisible();
                syncState();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                KeyboardUtils.hideKeyboard(MainDashboardActivity.this);
                syncState();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };

        mDrawerToggle.syncState();
        mainContainer.setDrawerListener(mDrawerToggle);

        final EditText findView = (EditText) findViewById(R.id.left_menu_search);


        findView.setOnEditorActionListener(
                (TextView textView, int actionId, KeyEvent keyEvent) ->
                {
                    boolean result = false;
                    if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                        String query = findView.getText().toString();
                        FragmentActionHandler.doAction(this, new Action(ActionType.OPEN_SEARCH, query));
                        result = true;
                        KeyboardUtils.hideKeyboard(MainDashboardActivity.this);
                        showContent();
                    }
                    findView.getText().clear();

                    return result;
                }
        );


        leftMenu.setLeftMenuItems(null);

        changeLeftMenuCityTitle = (TextView) findViewById(R.id.leftMenuCityTitle);

        changeLeftMenuCityTitle.setText(getRegionButtonString());

        application.getShopData().addRegionListener(R -> changeLeftMenuCityTitle.setText(R.getTitle()));

        leftMenu.setAnimated(true);

        cartListener = (items) -> {
            if (productCounter == null)
                return;
            int count = application.getCart().getItemsCount();
            if (count == 0) {
                productCounter.setVisibility(View.INVISIBLE);
            } else {
                productCounter.setVisibility(View.VISIBLE);
                productCounter.setText(String.valueOf(count));
            }
        };


        findViewById(R.id.leftMenuCityLayout).setOnClickListener(v -> {
                    if (storage.hasCustomRegions())
                        Dialogs.showChangeRegionDialogInLeftMenu(this, this::doOpenRegionsScreen);
                    else
                        Toast.makeText(this, "Регионы загружаются, подождите", Toast.LENGTH_SHORT);
                }
        );

        if (savedInstanceState == null)
            doOpenStart();

        handleIntent(getIntent());

        application.getCart().notifyListeners();
        if (NetworkUtils.isOnline(this))
            onHaveInternetAccess();
        else
            networkStateReceiver.addListener(this);

        RateDialogUtils.checkAppStartCount(this);

        setIconColor(R.color.application_color_accent);
//        CheckoutFragment fragment = CheckoutFragment.getInstance(new CheckoutFragment.GoodsModel(0, new HashMap<String, Integer>()));
//        FragmentUtils.replace(this, fragment, true);

        setUpLeftMenuStyle();
    }


    /**
     * Получить заголовок для кнопки смены региона в верхнем меню
     *
     * @return заголовок кнопки
     */
    private String getRegionButtonString() {
        if (!storage.hasCustomRegions())
            return "Загрузка регионов";
        else if (application.regionNotSelect())
            return "Выбрать регион";
        else
            return application.getRegion().get().getTitle();
    }

    private void setUpLeftMenuStyle() {
        ILeftMenu menu = Config.get().design().getLeftMenu();
        leftMenuWrapper.setBackgroundColor(menu.getBackgroundColor(this));
    }

    /**
     * Пробует получить текущую локацию, используя лучший provider
     *
     * @return Новая локация или последняя или null
     */

    public Optional<Location> getOptionalLocationUsingBestProvider() {

/**
 * Check Permissions in Android-M (Support Library v4 for versions =< 23)
 * Uncomment this for 23 API
 */
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainDashboardActivity.this, new String[]
//                    {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
//
//        } else {


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        try {
            Location currentLocation = null;
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 30, this);
                currentLocation = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            }
            if (currentLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String bestProvider = locationManager.getBestProvider(criteria, true);
                locationManager.requestLocationUpdates(bestProvider, 1000, 30, this);
                currentLocation = locationManager.getLastKnownLocation(bestProvider);
            }

            return Optional.ofNullable(currentLocation != null ? currentLocation : lastKnownLocation);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    private void showRegionNotDeterminateDialog(boolean canRepeat) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this)
                        .setTitle("Ваш город не определен")
                        .setMessage("Попробуйте включить сервис местоположения и повторите попытку или введите Ваш город вручную")
                        .setNegativeButton("Ввести город", (dialogInterface, l) -> {
                            doOpenRegionsScreen();
                        }).setCancelable(false);
        if (canRepeat)
            builder.setPositiveButton("Повторить", (dialogInterface, i) -> {
                tryDetermineRegionByNetworkOrGps();
            });
        builder.show();
    }

    public void tryDetermineRegionByNetworkOrGps() {
        String placeKey = Config.get().getApplicationData().getApiKeyGooglePlaceApi();
        if (placeKey == null)
            showRegionNotDeterminateDialog(false);
        else {
            final Optional<Location> currentLocation = getOptionalLocationUsingBestProvider();
            if (!currentLocation.isPresent()) {
                showRegionNotDeterminateDialog(true);
            } else {
                Location mLocation = currentLocation.get();
                RegionUtils.loadRegionByLocationWithDialog(MainDashboardActivity.this, mLocation, item -> {
                    runOnUiThread(() -> {
                        if (item.isPresent())
                            askForRegion(item.get());
                        else
                            doOpenRegionsScreen();
                    });

                });
            }
        }
    }

    private void askForRegion(AutoCompleteResult.Item item) {
        new AlertDialog.Builder(MainDashboardActivity.this)
                .setMessage(String.format("Ваш регион '%s'?", item.getDescription()))
                .setPositiveButton("Да", (dialogInterface, i) -> {
                    onUserSelectedRegion(item);
                })
                .setNegativeButton("Нет", (dialogInterface1, i1) -> {
                    doOpenRegionsScreen();
                })
                .setCancelable(false)
                .show();
    }

    private void onUserSelectedRegion(AutoCompleteResult.Item item) {
        if (storage.getRegions() == null ||
                storage.getRegions().size() == 0) {
            RegionUtils.loadRegionsByAutoCompleteRegionWithDialog(MainDashboardActivity.this, item, data -> {
                storage.onReceive(data.regions);
                storage.onChange(data.region);
            });
        } else {
            RegionUtils.loadRegionsByAutoCompleteRegionWithDialog(MainDashboardActivity.this, item, data -> {
                storage.onReceive(data.regions);
                storage.onChange(data.region);
            });
        }
    }

    private void setUpActionBar() {
        LayoutInflater inflater = getLayoutInflater();
        View customActionBar = inflater.inflate(R.layout.action_bar, toolbar, false);
        actionBarText = (TextView) customActionBar.findViewById(R.id.action_bar_title);
        actionBarMenuIcon = (ImageView) customActionBar.findViewById(R.id.action_bar_menu_icon);
        actionBarLogo = (ImageView) customActionBar.findViewById(R.id.action_bar_logo);
        actionBarLogoTitleContainer = (LinearLayout) customActionBar.findViewById(R.id.action_bar_wrapper);
        actionBarMenuIcon.setVisibility(View.GONE);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        toolbar.setLogo(R.drawable.logo);
        toolbar.setNavigationIcon(R.drawable.logo);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                android.view.Gravity.CENTER);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(customActionBar, params);


        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ViewCompat.setElevation(toolbar, ImageUtils.convertToPx(this, 4));

        actionBarMenuIcon.setOnClickListener(v -> doOpenStart());
        actionBarMenuIcon.setOnClickListener(v -> showMenu());

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
//            setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );
//        else if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
//            mBaseLayout.setSystemUiVisibility( View.STATUS_BAR_HIDDEN );

//        getToolbarLogoIcon(toolbar).setOnClickListener(view -> doOpenStart());

//        centerActionBarTitle();
    }

    @Override
    public boolean isMenuOpen() {
        return mainContainer.isDrawerOpen(leftMenuWrapper);
    }

    @Override
    public void showMenu() {
        mainContainer.openDrawer(leftMenuWrapper);
    }

    public boolean isAutoRegionEnabled() {
        return ShopDataStorage.getInstance().isGooglePlacesEnabled() &&
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
    }

    public void doOpenRegionsScreen() {
        Intent intent = new Intent(this, AutoRegionActivity.class);

        application.getRegion().ifPresent(region ->
                        intent.putExtra(ChoiceRegionActivity.REQUEST_EXTRA_CURRENT_REGION,
                                region)
        );

        startActivityForResult(intent, RequestCodes.REGION_CODE);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void onActionIntent(Intent intent) {
        String taskId = intent.getStringExtra("push_task_id");
        Action action = (Action) intent.getSerializableExtra("action");

//        onEvent(StatisticsEvent.PUSH_TAP, "action_name", action.getActionName(), "action_data", action.getActionData(),
//                "task_id", taskId);

        Events.get(this).app().onAppStartFromPush(taskId);

        FragmentActionHandler.doAction(this, action);
    }

    @Override
    protected void onResume() {
        super.onResume();
        application.getCart().addCartListener(cartListener);
        application.getCart().notifyListeners();
        showContent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onStop() {
        super.onStop();
        application.getCart().removeCartListener(cartListener);
        application.onAppPause();
    }

    public void updateTitle(String title) {
        actionBarText.setText(title);
    }

    @Override
    public void updateLogo(@DrawableRes int res) {
        actionBarLogo.setImageResource(res);
    }

    @Override
    public void setIconColor(@ColorRes int color) {
        actionBarLogo.setColorFilter(color);
    }

    @Override
    public void configure(INavigationBar model) {
        switch (model.getMode()) {
            case icon:
                actionBarLogo.setVisibility(View.VISIBLE);
                actionBarText.setVisibility(View.GONE);
                break;
            case icon_text:
                actionBarLogo.setVisibility(View.VISIBLE);
                actionBarText.setVisibility(View.VISIBLE);
                break;
            case text:
                actionBarLogo.setVisibility(View.VISIBLE);
                actionBarText.setVisibility(View.VISIBLE);
                break;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) actionBarLogoTitleContainer.getLayoutParams();
        switch (model.getGravity()) {
                case left:
                    params.addRule(RelativeLayout.RIGHT_OF, actionBarMenuIcon.getId());
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                    break;
                case center:
                    params.addRule(RelativeLayout.RIGHT_OF, -1);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    break;
                default:
                    break;
        }
        actionBarText.setText(model.getText(this));
        toolbar.setBackgroundColor(model.getColorBackground(this));
        actionBarText.setTextColor(model.getColorText(this));

        Optional
                .ofNullable(mMenu)
                .map(value -> value.findItem(R.id.top_cart))
                .ifPresent(cartMenuItem -> cartMenuItem.setVisible(model.uiCartEnabled()));

    }


    @Override
    public void setMode(Mode mode) {
        Optional<View> logoView = Optional.ofNullable(actionBarLogo);
        Optional<View> textView = Optional.ofNullable(actionBarText);
        switch (mode) {
            case icon:
                logoView
                        .ifPresent(value -> value.setVisibility(View.VISIBLE));
                textView.ifPresent(value1 -> value1.setVisibility(View.GONE));
                break;
            case icon_with_text:
                logoView
                        .ifPresent(value -> value.setVisibility(View.VISIBLE));
                textView.ifPresent(value1 -> value1.setVisibility(View.VISIBLE));
                break;
            case text:
                logoView
                        .ifPresent(value -> value.setVisibility(View.GONE));
                textView.ifPresent(value1 -> value1.setVisibility(View.VISIBLE));

        }
    }

    @Override
    public void setGravity(Gravity gravity) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) actionBarLogoTitleContainer.getLayoutParams();
        switch (gravity) {
            case left:
                params.addRule(RelativeLayout.RIGHT_OF, actionBarMenuIcon.getId());
                params.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                break;
            case center:
                params.addRule(RelativeLayout.RIGHT_OF, -1);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                break;
            default:
                break;
        }
    }


    @Override
    public void showContent() {
        mainContainer.closeDrawer(leftMenuWrapper);
        setDefaultValuesToLeftMenuDelay();
    }

    public void doOpenStart() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_wrapper) instanceof LaunchFragment)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_wrapper, new LaunchFragment())
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.pop_enter, R.anim.pop_exit)
                    .commit();
        }
        showContent();
    }


    private void handleIntent(Intent intent) {
        if (ACTION_CUSTOM_ACTION.equals(intent.getAction())) {
            onActionIntent(getIntent());
        }
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            FragmentActionHandler.doAction(this, new Action(ActionType.OPEN_SEARCH, query));
        }
    }


    @Override
    public void onBackPressed() {
        if (isMenuOpen()) {
            if (leftMenu.isStartState())
                showContent();
            else
                leftMenu.back();
        } else if (mMenu.findItem(R.id.top_search).isActionViewExpanded()) {
            invalidateOptionsMenu();
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById((R.id.fragment_wrapper));
            if (currentFragment instanceof FragmentUtils.BackButtonHandler)
                if (((FragmentUtils.BackButtonHandler) currentFragment).onBackPressed())
                    return;


            if (currentFragment instanceof LaunchFragment) {
                if (doubleBackToExitPressedOnce) {
                    finish();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Нажмите назад еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
                return;
            } else {
                getSupportFragmentManager().popBackStackImmediate();
                KeyboardUtils.hideKeyboard(this);
            }

            currentFragment = getSupportFragmentManager().findFragmentById((R.id.fragment_wrapper));


            if (currentFragment instanceof BasicContentFragment) {
                BasicContentFragment fragment = (BasicContentFragment) currentFragment;
                updateBackActionBarButton(fragment.isTopActionBarButtonMenu());

                if (fragment instanceof CartFragment && application.getCart().isEmpty())
                    onBackPressed();
            }

        }
    }


    public void doOpenScanner(View view) {
        Events.get(this).catalog().onOpenBarcodeScanner();
        startActivityForResult(new Intent()
                .setClass(this, ScanActivity.class), RequestCodes.SCAN_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.SCAN_CODE:
                    String code = data.getStringExtra("SCAN_RESULT");
                    FragmentActionHandler.doAction(this, new Action(ActionType.OPEN_SEARCH, code));
                    break;
                case RequestCodes.REGION_CODE:
                    Region newRegion = (Region) data.getSerializableExtra(ChoiceRegionActivity.RESULT_TAG_REGION);
                    if (application.getShopData().getCurrentRegion().isPresent() &&
                            newRegion.equals(application.getShopData().getCurrentRegion()))
                        break;
                    application.getShopData().onChange(newRegion);
                    doOpenStart();
                    break;
                case RequestCodes.REGISTER_APP_CODE:
                    if (application.regionNotSelect())
                        doOpenRegionsScreen();
                    else
                        doOpenStart();
                    break;
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            showMenu();
        } else if (id == R.id.top_filter) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Menu mMenu;
    private android.widget.SearchView mSearch;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        mMenu = menu;

        View topCart = MenuItemCompat.getActionView(menu.findItem(R.id.top_cart));
        productCounter = (TextView) topCart.findViewById(R.id.cartCounter);
        View cart = topCart.findViewById(R.id.cartButton);
        cart.setOnClickListener(v -> FragmentActionHandler.doAction(this, new Action(ActionType.OPEN_CART, null)));
        application.getCart().notifyListeners();


//        mSearch = (android.widget.SearchView) menu.findItem(R.id.top_search).getActionView();
//
//        menu.findItem(R.id.top_search).setOnMenuItemClickListener(
//                menuItem -> {
//                    mSearch.setIconified(false);
//
//                    return true;
//                }
//        );
//
//        mSearch.setOnQueryTextFocusChangeListener((view, queryTextFocused) -> {
//            if (!queryTextFocused) {
//                mSearch.setOnCloseListener(() -> menu.findItem(R.id.top_search).collapseActionView());
//                KeyboardUtils.hideKeyboard(this);
//            }
//        });
//
//        mSearch.setIconifiedByDefault(true);
//
//
//        final View serarchPoint = mSearch;
//
//        BiFunction<Activity, String, View> findViewByName = (activity, id) ->
//                serarchPoint.findViewById(activity.getResources().getIdentifier(id, null, null)
//                );
//
//
//        Optional<View> canselSearch = Optional.ofNullable(findViewByName.apply(this, "android:id/search_close_btn"));
//
//        menu.findItem(R.id.top_search).setOnMenuItemClickListener(
//                menuItem -> {
//                    mSearch.setIconified(false);
//                    canselSearch.ifPresent(view -> view.setVisibility(View.INVISIBLE));
//                    return true;
//                }
//        );
//
//
//        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                canselSearch.ifPresent(view -> view.setVisibility(s == null || s.length() == 0 ? View.GONE : View.VISIBLE));
//                return false;
//            }
//        });
//
//
//        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchableInfo searchableInfo = manager.getSearchableInfo(getComponentName());
//        mSearch.setSearchableInfo(searchableInfo);
//
//        View voiceBtn = findViewByName.apply(this, ("android:id/search_voice_btn"));
//        findViewByName.apply(this, ("android:id/search_go_btn")).setVisibility(View.GONE);
//
//
//        ImageView scannerView = new ImageView(this);
//        scannerView.setImageDrawable(ResourcesCompat.getDrawableRes(getResources(), R.drawable.qr, null));
//        scannerView.setColorFilter(Color.BLACK);
//        scannerView.setOnClickListener(this::doOpenScanner);
//
//        ((LinearLayout) findViewByName.apply(this, ("android:id/submit_area")))
//                .addView(scannerView, voiceBtn.getLayoutParams());

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * меняет значек кнопки home в ActionBar и параметр её поведения
     *
     * @param isMenu true - меню false - кнопка назад
     */
    public void updateBackActionBarButton(boolean isMenu) {
        isMenuEnabled = isMenu;
    }

    public void doVoiceSearch() {

    }

    private void showLeftMenuProgressBarVisible() {
        leftMenuProgressBar.setVisibility(View.VISIBLE);
        leftMenu.setVisibility(View.GONE);
    }

    private void setLeftMenuItemsVisible() {
        leftMenuProgressBar.setVisibility(View.GONE);
        leftMenu.setVisibility(View.VISIBLE);
        loadMenuTask = null;
    }


    public void loadCategoryToLeftMenu(final String category) {
        if (!NetworkUtils.isOnline(this)) {
            Dialogs.alertNotHaveInternet(this);
            return;
        }
        if (loadMenuTask != null)
            return;

        loadMenuTask = new ExecutingAsyncTask() {
            ShopCategory shopCategory;

            @Override
            public void beforeExecute() {
                showLeftMenuProgressBarVisible();
            }

            @Override
            public void executeAsync() throws ExecutingException {
                if (!application.getCategories().containsKey(category)) {
                    shopCategory = application.loadCategory(category);
                    if (shopCategory.getExtraBoolean(ShopApiExecutor.HAS_MORE_SUBCATEGORIES) && shopCategory.childs.size() == 0)
                        application.loadCategories(shopCategory);
                } else {
                    shopCategory = application.getCategories().get(category);
                    if (shopCategory.getExtraBoolean(ShopApiExecutor.HAS_MORE_SUBCATEGORIES) && shopCategory.childs.size() == 0)
                        application.loadCategories(shopCategory);
                }
            }

            @Override
            public void afterExecute() {
                if (loadMenuTask != null) {
                    setCategoryToLeftMenu(shopCategory);
                    setLeftMenuItemsVisible();
                }
            }
        };
        Runnable onError = () -> runOnUiThread(() -> {
            Toast.makeText(MainDashboardActivity.this,
                    R.string.error_connection,
                    Toast.LENGTH_SHORT)
                    .show();
            setLeftMenuItemsVisible();
        });

        executeAsyncHidden(loadMenuTask, onError);

    }

    private void setCategoryToLeftMenu(ShopCategory shopCategory) {
        if (shopCategory == null)
            return;

        if (shopCategory.childs.size() == 0) {

            FragmentActionHandler.doAction(this, new Action(ActionType.OPEN_CATEGORY, shopCategory.id));

            setDefaultValuesToLeftMenuDelay();
        } else {
            leftMenu.setCategory(shopCategory);
        }
    }


    private void setDefaultValuesToLeftMenuDelay() {
        new Handler().postDelayed(leftMenu::initByDefValues, LeftMenuListView.animationTime / 3);
    }


    public int getMainFrameHeight() {
        return findViewById(R.id.fragment_wrapper).getHeight();
    }


    @Override
    public void popCategory() {
        leftMenu.back();
    }

    @Override
    public void viewCategory(String categoryId) {
        loadCategoryToLeftMenu(categoryId);
    }

    @Override
    public boolean isHandling(@NonNull Action action) {
        return true;
    }

    @Override
    public void handleAction(@NonNull Action action) {
        switch (action.getType()) {
            case OPEN_SEARCH:
                if (mSearch != null) {
                    mSearch.setIconified(true);
                    mSearch.clearFocus();
                    if (mMenu != null && mMenu.findItem(R.id.top_search) != null) {
                        (mMenu.findItem(R.id.top_search)).collapseActionView();
                    }
                }
                break;
        }

        if (!(action.getType() == ActionType.OPEN_CATALOG_INSIDE_MENU ||
                action.getType() == ActionType.POP_CATALOG_INSIDE_MENU))
            setDefaultValuesToLeftMenuDelay();
    }


    // Location Listeners

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
//        TextView txt = (TextView) findViewById(R.id.coordinates);
//        txt.setText("LAT" + location.getLatitude() + "LAT" + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

//    public static View getToolbarLogoIcon(Toolbar toolbar) {
//        //check if contentDescription previously was set
//        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
//        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
//        toolbar.setLogoDescription(contentDescription);
//        ArrayList<View> potentialViews = new ArrayList<View>();
//        //find the view based on it's content description, set programatically or with android:contentDescription
//        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
//        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
//        View logoIcon = null;
//        if (potentialViews.size() > 0) {
//            logoIcon = potentialViews.get(0);
//        }
//        //Clear content description if not previously present
//        if (hadContentDescription)
//            toolbar.setLogoDescription(null);
//        return logoIcon;
//    }

//    private TextView geToolBarTextView(Toolbar mToolBar) {
//        TextView titleTextView = null;
//
//        try {
//            Field f = mToolBar.getClass().getDeclaredField("mTitleTextView");
//            f.setAccessible(true);
//            titleTextView = (TextView) f.get(mToolBar);
//        } catch (NoSuchFieldException e) {
//        } catch (IllegalAccessException e) {
//        }
//        return titleTextView;
//    }

    public DrawerLayout getNavigatinLayout() {
        return mainContainer;
    }


}