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

package com.mobium.reference.activity;

import android.annotation.TargetApi;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.mobium.client.LogicUtils;
import com.mobium.client.ShopDataStorage;
import com.mobium.reference.BaseApplication;
import com.mobium.reference.utils.NetworkStateReceiver;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.executing.ExecutingAsyncTask;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.storage.AppIdProvider;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

public abstract class BaseActivity<T extends BaseApplication>
        extends android.support.v7.app.AppCompatActivity
        implements NetworkStateReceiver.NetworkStateReceiverListener
{
    protected T application;

    protected NetworkStateReceiver networkStateReceiver;
    private android.content.IntentFilter intentFilter = new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
    private LogicUtils.OnChangeRegionListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (T) getApplication();
        networkStateReceiver = new NetworkStateReceiver();
        Events.get(this).app().onAppStart(AppIdProvider.getAppId(this).orElse(""));
        listener = Events.get(this).regions()::onRegionSelect;

        checkForUpdates();
        CrashManager.register(this, "a163c933284a192008903630aaa1d17e");
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkStateReceiver, intentFilter);
        Events.get(this).lifeCycle().onStart(this);
        ShopDataStorage.getInstance().addRegionListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkStateReceiver);
        Events.get(this).lifeCycle().onStop(this);
        ShopDataStorage.getInstance().removeRegionListner(listener);
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void executeAsyncHidden(final ExecutingAsyncTask runnable, final Runnable onError) {
        new VoidVoidVoidAsyncTask(runnable, onError).execute();
    }

    private void checkForCrashes() {
        if (Config.get().getApplicationData().getServiceHockeyappKey() != null)
            CrashManager.register(this, Config.get().getApplicationData().getServiceHockeyappKey());
    }

    private void checkForUpdates() {
        if (Config.get().getApplicationData().getServiceHockeyappKey() != null)
            UpdateManager.register(this, Config.get().getApplicationData().getServiceHockeyappKey());
    }


    private static class VoidVoidVoidAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ExecutingAsyncTask runnable;
        private final Runnable onError;
        ExecutingException exception;

        public VoidVoidVoidAsyncTask(ExecutingAsyncTask runnable, Runnable onError) {
            this.runnable = runnable;
            this.onError = onError;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runnable.beforeExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                runnable.executeAsync();
            } catch (ExecutingException e) {
                e.printStackTrace();
                runnable.onError(e);
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (exception == null) {
                runnable.afterExecute();
            } else {
                onError.run();
            }
        }

    }

}