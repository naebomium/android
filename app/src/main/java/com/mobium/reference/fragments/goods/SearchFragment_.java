package com.mobium.reference.fragments.goods;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.mobium.client.models.SearchResult;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.AdvancedSearchView;
import com.mobium.reference.views.adapters.CatalogRecyclerAdapter;

import java.util.Arrays;

/**
 *  on 09.07.15.
 * http://mobiumapps.com/
 */
public class SearchFragment_ extends BasicContentFragment {

    private Optional<String> categoryId;
    private String searchQuery;

    private Optional<SearchResult> result;
    private CatalogRecyclerAdapter adapter;

    private RecyclerView searchResultList;
    private View progressView;
    private TextView textHint;

    private AdvancedSearchView searchView;

    public static final String QUERY_STRING = "com.mobium.reference.fragments.goods.SearchFragment_::searchQuery";
    public static final String CATEGORY = "com.mobium.reference.fragments.goods.SearchFragment_::categoryId";
    public static final String RESULT = "com.mobium.reference.fragments.goods.SearchFragment_::result";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_, container, false);

        progressView = root.findViewById(R.id.fragment_search_progress_view);
        searchResultList = (RecyclerView) root.findViewById(R.id.fragment_search_list_view);
        searchResultList.setLayoutManager(new LinearLayoutManager(getActivity()));
        textHint = (TextView) root.findViewById(R.id.fragment_search_hint);
        searchView = (AdvancedSearchView) root.findViewById(R.id.fragment_search_searchView);
        result.ifPresent(this::fillResult);

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        searchView.getViewSearchField().setText(searchQuery);
        if (!result.isPresent())
            loadResult();
        else
            fillResult(result.get());
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.search.name());
    }


    public void setSearchQuery(@NonNull String searchQuery, @Nullable String catrgoryId) {
        if (!searchQuery.equals(this.searchQuery)) {
            this.searchQuery = searchQuery;
            this.categoryId = Optional.ofNullable(catrgoryId);
            loadResult();
        } else {
            this.searchQuery = searchQuery;
            this.categoryId = Optional.ofNullable(catrgoryId);
        }
        searchResultList.scrollToPosition(0);
    }


    private void fillResult(SearchResult r) {
        result = Optional.of(r);
        if (isFragmentUIActive()) {
            if (r.isEmpty()) {
                searchResultList.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                textHint.setVisibility(View.VISIBLE);
                textHint.setText("Поиск не дал результатов");
            } else {
                searchResultList.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);
                textHint.setVisibility(View.GONE);
                if (adapter == null)
                    adapter = new CatalogRecyclerAdapter(getDashboardActivity());

                adapter.setCategories(Arrays.asList(r.getCategories()));
                adapter.setShopItems(Arrays.asList(r.getItems()));

                if (searchResultList.getAdapter() == null)
                    searchResultList.setAdapter(adapter);
            }
        }
    }


    private boolean loading;
    private String loadingQuery;

    private void loadResult() {
        if (loading && searchQuery.equals(loadingQuery))
            return;

        progressView.setVisibility(View.VISIBLE);
        loading = true;
        loadingQuery = searchQuery;
        new AsyncTask<Void, Void, SearchResult> () {

            private void onError(ExecutingException ex) {
                loading = false;
                loadingQuery = null;

                new AlertDialog.Builder(getActivity())
                        .setTitle("Ошбика во время поиска")
                        .setMessage("Повторить попытку?")
                        .setPositiveButton("Повторить", (dialogInterface, i) -> {
                            loadResult();
                        }).setNegativeButton("Отмена", (dialogInterface1, i1) -> {
                            if (result.isPresent()) {
                                fillResult(result.get());

                            } else {
                                getFragmentManager().popBackStack();
                            }
                        }).show();
            }

            @Override
            protected SearchResult doInBackground(Void... voids) {
                try {
                    if (categoryId.isPresent())
                        return getReferenceApplication().getExecutor().search(searchQuery, categoryId.get());
                    else
                        return getReferenceApplication().getExecutor().search(searchQuery);
                } catch (ExecutingException e) {
                    onError(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(SearchResult result) {
                loading = false;
                if (searchQuery.equals(loadingQuery) && result != null)
                    fillResult(result);
            }
        }.execute();
    }




    public static SearchFragment_ getInstance(@NonNull String searchQuery, @Nullable String category) {
        SearchFragment_ result = new SearchFragment_();
        Bundle bundle = new Bundle();
        bundle.putString(QUERY_STRING, searchQuery);
        if (category != null)
            bundle.putString(CATEGORY, category);
        result.setArguments(bundle);
        return result;
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        searchQuery = savedInstanceState.getString(QUERY_STRING, "");
        categoryId = Optional.ofNullable(savedInstanceState.getString(CATEGORY));
        result =  Optional.ofNullable((SearchResult) savedInstanceState.getSerializable(RESULT));
    }

    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        categoryId.ifPresent(cat -> outState.putString(CATEGORY, cat));
        result.ifPresent(res -> outState.putSerializable(RESULT, res));
        outState.putString(QUERY_STRING, searchQuery);
    }


    @Override
    protected String getTitle() {
        return "Поиск";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchResultList.clearOnScrollListeners();
        searchResultList.setAdapter(null);
        if (adapter != null) {
            adapter.setShopItems(null);
            adapter.setCategories(null);
            adapter = null;
        }
        searchResultList.destroyDrawingCache();
    }
}
