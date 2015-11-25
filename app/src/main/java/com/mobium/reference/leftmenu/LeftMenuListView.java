package com.mobium.reference.leftmenu;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.client.LogicUtils;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.ShopCategory;
import com.mobium.config.common.Config;
import com.mobium.config.prototype.ILeftMenu;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.views.WebImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *   12.03.15.
 * http://mobiumapps.com/
 */
public class LeftMenuListView extends ListView implements
        LogicUtils.ProfileListener {
    public static final int animationTime = 200;
    private final Map<String, LeftMenuButton> cache = new HashMap<>();
    private final ArrayList<LeftMenuItem> currentMenuItems = new ArrayList<>();
    private final Stack<ShopCategory> categoryStack = new Stack<>();
    private Animation enter;
    private Animation exit;
    private Animation popEnter;
    private Animation popExit;
    private boolean animated;
    private boolean back;
    private boolean loginToProfile;

    public LeftMenuListView(Context context) {
        super(context);
        setUp(context);
    }

    public LeftMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public LeftMenuListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context);
    }

    private void setUp(Context context) {
        enter = AnimationUtils.loadAnimation(context, R.anim.exit);
        exit = AnimationUtils.loadAnimation(context, R.anim.enter);
        popEnter = AnimationUtils.loadAnimation(context, R.anim.pop_enter);
        popExit = AnimationUtils.loadAnimation(context, R.anim.pop_exit);

        setClickable(true);

        ShopDataStorage.getInstance().addFavouriteListener(newValue -> getActivity().runOnUiThread(this::updatedData));
        ShopDataStorage.getInstance().addProfileListener(this);
        ShopDataStorage.getInstance().addOrderListener((newValue, profile) -> getActivity().runOnUiThread(this::updatedData));

        ((ReferenceApplication) getActivity().getApplication())
                .getCart().addCartListener(n -> updatedData());
        loginToProfile = ShopDataStorage.getInstance().getProfileAccessToken() != null;
    }

    private List<LeftMenuItem> getDefaultValues() {
        List<LeftMenuItem> result = null;
        result = LeftMenuBuilder.build(Config.get().design().getLeftMenu(), getContext());
        if (result != null)
            return result;
        else
            return new LeftMenuItemArrayList();
    }

    public void setLeftMenuItems(List<LeftMenuItem> startItems) {
        if (startItems == null)
            currentMenuItems.addAll(getDefaultValues());
        else
            currentMenuItems.addAll(startItems);
        setAdapter(new LeftMenuAdapter(getContext(), Config.get().design().getLeftMenu(), currentMenuItems));
    }

    public void initByDefValues() {
        initByDefValues(animated);
    }

    public void initByDefValues(boolean withAnimation) {
        if (isStartState())
            return;
        categoryStack.clear();
        currentMenuItems.clear();
        currentMenuItems.addAll(getDefaultValues());
        setItems(withAnimation);
        setSelection(0);
    }


    private void updatedData() {
        Adapter adapter = getAdapter();
        if (adapter instanceof LeftMenuAdapter) {
            ((LeftMenuAdapter) adapter).updatedData();
        } else if (adapter instanceof HeaderViewListAdapter) {
            LeftMenuAdapter wrappedAdapter = (LeftMenuAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
            wrappedAdapter.updatedData();
        }
    }

    private void setItems(boolean withAnimation) {
        if (!withAnimation)
            updatedData();
        else {
            final Animation first;
            final Animation second;

            if (!back) {
                first = enter;
                second = exit;
            } else {
                first = popExit;
                second = popEnter;
            }
            first.setDuration(animationTime);
            second.setDuration(animationTime);
            second.setAnimationListener(null);
            first.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    startAnimation(second);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            second.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    updatedData();
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            startAnimation(second);
            back = false;
        }
    }

    public void setCategory(ShopCategory shopCategory) {
        if (shopCategory == null) {
            return;
        }


        currentMenuItems.clear();
        currentMenuItems.add(new LeftMenuButton(new Action(ActionType.POP_CATALOG_INSIDE_MENU, ""), shopCategory.title, shopCategory.icon.getUrl()));

        currentMenuItems.addAll(Stream.of(shopCategory.childs).map(this::makeButtonCategory).collect(Collectors.toList()));

        if (categoryStack.isEmpty() || !categoryStack.peek().id.equals(shopCategory.id))
            categoryStack.push(shopCategory);

        setItems(animated);
    }

    private LeftMenuButton makeButtonCategory(ShopCategory shopCategory) {
        if (cache.containsKey(shopCategory.id))
            return cache.get(shopCategory.id);

        LeftMenuButton result = new LeftMenuButton(shopCategory.id, shopCategory.title, shopCategory.icon.getUrl());
        cache.put(shopCategory.id, result);
        return result;
    }


    public boolean isStartState() {
        return categoryStack.isEmpty();
    }

    public void back() {
        back = true;
        if (categoryStack.size() == 1)
            initByDefValues();
        else {
            categoryStack.pop();
            ShopCategory shopCategory = categoryStack.pop();
            setCategory(shopCategory);
        }
    }

    private static void setAlpha(View view, float alpha) {
        if (Build.VERSION.SDK_INT < 11) {
            final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
            animation.setDuration(0);
            animation.setFillAfter(true);
            view.startAnimation(animation);
        } else
            view.setAlpha(alpha);
    }

    private MainDashboardActivity getActivity() {
        return (MainDashboardActivity) getContext();
    }


    public void setAnimated(boolean animated) {
        this.animated = animated;
    }


    public interface LeftMenuItem {
        String getViewTag();

        boolean isEnabled();

        Action getFirstAction();

        Action getSecondAction();

        Drawable getDrawable();

    }

    private class LeftMenuItemArrayList extends ArrayList<LeftMenuItem> {

        public LeftMenuItemArrayList() {
            add(new LeftMenuButton(new Action(ActionType.OPEN_START, "0"), "Главная", R.drawable.left_menu_home));

            add(new LeftMenuButton("0", "Каталог товаров", R.drawable.catalog));

            add(new LeftMenuButton(new Action(ActionType.OPEN_CATEGORY_BY_ALIAS, "actions"), "Акционные товары", R.drawable.ic_sale));

            add(new LeftMenuButton(new Action(ActionType.OPEN_USEFUL, null), "Полезное", R.drawable.left_menu_thumbs_up));

            if (Config.get().getApplicationData().getProfileEnabled()) {
                add(new MenuSeparator("Личный кабинет"));
                if (!loginToProfile)
                    add(new LeftMenuButton(new Action(ActionType.OPEN_LOGIN_SCREEN, null), "Вход/Регистрация", R.drawable.left_menu_user));
                else
                    add(new LeftMenuButton(new Action(ActionType.OPEN_PROFILE_SCREEN, null), "Профиль", R.drawable.left_menu_user));
            }

            add(new MenuSeparator("Личный кабинет"));


            add(new LeftMenuButton(new Action(ActionType.OPEN_CART, null), "Корзина", R.drawable.left_menu_cart));

            add(new LeftMenuButton(new Action(ActionType.OPEN_FAVOURITES, null), "Избранное", R.drawable.left_menu_star));

            add(new LeftMenuButton(new Action(ActionType.OPEN_HISTORY, null), "Мои заказы", R.drawable.left_menu_order_list));

            add(new MenuSeparator("Babadu"));

            add(new LeftMenuButton(new Action(ActionType.OPEN_SHOPS, null), "Адреса магазинов", R.drawable.left_menu_shops));

            add(new LeftMenuButton(new Action(ActionType.OPEN_SERVICES, null), "Адреса сервисных центров", R.drawable.index_support));

            add(new LeftMenuButton(new Action(ActionType.OPEN_CONTACTS, null), "Наши контакты", R.drawable.left_menu_contatcs));

//            add(new LeftMenuButton(new Action(ActionType.OPEN_ARTICLES, null), "Статьи", R.drawable.ic_article));

//            add(new MenuSeparator("dev menu"));
//
            add(new LeftMenuButton(new Action(ActionType.OPEN_PRODUCT_BY_REAL_ID, "255099"), "free delivery", R.drawable.left_menu_star));
//            add(new LeftMenuButton(new Action(ActionType.SEND_TEST_PUSH, " "), "test push", R.drawable.left_menu_star));
//
//            add(new LeftMenuButton(new Action(ActionType.SEND_TEST_PUSH_CONTENT, ""), "test push content", R.drawable.star));
        }
    }

    private class LeftMenuAdapter extends ArrayAdapter<LeftMenuItem> {
        private final LayoutInflater inflater;
        private Handler handler;
        private ILeftMenu model;

        public LeftMenuAdapter(Context context, ILeftMenu model, List<LeftMenuItem> items) {
            super(context, 0, items);
            notifyDataSetChanged();
            inflater = LayoutInflater.from(this.getContext());
            handler = new Handler();
            this.model = model;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LeftMenuItem item = getItem(position);

            if (item instanceof MenuSeparator) {
                convertView = create((MenuSeparator) item, convertView, parent);
            } else if (item instanceof LeftMenuButton) {
                convertView = create((LeftMenuButton) item, convertView, parent, position);
            }

            convertView.setTag(item.getViewTag());
            return convertView;
        }

        private View create(MenuSeparator separator, View result, ViewGroup parent) {
            if (result == null || !String.valueOf(result.getTag()).equals(separator.getViewTag())) {
                result = inflater.inflate(R.layout.menu_separator, parent, false);
            }
            TextView title = (TextView) result.findViewById(R.id.title);
            View divider = result.findViewById(R.id.divider);
            title.setText(separator.getName());
            if (model != null) {
                divider.setBackgroundColor(model.getSeparatorColor(getContext()));
                title.setTextColor(model.getCellTextColor(getContext()));
            }

            return result;
        }

        private View create(final LeftMenuButton button, View result, ViewGroup parent, int position) {
            if (result == null || !String.valueOf(result.getTag()).equals(button.getViewTag())) {
                result = inflater.inflate(R.layout.design_list_item, parent, false);
            }

            TextView title = (TextView) result.findViewById(R.id.leftMenuText);
            WebImageView icon = (WebImageView) result.findViewById(R.id.icon);
            View pointer = result.findViewById(R.id.pointer);

            title.setText(getTitleDependsOnAction(button.getTitle(), button.getFirstAction()));
            Action action = button.getFirstAction();

            if (action != null) {
                ActionType type = button.getFirstAction().getType();

                pointer.setVisibility(
                        type == ActionType.OPEN_CATALOG_INSIDE_MENU || type == ActionType.OPEN_CATEGORY ?
                                VISIBLE : INVISIBLE
                );
            } else {
                pointer.setVisibility(INVISIBLE);
            }

            if (position == 0 && button.getFirstAction().getType() == ActionType.POP_CATALOG_INSIDE_MENU) {
                pointer.setVisibility(INVISIBLE);
                int backIcon = R.drawable.previous;
                icon.setImageResource(backIcon);
                title.setText(button.getTitle().toUpperCase());
                title.setAlpha(0.5f);
                icon.setVisibility(VISIBLE);
            } else {
                title.setAlpha(1f);
                if (button.getIconResurse() != 0) {
                    icon.setImageResource(button.getIconResurse());
                    icon.setVisibility(VISIBLE);
                } else if (button.getDrawable() != null) {
                    icon.setVisibility(VISIBLE);
                    button.getDrawable().setColorFilter(getResources().getColor(R.color.application_color_accent), PorterDuff.Mode.SRC_ATOP);
                    icon.setImageDrawable(button.getDrawable());
                } else
                    icon.setVisibility(GONE);
            }

            if (button.getSecondAction() != null) {
                pointer.setOnClickListener(v -> FragmentActionHandler.doAction(getActivity(), button.getSecondAction()));
                pointer.setClickable(true);
            } else {
                pointer.setClickable(false);
            }
            final View finalResult = result;
            if (button.getFirstAction() != null)
                result.setOnClickListener(v -> handler.post(() -> {
                    ViewCompat.jumpDrawablesToCurrentState(finalResult);
                    FragmentActionHandler.doAction(getActivity(), button.getFirstAction());
                }));


            if (model != null) {
                title.setTextColor(model.getCellTextColor(getContext()));
                result.setBackgroundColor(model.getBackgroundColor(getContext()));

            }

            return result;
        }


        private String getTitleDependsOnAction(String title, Action action) {
            if (action == null || action.getType() == null)
                return title;

            int itemsCount;

            switch (action.getType()) {
                case OPEN_CART:
                    itemsCount = ((ReferenceApplication) getActivity().getApplication())
                            .getCart().getItems().length;

                    return title + (itemsCount == 0 ? "" : " ( " + itemsCount + " )");
                case OPEN_FAVOURITES:
                    itemsCount = ShopDataStorage.getInstance().getFavourites().size();
                    return title + (itemsCount == 0 ? "" : " ( " + itemsCount + " )");
                case OPEN_HISTORY:
                    itemsCount = ShopDataStorage.getInstance().getOrders().size() +
                            ShopDataStorage.getInstance().getProfileOrderProfiles().size();
                    return title + (itemsCount == 0 ? "" : " ( " + itemsCount + " )");
                default:
                    return title;
            }
        }

        public void updatedData() {
            notifyDataSetChanged();
        }


        @Override
        public boolean isEnabled(int position) {
            return getItem(position).isEnabled();
        }
    }


    @Override
    public void onLogin(String accessToken) {
        loginToProfile = true;
        categoryStack.clear();
        currentMenuItems.clear();
        currentMenuItems.addAll(getDefaultValues());
        setItems(false);
        setSelection(0);
        updatedData();
    }

    @Override
    public void onExit() {
        loginToProfile = false;
        categoryStack.clear();
        currentMenuItems.clear();
        currentMenuItems.addAll(getDefaultValues());
        setItems(false);
        initByDefValues();
    }
}

