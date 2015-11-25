package com.mobium.new_api.models.catalog_;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.new_api.cache.BaseCachedItem;
import com.mobium.new_api.models.Image;
import com.mobium.new_api.models.ShopItemExtra;
import com.mobium.reference.utils.Functional;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

/**
 *  on 14.07.15.
 * http://mobiumapps.com/
 */

public class ShopItem_ extends BaseCachedItem {
    public final @NonNull String id;
    public final @NonNull String title;
    public final @NonNull Integer cost;


    private @Nullable String description;
    private @Nullable Image icon;
    private @Nullable Image images;
    private @Nullable Object avabiliry;

    private @Nullable ShopItemExtra extra;

    private @Nullable ArrayList<Media_> media;
    private @Nullable ArrayList<Marketing_> marketing;
    private @Nullable ArrayList<Characteristics_> characteristics;


    private @Nullable Integer sortingOrder;
    private @Nullable Hashtable<String, String> sortings;
    private @Nullable ArrayList<Filter_> filterings;



    public ShopItem_(@NonNull String id, @NonNull String title, @NonNull Integer cost) {
        this.id = id;
        this.title = title;
        this.cost = cost;
    }


    public Optional<ArrayList<Media_>> getMedia() {
        return Functional.notNullAndNotEmpty(media);
    }

    public Optional<Stream<Media_>> getStreamOfMedia() {
        return getMedia().map(Stream::of);
    }

    public Optional<ArrayList<Marketing_>> getMarketing() {
        return Functional.notNullAndNotEmpty(marketing);
    }

    public Optional<Stream<Marketing_>> getStreamOftMarketing() {
        return getMarketing().map(Stream::of);
    }

    public rx.Observable<Characteristics_> getCharacteristics() {
        return rx.Observable.from(characteristics == null ? Collections.<Characteristics_>emptyList() : characteristics);
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public void setIcon(@Nullable Image icon) {
        this.icon = icon;
    }

    public void setAvabiliry(@Nullable Object avabiliry) {
        this.avabiliry = avabiliry;
    }

    public void setExtra(@Nullable ShopItemExtra extra) {
        this.extra = extra;
    }

    public void setMedia(@Nullable ArrayList<Media_> media) {
        this.media = media;
    }

    public void setMarketing(@Nullable ArrayList<Marketing_> marketing) {
        this.marketing = marketing;
    }

    public void setCharacteristics(@Nullable ArrayList<Characteristics_> characteristics) {
        this.characteristics = characteristics;
    }

    public void setSortingOrder(@Nullable Integer sortingOrder) {
        this.sortingOrder = sortingOrder;
    }

    public void setSortings(@Nullable Hashtable<String, String> sortings) {
        this.sortings = sortings;
    }

    public void setFilterings(@Nullable ArrayList<Filter_> filterings) {
        this.filterings = filterings;
    }

    @Override
    public int getTimeOutInMinutes() {
        return 60;
    }
}
