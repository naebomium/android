package com.mobium.reference.fragments.goods;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BiFunction;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.Opinion;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicLoadableFragment;
import com.mobium.reference.fragments.support.ViewPhotoFragment;
import com.mobium.reference.productPage.DetailsFactory;
import com.mobium.reference.productPage.DetailsType;
import com.mobium.reference.productPage.ProductDetailsBase;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.VideoPlayer;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.view.IRatingBar;
import com.mobium.reference.views.DotView;
import com.mobium.reference.views.ModificationView;
import com.mobium.reference.views.SaveTouchViewPager;
import com.mobium.reference.views.TabPanelController;
import com.mobium.reference.views.TabsFocuser;
import com.mobium.reference.views.Ui_configurator;
import com.mobium.reference.views.WebImageView;
import com.mobium.reference.views.WebTouchImageView;

import java.util.ArrayList;
import java.util.List;

import static com.mobium.client.models.Marketing.Type;
import static com.mobium.reference.utils.ImageUtils.convertToPx;

/**
 *   24.03.15.
 * http://mobiumapps.com/
 */
public class ProductFragment extends BasicLoadableFragment {
    private static final String STATE_PRODUCT_ID = "STATE_PRODUCT_ID";
    private static final String STATE_PRODUCT_REAL_ID = "STATE_PRODUCT_REAL_ID";
    private static final String STATE_CURRENT_PHOTO = "STATE_CURRENT_PHOTO";
    private static final String STATE_CURRENT_TOP_PANEL_INDEX = "STATE_CURRENT_TOP_PANEL_INDEX";
    private static final String STATE_CURRENT_BOTTOM_PANEL_INDEX = "STATE_BOTTOM_PANEL_INDEX";
    private static final String STATE_SCROLL_Y = "STATE_SCROLL_Y";
    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    private static final String TAG = "ProductFragment";

    private static final int prepareTime = 700;

    //other
    private static final String TAG_PHOTO_VIEW = "PRODUCT_FRAGMENT_PHOTO_TAG";


    //models
    private String productId; // передается через Bundle
    private String realId; // передается через Bundle
    private ShopItem product; // получается из сети или из кеша

    private static final int RECOVERY_DIALOG_REQUEST = 10;

//    private static boolean fullscreen;

    //views
    private SaveTouchViewPager viewPhotosPager;
    private LinearLayout topTabsTitles;
    private FrameLayout topTabsWrapper;
    private LinearLayout bottomTabsTitles;
    private FrameLayout bottomTabsWrapper;

    private FrameLayout videoPlaceHolder;
//    private WebView webView;

    private ViewGroup viewPhotoPagerDotsContainer;
    private ViewGroup viewAfterTitleInfo;
    private View stickyPriceView;

    private ScrollView scrollView;
    private View progressBar;
    private View content;

    //state
    private int currentPhoto;
    private int currentTopPanelIndex;
    private int currentBottomPanelIndex;
    private boolean haveMarketingsInfo;

    private List<DetailsFactory.DetailsLifeCycle> detailsLifeCycles;
    private VideoPlayer videoPlayer;
    private ViewGroup video_full_screen;
    private TextView viewProductId;


    public static ProductFragment getInstance(String itemId) {
        ProductFragment result = new ProductFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(STATE_PRODUCT_ID, itemId);
        result.setArguments(bdl);
        return result;
    }


    public static ProductFragment getInstanceForRealId(String realId) {
        ProductFragment result = new ProductFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(STATE_PRODUCT_REAL_ID, realId);
        result.setArguments(bdl);
        return result;
    }

    @Override
    protected boolean needLoading() {
        return realId != null ||
                productId != null &&
                        !getApplication().getShopCache().hasItem(productId) ||
                getApplication().getShopCache().fetchItem(productId).isFullInfo();
    }


    @Override
    protected void afterLoaded() {
        super.afterLoaded();
        fill();
    }

    @Override
    protected void alreadyLoaded() {
        super.alreadyLoaded();
        fill();
    }

    @Override
    protected void doesntNeedLoading() {
        product = getApplication().getShopCache().fetchItem(productId);
        fill();
    }

    public static long time() {
        return System.currentTimeMillis();
    }

    public void fill() {
        if (topTabsTitles.getChildCount() > 0)
            return;

        Events.get(getActivity()).catalog().onProductOpened(product);

        setUpPhotoPager();
        setUpProductDetails();
        setProductInfo();

        product.getMedia().ifPresent(
                medias -> Stream.of(medias)
                        .filter(media -> media.type == ShopItem.Media.Type.video)
                        .findFirst()
                        .ifPresent(media -> {
//                            initYoutubePlayer(media.res);
//                            initYoutubePlayer(media.res);
                            videoPlayer = new VideoPlayer(videoPlaceHolder, video_full_screen);
                            videoPlayer.playYoutubeVideo(media.res);
                            videoPlaceHolder.setVisibility(View.VISIBLE);
                            detailsLifeCycles.add(videoPlayer);
                        }));

    }


//    private void initYoutubePlayer(@NonNull String videoUrl) {
//        String youtubeApiKey = Config.get().getGoogleYouTubeApiKey();
//        String currentUrl = DescriptionDetails.buildVideoId(videoUrl);
//
//        YouTubePlayerSupportFragment youTubePlayerFragment = new YouTubePlayerSupportFragment();
//        getFragmentManager().beginTransaction().add(R.id.fragment_product_video_place, youTubePlayerFragment).commitAllowingStateLoss();
//        youTubePlayerFragment.initialize(youtubeApiKey, new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
//                if (wasRestored) {
//                    youTubePlayer.play();
//                } else {
//                    videoPlayer = youTubePlayer;
//                    videoPlayer.setOnFullscreenListener(isFullscreen -> {
//                        if (!fullscreen && isFullscreen) {
//                            showFullScreen(currentUrl, youtubeApiKey);
//                            fullscreen = true;
//                        } else if (!isFullscreen)
//                            fullscreen = false;
//                    });
//                    youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
//                    youTubePlayer.cueVideo(currentUrl);
//                }
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                YoutubePlayerUtil.handleError(getActivity(), youTubeInitializationResult);
//                Optional.ofNullable(youTubePlayerFragment.getView()).ifPresent(v -> v.setVisibility(View.GONE));
//            }
//        });
//    }

//    private void showFullScreen(String url, String apiKey) {
//        Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), apiKey, url);
//        if (intent != null && canResolveIntent(intent))
//            startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
//        else
//            YouTubeInitializationResult.SERVICE_MISSING.getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
//    }
//
//    private boolean canResolveIntent(Intent intent) {
//        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
//        return resolveInfo != null && !resolveInfo.isEmpty();
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != Activity.RESULT_OK) {
//            YouTubeInitializationResult errorReason =
//                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
//            if (errorReason.isUserRecoverableError()) {
//                errorReason.getErrorDialog(getActivity(), 0).show();
//            } else {
//                Log.d(TAG, "Error reason: " + errorReason.toString());
//            }
//        }
//        if (videoPlayer != null)
//            videoPlayer.setFullscreen(false);
//    }

    /**
     * заполняет детали о товаре: описание, характеристики, списки свзяных товаров
     */
    private void setUpProductDetails() {

        if (getView() != null)
            getView().findViewById(R.id.layout_for_dragged_to_screen_hight).getLayoutParams().height =
                    getDashboardActivity().getMainFrameHeight();

        DetailsFactory factory = new DetailsFactory(getDashboardActivity(), product);
        final TabsFocuser focuser = new TabsFocuser() {
            private final Handler handler = new Handler();

            @Override
            public void focusOnTopTabs() {
                handler.post(() -> scrollView.smoothScrollTo(0, topTabsTitles.getTop() - stickyPriceView.getHeight()));
            }

            @Override
            public void focusOnBottomTabs() {
                handler.post(() -> scrollView.smoothScrollTo(0, bottomTabsTitles.getTop() - stickyPriceView.getHeight()));
            }
        };


        List<ProductDetailsBase> allPages = new ArrayList<>();

        final List<DetailsType> topTypes = new ArrayList<>(3);


        product.getDescription().ifPresent(none -> topTypes.add(DetailsType.DESCRIPTION));
        product.getCharacteristics().ifPresent(none -> topTypes.add(DetailsType.PRODUCT_FEATURES));
        product.getOpinions().ifPresent(none -> topTypes.add(DetailsType.OPINIONS));

        if (!topTypes.isEmpty()) {

            List<ProductDetailsBase> current = factory.makeDetailsList(topTypes);


            for (ProductDetailsBase b : current)
                b.setFocuser(focuser);

            allPages.addAll(current);
            TabPanelController topPanelController;
            topPanelController = new TabPanelController(
                    topTabsTitles,
                    topTabsWrapper,
                    current
            );
            topPanelController.setColor(getResources().getColor(R.color.green));
            topPanelController.setOnChangeTabListener(index -> {
                focuser.focusOnTopTabs();
            });
        }

        final List<DetailsType> bottomItems = new ArrayList<>(3);

        if (product.hasOtherItems())
            bottomItems.add(DetailsType.OTHER_ITEMS);
        if (product.hasRelatedItems())
            bottomItems.add(DetailsType.RELATED_ITEMS);
        if (!bottomItems.isEmpty()) {

            List<ProductDetailsBase> current = factory.makeDetailsList(bottomItems);

            for (ProductDetailsBase b : current)
                b.setFocuser(focuser);

            allPages.addAll(current);
            TabPanelController bottomPanelController = new TabPanelController(
                    bottomTabsTitles,
                    bottomTabsWrapper,
                    current
            );

            bottomPanelController.setOnChangeTabListener(index -> {
                currentBottomPanelIndex = index;
                focuser.focusOnBottomTabs();
            });
            bottomPanelController.setColor(getResources().getColor(R.color.green));
        }
        detailsLifeCycles = Stream.of(allPages)
                .filter(p -> p instanceof DetailsFactory.DetailsLifeCycle)
                .map(p -> (DetailsFactory.DetailsLifeCycle) p)
                .collect(Collectors.toList());
    }

    /**
     * заполняет главную информацию о товаре: цена, название, рейтинг
     */
    private void setProductInfo() {

        View view = getView();
        if (view == null)
            return;

        product.getRealId().map(realId -> "# " + realId).ifPresent(viewProductId::setText);

        final BiFunction<TextView, Type, Boolean> setMarketingToTextView =
                (textView, type) -> {
                    textView.setVisibility(View.GONE);
                    product.getMarketing().ifPresent(marketings -> {

                        getView().findViewById(R.id.fragment_product_sale_icon)
                                .setVisibility(View.VISIBLE);

                        Stream.of(marketings)
                                .filter(marketing -> marketing.type == type)
                                .findFirst().ifPresent(
                                marketing1 ->
                                {
                                    textView.setVisibility(View.VISIBLE);
                                    textView.setText(marketing1.title);
                                    textView.setOnClickListener(v ->
                                                    new AlertDialog.Builder(getContext())
                                                            .setTitle(marketing1.title)
                                                            .setMessage(marketing1.description)
                                                            .show()
                                    );
                                    haveMarketingsInfo = true;
                                }
                        );

                    });
                    return null;
                };


        TextView otherAction = (TextView) view.findViewById(R.id.fragment_product_leftSale);
        TextView freeDelivery = (TextView) view.findViewById(R.id.fragment_product_rightSale);
        setMarketingToTextView.apply(otherAction, Type.NOTHING);
        setMarketingToTextView.apply(freeDelivery, Type.FREE_DELIVERY);
        if (otherAction.getVisibility() != View.VISIBLE && freeDelivery.getVisibility() == View.VISIBLE)
            freeDelivery.setGravity(Gravity.LEFT);

        if (!haveMarketingsInfo) {
            view.findViewById(R.id.fragment_product_after_title_info).setVisibility(View.GONE);
            view.findViewById(R.id.fragment_product_after_deliver).setVisibility(View.GONE);
        }

        View saleView = view.findViewById(R.id.fragment_product_sale_red_area);

        saleView.setVisibility(
                product.isSale() ?
                        View.VISIBLE :
                        View.INVISIBLE
        );

        final TextView viewProductCost =
                (TextView) view.findViewById(R.id.fragment_product_cost);
        final TextView viewProductOldCost =
                (TextView) view.findViewById(R.id.fragment_product_cost_old);
        final IRatingBar ratingBar =
                (IRatingBar) view.findViewById(R.id.fragment_product_rating);

        TextView addToCart = (TextView) view.findViewById(R.id.fragment_product_add_to_card_button);
        TextView title = (TextView) view.findViewById(R.id.fragment_product_title);

        Ui_configurator.getInstance(getActivity()).configureShopItemInfo(
                product,
                null,
                title,
                viewProductCost,
                viewProductOldCost,
                ratingBar,
                addToCart
        );

        if (product != null && product.getModifications() != null) {
            CardView modificationsCardView = (CardView) view.findViewById(R.id.modifications_cardview);
            modificationsCardView.setVisibility(View.VISIBLE);
            ModificationView modificationView = (ModificationView) view.findViewById(R.id.modifications_root);
            modificationView.initModifications(product.getModifications());

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product, container, false);

        video_full_screen = (ViewGroup) getDashboardActivity().findViewById(R.id.video_container);

        videoPlaceHolder = (FrameLayout) root.findViewById(R.id.fragment_product_video_place);
        scrollView = (ScrollView) root.findViewById(R.id.fragment_product_scroll);
        viewPhotosPager = (SaveTouchViewPager) root.findViewById(R.id.fragment_product_photos_pager);
        progressBar = root.findViewById(R.id.fragment_catalog_progress);
        content = root.findViewById(R.id.fragment_catalog_content);

        videoPlaceHolder.setVisibility(View.GONE);

        viewPhotoPagerDotsContainer = (ViewGroup) root.findViewById(R.id.fragment_product_photos_pager_dots);

        topTabsTitles = (LinearLayout) root.findViewById(R.id.fragment_product_top_tabs_titles);
        topTabsWrapper = (FrameLayout) root.findViewById(R.id.fragment_product_top_tabs_wrapper);

        bottomTabsTitles = (LinearLayout) root.findViewById(R.id.fragment_product_bottom_tabs_titles);
        bottomTabsWrapper = (FrameLayout) root.findViewById(R.id.fragment_product_bottom_tabs_wrapper);
        stickyPriceView = root.findViewById(R.id.sticky_price_view);

        viewProductId = (TextView) root.findViewById(R.id.fragment_product_id);



//        Изменение масштаба содержимого
//        changeViewHightOnStart(root.findViewById(R.id.layout_for_dragged_to_screen_hight), mainFrameHeight);
        root.findViewById(R.id.layout_for_dragged_to_screen_hight).getLayoutParams().height =
                getDashboardActivity().getMainFrameHeight();
        Button doCallButton = (Button) root.findViewById(R.id.fragment_product_do_call);
        doCallButton.setOnClickListener(view -> FragmentActionHandler.doAction(getActivity(), new Action(ActionType.DO_CALL, Config.get().getApplicationData().getShopPhone())));
        // установить текст с иконкой телефона
        Spannable doCallLabel = new SpannableString(" \t\tПОЗВОНИТЬ НАМ");
        doCallLabel.setSpan(new RelativeSizeSpan(1.2f), 0, doCallLabel.length(), 0);
        doCallLabel.setSpan(new ImageSpan(getContentView().getContext(), R.drawable.phone_black, ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        doCallButton.setText(doCallLabel);
        detailsLifeCycles = new ArrayList<>();
        return root;
    }

    private void setUpPhotoPager() {
        viewPhotoPagerDotsContainer.removeAllViews();

        if (!product.getImages().isPresent())
            return;

        //filling viewpager by images
        final List<String> urls = product.getImages().get();

        viewPhotosPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return urls.size();
            }

            @Override
            public Object instantiateItem(ViewGroup collection, int position) {
                WebImageView webImageView = new WebImageView(collection.getContext());
                webImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                webImageView.setTag(TAG_PHOTO_VIEW + String.valueOf(position));
                webImageView.setUrl(urls.get(position));
                webImageView.setOnClickListener(v ->
                                FragmentUtils.replace(getActivity(), ViewPhotoFragment.getInstance(urls.toArray(new String[urls.size()]), null, position), true)
                );
                collection.addView(webImageView);
                return webImageView;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view.equals(o);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);

            }
        });


        //end of filling viewpager by images

        //setup dots after pager
        final DotView[] dotViews = new DotView[urls.size()];

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                convertToPx(getActivity(), 8),
                convertToPx(getActivity(), 8)
        );
        lp.setMargins(convertToPx(getActivity(), 2), 0, convertToPx(getActivity(), 2), 0);


        final int activeColor = getResources().getColor(android.R.color.black);
        final int notActiveColor = getResources().getColor(android.R.color.darker_gray);
        for (int i = 0; i < urls.size(); i++) {
            dotViews[i] = new DotView(getActivity());
            dotViews[i].setActiveColor(activeColor);
            dotViews[i].setNotActiveColor(notActiveColor);
            dotViews[i].setState(DotView.State.NOT_ACTIVE);
            viewPhotoPagerDotsContainer.addView(dotViews[i], i, lp);
        }

        viewPhotosPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (DotView dotView : dotViews) {
                    dotView.setState(DotView.State.NOT_ACTIVE);
                }
                dotViews[i].setState(DotView.State.ACTIVE);
                currentPhoto = i;
                disableZoomAtPhotoWithTag(TAG_PHOTO_VIEW + String.valueOf(i + 1));
                disableZoomAtPhotoWithTag(TAG_PHOTO_VIEW + String.valueOf(i - 1));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        dotViews[currentPhoto].setState(DotView.State.ACTIVE);
        viewPhotosPager.setCurrentItem(currentPhoto);
        //end of setup dots after pager
    }

    @Override
    protected void loadInBackground() throws ExecutingException {
        final String finalProductId = productId;
        final String finalRealId = realId;

        if (finalProductId != null) {
            product = getApplication().getShopCache().fetchItemFromServer(finalProductId);
        } else if (finalRealId != null) {
            product = getApplication().getShopCache().getItemByRealId(finalRealId);
            productId = product.getId();
        } else {
            getDashboardActivity().runOnUiThread(() -> {
                getDashboardActivity().onBackPressed();
                Toast.makeText(getActivity(), "Ошибка во время загрузки товара", Toast.LENGTH_LONG);
            });
            return;
        }

        try {
            product.setOpinions(
                    getApplication().getExecutor().loadOpinions(product.getId(), Opinion.getObjectType(product), 3, 0));
        } catch (ExecutingException ex) {
            ex.printStackTrace();
        }

    }


    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        if (outState != null) {
            outState.putInt(STATE_CURRENT_PHOTO, currentPhoto);
            outState.putInt(STATE_CURRENT_TOP_PANEL_INDEX, currentTopPanelIndex);
            outState.putInt(STATE_CURRENT_BOTTOM_PANEL_INDEX, currentBottomPanelIndex);
            if (scrollView != null)
                outState.putInt(STATE_SCROLL_Y, scrollView.getScrollY());
        }
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        if (savedInstanceState == null) {
            if (getArguments() == null)
                return;
            else
                savedInstanceState = getArguments();
        }
        if (productId == null)
            productId = savedInstanceState.getString(STATE_PRODUCT_ID);

        if (realId == null)
            realId = savedInstanceState.getString(STATE_PRODUCT_REAL_ID);

        currentPhoto = savedInstanceState.getInt(STATE_CURRENT_PHOTO, currentPhoto);
        currentTopPanelIndex = savedInstanceState.getInt(STATE_CURRENT_TOP_PANEL_INDEX, 0);
        currentBottomPanelIndex = savedInstanceState.getInt(STATE_CURRENT_BOTTOM_PANEL_INDEX, 0);
    }


    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.product.name());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (detailsLifeCycles != null)
            Stream.of(detailsLifeCycles).forEach(DetailsFactory.DetailsLifeCycle::onResume);
//        if (webView != null)
//            webView.onResume();
    }

    @Override
    protected String getTitle() {
        return "О товаре";
    }


    private void disableZoomAtPhotoWithTag(String tag) {
        View photo = viewPhotosPager.findViewWithTag(tag);
        if (photo instanceof WebTouchImageView) {
            ((WebTouchImageView) photo).disabledZoom();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (detailsLifeCycles != null)
            Stream.of(detailsLifeCycles).forEach(DetailsFactory.DetailsLifeCycle::onPause);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (detailsLifeCycles != null)
            Stream.of(detailsLifeCycles).forEach(DetailsFactory.DetailsLifeCycle::onDestroy);
//        if (webView != null)
//            webView.destroy();

        detailsLifeCycles = null;
        if (viewPhotoPagerDotsContainer != null)
            viewPhotoPagerDotsContainer.removeAllViews();
    }


    @Override
    protected View getProgressView() {
        return progressBar;
    }

    @Override
    protected View getContentView() {
        return content;
    }

    @Override
    public boolean onBackPressed() {
        return videoPlayer != null && videoPlayer.onBackPressed() || super.onBackPressed();
    }
}