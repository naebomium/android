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

package com.mobium.client.api.networking;

import android.util.Log;
import com.mobium.reference.utils.executing.ExecutingException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Date: 19.09.12
 * Time: 16:36
 */
public class ApiExecutor {
    private static final int ATTEMPTS_COUNT = 3;

    private ApiInterface apiInterface;

    public ApiExecutor(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public JSONObject DoApiRequest(String methodName, String... args) throws NetworkingException, ExecutingException {
        JSONObject jArgs = new JSONObject();
        for (int i = 0; i < (args.length / 2); i++) {
            try {
                jArgs.put(args[i * 2], args[i * 2 + 1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return DoApiRequest(methodName, jArgs);
    }

    public JSONObject DoApiRequest(String methodName, JSONObject args) throws NetworkingException, ExecutingException {
        return DoApiRequestInternal(methodName, args, 0);
    }

    private JSONObject DoApiRequestInternal(String methodName, JSONObject args, int attempts) throws NetworkingException, ExecutingException {
        try {
            long start = System.currentTimeMillis();
            JSONObject res = apiInterface.DoApiRequest(methodName, args);
            Log.d("ApiExecutor", "RequestDuration: " + methodName + ", " + (System.currentTimeMillis() - start) + " ms");
            return res;
        } catch (NetworkingException e) {
            if (attempts > ATTEMPTS_COUNT) {
                throw e;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                return DoApiRequestInternal(methodName, args, attempts + 1);
            }
        }
    }
}