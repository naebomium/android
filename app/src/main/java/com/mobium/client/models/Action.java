package com.mobium.client.models;

import com.annimon.stream.function.FunctionalInterface;

import java.io.Serializable;

/**
 *
 *
 * Date: 14.12.12
 * Time: 2:17
 */
public class Action implements Serializable {
    private ActionType type;
    private String actionData;
    private String actionTitle;
    private String actionId;

    public Action(String actionName, String actionData) {
        this(actionName, actionData, null);
    }

    public Action(String actionName, String actionData, String actionTitle) {
        this.type = ActionType.getActionType(actionName);
        this.actionData = actionData;
        this.actionTitle = actionTitle;
    }

    public Action(ActionType type, String actionData) {
        this(type, actionData, null);
    }

    public Action(ActionType type) {
        this(type, null, null);
    }

    public Action(ActionType type, String actionData, String actionTitle) {
        this.type = type;
        this.actionData = actionData;
        this.actionTitle = actionTitle;
    }

    public ActionType getType() {
        return this.type;
    }

    public String getActionName() {
        return type.getName();
    }

    public String getActionData() {
        return actionData;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public void setActionTitle(String actionTitle) {
        this.actionTitle = actionTitle;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @FunctionalInterface
    public interface ActionHandler {
        void doAction(Action action, boolean animated);
    }

    @Override
    public String toString() {
        return "Action{" +
                "type=" + type +
                ", actionData='" + actionData + '\'' +
                ", actionTitle='" + actionTitle + '\'' +
                ", actionId='" + actionId + '\'' +
                '}';
    }
}
