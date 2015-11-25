package com.mobium.client.models;

import com.annimon.stream.Optional;
import com.mobium.reference.utils.Functional;
import com.mobium.new_api.models.ShopPoint;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 *  on 29.04.15.
 * http://mobiumapps.com/
 *
 *  отзыв
 */
public class Opinion implements Serializable {
    public static final String OWN_SOURSE = "own";
    public static final String MARKET_SOURSE = "yandex";

    public static String getObjectType(Object object) {
        if (object instanceof ShopItem)
            return "offer";
        else if (object instanceof ShopPoint)
            return "point";
        else if (object instanceof NewsRecord)
            return "article";
        else return "general";
    }

    //necessary fields
    private final long unixTime;
    public final String author;
    public final String text;

    //unnecessary fields
    private final byte rating;
    private final String source;
    private final String[] pros;
    private final String[] cons;

    public Opinion(String author, long unixTime, String text, String[] pros, String[] cons, int rating, String sourse) {
        this.unixTime = unixTime;
        this.author = author;
        this.text = text;
        this.pros = pros;
        this.cons = cons;
        this.rating = (byte) rating;
        this.source = sourse;
    }



    public static Functional.JSONFunction<JSONObject, Opinion> deserialize =
        (JSONObject t) -> new Opinion(
                t.getString("author"), //necessary
                t.getLong("date"),     //necessary
                t.getString("text"),   //necessary
                t.has("pros") ? t.getJSONArray("pros").toString().split(",") : null,
                t.has("cons") ? t.getJSONArray("cons").toString().split(",") : null,
                t.optInt("rating", Constants.nullInt),
                t.optString("source", Constants.nullStr)
        );



    public Date getData(){
        return new Date(unixTime * 1000L);
    }

    public Optional<Integer> getRating() {
        return rating == Constants.nullInt ? Optional.empty() : Optional.of((int) rating);
    }

    public Optional<String> getSourse() {
        return com.annimon.stream.Objects.equals(source, Constants.nullStr) ? Optional.empty() : Optional.of(source);
    }

    public Optional<String[]> getPros() {
        return pros == null ? Optional.empty() : Optional.of(pros);
    }

    public Optional<String[]> getCons () {
        return cons == null ? Optional.empty() : Optional.of(cons);
    }



}
