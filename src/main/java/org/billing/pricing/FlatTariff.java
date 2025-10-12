package org.billing.pricing;

import java.math.BigDecimal;
import java.util.Calendar;

public class FlatTariff implements TariffPlan {
    private static final String TariffCode = "A";
    private static final BigDecimal FIXED_COST= new BigDecimal("10.00");
    private final CallPricing calPricing=((call, subscriber) -> FIXED_COST);
    private final SmsPricing smsPricing=((sms, subscriber) -> FIXED_COST);
    private final InternetPricing internetPricing=((internet, subscriber) -> FIXED_COST);

    @Override
    public CallPricing getCallPricing() {
        return calPricing;
    }
    @Override
    public SmsPricing getSmsPricing() {
        return smsPricing;
    }
    @Override
    public InternetPricing getInternetPricing() {
        return internetPricing;
    }
    @Override
    public String getCode() {
        return TariffCode;
    }
}
