package com.mobium.reference.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.mobium.config.BlockViewFactory;
import com.mobium.config.ScreenParser;
import com.mobium.config.block_models.BlockModel;
import com.mobium.config.block_views.BaseView;
import com.mobium.config.common.CanSearch;
import com.mobium.config.common.Config;
import com.mobium.config.common.DataExchangeException;
import com.mobium.config.common.HaveActions;
import com.mobium.config.common.LoadableView;
import com.mobium.config.common.Provider;
import com.mobium.config.common.ProviderFactory;
import com.mobium.config.common.UpdatableLoadableView;
import com.mobium.config.common.UpdatesProvider;
import com.mobium.config.converters.BlockConverter;
import com.mobium.config.screen_models.Screen;
import com.mobium.new_api.Api;
import com.mobium.reference.config.ProviderFactoryIml;
import com.mobium.reference.utils.Actions;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.executing.ExecutingAsyncTask;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.views.StickyScrollView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *  on 30.10.15.
 */
public class ConfigScreen extends BasicContentFragment {
    private HashMap<LoadableView, Provider> loadableViews = new HashMap<>();
    private HashMap<UpdatableLoadableView, UpdatesProvider> updatesView = new HashMap<>();
    private ProviderFactory factory = new ProviderFactoryIml();
    private Screen screen;



    private Screen getScreen() throws IOException {
        if (screen != null)
            return screen;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(BlockModel.class, new BlockConverter())
                .create();
        ScreenParser parser = new ScreenParser();

        screen = parser.parseScreen("start_page.json", getApplication(), gson);
        // TODO: 16.11.15 do parser throw design
        return screen;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StickyScrollView scrollView = new StickyScrollView(getContext());
        LinearLayout layout = new LinearLayout(container.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        try {
                Screen screen = getScreen();
            for (BlockModel block : screen.getBlockModels()) {
                BaseView view = BlockViewFactory.build(block);
                configureView(view);
                view.getView(container.getContext(), layout, true);
            }

        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        scrollView.addView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return scrollView;
    }

    /**
     * Set dependency to view
     * @param view
     */
    private void configureView(BaseView view) {
        if (view instanceof CanSearch) {
            CanSearch canSearch = (CanSearch) view;
            canSearch.setOnScannerClickListener(v -> Actions.doOpenScanner(getActivity()));
            canSearch.setOnVoiceSearchListneter(v -> Actions.doOpenVoiceSearch(getActivity()));
            canSearch.setSearchHandler(qe -> Actions.doSearch(getActivity(), qe, null));
        }

        if (view instanceof HaveActions) {
            ((HaveActions) view).setActionHandler(action -> FragmentActionHandler.doAction(getActivity(), action));
        }

        if (view instanceof UpdatableLoadableView) {
            UpdatableLoadableView updatableLoadableView = (UpdatableLoadableView) view;
            updatableLoadableView.setUpdater((limit, offset) -> onRequestData(updatableLoadableView, limit, offset));
            updatesView.put(updatableLoadableView, factory.get(getActivity(), updatableLoadableView));
        } else if (view instanceof LoadableView) {
            LoadableView loadableView = (LoadableView) view;
            loadableViews.put(loadableView, factory.get(getActivity(), loadableView));
        }
    }

    /**
     * some with send request of data
     * @param updatableLoadableView View, which need new data
     * @param limit limit of data
     * @param offset offset of data from first element
     * @param <T> Type of Data
     */
    private <T> void onRequestData(UpdatableLoadableView<T> updatableLoadableView, int limit, int offset) {
        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {
                try {
                    return (T) updatesView.get(updatableLoadableView).get(limit, offset);
                } catch (DataExchangeException e) {
                    onError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(T t) {
                super.onPostExecute(t);
                if (FragmentUtils.isUiFragmentActive(ConfigScreen.this)) {
                    if (t != null)
                        updatableLoadableView.setData(t, offset);
                    else
                        updatableLoadableView.onEmptyData(offset);
                }
            }

            public void onError(DataExchangeException e) {
                if (FragmentUtils.isUiFragmentActive(ConfigScreen.this))
                    getActivity().runOnUiThread(() -> {
                        if (e.canRepeat)
                            Dialogs.showNetworkErrorDialog(getActivity(),
                                    e.getMessage(),
                                    () -> onRequestData(updatableLoadableView, limit, offset),
                                    getFragmentManager()::popBackStack
                            );
                        else
                            Dialogs.showExitScreenDialog(getActivity(),
                                    getFragmentManager()::popBackStack,
                                    e.getMessage()
                            );
                    });
            }
        }.execute();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loadableViews.clear();
        updatesView.clear();
    }


    @Override
    public void onStart() {
        super.onStart();
        executeAsync(new ExecutingAsyncTask() {
            @Override
            public void executeAsync() throws ExecutingException {
                for (Map.Entry<LoadableView, Provider> viewProviderEntry : loadableViews.entrySet()) {
                    if (viewProviderEntry.getKey().hasData())
                        continue;
                    try {
                        Object o = viewProviderEntry.getValue().get();
                        getActivity().runOnUiThread(() -> viewProviderEntry.getKey().setData(o));
                    } catch (DataExchangeException e) {
                        throw new ExecutingException(e.getMessage(), e.canRepeat);
                    }
                }
            }
        });
    }

}
