package com.mobium.reference.utils.text;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

/**
 *  on 03.08.15.
 * http://mobiumapps.com/
 */
public class Validator {
    private static final String emailPatternStr = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String namePatternStr = "[a-zA-Z]+[0-9]+";

    private static final Pattern emailPattern = Pattern.compile(emailPatternStr);
    private static final Pattern namePattern = Pattern.compile(namePatternStr);

    public static boolean testEmail(@NonNull String email) {
        return emailPattern.matcher(email).matches();
    }

    public static boolean testName(@NonNull String name) {
        return namePattern.matcher(name).matches();
    }
}
