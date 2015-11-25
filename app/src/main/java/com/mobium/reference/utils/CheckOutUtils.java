package com.mobium.reference.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.client.models.CartItem;
import com.mobium.client.models.ShoppingCart;
import com.mobium.config.common.ConfigUtils;
import com.mobium.new_api.methodParameters.GetDeliveryMethodParam;
import com.mobium.new_api.models.order.DeliveryMethod;
import com.mobium.new_api.models.order.Field;
import com.mobium.reference.R;

import java.util.List;

/**
 *  on 13.10.15.
 */
public class CheckOutUtils {
    static public class FieldAndEditView {
        public EditText editText;
        public Field field;
    }

    public static boolean isInputCorrect(Field field, CharSequence editText) {
        return field.isRequired() ?
                editText != null && editText.length() > 3 :
                true;
    }

    public static boolean isInputCorrect(RxUtil.EditTextEvent event) {
        TextView textView = event.editText;
        return CheckOutUtils.isInputCorrect((Field) textView.getTag(), event.text);
    }

    public static boolean checkFieldAndShowErrorIfIncorrect(EditText editText, Field field) {
        boolean result = isInputCorrect(field, editText.getText());
        if (!result)
            editText.setError("Проверьте поле");
        return result;
    }


    public static GetDeliveryMethodParam buildParam(ShoppingCart cart, String regionId) {
        CartItem cartItems[] = cart.getItems();
        GetDeliveryMethodParam.OrderItem items[] = new GetDeliveryMethodParam.OrderItem[cart.getItems().length];
        for (int i = 0; i < items.length; i++) {
            items[i] = new GetDeliveryMethodParam.OrderItem(cartItems[i].shopItem.id, cartItems[0].count);
        }
        return new GetDeliveryMethodParam(cart.getItemsCost(), regionId, items);
    }


    public static AlertDialog buildDeliveryMethodsDialog(Activity activity, List<DeliveryMethod> methods, Functional.Receiver<DeliveryMethod> receiver) {
        List<DeliveryMethod> sorted = Stream.of(methods).sorted((lhs, rhs) -> lhs.getDeliveryCost() - rhs.getDeliveryCost()).collect(Collectors.toList());

        return
                new AlertDialog.Builder(activity)
                .setTitle("Выбор способа доставки")
                .setAdapter(new BaseAdapter() {
                                @Override
                                public int getCount() {
                                    return sorted.size();
                                }

                                @Override
                                public Object getItem(int position) {
                                    return sorted.get(position);
                                }

                                @Override
                                public long getItemId(int position) {
                                    return position;
                                }

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    if (convertView == null)
                                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_method, parent, false);
                                    ((TextView) convertView.findViewById(R.id.delivery_name)).setText(sorted.get(position).getTitle());
                                    ((TextView) convertView.findViewById(R.id.delivery_cost)).setText(
                                            "Цена " + ConfigUtils.formatCurrency(sorted.get(position).getTotalCost())
                                    );
                                    return convertView;
                                }
                            }, (dialog, which) -> receiver.onReceive(sorted.get(which))
                )
                .create();
    }

}
