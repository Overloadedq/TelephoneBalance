package org.billing.pricing;

public class SimpleTariffFactory implements TariffFactory {
    @Override
    public TariffPlan getTariff(String tariffCode) {
        if ("A".equals(tariffCode)) {
            return new FlatTariff();
        }
        return null;
    }
}