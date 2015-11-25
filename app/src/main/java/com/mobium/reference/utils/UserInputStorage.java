package com.mobium.reference.utils;

import android.content.Context;

import com.annimon.stream.Optional;
import com.mobium.reference.utils.persistence.ContextPersistence;

/**
 *  on 12.10.15.
 */
public final class UserInputStorage extends ContextPersistence {
    private String email;
    private String phone;
    private String name;

    public UserInputStorage(Context context) {
        super(context);
        tryLoad();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getPhone() {
        return Optional.ofNullable(phone);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }
}
