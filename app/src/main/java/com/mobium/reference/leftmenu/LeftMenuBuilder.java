package com.mobium.reference.leftmenu;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.client.models.Action;
import com.mobium.config.prototype.ILeftMenu;

import java.util.ArrayList;
import java.util.List;

/**
 *  on 10.11.2015.
 */
public class LeftMenuBuilder {
    public static List<LeftMenuListView.LeftMenuItem> build(ILeftMenu model, Context context) {
        return
                Stream.of(model.getMenuSections(context))
                        .flatMap(section -> {
                            ArrayList<LeftMenuListView.LeftMenuItem> sectionMapped = new ArrayList<>();
                            if (section.getSectionTitle().length() > 0)
                                sectionMapped.add(new MenuSeparator(section.getSectionTitle()));
                            for (ILeftMenu.ILefMenuCell cell : section.getCells())
                                sectionMapped.add(new LeftMenuButton(
                                        new Action(
                                                cell.getActionType(),
                                                cell.getActionData()),
                                                cell.getTitle(),
                                                cell.getIcon(context)));
                            return Stream.of(sectionMapped);
                        })
                        .collect(Collectors.toList());
    }
}
