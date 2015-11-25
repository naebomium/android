package com.mobium.config.common;

/**
 *  on 02.11.15.
 */
public interface UpdatesProvider<T>{
    T get(int limit, int offset) throws DataExchangeException;
}
