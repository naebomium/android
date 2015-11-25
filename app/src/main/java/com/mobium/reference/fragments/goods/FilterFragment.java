package com.mobium.reference.fragments.goods;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.client.models.Sorting;
import com.mobium.client.models.filters.FilterState;
import com.mobium.client.models.filters.Filtering;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.filters.FilterViewController;
import com.mobium.reference.filters.SortingViewController;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.model.SortingDescriptor;
import com.mobium.reference.utils.Functional;
import com.mobium.reference.utils.SortingManager;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *  on 08.07.15.
 * http://mobiumapps.com/
 */
public class FilterFragment extends BasicContentFragment
        implements Functional.ChangeListener<SortingDescriptor> {

    public final static String ITEMS_ID = "FilterFragment_ITEMS";
    public final static String CATEGORY_ID = "FilterFragment_CATEGOTY";
    public final static String ID = "com.mobium.reference.fragments.FilterFragment";

    private List<Sorting> sortings;

    private List<ShopItem> items;

    private List<SortingDescriptor> sortingDescriptors;

    private HashMap<Filtering, FilterState> filterMap;

    private LinearLayout filterList;
    private TextView hint;
    private ShopCategory category;


    public static FilterFragment newInstance(ShopCategory category) {
        FilterFragment myFragment = new FilterFragment();

        Bundle args = new Bundle();

        args.putString(CATEGORY_ID, category.id);

        myFragment.setArguments(args);

        return myFragment;
    }


    public ReferenceApplication getApplication() {
        return (ReferenceApplication) getActivity().getApplication();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arg = getArguments();
        if (arg != null) {

            category = getApplication().getCategories().get(arg.getString(CATEGORY_ID));

            sortings = category.sortings;

            sortingDescriptors = new ArrayList<>(sortings.size());
            Stream.of(sortings)
                    .forEach(sorting -> {
                        sortingDescriptors.add(new SortingDescriptor(sorting, true));
                        sortingDescriptors.add(new SortingDescriptor(sorting, false));
                    });


            if (category.filterMap == null) {
                category.filterMap = new HashMap<>(category.filterings.size());
                for (Filtering f : category.filterings)
                    category.filterMap.put(f, f.createState());
            }


            items = Arrays.asList(getApplication().getShopCache().fetchCategoryItems(category.id));
            filterMap = category.filterMap;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.filters.name());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.acticvity_filter, container, false);
        filterList = (LinearLayout) rootView.findViewById(R.id.activity_filter_filters);
        hint = (TextView) rootView.findViewById(R.id.activity_filter_hint);


        rootView.findViewById(R.id.activity_filter_clear)
                .setOnClickListener(view ->
                                Stream.of(filterMap.values())
                                        .forEach(FilterState::clear)
                );

        rootView.findViewById(R.id.activity_filter_filterOut)
                .setOnClickListener(this::onButtonClick);



        filterList.removeAllViews();

        View sortingView = SortingViewController.createView(
                filterList,
                false,
                category.selectedSorting,
                this,
                sortingDescriptors
        );

        filterList.addView(sortingView);

        Stream.of(filterMap.entrySet())
                .map(e -> FilterViewController.make(e.getValue(), e.getKey()))
                .map(c -> c.getView(getActivity(), filterList, false))
                .forEach(filterList::addView);


        return rootView;
    }


    /**
     * is item not removed by filtering
     */
    private final Predicate<ShopItem> saveItem = (ShopItem item) -> {
        for (FilterState filterState : filterMap.values())
            if (filterState.isCustomState())
                if (filterState.filterOut(item))
                    return false;
        return true;
    };


    /**
     * Filter out goods with current filterStates
     * @return filtered list of goods
     */
    private List<ShopItem> filterOut() {
        return
               Stream.of(items)
                        .filter(saveItem)
                        .collect(Collectors.toList());
    }

    private void onButtonClick(View view) {
        ArrayList<ShopItem> resultItems = new ArrayList<>(
                SortingManager.getSorted(filterOut(), category.selectedSorting)
        );


        Intent intent = new Intent();
        intent.putExtra(ITEMS_ID, resultItems);

        getTargetFragment()
                .onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        intent
                );
        Stream.of(filterMap.values()).forEach(FilterState::clearListeners);
        new Handler().postDelayed(() -> getFragmentManager().popBackStack(), 100);
    }

    @Override
    protected String getTitle() {
        return "Фильтры";
    }

    @Override
    public boolean onBackPressed() {
        onButtonClick(null);

        return true;
    }


    //    @Override
//    public Animator     (int transit, boolean enter, int nextAnim)
//    {
//        final int animatorId = (enter) ? R.anim.in_anim : R.anim.out_anim;
//
//
//        return super.onCreateAnimator(transit, enter, nextAnim);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Stream.of(filterMap.values()).forEach(FilterState::clearListeners);
    }

    @Override
    public void onChange(SortingDescriptor newValue) {
        category.selectedSorting = newValue;
    }
}
