package com.mobium.reference.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobium.new_api.models.Region;
import com.mobium.reference.R;
import com.mobium.reference.utils.Functional;
import com.mobium.reference.views.adapters.ClickableHolder;
import com.mobium.reference.views.adapters.ListClickableAdapter;

import java.util.List;

/**
 *  on 20.06.15.
 * http://mobiumapps.com/
 */
public class ChoiceRegionActivity extends BaseStyledActivity {
    public final static String REQUEST_EXTRA_CURRENT_REGION = "ChoiceRegionActivity.region";
    public final static String RESULT_TAG_REGION = "ChoiceRegionActivity.region";

    private RecyclerView regionsView;
    private MenuItem apply;
    private Region current;
    private List<Region> regions;

    @Override
    public void onBackPressed() {
        if (((RegionAdapter) regionsView.getAdapter())
                .getCurrentRegion() != null)
            sendResult();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMenuButtonState(ButtonState.gone);

        Intent intent = getIntent();
        regions = storage.getRegions();

        current = (Region) intent.getSerializableExtra(REQUEST_EXTRA_CURRENT_REGION);

        regionsView = new RecyclerView(this);
        regionsView.setLayoutManager(new LinearLayoutManager(this));

        regionsView.setAdapter(new RegionAdapter(regions, this, current, data -> {
            apply.setVisible(true);
            current = data;
        }));

        int index = regions.indexOf(current);
        if (index > 0) {
            int offset = 5;
            if (index > 10)
                index -= offset;
            regionsView.scrollToPosition(index);
        }

        container.addView(regionsView,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        setTitle("Выбор региона");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            sendResult();
            return true;
        } else if (item.equals(apply)) {
            sendResult();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void sendResult() {
        Region result = ((RegionAdapter) regionsView.getAdapter())
                .getCurrentRegion();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_TAG_REGION, result);

        setResult(RESULT_OK, resultIntent);
        finish();
    }


    private static class RegionAdapter extends
            ListClickableAdapter<RegionHolder, Region> {
        private Region currentRegion;
        private android.os.Handler handler = new android.os.Handler();
        private Functional.Receiver<Region> regionReceiver;


        @Override
        protected void onItemClick(Region region, int position) {
            currentRegion = region;
            handler.postDelayed(this::notifyDataSetChanged, 300);
            regionReceiver.onReceive(region);
        }


        public RegionAdapter(List<Region> regions, Context context,
                             @Nullable Region current,
                             Functional.Receiver<Region> regionReceiver) {
            super(regions, context);
            if (current != null)
                currentRegion = current;
            this.regionReceiver = regionReceiver;
        }

        @Override
        protected void applyItemToHolder(RegionHolder holder, Region region, int itemPosition) {
            holder.title.setText(region.getTitle());
            holder.choice.setVisibility(region.equals(currentRegion) ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public RegionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RegionHolder(
                    inflater.inflate(R.layout.fragment_choice_region_item, parent, false)
            );
        }

        public Region getCurrentRegion() {
            return currentRegion;
        }

        public void setCurrentRegion(Region currentRegion) {
            this.currentRegion = currentRegion;
        }


    }

    public static class RegionHolder extends ClickableHolder {
        public TextView title;
        public View choice;

        public RegionHolder(View itemView) {
            super(itemView);
            choice = itemView.findViewById(R.id.fragment_choice_region_item_selected);
            title = (TextView) itemView.findViewById(R.id.fragment_choice_region_item_title);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.apply_menu, menu);
        apply = menu.findItem(R.id.apply_menu_ok_button);
        apply.setVisible(false);
        return true;
    }


}
