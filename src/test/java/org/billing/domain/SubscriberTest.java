package org.billing.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubscriberTest {
    private Subscriber subscriber;

    @BeforeEach
    void setUp() {
        subscriber = new Subscriber("375336684628",
                new BigDecimal("100.00"),
                "Premium",
                new BigDecimal("20.00"));
    }

    @Test
    void testDebit() {
        subscriber.debit(new BigDecimal("20.00"));
        assertEquals(new BigDecimal("80.00"), subscriber.getBalance(), "Баланс должен уменьшиться на 20");

        // Проверка rounding
        subscriber.debit(new BigDecimal("0.555"));
        assertEquals(new BigDecimal("79.45"), subscriber.getBalance(), "RoundingMode.HALF_UP должно округлить 0.555 до 0.55");
    }
}