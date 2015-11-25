package com.mobium.reference.fragments.goods;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.CartItem;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.fragments.InjectAbstractFragment;
import com.mobium.reference.presenter.PreCartPresenter;
import com.mobium.reference.presenter.PreCartPresenterImp;
import com.mobium.reference.utils.BundleUtils;
import com.mobium.config.common.ConfigUtils;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.Functional;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.reference.utils.PhoneUtils;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.view.PreCartView;
import com.mobium.reference.views.Ui_configurator;
import com.mobium.reference.views.WebImageView;
import com.mobium.reference.views.adapters.PreCartItemHolder;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.mobium.reference.utils.text.RussianPlurals.formatGoodsCount;

/**
 *  on 05.10.15.
 */
public class PreCartFragment extends InjectAbstractFragment implements PreCartView {
    private Optional<PreCartPresenter> presenter = Optional.empty();
    private PreCartItemHolder viewHolder;
    protected
    @Bind(R.id.relatedItemsTitle)
    TextView relatedItemsFragment;
    protected
    @Bind(R.id.relatedItemsCard)
    CardView relatedFragmentsContainer;
    protected
    @Bind(R.id.relatedItemsRecyclerView)
    GridView recyclerView;

    protected
    @Bind(R.id.checkoutFooter)
    View checkoutFooter;


    public static PreCartFragment getInstance(@NonNull CartItem productId) {
        PreCartFragment result = new PreCartFragment();
        result.setArguments(BundleUtils.toBundle(new Bundle(1), productId));
        return result;
    }


    /**
     * Поставщик Related Items
     *
     * @return () -> "с этим товаром покупают" throws ExecutingException
     */
    private Functional.ThrowableSupplier<List<ShopItem>, ExecutingException> load(ShopItem item) {
        return () -> {
            if (item.detailsInfo.otherItems == null)
                item.detailsInfo.otherItems = ReferenceApplication.getInstance().getExecutor().loadOtherItems(item.id, ShopDataStorage.getRegionId());
            return Arrays.asList(item.detailsInfo.otherItems);
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartItem item = BundleUtils.fromBundle(getArguments(), CartItem.class);
        presenter = Optional.of(new PreCartPresenterImp(this, item, load(item.shopItem)));
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.ifPresent(PreCartPresenter::mayLoad);
    }

    @OnClick(R.id.fragment_cart_purchase)
    public void goToCart(View v) {
        FragmentActionHandler.doAction(getActivity(), new Action(ActionType.OPEN_CART, null));
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new PreCartItemHolder(view.findViewById(R.id.cartItemPlace));

        presenter.ifPresent(PreCartPresenter::viewCreated);
        relatedItemsFragment.setVisibility(View.GONE);
        relatedFragmentsContainer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        TextView totalCost = (TextView) checkoutFooter.findViewById(R.id.fragment_cart_total_cost);
        totalCost.setText(ConfigUtils.formatCurrency((ReferenceApplication.getInstance().getCart().getItemsCost())));

        TextView count = (TextView) checkoutFooter.findViewById(R.id.fragment_cart_count);
        count.setText(formatGoodsCount(ReferenceApplication.getInstance().getCart().getItemsCount()));
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_cart_fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.ifPresent(PreCartPresenter::abortLoad);
    }

    @Override
    public void updateCounts(CartItem item) {
        if (item.count <= 0)
            showExitDialog();
        else {
            viewHolder.count.setText("" + item.count);
            viewHolder.totalCost.setText(ConfigUtils.formatCurrency(item.shopItem.cost.getCurrentConst() * item.count));
        }

    }

    private void showExitDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Удалить товар из корзины")
                .setPositiveButton("Удалит и выйти", (dialog, which) -> exit())
                .setNegativeButton("Оставить", (dialog, which) -> presenter.ifPresent(value -> value.onAddButtonClick(getView())))
                .setCancelable(false)
                .show();
    }


    @Override
    public void showCartItem(CartItem item) {
        ShopItem si = item.shopItem;
        viewHolder.title.setText(si.title);
        viewHolder.count.setText("" + item.count);
        viewHolder.cost.setText(ConfigUtils.formatCurrency(item.shopItem.cost.getCurrentConst()));
        viewHolder.add.setOnClickListener(v -> {
            presenter.ifPresent(value -> value.onAddButtonClick(v));
            PhoneUtils.vibrate(getActivity(), 200);
        });
        viewHolder.remove.setOnClickListener(v -> {
            presenter.ifPresent(value -> value.onRemoveButtonClick(v));
            PhoneUtils.vibrate(getActivity(), 200);
        });
    }

    @Override
    public void showRelatedItems(List<ShopItem> itemList, String label) {
        if (itemList != null && itemList.size() > 0) {
            relatedItemsFragment.setVisibility(View.VISIBLE);
            relatedItemsFragment.setText(label);
            relatedFragmentsContainer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setNumColumns(2);

            LayoutInflater inflater = LayoutInflater.from(getActivity());

            final int dp6 = ImageUtils.convertToPx(getContext(), 6);

            recyclerView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return itemList.size();
                }

                @Override
                public Object getItem(int i) {
                    return itemList.get(i);
                }

                @Override
                public long getItemId(int i) {
                    return i;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {
                    ShopItem analog = (ShopItem) getItem(i);
                    if (view == null)
                        view = inflater.inflate(R.layout.shop_item_card, viewGroup, false);

                    WebImageView icon = (WebImageView) view.findViewById(R.id.image);
                    TextView title = (TextView) view.findViewById(R.id.label);
                    TextView cost = (TextView) view.findViewById(R.id.cost);
                    TextView oldCost = (TextView) view.findViewById(R.id.oldCost);

                    Ui_configurator.getInstance(getActivity()).configureShopItemInfo(analog, icon, title, cost, oldCost, null, null);
                    Ui_configurator.getInstance(getActivity()).configureOnShopItemClicks(view.findViewById(R.id.frame), analog);

                    ((ViewGroup.MarginLayoutParams) view.findViewById(R.id.card).getLayoutParams())
                            .bottomMargin = (i == getCount() - 1) ?
                            checkoutFooter.getHeight() : dp6;
                    return view;
                }
            });
        }

    }


    @Override
    public void exit() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void showError(String title, String message, DialogInterface.OnClickListener apply, DialogInterface.OnClickListener cancel) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Повторить", apply)
                .setNegativeButton("Пропустить", cancel);
    }

    @Override
    public String getTitle() {
        return "Корзина";
    }
}
