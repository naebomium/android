package com.mobium.config.common;

import java.io.IOException;

/**
 *  on 02.11.15.
 */
public interface Provider<T> {
    T get() throws DataExchangeException;
}
