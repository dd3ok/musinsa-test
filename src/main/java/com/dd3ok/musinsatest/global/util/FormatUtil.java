package com.dd3ok.musinsatest.global.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class FormatUtil {

    private FormatUtil() {
    }

    public static String formatPrice(BigDecimal price) {
        if (price == null) {
            return "";
        }
        return NumberFormat.getNumberInstance(Locale.KOREA).format(price.intValue());
    }
}