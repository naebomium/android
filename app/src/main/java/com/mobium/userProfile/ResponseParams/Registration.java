package com.mobium.userProfile.ResponseParams;

import java.util.List;

/**
 *  on 11.06.15.
 * http://mobiumapps.com/
 */
public class Registration {
    public List<FieldError> errors;
    public String accessToken;
    public String confirmToken;
    public String errorMessage;


    public static class FieldError {
        public final String fieldId;
        public final String errorMessage;

        public FieldError(String fieldId, String errorMessage) {
            this.fieldId = fieldId;
            this.errorMessage = errorMessage;
        }

        public static String format(List<FieldError> list) {
            StringBuilder builder = new StringBuilder(list.size());
            for (FieldError error : list)
                builder.append(error.errorMessage).append("\n");
            return builder.toString();
        }
    }




}
