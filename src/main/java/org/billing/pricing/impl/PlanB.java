package org.billing.pricing.impl;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;
import org.billing.pricing.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public class PlanB implements TariffPlan {

    @Override
    public CallPricing getCallPricing() {
        return (record, sub) -> {
            String destination = record.getDestination();
            long durationSeconds = Duration.between(record.getStart(), record.getEnd()).getSeconds();

            int firstMinuteRate;
            int nextMinuteRate;
            boolean isRoaming = !destination.startsWith("00375");

            if (isRoaming) {
                long minutes = (long) Math.ceil(durationSeconds / 60.0);
                return BigDecimal.valueOf(5000L * minutes).setScale(2, RoundingMode.HALF_UP);
            }

            // Внутрисеть
            if (destination.startsWith("00375-55")) {
                firstMinuteRate = 5;
                nextMinuteRate = 50;
            }
            // Другие мобильные
            else if (destination.startsWith("00375-25") ||
                    destination.startsWith("00375-29") ||
                    destination.startsWith("00375-33") ||
                    destination.startsWith("00375-44")) {
                firstMinuteRate = 75;
                nextMinuteRate = 500;
            }
            // Стационар
            else {
                firstMinuteRate = 50;
                nextMinuteRate = 250;
            }

            long minutes = (long) Math.ceil(durationSeconds / 60.0);
            if (minutes <= 1) {
                return BigDecimal.valueOf(firstMinuteRate);
            } else {
                return BigDecimal.valueOf(firstMinuteRate + (minutes - 1) * nextMinuteRate);
            }
        };
    }

    @Override
    public SmsPricing getSmsPricing() {
        return (record, sub) -> {
            boolean isRoaming = !record.getDestination().startsWith("00375");
            int price = isRoaming ? 1000 : 150;
            return BigDecimal.valueOf(price);
        };
    }

    @Override
    public InternetPricing getInternetPricing() {
        return (record, sub) -> {
            long usedMb = record.getBytes();
            if (usedMb <= 50) {
                return BigDecimal.valueOf(usedMb * 1000L);
            } else {
                long extra = usedMb - 50;
                return BigDecimal.valueOf(50 * 1000L + extra * 1250L);
            }
        };
    }

    @Override
    public String getCode() {
        return "B";
    }
}
