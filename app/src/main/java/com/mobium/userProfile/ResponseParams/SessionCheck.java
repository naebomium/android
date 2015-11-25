package com.mobium.userProfile.ResponseParams;

/**
 *  on 11.06.15.
 * http://mobiumapps.com/
 */
public class SessionCheck {
    public String errorMessage;
    public OpenPage openPage;

    public static final class OpenPage {
        public OpenPage(Type type, String id) {
            this.type = type;
            this.id = id;
        }

        public enum Type {
            category, offer
        }
        public final Type type;
        public final String id;
    }
}
