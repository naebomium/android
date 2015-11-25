package com.mobium.config.common;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.mobium.config.common.Config;
import com.mobium.reference.utils.text.MoneyFormatter;

/**
 *  on 06.10.15.
 */
public class ConfigUtils {
    public static @NonNull String formatCurrency(@IntRange(from = 0) int count) {
        return String.format(Config.get().getApplicationData().getCurrencyPlaceholder(), MoneyFormatter.formatRubles(count));
    }
}
