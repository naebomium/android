package com.mobium.reference.filters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mobium.client.models.filters.FilterState;
import com.mobium.client.models.filters.FilteringRange;
import com.mobium.reference.R;
import com.mobium.reference.utils.ZeroTrimmer;
import com.mobium.reference.views.RangeSeekBar;


/**
 *  on 07.07.15.
 * http://mobiumapps.com/
 */
public class RangeViewController extends FilterViewController<FilteringRange.FilterRangeState, FilteringRange> {
    private TextView minValueView;
    private TextView maxValueView;
    private String syffix;
    private boolean syffixValid;

    private RangeSeekBar<Double> rangeBar;


    public RangeViewController(FilteringRange.FilterRangeState state, FilteringRange filteringRange) {
        super(state, filteringRange);

        syffix = m_filter.getSuffix();
        syffixValid = syffix != null && syffix.trim().length() > 0;
        state.addLister(this);
    }

    private String formatNumber(Double d) {
        return ZeroTrimmer.trimZero(d, m_filter.getStepValue());
    }

    @NonNull
    @Override
    public View getView(Context context, ViewGroup parent, boolean addView) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.filter_range_view2, parent, addView);

        ((TextView) rootView.findViewById(R.id.filter_view_title))
                .setText(m_filter.getTitle());


        rangeBar = new RangeSeekBar<>(m_filter.getMinValue(), m_filter.getMaxValue(), context);
        rangeBar.setNotifyWhileDragging(true);

        rangeBar.setSelectedMaxValue(m_state.getCurrentMaxValue());
        rangeBar.setSelectedMinValue(m_state.getCurrentMinValue());

        ((ViewGroup)rootView.findViewById(R.id.filter_range_view_range))
                .addView(rangeBar);

        minValueView = (TextView) rootView.findViewById(R.id.filter_range_view_from);
        maxValueView = (TextView) rootView.findViewById(R.id.filter_range_view_to);

        rangeBar.setOnRangeSeekBarChangeListener((bar, minValue, maxValue) -> {
                    m_state.setCurrentMinValue(minValue);
                    m_state.setCurrentMaxValue(maxValue);

                }
        );


//        maxValueView.setOnEditorActionListener((textView, i, keyEvent) -> {
//            Double rangeMaxValue = Double.valueOf(textView.getText().toString());
//
//            if (rangeMaxValue > rangeBar.getAbsoluteMaxValue()) {
//                rangeMaxValue = rangeBar.getAbsoluteMaxValue();
//                minValueView.setText(ZeroTrimmer.trimZero(rangeMaxValue, m_filter.getStepValue()));
//            }
//
//            if (rangeMaxValue < rangeBar.getSelectedMinValue()) {
//                rangeMaxValue = rangeBar.getSelectedMinValue();
//                minValueView.setText(ZeroTrimmer.trimZero(rangeMaxValue, m_filter.getStepValue()));
//            }
//            m_state.setCurrentMaxValue(rangeMaxValue);
//            return true;
//        });

//        minValueView.setOnEditorActionListener((textView, i, keyEvent) -> {
//            double rangeMinValue = ParserHelper.parseDouble(maxValueView.getText().toString());
//            if(rangeMinValue < rangeBar.getAbsoluteMinValue()) {
//                rangeMinValue = rangeBar.getAbsoluteMinValue();
//                maxValueView.setText(ZeroTrimmer.trimZero(rangeMinValue, m_filter.getStepValue()));
//            }
//            if (rangeMinValue > rangeBar.getSelectedMaxValue()) {
//                rangeMinValue = rangeBar.getSelectedMaxValue();
//                maxValueView.setText(ZeroTrimmer.trimZero(rangeMinValue, m_filter.getStepValue()));
//            }
//            m_state.setCurrentMinValue(rangeMinValue);
//            return true;
//        });

        m_state.notyfiListers();
        return rootView;
    }

    @Override
    public void onChange(FilterState newValue) {

        String currentMin = formatNumber(m_state.getCurrentMinValue());
        String currentMax = formatNumber(m_state.getCurrentMaxValue());

        minValueView.setText(syffixValid ? currentMin + " " + syffix : currentMin);
        maxValueView.setText(syffixValid ? currentMax + " " + syffix : currentMax);

        rangeBar.setSelectedMinValue(m_state.getCurrentMinValue());
        rangeBar.setSelectedMaxValue(m_state.getCurrentMaxValue());
    }


}
