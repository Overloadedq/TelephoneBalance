package org.billing.pricing;

public interface TariffPlan {
    CallPricing getCallPricing();
    SmsPricing getSmsPricing();
    InternetPricing getInternetPricing();
    String getCode();
}
