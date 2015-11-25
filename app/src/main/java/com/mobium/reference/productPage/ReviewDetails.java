package com.mobium.reference.productPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.annimon.stream.Stream;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.fragments.shop_info.OpinionsFragment;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.views.adapters.ReviewAdapter;


/**
 *  on 07.04.15.
 * http://mobiumapps.com/
 */
public class ReviewDetails extends ProductDetailsBase {
    private View contentView;


    public ReviewDetails(MainDashboardActivity activity, ShopItem shopItem) {
        super(activity, DetailsType.OPINIONS, shopItem);
    }


    @Override
    protected View fillContentWrapper(final LayoutInflater inflater, ViewGroup contentWrapper) {
        if (contentView != null)
            return contentView;
        contentView = inflater.inflate(R.layout.fragment_product_details_reviews, contentWrapper, true);
        LinearLayout reviews = (LinearLayout) contentView.findViewById(R.id.product_review_review_list);

        Stream.of(shopItem.getOpinions().get().opinions)
                .limit(3)
                .forEach(opinion -> {
            View reviewView = inflater.inflate(R.layout.fragment_product_details_reviews_item, reviews, false);
            ReviewAdapter.confiqureView(reviewView, opinion);
            reviews.addView(reviewView);
        });


        View more = contentView.findViewById(R.id.product_review_review_list_more);
        if (shopItem.getOpinions().get().size > 3) {
            more.setVisibility(View.VISIBLE);
            more.setOnClickListener(view ->
                FragmentUtils.replace(activity,
                        OpinionsFragment.getInstance(shopItem),
                        true
                )
            );
        }

        return contentView;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    protected boolean needAddButtons() {
        return true;
    }

}
