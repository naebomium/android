package com.mobium.new_api.models;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.mobium.reference.utils.Functional.equalsMapPredicate;
import static com.mobium.reference.utils.Functional.optFind;

/**
 *   11.02.15.
 * http://wiki.mobium.pro/pages/viewpage.action?pageId=6947822 - формат получения регионов
 */

public class Region implements Serializable {
    private String id;
    private Type type;
    private String title;
    private String google_places_id;
    private String deliveryListString;

    private transient ShopPoint[] pickUp;
    private transient List<ShopPoint> points;
    public transient Map<String, List<ShopPoint>> services;


    public Region() {

    }

    public Region(String id, String title, String typeId) {
        this.id = id;
        this.title = title;
        this.type = Type.findType(typeId);
        deliveryListString = "";
    }



    public Region(String id, String title, String typeId, String deliveryIdString) {
        this.id = id;
        this.title = title;
        this.type = Type.findType(typeId);
        deliveryListString = deliveryIdString;
    }

    public Region(String id, String title, Type type) {
        this.id = id;
        this.type = type;
        this.title = title;
        deliveryListString = "";
    }

    // Список идентификаторов доставки, доступных для данного региона, через запятую
    private List<String> formatDelivery() {
        return  Stream.of(deliveryListString.split(",")).collect(Collectors.toList());
    }

    public enum Type {
        // Город, подверженный геолокации. Определение производится по названию города
        @SerializedName("city")
        CITY("city"),

        // Cтандартное значение, на которое производится откат, если геолокация не дала результат
        @SerializedName("default")
        DEFAULT("default"),
        @SerializedName("")
        NONE("");
        final String id;
        Type(String id) {
            this.id = id;
        }

        public static Type findType(String typeTitle) {
            return optFind(Arrays.asList(Type.values()),
                    equalsMapPredicate(typeTitle, (Type t) -> t.id),
                    null);

        }
    }

    /**
     * регион россия используется в случае, если пользователь не указал регионов
     * @return Region с возможностью оплаты только оффлайн
     */
    public static Region getRussiaRegion() {
        return new Region("NO_ID", "Россия", Type.NONE) {
            @Override
            public List<String> getDelivery() {
                return Collections.singletonList("cash");
            }

            @Override
            public String[] getDeliveryArray() {
                return new String[]{"cash"};
            }
        };
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Region region = (Region) o;

        if (!id.equals(region.id)) return false;
        if (type != region.type) return false;
        if (!title.equals(region.title)) return false;
        return !(deliveryListString != null ? !deliveryListString.equals(region.deliveryListString) : region.deliveryListString != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }

    public static Region findRegionById(List<Region> regions, String id) {
        return optFind(regions,
                equalsMapPredicate(id, (Region r) -> r.id),
                getRussiaRegion());
    }

    /**
     * поиск региона с тегом @id в списке регионов
     * @param regions список регионов
     * @param id айди искомого
     * @return регион c id, если он найден, иначе регион Россия
     */
    public static Region findRegionById(Region[] regions, String id) {
        return Region.findRegionById(Arrays.asList(regions), id);
    }

    public boolean hasPoinst() {
        return !(points == null || points.size() == 0);
    }

    public boolean isPointsHasCoordinates() {
        if (points == null || points.size() == 0)
            return false;
        for (ShopPoint point : points) {
            if (point.getCoordinates().isPresent())
                return true;
        }
        return false;
    }

    public List<ShopPoint> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return title;
    }


    public boolean isNoneVariant(){
        return type == Type.NONE;
    }

    public void setPoints(ShopPoint... points) {
        this.points = Arrays.asList(points);
    }

    public void setPoints(List<ShopPoint> points) {
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getDelivery() {
        return formatDelivery();
    }

    public String[] getDeliveryArray(){
        return deliveryListString.split(",");
    }

    public void setDeliveryListString(String deliveryListString) {
        this.deliveryListString = deliveryListString;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public enum PlacesService {
        none, google_places
    }

    public Optional<String> getGooglePlacesId() {
        return Optional.ofNullable(google_places_id);
    }

    public void setPickUp(ShopPoint[] pickUp) {
        this.pickUp = pickUp;
    }

    public Optional<ShopPoint[]> pickUps() {
        return Optional.ofNullable(pickUp);
    }
}
