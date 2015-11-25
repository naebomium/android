package com.mobium.reference.model;

import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.parceler.Parcel;
import org.parceler.ParcelConverter;
import org.parceler.ParcelPropertyConverter;
import org.parceler.Parcels;
import org.parceler.converter.ArrayListParcelConverter;

import java.util.ArrayList;
import java.util.List;

/**
 *  on 07.08.15.
 * http://mobiumapps.com/
 */
@Parcel
public class ContentPageSource {

    @ParcelPropertyConverter(TextParcelableConverter.class)
    public List<Text> texts;
    @ParcelPropertyConverter(ImageParcelableConverter.class)
    public List<Image> images;
    @ParcelPropertyConverter(OfferParcelableConverter.class)
    public List<Offer> offers;
    String title;

    public ContentPageSource() {
    }

    public static Optional<ContentPageSource> tryDeseriale(Gson gson, String source) {
        ContentPageSource res = null;
        try {
            res = gson.fromJson(source, ContentPageSource.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(res);
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public static class TextParcelableConverter implements ParcelConverter<List<Text>> {
        @Override
        public void toParcel(List<Text> input, android.os.Parcel parcel) {
            if (input == null) {
                parcel.writeInt(-1);
            } else {
                parcel.writeInt(input.size());
                for (Text item : input) {
                    parcel.writeParcelable(Parcels.wrap(item), 0);
                }
            }
        }

        @Override
        public List<Text> fromParcel(android.os.Parcel parcel) {
            int size = parcel.readInt();
            if (size < 0) return null;
            List<Text> items = new ArrayList<Text>();
            for (int i = 0; i < size; ++i) {
                items.add(Parcels.unwrap(parcel.readParcelable(Text.class.getClassLoader())));
            }
            return items;
        }
    }

    @Parcel
    public static class Text {

        public Integer order;
        public String text;

        public Text() {
        }

        public Text(Integer order, String text) {
            this.order = order;
            this.text = text;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class ImageParcelableConverter implements ParcelConverter<List<Image>> {
        @Override
        public void toParcel(List<Image> input, android.os.Parcel parcel) {
            if (input == null) {
                parcel.writeInt(-1);
            } else {
                parcel.writeInt(input.size());
                for (Image item : input) {
                    parcel.writeParcelable(Parcels.wrap(item), 0);
                }
            }
        }

        @Override
        public List<Image> fromParcel(android.os.Parcel parcel) {
            int size = parcel.readInt();
            if (size < 0) return null;
            List<Image> items = new ArrayList<Image>();
            for (int i = 0; i < size; ++i) {
                items.add(Parcels.unwrap(parcel.readParcelable(Image.class.getClassLoader())));
            }
            return items;
        }
    }

    @Parcel
    public static class Image {

        public Integer order;
        public String url;

        public Image() {
        }

        public Image(Integer order, String url) {
            this.order = order;
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }
    }

    public static class OfferParcelableConverter extends ArrayListParcelConverter<Offer> {

        @Override
        public void itemToParcel(Offer offer, android.os.Parcel parcel) {
            parcel.writeParcelable(Parcels.wrap(offer), 0);
        }

        @Override
        public Offer itemFromParcel(android.os.Parcel parcel) {
            return Parcels.unwrap(parcel.readParcelable(Offer.class.getClassLoader()));
        }
    }

    @Parcel
    public static class Offer {
        public Integer order;
        public List<String> relatedOffers;
        public List<String> relatedCategories;

        public Offer() {
        }

    }
}