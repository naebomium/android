package com.mobium.reference.views.adapters;

import java.util.Set;

/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public abstract class SetClicableAdapter<VH extends ClickableHolder, ITEM>
        extends ClickableAdapter<VH, ITEM> {
    final Set<ITEM> set;

    protected SetClicableAdapter(Set<ITEM> set) {
        this.set = set;
    }


}
