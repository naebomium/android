package com.mobium.reference.filters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.reference.R;
import com.mobium.reference.model.SortingDescriptor;
import com.mobium.reference.utils.Functional;

import java.util.List;

/**
 *  on 08.07.15.
 * http://mobiumapps.com/
 */
public class SortingViewController {

    public static View createView(ViewGroup parent,
                                  boolean add,
                                  SortingDescriptor current,
                                  final Functional.ChangeListener<SortingDescriptor> listener,
                                  final List<SortingDescriptor> sorts) {
        final Context context = parent.getContext();
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.filter_flag_view, parent, add);

        ((TextView) rootView.findViewById(R.id.filter_view_title)).
                setText("Сортировка");


        final TextView title = (TextView) rootView.findViewById(R.id.button);

        if (current == null)
            title.setText("Выбрать");
        else
            title.setText(current.toString());

        final ArrayAdapter<SortingDescriptor> adapter =
                new ArrayAdapter<>(
                        context,
                        android.R.layout.simple_spinner_item,
                        sorts
                );

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Выбор сортировки");


        final String[] titles = Stream.of(sorts)
                .map(SortingDescriptor::toString)
                .collect(Collectors.toList())
                .toArray(new String[sorts.size()]);

        rootView.setOnClickListener(view ->
                        builder.setItems(titles, (dialogInterface1, i) -> {
                            listener.onChange(sorts.get(i));
                            title.setText(sorts.get(i).toString());
                        }).show()
        );

        return rootView;
    }



}
