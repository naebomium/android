package com.mobium.client.models;

import java.io.Serializable;

/**
 *
 *
 * Date: 12.04.12
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
public class CommentItem implements Serializable {
    private String text;

    public String getText() {
        return text;
    }

    public CommentItem(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
