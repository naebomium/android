package com.mobium.client.models.modifications;

import android.util.Log;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  on 17.09.2015.
 */
public class Modification implements Serializable {

    private static final String TAG = "Modification";

    public final String id;
    public final String title;
    public final OfferModification[] offerModifications;
    public final String defaultId;

    public OfferModification getSelectedModification() {
        return selectedModification;
    }

    public void setSelectedModification(OfferModification selectedModification) {
        this.selectedModification = selectedModification;
    }

    private OfferModification selectedModification;

    public Modification(String id, String title, OfferModification[] offerModifications, String defaultId) {
        this.id = id;
        this.title = title;
        this.offerModifications = offerModifications;
        this.defaultId = defaultId;
    }

    public static Modification deserialize (JSONObject modification) throws JSONException{
        String id  = modification.getString("id");
        String title = modification.getString("title");
        String defaultId = modification.getString("default");
        OfferModification[] offerModification = OfferModification.deserialize(modification.getJSONArray("values"));
        return new Modification(id, title, offerModification, defaultId);
    }

    public static Modification[] deserialize (JSONArray modifications) {
        if (modifications == null || modifications.length() == 0)
            return null;
        int size = modifications.length();
        List<Modification> result = new ArrayList<>(modifications.length());

        for (int i = 0; i < size; i++) {
            try {
                result.add(deserialize(modifications.getJSONObject(i)));
            } catch (JSONException e) {
                Log.w(TAG, "error deserialize modification:" + modifications.optJSONObject(i));
                e.printStackTrace();
            }
        }
        return result.size() == 0 ? null : result.toArray(new Modification[result.size()]);
    }


    private String getTitleByOfferId(final String offerId) {
        if (offerModifications != null) {
            Optional<OfferModification> mod =
                    Stream.of(offerModifications)
                            .filter(r -> r.id.equals(offerId))
                            .findFirst();
            if(mod.isPresent())
                return mod.get().title;
        }
        return "";
    }

    // если есть выбранная модификация, показываем её
    // иначе если есть значение по-умолчанию, показываем его
    // иначе показываем первую модификацию

    public String getDefaultTitle () {
        if(selectedModification != null) {
            return selectedModification.title;
        }
        if (defaultId != null) {
            return getTitleByOfferId(defaultId);
        } else {
            Optional<OfferModification> mod = Stream.of(offerModifications).findFirst();
            if(mod.isPresent())
                return mod.get().title;
        }
        return "";
    }
}
