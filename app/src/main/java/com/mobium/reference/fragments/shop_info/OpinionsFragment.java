package com.mobium.reference.fragments.shop_info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mobium.client.models.OpinionDiscussed;
import com.mobium.client.models.Opinions;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicLoadableFragment;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.views.adapters.ReviewAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 *  on 21.07.15.
 * http://mobiumapps.com/
 */
public class OpinionsFragment extends BasicLoadableFragment {
    private OpinionDiscussed discussed;
    @Bind(R.id.dontsave)    protected TextView hint;
    @Bind(R.id.progress)    protected View progressBar;
    @Bind(R.id.list)        protected ListView list;


    @Override
    protected boolean needLoading() {
        if (!discussed.getOpinions().isPresent())
            return true;
        Opinions opinions = discussed.getOpinions().get();
        return opinions.size > opinions.opinions.size();
    }

    @Override
    protected void loadInBackground() throws ExecutingException {
        Opinions opinions;
        if (discussed.getOpinions().isPresent()) {
            opinions = discussed.getOpinions().get();
            opinions = getApplication().getExecutor().loadOpinions(
                    discussed.getId(),
                    discussed.getOpinionTag(),
                    opinions.size,
                    0
            );
        } else {
            opinions = getApplication().getExecutor().loadOpinions(
                    discussed.getId(),
                    discussed.getOpinionTag(),
                    100,
                    0
            );
        }
        discussed.setOpinions(opinions);
    }

    public static OpinionsFragment getInstance(OpinionDiscussed discussed) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(OpinionDiscussed.class.getSimpleName(), discussed);
        OpinionsFragment result = new OpinionsFragment();
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            savedInstanceState = getArguments();
        discussed = (OpinionDiscussed)
                savedInstanceState.getSerializable(
                        OpinionDiscussed.class.getSimpleName()
                );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.loadable_list_fragment, container, false);
        result.setBackgroundColor(getActivity().getResources().getColor(R.color.white_background));
        ButterKnife.bind(this, result);
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        list.setAdapter(null);
        ButterKnife.unbind(this);
    }

    @Override protected View getProgressView() {
        return progressBar;
    }

    @Override protected void afterLoaded() {
        super.afterLoaded();
        fillList();
    }


    @Override protected void doesntNeedLoading() {
        super.doesntNeedLoading();
        fillList();
    }

    private void fillList() {
        if (discussed.getOpinions().get().size > 0) {
            list.setAdapter(
                    new ReviewAdapter(
                        getDashboardActivity(),
                        discussed.getOpinions().get().opinions
                    )
            );
            list.setVisibility(View.VISIBLE);
            hint.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.GONE);
            hint.setVisibility(View.VISIBLE);
            hint.setText("Отзывов не найдено");
        }
    }



}
