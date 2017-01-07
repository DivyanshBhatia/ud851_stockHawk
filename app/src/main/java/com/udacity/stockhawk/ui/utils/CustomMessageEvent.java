package com.udacity.stockhawk.ui.utils;

/**
 * Created by dnbhatia on 1/6/2017.
 */
public class CustomMessageEvent {
    String customSymbol;

    public CustomMessageEvent(String customSymbol){
        this.customSymbol=customSymbol;
    }

    public String getCustomSymbol() {
        return customSymbol;
    }
}
