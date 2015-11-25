package com.mobium.reference.utils.persistence;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 *  on 23.06.15.
 * http://mobiumapps.com/
 */
public class TimePersistenceCache<T extends Serializable> extends ContextPersistence {
    private final TimedCache<T> cache;

    public TimePersistenceCache(Context context, int time) {
        super(context);
        cache = new TimedCache<>(time);
    }

    public void save(String tag, T object) {
        new AsyncTask<Void, Void, Void> () {
            @Override
            protected Void doInBackground(Void... voids) {
                cache.putToCache(tag, object);
                return null;
            }
        }.execute();
    }

    public void clearCache() {
        cache.clear();
    }


    public boolean has(String tag) {
        return cache.has(tag);
    }

    public @Nullable T load(String tag) {
        return cache.fetchFromCache(tag);
    }

}
