package org.billing.pricing.impl;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;
import org.billing.domain.UsageType;
import org.billing.pricing.impl.PlanA;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlanATest {

    @Test
    void testCallWithinNetwork() {
        Subscriber sub = new Subscriber("00375-55-1234567", BigDecimal.valueOf(10000), "A",null);
        PlanA plan = new PlanA();

        UsageRecord call = new UsageRecord(
                UsageType.CALL,
                sub.getPhoneNumber(),
                "00375-55-7654321",
                LocalDateTime.of(2025, 10, 13, 10, 0, 0),
                LocalDateTime.of(2025, 10, 13, 10, 1, 25), // 85 секунд
                0
        );

        BigDecimal cost = plan.getCallPricing().calculateCallCost(call, sub);
        assertEquals(new BigDecimal("36.00"), cost);
    }

    @Test
    void testSmsDomestic() {
        Subscriber sub = new Subscriber("00375-55-1234567", BigDecimal.valueOf(10000), "A",null);
        PlanA plan = new PlanA();

        UsageRecord sms = new UsageRecord(
                UsageType.SMS,
                sub.getPhoneNumber(),
                "00375-25-1111111",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0
        );

        BigDecimal cost = plan.getSmsPricing().calculateSmsCost(sms, sub);
        assertEquals(new BigDecimal("150.00"), cost);
    }

    @Test
    void testSmsRoaming() {
        Subscriber sub = new Subscriber("00375-55-1234567", BigDecimal.valueOf(10000), "A",null);
        PlanA plan = new PlanA();

        UsageRecord sms = new UsageRecord(
                UsageType.SMS,
                sub.getPhoneNumber(),
                "007-111-1111111", // роуминг
                LocalDateTime.now(),
                LocalDateTime.now(),
                0
        );

        BigDecimal cost = plan.getSmsPricing().calculateSmsCost(sms, sub);
        assertEquals(new BigDecimal("1500.00"), cost);
    }

    @Test
    void testInternetUsage() {
        Subscriber sub = new Subscriber("00375-55-1234567", BigDecimal.valueOf(10000), "A",null);
        PlanA plan = new PlanA();

        UsageRecord internet = new UsageRecord(
                UsageType.INTERNET,
                sub.getPhoneNumber(),
                null,
                LocalDateTime.of(2025, 10, 13, 12, 0, 0),
                LocalDateTime.of(2025, 10, 13, 13, 30, 0), // 1.5 часа
                0
        );

        BigDecimal cost = plan.getInternetPricing().calculateInternetCost(internet, sub);
        // 1.5 часа * 3000 руб/час = 4500.00
        assertEquals(new BigDecimal("4500.00"), cost);
    }
}
