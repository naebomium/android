package com.mobium.reference.filters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.client.models.filters.FilterState;
import com.mobium.client.models.filters.FilteringFlag;
import com.mobium.reference.R;
import com.mobium.reference.views.MultiSpinner;

import java.util.List;

/**
 *  on 30.06.15.
 * http://mobiumapps.com/
 */
public class FlagViewController extends FilterViewController<FilteringFlag.FilterFlagState, FilteringFlag> {
    private TextView  title;

    public FlagViewController(FilteringFlag.FilterFlagState state,
                              FilteringFlag filteringFlag) {
        super(state, filteringFlag);
        m_state.addLister(this);
    }


    @NonNull
    @Override
    public View getView(Context context, ViewGroup parent, boolean addView) {
        View resultView = LayoutInflater.from(context)
                .inflate(R.layout.filter_flag_view, parent, addView);

        ((TextView) resultView.findViewById(R.id.filter_view_title)).
                setText(m_filter.getTitle());

        title = (TextView) resultView.findViewById(R.id.button);

        title.setText("Выбрать");

        resultView.setOnClickListener(view -> {
            final MultiSpinner spinner = new MultiSpinner(context);

            final List<String> titlesId = Stream.of(m_state.getFlags().keySet())
                    .collect(Collectors.toList());

            final List<String> titles = Stream.of(titlesId)
                    .map(m_filter::getTitle)
                    .collect(Collectors.toList());

            boolean selectedItems[] = new boolean[titles.size()];

            for (int j = 0; j < selectedItems.length; j++) {
                selectedItems[j] = m_state.getSelectedFlags().get(titlesId.get(j));
            }

            spinner.setItems(titles, selectedItems, m_filter.getTitle(), selected -> {
                for (int i = 0; i < selected.length; i++)
                    m_state.getSelectedFlags().put(titlesId.get(i), selected[i]);
                m_state.notyfiListers();
            });

            spinner.setTitle(m_filter.getTitle());

            spinner.performClick();
        });
        m_state.notyfiListers();

        return resultView;
    }


    @Override
    public void onChange(FilterState value) {
        FilteringFlag.FilterFlagState newValue = (FilteringFlag.FilterFlagState) value;
        if (title != null) {

            List<String> selectedTitle = Stream.of(m_filter.getKeys())
                    .filter(newValue.getSelectedFlags()::get)
                    .map(key -> m_state.getFlags().get(key))
                    .collect(Collectors.toList());

            String newTitle = "";
            if (selectedTitle.size() == 0)
                title.setText("Выбрать");
            else {
                newTitle += selectedTitle.get(0);
                for (int i = 1; i < selectedTitle.size(); i++)
                    newTitle += ", ".concat(selectedTitle.get(i));

                if (newTitle.length() > 0)
                    title.setText(newTitle);
                else
                if (title.getMeasuredWidth() > title.getPaint().measureText(newTitle))
                    newTitle += "...";
                title.setText(newTitle);
            }
        }
    }
}
