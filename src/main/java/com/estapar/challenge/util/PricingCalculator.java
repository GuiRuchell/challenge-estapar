package com.estapar.challenge.util;


public class PricingCalculator {

    public static double applyDynamicPricing(double basePrice, int occupied, int capacity) {
        double rate = (double) occupied / capacity;

        if (rate < 0.25) return basePrice * 0.9;
        if (rate <= 0.5) return basePrice;
        if (rate <= 0.75) return basePrice * 1.1;
        return basePrice * 1.25;
    }
}
