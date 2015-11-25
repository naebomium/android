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

package com.mobium.reference;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mobium.reference.modules.NetworkModule;
import com.mobium.reference.utils.PersistenceState;
import com.mobium.reference.utils.RateDialogUtils;
import com.mobium.config.common.Config;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 *
 * Date: 19.09.12
 * Time: 16:12
 */


public abstract class BaseApplication extends Application {

    private PersistenceState persistenceState;
    private ObjectGraph graph;
    private boolean debug;

    @Override
    public void onCreate() {
        super.onCreate();

        Config.init(this);

        graph = ObjectGraph.create(getModules().toArray());

        debug = (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

        persistenceState = new PersistenceState(this);

        RateDialogUtils.updateAppStartCount(this);
    }

    public String getDeviceId() {
        return persistenceState.getDeviceId();
    }

    public String getInstallationId() {
        return persistenceState.getInstallationId();
    }

    public String getApplicationVersionName() {
        String name = getPackageName();
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(name, 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public boolean isDebug() {
        return debug;
    }


    protected List<Object> getModules() {
        return Arrays.asList(
                new NetworkModule()
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}
