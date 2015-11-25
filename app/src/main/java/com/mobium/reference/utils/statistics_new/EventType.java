package com.mobium.reference.utils.statistics_new;

/**
 *  on 24.09.15.
 */

public enum EventType {
    //cart
    add_to_cart(EventGroup.cart),
    remove_from_cart(EventGroup.cart),
    open_from_cart(EventGroup.cart),

    //navigation
    add_to_favorites(EventGroup.navigation),
    remove_from_favorites(EventGroup.navigation),
    open_from_favorites(EventGroup.navigation),
    open_page(EventGroup.navigation),

    //connectivity
    make_call(EventGroup.connectivity),
    send_feedback(EventGroup.connectivity),

    //search
    search(EventGroup.search),

    //city
    choose_city(EventGroup.city),

    //banners
    open_banner(EventGroup.banners),

    //app,
    app_start(EventGroup.app),
    app_start_from_push(EventGroup.app),

    //order
    make_order(EventGroup.order),

    //catalog
    open_offer(EventGroup.catalog),
    open_category(EventGroup.catalog),
    open_filters(EventGroup.catalog),
    open_barcode_scanner(EventGroup.catalog);

    public final EventGroup group;

    EventType(EventGroup group) {
        this.group = group;
    }
}
