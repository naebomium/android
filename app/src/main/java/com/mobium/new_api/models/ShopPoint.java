package com.mobium.new_api.models;

import com.annimon.stream.Objects;
import com.annimon.stream.Optional;
import com.google.gson.annotations.SerializedName;
import com.mobium.client.models.Opinion;
import com.mobium.client.models.OpinionDiscussed;
import com.mobium.client.models.Opinions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *   17.02.15.
 * Физический магазин в регионе.
 */

public class ShopPoint implements OpinionDiscussed {
    private String id;
    private String title;

    private String address;

    @SerializedName("region_id")
    private String regionId;
    private ShopPointType type;

    private String subway;
    private String phone;
    private String image;
    private String workday;
    private Filters filters;

    private Coordinates coordinates;
    private transient Opinions opinions;


    @Override
    public String toString() {
        return address;
    }


    public ShopPoint() {
    }

    @Override
    public String getOpinionTag() {
        return Opinion.getObjectType(this);
    }

    /**
     *  on 11.07.15.
     * http://mobiumapps.com/
     */
    public static class Coordinates implements Serializable {
        public String lon;
        public String lat;

        public String getLonStr() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getLatStr() {
            return lat;
        }

        public int getLon() {
            return  (int) (Double.parseDouble(lon) * 1e6);
        }

        public int getLat() {
            return  (int) (Double.parseDouble(lat) * 1e6);
        }



        public void setLat(String lat) {
            this.lat = lat;
        }
        boolean invalid() {
            boolean isEmpty = lat == null ||
                    lon == null ||
                    lat.trim().length() == 0 ||
                    lat.trim().length() == 0;
            if (isEmpty)
                return true;
            try {
                double d = Double.parseDouble(lon);
                double d2 = Double.parseDouble(lat);
                return d == 0. || d2 == 0.;
            } catch (NumberFormatException e) {
                return true;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Coordinates)) return false;
            Coordinates coord = (Coordinates) o;
            return Objects.equals(lat, coord.lat) &&
                    Objects.equals(lon, coord.lon);
        }
    }

    public Optional<ArrayList<String>> getFilters() {
        return filters == null ? Optional.empty() : Optional.ofNullable(filters.brand);
    }

    public enum ShopPointType {
        noPickupShop("noPickupShop", "Магазин без возможности самовывоза"),
        pickupShop("pickupShop", "Магазин с возможностью самовывоза"),
        service("service", "Сервисный центр");

        final String id;
        final String title;

        ShopPointType(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
        public static ShopPointType findById(String id) {
            for (ShopPointType t : ShopPointType.values()) {
                if (t.getId().equals(id))
                    return t;
            }
            return noPickupShop;
        }
    }

    public static class Filters implements Serializable {
        public ArrayList<String> brand;

        public Filters() {
        }
    }

    public static final class ShopPointList  extends ResponseBase {
        public ShopPoint[] points;

        public ShopPointList() {
        }

        public List<ShopPoint> getPoints() {
            return Arrays.asList(points);
        }

        public void setPoints(ShopPoint[] points) {
            this.points = points;
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getRegionId() {
        return regionId;
    }

    public ShopPointType getType() {
        return type;
    }


    public Optional<String> getSubway() {
        return Optional.ofNullable(subway);
    }

    public Optional<String> getPhone() {
        return Optional.ofNullable(phone);
    }

    public Optional<String> getImage() {
        return Optional.ofNullable(image);
    }

    public Optional<String> getWorkday() {
        return Optional.ofNullable(workday);
    }

    public Optional<Coordinates> getCoordinates() {
        if (coordinates != null && !coordinates.invalid())
            return Optional.of(coordinates);
        return Optional.empty();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRegionId(String region_id) {
        this.regionId = region_id;
    }

    public void setType(ShopPointType type) {
        this.type = type;
    }

    public void setSubway(String subway) {
        this.subway = subway;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }


    @Override
    public void setOpinions(Opinions opinions) {
        this.opinions = opinions;
    }

    @Override
    public Optional<Opinions> getOpinions() {
        return Optional.ofNullable(opinions);
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopPoint)) return false;
        ShopPoint point = (ShopPoint) o;
        return Objects.equals(id, point.id) &&
                Objects.equals(title, point.title) &&
                Objects.equals(address, point.address) &&
                Objects.equals(regionId, point.regionId) &&
                Objects.equals(type, point.type) &&
                Objects.equals(subway, point.subway) &&
                Objects.equals(phone, point.phone) &&
                Objects.equals(image, point.image) &&
                Objects.equals(workday, point.workday) &&
                Objects.equals(filters, point.filters) &&
                Objects.equals(coordinates, point.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, address, regionId, type, subway, phone, image, workday, filters, coordinates, opinions);
    }
}
