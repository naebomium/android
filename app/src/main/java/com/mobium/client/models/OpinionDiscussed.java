package com.mobium.client.models;

import com.annimon.stream.Optional;

import java.io.Serializable;

/**
 *  on 21.07.15.
 * http://mobiumapps.com/
 */
public interface OpinionDiscussed extends Serializable {
    String getOpinionTag();

    void setOpinions(Opinions opinions);

    Optional<Opinions> getOpinions();

    String getId();
}
