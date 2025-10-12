package org.billing.pricing;

public interface TariffFactory {
    default TariffPlan getTariffPlan(String tariffCode){
        return getTariffPlan(String.valueOf(tariffCode));
    }

    TariffPlan getTariff(String tariffCode);
}
