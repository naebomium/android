/*
 * Copyright (c) 2013 Extradea LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobium.reference.utils.persistence;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *   (aka Ex3NDR) Korshakov
 * Date: 23.08.11
 * Time: 12:06
 */
public class ContextPersistence extends PersistenceObject {

    protected transient Context context;

    public ContextPersistence(Context context) {
        this.context = context;
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

    @Override
    public boolean trySave() {
        if (context == null)
            throw new IllegalStateException("Context is null, did you forget call init?");
        return super.trySave();
    }

    @Override
    public boolean tryLoad() {
        if (context == null)
            throw new IllegalStateException("Context is null, did you forget call init?");
        return super.tryLoad();
    }

    public void init(Context context) {
        this.context = context;
    }
}
