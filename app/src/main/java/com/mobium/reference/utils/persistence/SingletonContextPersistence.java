package com.mobium.reference.utils.persistence;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *  on 23.06.15.
 * http://mobiumapps.com/
 */
public class SingletonContextPersistence extends PersistenceObject {
    private transient Context context;

    protected SingletonContextPersistence(Context context) {
        this.context = context;
    }

    protected SingletonContextPersistence() {
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected OutputStream openWrite(String path) throws FileNotFoundException {
        return context.openFileOutput(path, Context.MODE_PRIVATE);
    }

    @Override
    protected InputStream openRead(String path, boolean error) throws IOException {
        if (error) {
            return context.getAssets().open(path);
        } else {
            return context.openFileInput(path);
        }
    }
}
