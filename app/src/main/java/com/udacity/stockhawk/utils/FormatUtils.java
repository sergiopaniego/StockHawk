package com.udacity.stockhawk.utils;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sergiopaniegoblanco on 09/04/2017.
 */

public class FormatUtils {
    private static final DecimalFormat DOLLAR_FORMAT = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
    private static final DecimalFormat DOLLAR_FORMAT_WITH_PLUS = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
    private static final DecimalFormat PERCENTAGE_FORMAT = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
    private static final String HIST_LINE_SEP = "\n";
    private static final String HIST_DATA_SEP = ", ";

    static {
        DOLLAR_FORMAT_WITH_PLUS.setPositivePrefix("+$");
        PERCENTAGE_FORMAT.setMaximumFractionDigits(2);
        PERCENTAGE_FORMAT.setMinimumFractionDigits(2);
        PERCENTAGE_FORMAT.setPositivePrefix("+");
    }


    public static String formatPrice(float price) {
        return format(price, DOLLAR_FORMAT);
    }

    public static String formatPriceWithSign(float price) {
        return format(price, DOLLAR_FORMAT_WITH_PLUS);
    }

    public static String formatPercentage(float value) {
        return format(value/100, PERCENTAGE_FORMAT);
    }

    private static String format(Object value, Format format) {
        return format.format(value);
    }
}
