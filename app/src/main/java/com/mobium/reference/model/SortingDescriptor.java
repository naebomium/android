package com.mobium.reference.model;

import com.mobium.client.models.Sorting;

import java.io.Serializable;

/**
 *  on 06.03.14.
 */
public class SortingDescriptor implements Serializable {
    private boolean isAscending;
    private Sorting sorting;

    public SortingDescriptor(Sorting sorting, boolean ascending) {
        this.sorting = sorting;
        this.isAscending = ascending;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public Sorting getSorting() {
        return sorting;
    }

    @Override
    public String toString() {
        if (sorting.getType() == Sorting.Type.DEFAULT) {
            return sorting.getTitle();
        }

        if (isAscending) {
            if (sorting.getType() == Sorting.Type.NUMBER) {
                return sorting.getTitle() + " (Вниз)";
            } else {
                return sorting.getTitle() + " (А-Я)";
            }
        } else {
            if (sorting.getType() == Sorting.Type.NUMBER) {
                return sorting.getTitle() + " (Вверх)";
            } else {
                return sorting.getTitle() + " (Я-А)";
            }
        }
    }


}