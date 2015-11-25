package com.mobium.reference.views;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.annimon.stream.Objects;
import com.annimon.stream.Stream;
import com.annimon.stream.function.FunctionalInterface;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  on 08.04.15.
 */
public class TabPanelController {
    private ViewGroup titleContainer;
    private FrameLayout contentContainer;

    private HashMap<TabPanelItem, View> views = new HashMap<>();
    private List<View> titles = new ArrayList<>();
    private List<? extends TabPanelItem> items;

    private OnChangeTabListener listener;
    int currentIndex;

    private int color;

    public TabPanelController(ViewGroup titleContainer, FrameLayout contentView, List<? extends TabPanelItem> items) {
        this.titleContainer = titleContainer;
        this.contentContainer = contentView;
        this.items = items;
        color = titleContainer.getContext().getResources().getColor(R.color.green);
        contentContainer.removeAllViews();
        setSetUpTitles(items);
        setCurrentPage(0);
    }



    private void setItemViewActive(View item, boolean active) {
        if (item == null)
            return;
        TextView textView = (TextView) item.findViewById(R.id.titled_pager_panel_item_title);
        textView.setTextColor(active ? color : Color.BLACK);
        View view = item.findViewById(R.id.titled_pager_panel_item_separator);
        view.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    public void setCurrentPage(int number) {
        if (number < 0 && number >= items.size() || (number == currentIndex && views.size() > 0))
            return;

        currentIndex = number;
        TabPanelItem currenetItem = items.get(number);
        View currentView;

        if (views.containsKey(currenetItem))
            currentView = views.get(currenetItem);
        else {
            currentView = currenetItem.getView(contentContainer);
            views.put(currenetItem, currentView);
            contentContainer.addView(currentView);
        }

        currentView.setVisibility(View.VISIBLE);

        for (TabPanelItem item : items) {
            if (views.containsKey(item) && !Objects.equals(item, currenetItem))
                views.get(item).setVisibility(View.GONE);
        }

        if (titles.size() > number)
            setItemViewActive(titles.get(number), true);

        for (int indexPage = 0; indexPage < titles.size(); indexPage++) {
            if (indexPage == number)
                continue;
                if (titles.size() > indexPage)
                    setItemViewActive(titles.get(indexPage), false);
        }

        if (listener != null)
            listener.onChangeTab(number);
    }

    private void setSetUpTitles(List<? extends TabPanelItem> items) {
        titleContainer.removeAllViews();
        int count = items.size();

        for (int i = 0; i < count; i++) {
            String title = items.get(i).getTitle();

            View item = View.inflate(titleContainer.getContext(), R.layout.titled_pager_panel_item, null);
            TextView textView = (TextView) item.findViewById(R.id.titled_pager_panel_item_title);
            textView.setText(title);
            textView.setTextColor(color);
            item.findViewById(R.id.titled_pager_panel_item_separator).setBackgroundColor(color);

            final int finalI = i;
            item.setOnClickListener(v -> setCurrentPage(finalI));
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1);

            titles.add(item);
            titleContainer.addView(item, param);
        }

    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setOnChangeTabListener(OnChangeTabListener listner) {
        this.listener = listner;
    }

    public interface TabPanelItem {
        String getTitle();

        View getView(ViewGroup viewGroup);
    }

    @FunctionalInterface
    public interface OnChangeTabListener {
        void onChangeTab(int index);
    }

}
