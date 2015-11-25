package com.mobium.client.models;

import android.support.v4.util.Pair;

import java.util.List;

/**
 *  on 09.06.15.
 * http://mobiumapps.com/
 */
public class Opinions {

    public final int size;
    public final List<Opinion> opinions;

    public Opinions(int size, List<Opinion> opinions) {
        this.size = size;
        this.opinions = opinions;
    }

    public Pair<Integer, List<Opinion>> getPair() {
        return Pair.create(size, opinions);
    }

}
