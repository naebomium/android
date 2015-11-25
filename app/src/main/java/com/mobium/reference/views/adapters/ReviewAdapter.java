package com.mobium.reference.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mobium.client.models.Opinion;
import com.mobium.reference.R;
import com.mobium.reference.utils.ImageUtils;

import java.util.Arrays;
import java.util.List;

/**
 *  on 21.07.15.
 * http://mobiumapps.com/
 */
public class ReviewAdapter extends ArrayAdapter<Opinion> {
    private final List<Opinion> opinions;

    public ReviewAdapter(Context context, Opinion... objects) {
        super(context, 0, objects);
        this.opinions = Arrays.asList(objects);
    }
    public ReviewAdapter(Context context, List<Opinion> opinions) {
        super(context, 0, opinions);
        this.opinions = opinions;
    }

    public static void confiqureView(View view, Opinion opinion) {
        TextView date = (TextView) view.findViewById(R.id.product_review_item_date);
        TextView name = (TextView) view.findViewById(R.id.product_review_item_name);
        TextView label = (TextView) view.findViewById(R.id.product_review_item_label);
        TextView text = (TextView) view.findViewById(R.id.product_review_item_text);
        CharSequence dateString = android.text.format.DateFormat.format("dd-MM-yyyy", opinion.getData());
        date.setText(dateString);

        name.setText(opinion.author);
        text.setText(opinion.text);
        if (opinion.getRating().isPresent()) {
            label.setVisibility(View.VISIBLE);
            int rating = opinion.getRating().get();
            Context context = view.getContext();
            if (rating < 20) {
                label.setText(context.getString(R.string.reviewLabel_1));
                label.setTextColor(context.getResources().getColor(R.color.product_review_label_bad_color));
            } else if (rating < 40) {
                label.setText(context.getString(R.string.reviewLabel_2));
                label.setTextColor(context.getResources().getColor(R.color.product_review_label_bad_color));
            } else if (rating < 60) {
                label.setText(context.getString(R.string.reviewLabel_3));
                label.setTextColor(context.getResources().getColor(R.color.product_review_label_bad_color));
            } else if (rating < 80) {
                label.setText(context.getString(R.string.reviewLabel_4));
                label.setTextColor(context.getResources().getColor(R.color.product_review_label_good_color));
            } else {
                label.setText(context.getString(R.string.reviewLabel_5));
                label.setTextColor(context.getResources().getColor(R.color.product_review_label_good_color));
            }
        } else {
            label.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_product_details_reviews_item, parent, false);
            int padding = ImageUtils.convertToPx(parent.getContext(), R.dimen.mobium_card_inner_padding);
//                convertView.setPadding(padding, padding, padding, padding);
        }

        Opinion opinion = getItem(position);
        confiqureView(convertView, opinion);
        return convertView;
    }

    public int getCount() {
        return opinions.size();
    }
}
