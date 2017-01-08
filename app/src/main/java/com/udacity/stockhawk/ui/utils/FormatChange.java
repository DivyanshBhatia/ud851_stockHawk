package com.udacity.stockhawk.ui.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by dnbhatia on 1/8/2017.
 */
public class FormatChange {
    private static FormatChange ourInstance = new FormatChange();

    public static FormatChange getInstance() {
        return ourInstance;
    }

    private FormatChange() {
    }
    public static String convertToDecimalFormat(float change, DecimalFormatType format){
        DecimalFormat currencyFormat = null;

        switch(format){
            case DOLLAR_FORMAT:
                currencyFormat=(DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                break;
            case DOLLAR_FORMAT_PLUS:
                currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                currencyFormat.setPositivePrefix("+$");
                break;
            case PERCENTAGE_FORMAT:
                currencyFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
                currencyFormat.setMaximumFractionDigits(2);
                currencyFormat.setMinimumFractionDigits(2);
                currencyFormat.setPositivePrefix("+");
                break;
            default:
                throw new IllegalArgumentException("Given Format is not supported");

            }
        return currencyFormat.format(change);
    }
}
