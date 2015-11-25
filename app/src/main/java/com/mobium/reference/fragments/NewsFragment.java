package com.mobium.reference.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobium.client.models.Action;
import com.mobium.client.models.NewsRecord;
import com.mobium.reference.R;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.utils.text.DurationFormatters;
import com.mobium.reference.views.WebImageView;


/**
 *
 *
 * Date: 19.12.12
 * Time: 14:20
 */
public class NewsFragment extends BasicLoadableFragment {
    private NewsRecord[] records;
    private ListView newsListView;
    private String title;
    private View progress;

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected String getTitle() {
        return title != null ? title : getString(R.string.articles_title);
    }


    @Override
    protected View getContentView() {
        return newsListView;
    }

    @Override
    protected View getProgressView() {
        return progress;
    }

    @Override
    protected boolean needLoading() {
        return records == null;
    }

    @Override
    protected void loadInBackground() throws ExecutingException {
        records = getApplication().getExecutor().getNews();
    }


    @Override
    protected void afterPrepared() {
        if (newsListView.getAdapter() == null)
            newsListView.setAdapter(new BaseAdapter() {
            private final LayoutInflater inflater = LayoutInflater.from(getDashboardActivity());


            View.OnClickListener listener = view -> {
                Action action = (Action) view.getTag();
                FragmentActionHandler.doAction(getActivity(), action);
                Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.content_page.name());
            };

            @Override
            public int getCount() {
                return records.length;
            }

            @Override
            public NewsRecord getItem(int i) {
                return records[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                WebImageView imageView;
                if (view == null)
                    view = inflater.inflate(R.layout.item_news_card, viewGroup, false);

                imageView = (WebImageView) view.findViewById(R.id.item_news_card_icon);


                NewsRecord record = getItem(i);
                View clickView = view.findViewById(R.id.item_news_card_clickArea);
                clickView.setOnClickListener(listener);
                clickView.setTag(record.getRecordAction());


                ((TextView) view.findViewById(R.id.item_news_card_title)).setText(Html.fromHtml(record.getTitle()));
                ((TextView) view.findViewById(R.id.item_news_card_date)).setText(DurationFormatters.formatHumanReadableDate(
                        record.getDate() * 1000L, "ru"));


                String url = record.getGraphics().getUrl("hd");


                if (url == null || "null".equals(url)) {
                    imageView.setVisibility(View.INVISIBLE);
                } else {
                    imageView.setUrl(record.getGraphics().getUrl());
                    imageView.setVisibility(View.VISIBLE);
                }

                return view;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_news, container, false);
        newsListView = (ListView) res.findViewById(R.id.newsItems);
        progress = res.findViewById(R.id.progress);
        newsListView.setDividerHeight(1);
        return res;
    }


    public static NewsFragment getInstance(final String title) {
        return new NewsFragment() {
            {
                setTitle(title);
            }
        };
    }

}
