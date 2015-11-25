package com.mobium.reference.utils.statistics_new;

import android.app.Activity;
import android.app.Application;

import com.mobium.new_api.Api;

import java.util.HashSet;
import java.util.Set;

/**
 *  on 29.09.15.
 */
public class PushGoalAgent extends StatisticAgent {
    private final static Set<String> viewOffers = new HashSet<>();
    private boolean cartEventSend = false;
    private final int limitToEvent;

    public PushGoalAgent(int limitToEvent) {
        this.limitToEvent = limitToEvent;
    }


    @Override
    public void onEvent(Event<?> event) {
        switch (event.type) {
            case open_offer:
                viewOffers.add(((Event.OpenProduct) event).data.getId());
                if (viewOffers.size() >= limitToEvent) {
                    sendViewProductEvent();
                    viewOffers.clear();
                }
                break;
            case add_to_cart:
                if (!cartEventSend) {
                    cartEventSend = true;
                    sendCartEvent();
                }
                break;
        }
    }

    private void sendCartEvent() {
        Api.getInstance().PostPushGoal("cart").invokeWithoutHandling(30, 2000);
    }

    private void sendViewProductEvent() {
        Api.getInstance().PostPushGoal("offersViewed").invokeWithoutHandling(30, 2000);
    }

    @Override
    public void onStart(Activity context) {
        viewOffers.clear();
        cartEventSend = false;
    }

    @Override
    public void onStop(Activity context) {

    }

    @Override
    public void onApplicationStart(Application application) {

    }
}
