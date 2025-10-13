package org.billing.pricing.impl;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;
import org.billing.pricing.CallPricing;
import org.billing.pricing.InternetPricing;
import org.billing.pricing.SmsPricing;
import org.billing.pricing.TariffPlan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public class PlanA implements TariffPlan {

    @Override
    public CallPricing getCallPricing() {
        return new CallPricing() {
            @Override
            public BigDecimal calculateCallCost(UsageRecord record, Subscriber sub) {
                String destination = record.getDestination();

                int ratePerMinute;
                if (destination.startsWith("00375-55")) {
                    ratePerMinute = 25; // внутрисеть
                } else if (destination.startsWith("00375-25") ||
                        destination.startsWith("00375-29") ||
                        destination.startsWith("00375-33") ||
                        destination.startsWith("00375-44")) {
                    ratePerMinute = 125; // другие мобильные
                } else if (destination.startsWith("00375")) {
                    ratePerMinute = 95; // стационарные
                } else {
                    ratePerMinute = 2500; // роуминг
                }

                long durationSeconds = Duration.between(record.getStart(), record.getEnd()).getSeconds();
                long units = (long) Math.ceil(durationSeconds / 10.0);

                BigDecimal unitPrice = BigDecimal.valueOf(ratePerMinute)
                        .divide(BigDecimal.valueOf(6), RoundingMode.HALF_UP);

                BigDecimal cost = unitPrice.multiply(BigDecimal.valueOf(units));
                return cost.setScale(2, RoundingMode.HALF_UP);
            }
        };

    }
    @Override
    public SmsPricing getSmsPricing() {

        return new SmsPricing() {
            @Override
            public BigDecimal calculateSmsCost(UsageRecord record, Subscriber sub) {
                String destination = record.getDestination();
                boolean isRoaming = !destination.startsWith("00375");

                int price = isRoaming ? 1500 : 150;
                return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
            }


        };
    }
    @Override
    public InternetPricing getInternetPricing() {

        return new InternetPricing() {
            @Override
            public BigDecimal calculateInternetCost(UsageRecord record, Subscriber sub) {
                long durationSeconds = Duration.between(record.getStart(), record.getEnd()).getSeconds();
                BigDecimal hours = BigDecimal.valueOf(durationSeconds)
                        .divide(BigDecimal.valueOf(3600), 10, RoundingMode.HALF_UP);

                BigDecimal cost = hours.multiply(BigDecimal.valueOf(3000));
                return cost.setScale(2, RoundingMode.HALF_UP);
            }


        };
    }

    @Override
    public String getCode() {
        return "A";
    }


}
