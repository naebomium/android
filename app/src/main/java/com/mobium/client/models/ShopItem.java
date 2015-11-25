package com.mobium.client.models;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.client.models.modifications.Modification;
import com.mobium.client.models.resources.Graphics;
import com.mobium.userProfile.ResponseParams.OrderItem;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mobium.client.models.Constants.nullInt;

/**
 *  IDEA.
 *
 * Date: 18.09.11
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class ShopItem implements Serializable, OpinionDiscussed {
    //necessary fields
    public final String id;
    public final String title;


    //unnecessary fields
    public DetailsInfo detailsInfo;
    public final Price cost;
    private final Graphics icon;

    public final Map<String, Serializable> sorting;
    public final Map<String, Serializable> filtering;
    private transient Opinions opinions;


    private List<Media> medias;
    private Graphics[] images;
    private Marketing[] marketing;
    private Modification[] modifications;

    private Map<String, String> selectedModifications = new HashMap<>();

    private CharacteristicsGroup[] characteristics;

    private volatile Integer rating = null;
    private String description;

    private OrderItem resource; // non null if item loaded from userProfile

    public ShopItem(String id, String title, Graphics icon, Price cost, Map<String, Serializable> sorting,
                    Map<String, Serializable> filtering) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.cost = cost;
        this.sorting = sorting;
        this.filtering = filtering;
    }

    public Map<String, String> getModificationsMap() {
        if(modifications != null)
        Stream.of(modifications).forEach(mod -> {
            if(mod != null) {
                if(mod.getSelectedModification() != null)
                    selectedModifications.put(mod.id, mod.getSelectedModification().id);
            }
        });
        return selectedModifications;
    }

    /**
     * apply marketing modifications on ShopItems
     * LowerPrice makes price lower
     */
    private boolean marketingApplied = false;
    public  void applyMarketing() {
        if (marketing == null || marketingApplied  )
            return;

        marketingApplied = true;

        for (Marketing m : this.marketing) {
            switch (m.type) {
                case LOWER_PRICE:
                    Marketing.LowerPrice lowerPrice = (Marketing.LowerPrice) m;
                    int cost = this.cost.getCurrentConst();
                    if (lowerPrice.lowerTo != nullInt) {
                        this.cost.change(lowerPrice.lowerTo, cost);
                    } else  if (lowerPrice.lowerBy != nullInt) {
                        this.cost.change(cost - cost * lowerPrice.lowerBy / 100, cost);
                    }
                    break;
            }

        }
    }

    public boolean isFullInfo() {
        return detailsInfo != null &&
                detailsInfo.hasOtherItems == (detailsInfo.otherItems != null) &&
                detailsInfo.hasRelatedItems == (detailsInfo.relatedItems != null);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isSale() {
        return cost.getOldCost() != Price.NONE_COST
                && cost.getOldCost() != cost.getCurrentConst() ;
    }

    @Override
    public String getOpinionTag() {
        return Opinion.getObjectType(this);
    }

    public Optional<String> getRealId() {
        return Optional.ofNullable(detailsInfo.realId);
    }

    public static class DetailsInfo implements Serializable {
        public transient ShopItem[] otherItems;
        public transient ShopItem[] relatedItems;

        public boolean hasOtherItems;
        public boolean hasRelatedItems;

        public String categoryId;
        public String categoryTitle;

        public String shortDescription;
        public String description2;
        public String vendor;
        public String typePrefix;
        public String model;
        public String realId;
        public String warranty;
    }


    public static class Media implements Serializable{
        public enum Type {
            video, audio
        }
        public final Type type;
        public final String res;

        public Media(Type type, String res) {
            this.type = type;
            this.res = res;
        }

        public Media(String type, String res) {
            this.type = Type.valueOf(type);
            this.res = res;
        }
    }


    public Optional<List<String>> getImages() {
        if (images != null) {
            return Optional.of(Stream.of(images).map(i -> i.getUrl("hd")).collect(Collectors.toList()));
        } else if (icon != null) {
            return Optional.of(Collections.singletonList(icon.getUrl("hd")));
        } else
            return Optional.empty();
    }



    public void setImages(List<Graphics> images) {
        setImages(images.toArray(new Graphics[images.size()]));
    }

    public synchronized void setRating(int r) {
        this.rating = r;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setImages(Graphics... images) {
        this.images = images;
    }

    public void setCharacteristics(CharacteristicsGroup[] characteristics) {
        this.characteristics = characteristics;
    }

    public Optional<CharacteristicsGroup[]> getCharacteristics() {
        return Optional.ofNullable(characteristics);
    }

    public void setMedia(List<Media> media) {
        this.medias = media;
    }

    public Optional<Marketing[]> getMarketing() {
        if (marketing == null || marketing.length == 0)
            return Optional.empty();
        return Optional.of(marketing);
    }

    public void setMarketing(Marketing[] marketing) {
        this.marketing = marketing;
    }

    public void setModifications(Modification[] modifications) {
        this.modifications = modifications;
    }

    public Modification[] getModifications() {
        return modifications;
    }

    public Optional<List<Media>> getMedia() {
        return  Optional.ofNullable(medias);
    }


    public boolean hasOtherItems() {
        return detailsInfo.hasOtherItems;
    }

    public boolean hasRelatedItems() {
        return detailsInfo.hasRelatedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ShopItem) {
            ShopItem item = (ShopItem) o;
            return item.id.equals(this.id);
        }
        return super.equals(o);
    }


    public boolean hasFlag(String id) {
        if (filtering.containsKey(id)) {
            Object o = filtering.get(id);
            if (o instanceof Boolean) {
                return (Boolean) o;
            }
        }

        return false;
    }

    public void setOpinions(Opinions opinions) {
        this.opinions = opinions;
    }

    public Optional<Opinions> getOpinions() {
        return Optional.ofNullable(opinions);
    }

    @Override
    public String getId() {
        return id;
    }

    public synchronized Optional<Integer> getRating() {
        return Optional.ofNullable(rating);
    }

    public Optional<Graphics> getIcon() {
        return Optional.ofNullable(icon);
    }

    public Optional<String> categoryId() {
        return Optional.ofNullable(detailsInfo).map(value -> value.categoryId);
    }

    public OrderItem getProfileResource() {
        return resource;
    }

    public ShopItem setProfileItemResource(OrderItem resource) {
        this.resource = resource;
        return this;
    }
}
