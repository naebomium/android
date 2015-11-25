package com.mobium.reference.productPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.mobium.client.models.CharacteristicItem;
import com.mobium.client.models.CharacteristicsGroup;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.activity.MainDashboardActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *   01.04.15.
 * http://mobiumapps.com/
 */
public class FeaturesDetails extends ProductDetailsBase {
    private View contentView;
    private ViewGroup tableLayout;


    public FeaturesDetails(MainDashboardActivity context, ShopItem item) {
        super(context, DetailsType.PRODUCT_FEATURES, item);

    }


    @Override
    protected View fillContentWrapper(final LayoutInflater inflater, ViewGroup contentWrapper) {
        if (contentView != null) {
            contentView.requestLayout();
        } else {
            contentView = inflater.inflate(R.layout.fragment_product_details_features, contentWrapper);
            if (shopItem.getCharacteristics().isPresent()) {

                tableLayout = (ViewGroup) contentView.findViewById(R.id.feature_table);
                tableLayout.removeAllViews();
                for (CharacteristicsGroup charGroup : shopItem.getCharacteristics().get()) {
                    if (shopItem.getCharacteristics().get().length > 1) {
                        addCharacteristicsGroupInTable(charGroup);
                    } else {
                        addCharacteristicsItems(charGroup);
                    }
                }
//                tableLayout.addView(inflater.inflate(R.layout.share_buttons, null));
            }
        }

        return contentView;
    }

    @Override
    protected boolean needAddButtons() {
        return true;
    }


    private void addCharacteristicsGroupInTable(CharacteristicsGroup charGroup) {
        if (charGroup.getGroupTitle() != null && !"".equals(charGroup.getGroupTitle())) {
            addCharacteristicGroupTitle(charGroup.getGroupTitle());
        }
        addCharacteristicsItems(charGroup);
    }

    private void addCharacteristicGroupTitle(String title) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        ViewGroup row = (TableRow) inflater.inflate(R.layout.feature_item, null);
        TextView featureKey = (TextView) row.findViewById(R.id.feature_key);
        featureKey.setText(title);


        tableLayout.addView(row, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void addCharacteristicsItems(CharacteristicsGroup charGroup) {
//        int count = 0;
        LayoutInflater inflater = LayoutInflater.from(activity);
        HashMap<String, String> keyAndValues = groupingCharacteristicsWithEqualKey(charGroup.getCharacteristics());
        for (String key : keyAndValues.keySet()) {
            String values = keyAndValues.get(key);
            ViewGroup row = (ViewGroup) inflater.inflate(R.layout.feature_item, null);
            ((TextView) row.findViewById(R.id.feature_key)).setText(key);
            ((TextView) row.findViewById(R.id.feature_value)).setText(values);
            tableLayout.addView(row, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//            if (count % 2 == 0) {
//                row.setBackgroundResource(R.drawable.product_tab_content_greyline);
//            } else {
//                row.setBackgroundResource(R.drawable.product_tab_content_whiteline);
//            }

//            count++;
        }
    }

    private HashMap<String, String> groupingCharacteristicsWithEqualKey(CharacteristicItem... characteristics) {
        HashMap<String, ArrayList<String>> listMultimap = new HashMap<>();
        for (CharacteristicItem charItem : characteristics) {
            String key = charItem.getKeyTitle();
            String value = charItem.getValueTitle();
            if (!listMultimap.containsKey(key)) {
                listMultimap.put(key, new ArrayList<>());
            }
            listMultimap.get(key).add(value + getSpaceAndSuffix(charItem));
        }

        HashMap<String, String> result = new HashMap<>();
        for (String key : listMultimap.keySet()) {
            result.put(key, concatCharacteristicValues(listMultimap.get(key)));
        }
        return result;
    }

    private String concatCharacteristicValues(ArrayList<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(values.get(i));
        }
        return sb.toString();
    }

    private static String getSpaceAndSuffix(CharacteristicItem charItem) {
        String suffix = charItem.getExtraString("suffix", "");
        if ("".equals(suffix)) {
            return "";
        } else {
            return ' ' + suffix;
        }
    }
}
