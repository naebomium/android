package com.mobium.reference.fragments.support;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.client.api.ShopCache;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.fragments.InjectAbstractFragment;
import com.mobium.reference.model.ContentPageSource;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.views.DotedViewPager;
import com.mobium.reference.view.IContentPageView;
import com.mobium.reference.views.Ui_configurator;
import com.mobium.reference.views.VisibilityViewUtils;
import com.mobium.reference.views.adapters.ProductAdapter;
import com.mobium.reference.views.adapters.TopCategoryAdapter;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  on 07.08.15.
 * http://mobiumapps.com/
 */
public class ContentPageFragment
        extends InjectAbstractFragment
        implements IContentPageView {

    @Bind(R.id.content_page_pager) protected DotedViewPager photos;
    @Bind(R.id.content_page_title) protected TextView m_title;
    @Bind(R.id.content_page_text)  protected HtmlTextView textView;
    @Bind(R.id.show_more)          protected TextView showMore;

    @Bind(R.id.content_page_products_title)   protected TextView productsTitle;
    @Bind(R.id.content_page_categories_title) protected TextView categoriesTitle;
    @Bind(R.id.content_page_products)   protected RecyclerView productsView;
    @Bind(R.id.content_page_categories) protected RecyclerView categoriesView;
    @Bind(R.id.content_page_products_deliver) protected View productDeliver;
    @Bind(R.id.progress_view) protected View loadBar;

    private static final double IMAGE_RATIO = 0.495;

    private ContentPagePresenter presenter;



    private static class ContentPagePresenter {
        private final IContentPageView view;
        private final ContentPageSource source;
        private final ShopCache cache;

        private Subscription loadingProductSubscription;
        private Subscription loadingCategorySubscription;


        private ContentPagePresenter(IContentPageView view, ContentPageSource source) {
            this.view = view;
            this.source = source;
            cache = ReferenceApplication.getInstance().getShopCache();
        }


        private Observable<ShopItem[]> getItemsFromInternet(List<String> realId) {
            return Observable.create(subscriber -> {
                try {
                    if (!subscriber.isUnsubscribed())
                        subscriber.onNext(cache.getItemsByRealId(realId));
                    if (!subscriber.isUnsubscribed())
                        subscriber.onCompleted();
                } catch (ExecutingException e) {
                    subscriber.onError(e);
                }
            });
        }

        /**
         * nedd add method getCategoriesByRealID
         * @param category
         * @return
         */
        private Observable<ShopItem[]> getCategoryesFromInterner(List<String> category) {
//            return Observable.create(subscriber -> {
//                try {
//                    if (!subscriber.isUnsubscribed())
//                        subscriber.onNext(cache.getCategoryisByRealId());
//                    if (!subscriber.isUnsubscribed())
//                        subscriber.onCompleted();
//                } catch (ExecutingException e) {
//                    subscriber.onError(e);
//                }
//            });
            return Observable.empty();
        }



        public void startLoadingIfNeed() {
            if (loadingProductSubscription == null) {

                view.showLoadBar();

                final List<String> products =
                        Stream.of(source.getOffers())
                                .flatMap(offer -> Stream.of(offer.relatedOffers))
                                .collect(Collectors.toList());

                loadingProductSubscription = getItemsFromInternet(products)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                items -> {
                                    if (items.length > 0)
                                        view.showProducts(Arrays.asList(items), null);
                                    else
                                        view.hideProducts();
                                    view.showContent();
                                },
                                throwable -> view.showError(
                                        "Ошибка во время загрузки, Повторить?",
                                        throwable.getMessage(),
                                        (v, d) -> {
                                            loadingProductSubscription.unsubscribe();
                                            loadingProductSubscription = null;
                                            startLoadingIfNeed();
                                        },
                                        (v, d) -> view.doExit()
                                )
                        );
            }

            if (loadingCategorySubscription == null) {
                final List<String> category =
                        Stream.of(source.getOffers())
                                .flatMap(offer -> Stream.of(offer.relatedCategories))
                                .collect(Collectors.toList());
                loadingCategorySubscription = getCategoryesFromInterner(category).subscribe();
                view.hideCategories();
            }
        }


        public void initializeNotLoadedViews() {
            view.showTitle(source.getTitle());
            view.showImages(Stream.of(source.images).map(ContentPageSource.Image::getUrl).collect(Collectors.toList()));
            view.showText(Stream.of(source.texts).map(ContentPageSource.Text::getText).collect(Collectors.joining()));
        }

        public String getTitle() {
            return source.getTitle();
        }

        public void unSubscribe() {
            if (loadingCategorySubscription != null) {
                loadingCategorySubscription.unsubscribe();
                loadingCategorySubscription = null;
            }
            if (loadingProductSubscription != null) {
                loadingProductSubscription.unsubscribe();
                loadingCategorySubscription = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.startLoadingIfNeed();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unSubscribe();
    }

    @Override
    public String getTitle() {
        return Optional.ofNullable(presenter).map(ContentPagePresenter::getTitle).orElse("Контент Страница");
    }

    @Override
    public void showTitle(String title) {
        m_title.setText(title);
    }

    @Override
    public void showText(String html) {
        textView.setHtmlFromString(html, new HtmlTextView.RemoteImageGetter());
        showMore.setVisibility(View.GONE);
    }

    @Override
    public void showImages(final List<String> images) {
        photos.setAdapter(new PagerAdapter() {

            private final ViewGroup.LayoutParams lp =
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

            @Override
            public int getCount() {
                return images.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView webImageView = new ImageView(container.getContext());
                Picasso.with(container.getContext())
                        .load(images.get(position))
                        .error(Ui_configurator.getErrorImage(getActivity()))
                        .fit()
                        .into(webImageView);
                container.addView(webImageView, lp);
                return webImageView;
            }
        });
        com.mobium.reference.views.ViewUtils.setRatioOfView(photos.getPager(), IMAGE_RATIO);
    }

    @Override
    public void showCategories(List<ShopCategory> categories, String title) {
        categoriesTitle.setVisibility(View.VISIBLE);
        categoriesView.setVisibility(View.VISIBLE);
        categoriesTitle.setText(title);
        categoriesView.setAdapter(new TopCategoryAdapter((MainDashboardActivity) getActivity(), categories));
    }

    @Override
    public void showProducts(List<ShopItem> products, String title) {
        productsTitle.setVisibility(View.VISIBLE);
        productsView.setVisibility(View.VISIBLE);
        productDeliver.setVisibility(View.GONE);
        productsTitle.setText(Optional.ofNullable(title).orElse("Продукты"));
        productsView.setAdapter(new ProductAdapter(getActivity(), Ui_configurator.getInstance(getActivity()), products));
    }

    @Override
    public void showLoadBar() {
        VisibilityViewUtils.show(loadBar, false);
    }

    @Override
    public void showContent() {
        VisibilityViewUtils.hide(loadBar, true);
    }

    @Override
    public void showError(String title, String message, DialogInterface.OnClickListener apply, DialogInterface.OnClickListener cancel) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("Повторить", apply)
                .setNegativeButton("Выход", cancel)
                .show();
    }

    @Override
    public void doExit() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void hideProducts() {
        productsTitle.setVisibility(View.GONE);
        productsView.setVisibility(View.GONE);
        productDeliver.setVisibility(View.GONE);
    }

    @Override
    public void hideCategories() {
        categoriesView.setVisibility(View.GONE);
        categoriesTitle.setVisibility(View.GONE);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_content_page;
    }


    @Override
    public void onViewCreated(View view, Bundle s) {
        super.onViewCreated(view, s);
        productsView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoriesView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        photos.setNotActiveColor(getResources().getColor(android.R.color.black));
        photos.setActiveColor(getResources().getColor(R.color.application_color_accent));

        presenter.initializeNotLoadedViews();
    }

    public static ContentPageFragment getInstance(@NonNull ContentPageSource source) {
        ContentPageFragment res = new ContentPageFragment();
        Bundle bundle = new Bundle(1);
        bundle.putParcelable(ContentPageSource.class.getSimpleName(), Parcels.wrap(source));
        res.setArguments(bundle);
        return res;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentPageSource source = Parcels.unwrap(getArguments().getParcelable(ContentPageSource.class.getSimpleName()));
        presenter = new ContentPagePresenter(this, source);
    }

}
