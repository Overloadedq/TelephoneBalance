package org.billing.pricing.impl;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;
import org.billing.pricing.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public class PlanD implements TariffPlan {

    @Override
    public CallPricing getCallPricing() {
        return (record, sub) -> {
            String destination = record.getDestination();
            long durationSeconds = Duration.between(record.getStart(), record.getEnd()).getSeconds();

            boolean isRoaming = !destination.startsWith("00375");
            if (isRoaming) {
                long units = (long) Math.ceil(durationSeconds / 10.0);
                BigDecimal unitPrice = BigDecimal.valueOf(500)
                        .divide(BigDecimal.valueOf(6), RoundingMode.HALF_UP);
                return unitPrice.multiply(BigDecimal.valueOf(units))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            long minutes = (long) Math.ceil(durationSeconds / 60.0);
            if (minutes <= 1) {
                return BigDecimal.valueOf(500);
            } else {
                return BigDecimal.valueOf(500 + (minutes - 1) * 100);
            }
        };
    }

    @Override
    public SmsPricing getSmsPricing() {
        return (record, sub) -> {
            boolean isRoaming = !record.getDestination().startsWith("00375");
            int price = isRoaming ? 5000 : 150;
            return BigDecimal.valueOf(price);
        };
    }

    @Override
    public InternetPricing getInternetPricing() {
        return (record, sub) -> {
            long usedMb = record.getBytes();
            if (usedMb <= 100) {
                return BigDecimal.valueOf(usedMb * 500L);
            } else if (usedMb <= 500) {
                long firstPart = 100 * 500L;
                long extra = usedMb - 100;
                return BigDecimal.valueOf(firstPart + extra * 1000L);
            } else {
                long firstPart = 100 * 500L;
                long secondPart = 400 * 1000L;
                long extra = usedMb - 500;
                return BigDecimal.valueOf(firstPart + secondPart + extra * 5000L);
            }
        };
    }

    @Override
    public String getCode() {
        return "D";
    }
}
