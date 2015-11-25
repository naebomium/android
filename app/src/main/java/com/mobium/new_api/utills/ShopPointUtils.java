package com.mobium.new_api.utills;

import android.support.annotation.NonNull;
import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.new_api.models.LocationFilters;
import com.mobium.new_api.models.ShopPoint;

import java.util.*;

/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public class ShopPointUtils {

    public static List<ShopPoint> filter(@NonNull List<ShopPoint> p, @NonNull ShopPoint.ShopPointType... allowTypes) {
        return Stream.of(p)
                .filter(shopPoint -> {
                    for (ShopPoint.ShopPointType t : allowTypes)
                        if (t.equals(shopPoint.getType()))
                            return true;
                    return false;
                }).collect(Collectors.toList());
    }

    /**
     * группирует сервисные центры по брендам.
     * @param list - список сервисных центорв, каждый обслуживает от 1 до n брендов
     * @return таблица бренд - список сервисных центров, обслуживающих его
     */
    public static TreeMap<String, List<ShopPoint>> buildServiceMap(@NonNull List<ShopPoint> list) {
        TreeMap<String, List<ShopPoint>> result = new TreeMap<>();

        Stream.of(list)
                .filter(shopPoint -> shopPoint.getFilters().isPresent())
                .forEach(point -> Stream.of(point.getFilters().get())
                        .forEach(filter -> {
                            if (!result.containsKey(filter))
                                result.put(filter, new LinkedList<>());
                            result.get(filter).add(point);
                        }));

        return result;
    }

    /**
     * Найти url бренда в писке фильров локаций
     * @param filters фильтры локаций
     * @param brandTitle название бренда
     * @return возможно url
     */

    public static Optional<String> getBrandIconUrl(LocationFilters filters, String brandTitle) {
        Optional<LocationFilters.LocationFilter> brandFilter =
                Stream.of(filters.filters)
                    .filter(filter -> filter.id.equals("brand"))
                    .findFirst();
        if (brandFilter.isPresent())
            return Stream.of(brandFilter.get().values)
                .filter(value -> value.title.equals(brandTitle))
                .map(LocationFilters.Value::getIcon)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
        return Optional.empty();
    }
}
