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

package com.mobium.reference.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.mobium.reference.utils.persistence.ContextPersistence;

import java.util.UUID;

/**
 *
 * Date: 19.09.12
 * Time: 16:12
 */
public class PersistenceState extends ContextPersistence {

    private static final long serialVersionUID = 1L;

    private String installationId;
    private String deviceId;

    public PersistenceState(Context context) {
        super(context);
        tryLoad();
    }

    public String getDeviceId() {
        if (deviceId == null) {
            String res = null;
            try {
                final String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                if (android_id != null)
                    res = android_id;
                else {
                    final String tmDevice = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    if (tmDevice != null)
                        res = tmDevice;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res == null) {
                res = "install:" + getInstallationId();
            }
            deviceId = res;
            trySave();
        }
        return deviceId;
    }

    public String getInstallationId() {
        if (installationId == null) {
            installationId = UUID.randomUUID().toString();
            trySave();
        }

        return installationId;
    }
}
