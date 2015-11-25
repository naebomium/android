package com.mobium.reference.filters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.mobium.client.models.filters.*;
import com.mobium.reference.utils.Functional;

/**
 *  on 30.06.15.
 * http://mobiumapps.com/
 */
public abstract class FilterViewController<State extends FilterState, Filter extends Filtering>
        implements Functional.ChangeListener<FilterState> {

    protected final State m_state;
    protected final Filter m_filter;


    public FilterViewController(State state, Filter filter) {
        m_state = state;
        m_filter = filter;
    }

    @NonNull
    public abstract View getView(Context context, ViewGroup parent, boolean addView);



    @NonNull
    public static <State extends FilterState, Filter extends Filtering>
    FilterViewController<State, Filter> make(State s, Filter f) {
        switch (f.getType()) {
            case flags:
                return (FilterViewController<State, Filter>)
                        new FlagViewController((FilteringFlag.FilterFlagState) s, (FilteringFlag) f);
            case single:
                return (FilterViewController<State, Filter>)
                        new SingleViewController((FilteringSingle.SingleState) s, (FilteringSingle) f);
            case range:
                return (FilterViewController<State, Filter>)
                        new RangeViewController((FilteringRange.FilterRangeState) s, (FilteringRange) f);
            default:
                throw new IllegalArgumentException();
        }
    }
}
