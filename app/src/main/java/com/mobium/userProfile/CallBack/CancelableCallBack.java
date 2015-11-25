package com.mobium.userProfile.CallBack;

import retrofit.Callback;
import retrofit.client.Response;

/**
 *  on 10.06.15.
 * http://mobiumapps.com/
 * <p>
 * блокирумый callback: в случае cancel = true обработка результата не произойдет
 * устанвока тега для поиска в списках и т.п.
 */
public abstract class CancelableCallBack<T> implements Callback<T> {
    public boolean cancel = false;
    private Object tag;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public void success(T t, Response response) {
        if (cancel)
            return;
    }

    public void cancel() {
        cancel = true;
    }
}
