package com.mobium.reference.anotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *  on 11.08.15.
 */

@Inherited
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface NeedInternetAccess {

}
