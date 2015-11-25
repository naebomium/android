package com.mobium.reference.utils;

import android.support.annotation.Nullable;
import com.annimon.stream.function.BiFunction;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.model.SortingDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortingManager {

    public static void applySort(List<ShopItem> items, @Nullable SortingDescriptor sortingDescriptor) {
        if (sortingDescriptor == null)
            return;
        switch (sortingDescriptor.getSorting().getType()) {
            case NUMBER:
                Collections.sort(items,
                        numberComparator.apply(
                                sortingDescriptor.getSorting().getId(),
                                sortingDescriptor.isAscending())
                );
                break;
            case STRING:
            default:
                Collections.sort(items,
                        stringComparator.apply(
                                sortingDescriptor.getSorting().getId(),
                                sortingDescriptor.isAscending())
                );
                break;
        }
    }


    public static List<ShopItem> getSorted(List<ShopItem> items, @Nullable SortingDescriptor sortingDescriptor) {
        applySort(items, sortingDescriptor);
        return items;
    }


    public static List<ShopItem> getSortedCopy(List<ShopItem> items, @Nullable SortingDescriptor sortingDescriptor) {
        if (sortingDescriptor == null)
            return items;

        ArrayList<ShopItem> result = new ArrayList<ShopItem>(items);

        applySort(result, sortingDescriptor);

        return result;
    }




    static final BiFunction<String, Boolean, Comparator<ShopItem>> numberComparator =
        (sortingId, isAscending) -> (a, b) -> {
            int valueA = 0;
            int valueB = 0;

            try {
                if (a.sorting.containsKey(sortingId))
                    valueA = (Integer) a.sorting.get(sortingId);
            } catch (Exception e) {
                // Ignore this
                e.printStackTrace();
            }

            try {
                if (b.sorting.containsKey(sortingId))
                    valueB = (Integer) b.sorting.get(sortingId);
            } catch (Exception e) {
                // Ignore this
                e.printStackTrace();
            }

            if (isAscending) {
                return -(valueA - valueB);
            } else {
                return valueA - valueB;
            }
        };

    static final BiFunction<String, Boolean, Comparator<ShopItem>> stringComparator =
            (sortingId, isAscending) -> (a, b) -> {
        {
            String valueA = "";
            String valueB = "";

            try {
                if (a.sorting.containsKey(sortingId))
                    valueA = (String) a.sorting.get(sortingId);
            } catch (Exception e) {
                // Ignore this
                e.printStackTrace();
            }

            try {
                if (b.sorting.containsKey(sortingId))
                    valueB = (String) b.sorting.get(sortingId);
            } catch (Exception e) {
                // Ignore this
                e.printStackTrace();
            }

            if (isAscending) {
                return valueA.compareTo(valueB);
            } else {
                return -valueA.compareTo(valueB);
            }
        }
    };
}
