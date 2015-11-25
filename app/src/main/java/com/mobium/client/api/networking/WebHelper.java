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


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;


/**
 *
 *
 * Date: 30.11.12
 * Time: 23:28
 */
public class WebHelper {
    public static String downloadString(String url, OkHttpClient okHttpClient) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept-Encoding", "utf-8")
                .build();
        byte[] data =
        okHttpClient.newCall(request).execute().body().bytes();
        return new String(data, "utf-8");
    }

}
