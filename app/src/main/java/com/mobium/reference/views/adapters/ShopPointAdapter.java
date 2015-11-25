package com.mobium.reference.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.reference.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public class ShopPointAdapter extends ListClickableAdapter<ShopPointAdapter.Holder,
        Map.Entry<String, List<ShopPoint>>

        > {

    private OnClickListener listener;

    public ShopPointAdapter(Set<Map.Entry<String, List<ShopPoint>>> entries, Context context) {
        super(new ArrayList<>(entries), context);
    }

    @Override
    public int getFirstElemMarginInDp() {
        return 4;
    }

    @Override
    protected void onItemClick(Map.Entry<String, List<ShopPoint>> stringListEntry, int position) {
        if (listener != null)
            listener.onClick(stringListEntry.getKey(), stringListEntry.getValue(), position);
    }

    public ShopPointAdapter(List<Map.Entry<String, List<ShopPoint>>> entries, Context context) {
        super(entries, context);
    }

    @Override
    protected void applyItemToHolder(Holder holder, Map.Entry<String, List<ShopPoint>> stringListEntry, int itemPosition) {
        holder.textView.setText(stringListEntry.getKey());
        holder.deliver.setVisibility(itemPosition == getItemCount() - 1 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_service_single_line, parent, false));
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public static class Holder extends ClickableHolder {
        @Bind(R.id.item_service_single_line_title)
        public TextView textView;
        @Bind(R.id.item_service_single_line_deliver)
        public View deliver;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener {
        void onClick(String name, List<ShopPoint> list, int position);
    }
}
