package com.mobium.reference.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.mobium.reference.filters.FilterViewController;
import com.mobium.reference.model.SortingDescriptor;
import com.mobium.reference.utils.SortingManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  on 24.06.15.
 * http://mobiumapps.com/
 */
public class FilterActivity extends BaseStyledActivity {
    public final static String EXTRA_FILTERS = "EXTRA_FILTERS";
    public final static String EXTRA_SORTING = "EXTRA_SORTING";
    public final static String EXTRA_PRODUCT_LIST = "EXTRA_PRODUCTS";

    private List<Sorting> sortings;
    private List<Filtering> filterings;
    private ArrayList<ShopItem> items;


    private SortingDescriptor selectedSorting;
    private List<FilterState> filterStates = new ArrayList<>();


    private LinearLayout filterList;
    private TextView hint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.light_gray_background));
        parseIntent(getIntent());


        setContentView(R.layout.acticvity_filter);

        filterList = (LinearLayout) findViewById(R.id.activity_filter_filters);
        hint = (TextView) findViewById(R.id.activity_filter_hint);

        Button filterOutBtn = (Button) findViewById(R.id.activity_filter_filterOut);


        View clearFilers = findViewById(R.id.activity_filter_clear);

        Collections.sort(filterings, (f1, f2) ->
            f1.getType().compareTo(f2.getType())
        );

        createAndShowFilterView();

        clearFilers.setOnClickListener(this::clearFilters);

        filterOutBtn.setOnClickListener(this::onButtonClick);

    }



    private void createAndShowFilterView() {
        filterStates.clear();
        filterList.removeAllViews();

        for (Filtering f: filterings) {
            FilterState state = f.createState();
            FilterViewController controller = FilterViewController.make(state, f);
            if (controller == null)
                continue;
            filterStates.add(state);
            filterList.addView(
                    controller.getView(this, filterList, false)
            );
        }
    }


    /**
     * create intent for StartActivityForResult
     * @return intent
     */
    public static Intent makeIntent(Context context, ShopCategory category, ArrayList<ShopItem> items) {
        Intent result = new Intent();
        result.setClass(context, FilterActivity.class);
        result.putExtra(EXTRA_SORTING, category.sortings);
        result.putExtra(EXTRA_FILTERS, category.filterings);
        result.putExtra(EXTRA_PRODUCT_LIST, items);
        return result;
    }


    /**
     * is item not removed by filtering
     */
    private final Predicate<ShopItem> saveItem = (ShopItem item) -> {
        for (FilterState filterState : filterStates)
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

    private void applySorts(List<ShopItem> items) {
        SortingManager.applySort(items, selectedSorting);
    }



    private void parseIntent(Intent intent) {
        sortings = (List<Sorting>) intent.getSerializableExtra(EXTRA_SORTING);
        filterings = (List<Filtering>) intent.getSerializableExtra(EXTRA_FILTERS);
        items = (ArrayList<ShopItem>) intent.getSerializableExtra(EXTRA_PRODUCT_LIST);
    }


    private void clearFilters(View clearBnt) {
        for (FilterState state : filterStates)
            state.clear();
    }

    private void onButtonClick(View view) {
        List<ShopItem> resultItems = filterOut();


        resultItems.size();
    }
}
