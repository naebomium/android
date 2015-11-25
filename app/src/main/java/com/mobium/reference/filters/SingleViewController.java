package com.mobium.reference.filters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.mobium.client.models.filters.FilterState;
import com.mobium.client.models.filters.FilteringSingle;
import com.mobium.reference.R;


/**
 *  on 06.07.15.
 * http://mobiumapps.com/
 */
public class SingleViewController extends FilterViewController<FilteringSingle.SingleState, FilteringSingle> {

    private CheckBox selectedBox;
    private TextView title;


    public SingleViewController(FilteringSingle.SingleState state, FilteringSingle filteringSingle) {
        super(state, filteringSingle);
        m_state.addLister(this);
    }

    @NonNull
    @Override
    public View getView(Context context, ViewGroup parent, boolean addView) {
        View resultView = LayoutInflater.from(context)
                .inflate(R.layout.filter_signle_view, parent, addView);

        selectedBox = (CheckBox) resultView.findViewById(R.id.filter_single_view_checkbox);
        title = (TextView) resultView.findViewById(R.id.filter_single_view_title);

        title.setText(m_filter.getTitle());

        selectedBox.setChecked(m_state.isSelected());


        selectedBox.setOnCheckedChangeListener((compoundButton, b) ->
               m_state.setSelected(b)
        );

        m_state.notyfiListers();


        resultView.setOnClickListener(view ->
                        m_state.setSelected(!m_state.isSelected())
        );

        return resultView;
    }



    @Override
    public void onChange(FilterState newValue) {
        selectedBox.setChecked(((FilteringSingle.SingleState) newValue).isSelected());
    }
}
