package com.mobium.reference.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.mobium.client.models.modifications.Modification;
import com.mobium.client.models.modifications.OfferModification;
import com.mobium.reference.R;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.Functional;

import java.util.List;
import java.util.Observable;
import java.util.UUID;

/**
 *  on 23.09.2015.
 */
public class ModificationView extends LinearLayout {

    private static final String TAG = "ModificationView";

    Modification[] modifications;

    public ModificationView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initModifications(Modification[] modifications) {
        this.modifications = modifications;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.modification_menu, null);
        LinearLayout rootPopupView = (LinearLayout) view.findViewById(R.id.modification_root_layout);
        int size = modifications.length;
        Stream.of(modifications).forEach(modification -> {
            LinearLayout headerLayout = (LinearLayout) inflater.inflate(R.layout.modification_item, null);
            headerLayout.setTag(modification.id);
            TextView headerText = (TextView) headerLayout.findViewById(R.id.modification_title);
            headerText.setText(modification.title);
            TextView valueText = (TextView) headerLayout.findViewById(R.id.modification_value);
            valueText.setText(modification.getDefaultTitle());
            rootPopupView.addView(headerLayout);

            headerLayout.setOnClickListener(e -> {
                initDialog(modification);
            });
        });

        if(size > 0) {
            setDividerGone(view, modifications[size - 1]);
        }

        this.addView(view);
    }

    private View getDivider(View rootView, Modification m) {
        View v = rootView.findViewWithTag(m.id);
        if(v != null)
            return v.findViewById(R.id.divider);
        return null;
    }

    private void setDividerGone(View v, Modification m) {
        View divider = getDivider(v, m);
        if(divider != null)
            divider.setVisibility(GONE);
    }

    private void initDialog(Modification mod) {
        Dialogs.showSelectDialog(
                getContext(),
                mod.title,
                Stream.of(mod.offerModifications).collect(Collectors.toList()),
                this::OfferModificationApply,
                newOfferModification -> {
                    mod.setSelectedModification(newOfferModification);
                    setModificationValue(newOfferModification.title, mod.id);
                });
    }

    private void setModificationValue(String value, String modificationId) {
        LinearLayout layout = (LinearLayout) findViewWithTag(modificationId);
        if(layout != null) {
            TextView valueText = (TextView) layout.findViewById(R.id.modification_value);
            valueText.setText(value);
        }
    }

    private String OfferModificationApply(OfferModification offerModification) {
        return offerModification.title;
    }
}
