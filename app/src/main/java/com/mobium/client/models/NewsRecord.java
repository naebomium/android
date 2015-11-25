package com.mobium.client.models;

import com.annimon.stream.Optional;
import com.mobium.client.models.resources.Graphics;

/**
 *
 *
 * Date: 19.12.12
 * Time: 14:02
 */
public class NewsRecord extends Extralable<NewsRecord> implements OpinionDiscussed {
    private Action recordAction;
    private int id;
    private String title;
    private Graphics graphics;
    private String description;
    private int date;
    private transient Opinions opinions;

    public NewsRecord(int id, String title, Action recordAction, Graphics graphics, String description, int date) {
        this.recordAction = recordAction;
        this.id = id;
        this.title = title;
        this.graphics = graphics;
        this.description = description;
        this.date = date;
    }

    public int getIntId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public String getDescription() {
        return description;
    }

    public int getDate() {
        return date;
    }

    public Action getRecordAction() {
        return recordAction;
    }

    @Override
    public String getOpinionTag() {
        return Opinion.getObjectType(this);
    }

    @Override
    public void setOpinions(Opinions opinions) {
        this.opinions = opinions;
    }

    @Override
    public Optional<Opinions> getOpinions() {
        return Optional.ofNullable(opinions);
    }

    @Override
    public String getId() {
        return "" + id;
    }


}
