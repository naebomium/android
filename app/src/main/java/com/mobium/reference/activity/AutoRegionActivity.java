package com.mobium.reference.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobium.google_places_api.PlaceHttpApi;
import com.mobium.google_places_api.models.AutoCompleteResult;
import com.mobium.google_places_api.models.AutoCompleteType;
import com.mobium.new_api.utills.RegionUtils;
import com.mobium.reference.R;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.ChangeRegionUtils;
import com.mobium.reference.views.DelayAutoCompleteTextView;

import retrofit.RetrofitError;

/**
 *  on 27.08.15.
 */


public class AutoRegionActivity extends BaseStyledActivity {
    private DelayAutoCompleteTextView autoCompleteTextView;
    private ProgressBar progressBar;
    private PlaceHttpApi api;
    private AutoCompleteResult.Item currentItem;
    private MenuItem apply;
    private MenuItem clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMenuButtonState(ButtonState.gone);
        setTitle("Выбор региона");

        api = ChangeRegionUtils.getApi(this, Config.get().getApplicationData().getApiKeyGooglePlaceApi(), Config.get().logDebugInfo());

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_auto_region, container, false);

        autoCompleteTextView = (DelayAutoCompleteTextView) view.findViewById(R.id.autoText);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        autoCompleteTextView.setLoadingIndicator(progressBar);

        autoCompleteTextView.setText("Москва");
        container.addView(view);

        autoCompleteTextView.setAdapter(new AutoAdapter(api));
        autoCompleteTextView.setThreshold(1);


        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            currentItem = (AutoCompleteResult.Item) autoCompleteTextView.getAdapter().getItem(position);
            apply.setVisible(true);
            clear.setVisible(true);
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                apply.setVisible(false);
                if (s.length() == 0)
                    clear.setVisible(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Events.get(this).navigation().onPageOpen(OpenPageEvents.city.name());
    }


    private void sendResult() {
        RegionUtils.loadRegionsByAutoCompleteRegionWithDialog(this, currentItem, regionAndList -> {
            Intent resultIntent = new Intent();
            storage.onReceive(regionAndList.regions);
            resultIntent.putExtra(ChoiceRegionActivity.RESULT_TAG_REGION, regionAndList.region);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }


    public static class AutoAdapter extends BaseAdapter implements Filterable {
        private AutoCompleteResult.Item items[] = new AutoCompleteResult.Item[0];
        private final PlaceHttpApi api;

        public AutoAdapter(PlaceHttpApi api) {
            this.api = api;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public AutoCompleteResult.Item getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.item_dropdown, parent, false);
            }
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position).getDescription());
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
//                        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
                        try {
                            AutoCompleteResult result = api.getAutoComplete(
                                    constraint.toString(),
                                    new AutoCompleteType[]{AutoCompleteType.cities},
                                    null,
                                    null,
                                    null,
                                    null,
                                    "country:ru",
                                    null
                            );
                            result.getPredictions().ifPresent(values -> {
                                filterResults.values = values;
                                filterResults.count = values.length;
                            });

//                            runOnUiThread(() -> progressBar.setVisibility(View.GONE));

                        } catch (RetrofitError e) {
                            e.printStackTrace();
//                            runOnUiThread(() -> progressBar.setVisibility(View.GONE));

                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        items = (AutoCompleteResult.Item[]) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(apply)) {
            sendResult();
            return true;
        } else if (item.equals(clear)) {
            if (autoCompleteTextView != null) {
                autoCompleteTextView.getText().clear();
                clear.setVisible(false);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.apply_menu, menu);
        apply = menu.findItem(R.id.apply_menu_ok_button);
        apply.setVisible(false);
        clear = menu.findItem(R.id.apply_menu_clear_button);
        clear.setVisible(false);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (autoCompleteTextView.getText().length() > 0) {
            autoCompleteTextView.getText().clear();
            currentItem = null;
        }
    }
}
