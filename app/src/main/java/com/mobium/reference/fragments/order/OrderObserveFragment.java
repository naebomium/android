package com.mobium.reference.fragments.order;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.client.api.ShopCache;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.CartItem;
import com.mobium.new_api.models.order.IOrder;
import com.mobium.client.models.SuccessOrderData;
import com.mobium.new_api.models.order.OrderItem;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.fragments.AbstractLoadBarFragment;
import com.mobium.reference.utils.CollectionUtil;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.utils.text.MoneyFormatter;
import com.mobium.reference.utils.text.RussianPlurals;
import com.mobium.reference.views.WrapContentManger;
import com.mobium.reference.views.adapters.OrderItemsAdapter;
import com.mobium.userProfile.ResponseParams.OrderProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Time: 19:14
 * look
 */
public class OrderObserveFragment extends AbstractLoadBarFragment {

    private static final String ORDER_DATA_ID = "ORDER_DATA_ID";

    private IOrder orderData;
    private TextView date;
    private TextView status;
    private TextView message;
    private RecyclerView listOfGoods;

    @Override
    protected void afterPrepared() {
        super.afterLoaded();


        status.setText(orderData.getStatus());
        message.setText("Всего " + RussianPlurals.formatGoodsCount(orderData.getCount()) +
                        " на сумму " + MoneyFormatter.formatRubles(orderData.getCost())
        );
        date.setVisibility(View.INVISIBLE);
        orderData.getDate().ifPresent(time -> {
            date.setText(android.text.format.DateFormat.format("dd-MM-yyyy", time));
            date.setVisibility(View.VISIBLE);
        });

        List<CartItem> items = null;

        if (orderData instanceof SuccessOrderData)
            items = items((SuccessOrderData) orderData);
        else if (orderData instanceof OrderProfile)
            items = items((OrderProfile) orderData);

        if (items != null && items.size() > 0)
            listOfGoods.setAdapter(
                    new OrderItemsAdapter(
                            items,
                            getActivity(),
                            item -> {
                                if (!(item.shopItem instanceof OldShopItem))
                                    FragmentActionHandler
                                            .doAction(getActivity(), new Action(ActionType.OPEN_PRODUCT, item.shopItem.id));
                            }
                    )

            );
    }

    private List<CartItem> items(SuccessOrderData order) {
        if (order.getChacheItems() != null)
            return order.getChacheItems();

        ShopCache cache = getApplication().getShopCache();

        return Stream.of(order.getItemCounts())
                .map(simpleOrderItem -> new CartItem(cache.fetchItem(simpleOrderItem.itemId), simpleOrderItem.count))
                .filter(cartItem -> cartItem.shopItem != null)
                .collect(Collectors.toList());
    }

    public List<CartItem> items(OrderProfile profile) {
        return profile.getCacheItems();
    }

    public static OrderObserveFragment getInstance(IOrder data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER_DATA_ID, data);
        OrderObserveFragment result = new OrderObserveFragment();
        result.setArguments(bundle);
        return result;
    }


    @Override
    protected boolean needLoading() {
        boolean result = false;
        if (orderData instanceof SuccessOrderData) {
            result = ((SuccessOrderData) orderData).getChacheItems() == null;
        } else if (orderData instanceof OrderProfile) {
            OrderProfile orderProfile = (OrderProfile) orderData;
            result = orderProfile.getCacheItems() == null;
        }
        return result;
    }

    @Override
    protected void loadInBackground() throws ExecutingException {
        ShopCache cache = getApplication().getShopCache();
        if (orderData instanceof SuccessOrderData) {
            SuccessOrderData data = (SuccessOrderData) orderData;

            List<String> listToDownLoad = Stream.of(data.getItemCounts())
                    .map(value -> value.itemId)
                    .collect(Collectors.toList());

            //загрузить все items
            ShopItem items[] = cache.getItemsById(listToDownLoad);


            ArrayList<CartItem> cartItems = new ArrayList<>();

            //формуруем cartItems
            for (OrderItem item : data.getItemCounts()) {
                Optional<ShopItem> shopItem = Stream.of(items)
                        .filter(it -> it.getId().equals(item.itemId))
                        .findFirst();

                shopItem.ifPresent(si -> cartItems.add(new CartItem(si, item.count)));
            }

            data.setCacheItems(cartItems);

        } else if (orderData instanceof OrderProfile) {
            OrderProfile data = (OrderProfile) orderData;

            //items of order
            List<com.mobium.userProfile.ResponseParams.OrderItem> orderItems = data.items;
            //id of orderItems
            List<String> realIds = Stream.of(orderItems).map(item -> item.id).collect(Collectors.toList());

            //list of shopItems got by real id. Can be less then realIds! Warring
            List<ShopItem> items = Arrays.asList(cache.getItemsByRealId(realIds));

            List<CartItem> cartItems =
                Stream.of(orderItems).map(orderItem -> {
                            String realId = orderItem.id; // realId of orderItem
                            ShopItem shopItem =
                                    Stream.of(items) // finding shopItem with realId equals current
                                            .filter(item -> item.getRealId().isPresent())
                                            .filter(item -> item.getRealId().get().equals(realId))
                                            .findFirst()
                                            .map(value -> { //set orderItem
                                                value.setProfileItemResource(orderItem);
                                                return value;
                                            })
                                            .orElseGet(() -> new OldShopItem(orderItem));// if not findEnum, create empty shopItem
                            return new CartItem(shopItem, orderItem.count);
                        })
                        .collect(Collectors.toList());

            data.setCachedItems(CollectionUtil.toArrayList(cartItems));

        }

    }

    @Override
    protected View addContentView(LayoutInflater inflater, ViewGroup contentWrapper) {
        View root = inflater.inflate(R.layout.fragment_success_order, contentWrapper, true);
        date = (TextView) root.findViewById(R.id.fragment_success_order_date);
        status = (TextView) root.findViewById(R.id.fragment_success_order_status);
        message = (TextView) root.findViewById(R.id.fragment_success_order_massage);
        listOfGoods = (RecyclerView) root.findViewById(R.id.fragment_success_order_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Context context = contentWrapper.getContext();
            root.findViewById(R.id.fragment_success_order_header)
                    .setElevation(ImageUtils.convertToPx(context, 4));
            listOfGoods
                    .setElevation(ImageUtils.convertToPx(context, 2));
        }

        listOfGoods.setItemAnimator(new DefaultItemAnimator());
        listOfGoods.setLayoutManager(new WrapContentManger(getActivity(), LinearLayoutManager.VERTICAL, false));
        listOfGoods.setHasFixedSize(true);

        return root;
    }


    @Override
    protected String getTitle() {
        String orderId = orderData != null ? " " + orderData.getId() : "";
        return "Заказ № " + orderId;
    }


    protected void onRestoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            orderData = (IOrder) savedInstanceState.getSerializable(ORDER_DATA_ID);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.order_info.name());
    }

    @Override
    public void onSaveState(Bundle outState) {
        outState.putSerializable(ORDER_DATA_ID, orderData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listOfGoods.clearOnScrollListeners();
        listOfGoods.destroyDrawingCache();
        listOfGoods.setAdapter(null);
        listOfGoods = null;
    }

    public static final class OldShopItem extends ShopItem {
        public OldShopItem(com.mobium.userProfile.ResponseParams.OrderItem item) {
            super(null, "Товар устарел", null, null, null, null);
            setProfileItemResource(item);
            DetailsInfo di = new DetailsInfo();
            di.realId = item.id;
            detailsInfo = di;
        }
    }
}
