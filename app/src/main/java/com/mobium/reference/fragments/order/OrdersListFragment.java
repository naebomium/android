package com.mobium.reference.fragments.order;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.mobium.client.ShopDataStorage;
import com.mobium.new_api.models.order.IOrder;
import com.mobium.client.models.SuccessOrderData;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicLoadableFragment;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.reference.utils.ProfileUtils;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.utils.text.MoneyFormatter;
import com.mobium.reference.views.WrapContentManger;
import com.mobium.reference.views.adapters.ClickableHolder;
import com.mobium.reference.views.adapters.ListClickableAdapter;
import com.mobium.userProfile.ResponseParams.OrderProfile;

import java.util.ArrayList;
import java.util.List;

import static com.mobium.reference.utils.text.RussianPlurals.formatGoodsCount;

/**
 *  on 19.05.15.
 * http://mobiumapps.com/
 *
 * List of user offers
 */
public class OrdersListFragment extends BasicLoadableFragment {
    private List<String> idOfNotCachedItems;

    private RecyclerView listOfOrders;
    private TextView hint;
    private View loadBar;

    @Override
    protected void loadInBackground() throws ExecutingException {

        Predicate<String> notHaveItem = s -> !getApplication().getShopCache().hasItem(s);

        try {
            Optional.ofNullable(ProfileUtils.updateOrderTask()).map(or -> or.data).map(data -> data.orderProfiles)
                    .ifPresent(value -> getActivity().runOnUiThread(() -> ShopDataStorage.getInstance().setProfileOrderProfiles(value)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExecutingException(e.getMessage(), true);
        }

        idOfNotCachedItems = Stream.of(getApplication().getShopData().getOrders())
                .map(orderData -> orderData.getItemCounts().iterator().next().itemId)
                .filter(notHaveItem)
                .collect(Collectors.toList());

        if (idOfNotCachedItems.isEmpty())
            return;

        getApplication().getShopCache().getItemsById(idOfNotCachedItems);
    }

    @Override
    protected boolean needLoading() {
        return true;
    }

    @Override
    protected View getProgressView() {
        return loadBar;
    }

    @Override
    protected void afterLoaded() {
        super.afterLoaded();
        fill();
    }

    @Override
    protected void doesntNeedLoading() {
        super.doesntNeedLoading();
        fill();
    }

    @Override
    protected void alreadyLoaded() {
        super.alreadyLoaded();
        fill();
    }

    private void fill() {
        ArrayList<IOrder> iOrders = new ArrayList<>();

        iOrders.addAll(ShopDataStorage.getInstance().getOrders());
        iOrders.addAll(ShopDataStorage.getInstance().getProfileOrderProfiles());

        if (iOrders.size() != 0) {
            listOfOrders.setAdapter(
                    new OrdersAdapterClickable(iOrders, getActivity())
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                listOfOrders.setElevation(ImageUtils.convertToPx(getActivity(), 4));
        }
        else {
            listOfOrders.setVisibility(View.GONE);
            hint.setText("Еще нет заказов");
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.order_list_fragment, container, false);

        listOfOrders = (RecyclerView)root.findViewById(R.id.order_list_fragment_list);
        listOfOrders.setBackgroundResource(R.color.white_background);
        hint = (TextView) root.findViewById(R.id.order_list_fragment_hint);
        loadBar = root.findViewById(R.id.order_list_fragment_progress);


        listOfOrders.setLayoutManager(new WrapContentManger(getActivity(), LinearLayoutManager.VERTICAL, false));

        listOfOrders.setItemAnimator(new DefaultItemAnimator());
        listOfOrders.setHasFixedSize(true);



        return root;
    }



    private class OrdersAdapterClickable extends ListClickableAdapter<OrdersAdapterClickable.Holder, IOrder> {

        public OrdersAdapterClickable(List<IOrder> data, Context context) {
            super(data, context);
        }

        @Override
        protected void applyItemToHolder(Holder holder, IOrder iOrder, int type) {
            if (iOrder instanceof SuccessOrderData) {
                applyItemToHolder(holder, (SuccessOrderData) iOrder);
            } else if (iOrder instanceof OrderProfile)
                applyItemToHolder(holder, (OrderProfile) iOrder);
        }

        @Override
        protected void onItemClick(IOrder iOrder, int po) {
            FragmentActionHandler.doOpenSuccessOrder(getDashboardActivity(), iOrder);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(inflater.inflate(R.layout.item_order, parent, false));
        }


        protected void applyItemToHolder(Holder holder, OrderProfile orderProfileData) {

            holder.firstItemName.setVisibility(View.INVISIBLE);
            holder.cost.setText(MoneyFormatter.formatRubles(orderProfileData.getCost()) + " р.");
            int sizeOfItems = orderProfileData.getCount();

            holder.showAnotherItems.setText(
                    formatGoodsCount(sizeOfItems)
            );

            holder.orderId.setText("Заказ №" + orderProfileData.id);
            holder.status.setText(orderProfileData.status);
            holder.date.setVisibility(View.INVISIBLE);
        }

        protected void applyItemToHolder(Holder holder, SuccessOrderData orderData) {
            holder.firstItemName.setVisibility(View.VISIBLE);
            if (getApplication().getShopCache().hasItem(orderData.getFirstId())) {
                ShopItem item = getApplication().getShopCache().fetchItem(orderData.getFirstId());
                holder.firstItemName.setText(item.title);
                holder.firstItemName.setAlpha(1);
            } else {
                holder.firstItemName.setText("Товар устарел");
                holder.firstItemName.setAlpha(0.4f);
            }
            holder.cost.setText(MoneyFormatter.formatRubles(orderData.getCost()) + " р.");
            int sizeOfItems = orderData.getItemCounts().size();

            holder.showAnotherItems.setText(
                    sizeOfItems > 1 ? "Еще " + formatGoodsCount(sizeOfItems) : "Один товар"
            );

            holder.orderId.setText("Заказ №" + orderData.getOrderId());
            holder.status.setText(orderData.getStatus());
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(DateFormat.format("dd-MM-yyyy", orderData.getTime()));

        }


        public class Holder extends ClickableHolder {
            public TextView cost;
            public TextView firstItemName;
            public TextView showAnotherItems;
            public TextView status;
            public TextView orderId;
            public final TextView date;

            public Holder(View itemView) {
                super(itemView);
                cost = (TextView) itemView.findViewById(R.id.item_order_cost);
                firstItemName = (TextView) itemView.findViewById(R.id.item_order_title);
                showAnotherItems = (TextView) itemView.findViewById(R.id.item_order_otherItems);
                status = (TextView) itemView.findViewById(R.id.item_order_status);
                orderId = (TextView) itemView.findViewById(R.id.item_order_id);
                date = (TextView) itemView.findViewById(R.id.item_order_date);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.orders_history.name());
    }

    @Override
    protected String getTitle() {
        return "История заказов";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listOfOrders.clearOnScrollListeners();
        listOfOrders.destroyDrawingCache();
        listOfOrders.setAdapter(null);
    }
}


