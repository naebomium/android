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

import com.annimon.stream.function.FunctionalInterface;
import com.mobium.reference.utils.executing.ExecutingException;

import org.json.JSONObject;

/**
 * Date: 19.09.12
 * Time: 16:52
 */
@FunctionalInterface
public interface ApiInterface {
    JSONObject DoApiRequest(String method, JSONObject args) throws NetworkingException, ExecutingException;
    String EXTRA_REGION_ID = "extra_regionId";
}