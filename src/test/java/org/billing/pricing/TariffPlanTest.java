package org.billing.pricing;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;
import org.billing.domain.UsageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TariffPlanTest {
    private UsageRecord call;
    private UsageRecord sms;
    private UsageRecord internet;
    private Subscriber subscriber;
    private TariffPlan tariffPlan;
    private TariffFactory tariffFactory;
    @BeforeEach
    public void setUp() {
        call = new UsageRecord(UsageType.CALL,
                        "00375-25-1234567",
                     "00375-33-4444444",
                                LocalDateTime.parse("2025-10-01T10:00:01"),
                                LocalDateTime.parse("2025-10-01T10:01:30"),
                                0);
        sms = new UsageRecord(UsageType.SMS,
                      "00375-25-1234567",
                   "00375-44-0000000",
                              LocalDateTime.parse("2025-10-01T10:20:01"),
                              LocalDateTime.parse("2025-10-01T10:20:20"),
                             0);
        internet = new UsageRecord(UsageType.INTERNET,
                           "00375-25-1234567",
                        null,
                                  LocalDateTime.parse("2025-10-01T10:25:01"),
                                  LocalDateTime.parse("2025-10-01T10:25:51"),
                            1234567);
        subscriber = new Subscriber("00375-25-1234567", new BigDecimal("1000.00"),"A",BigDecimal.ZERO);
        tariffPlan=new FlatTariff();
        tariffFactory=new SimpleTariffFactory();
    }

    @Test
    @DisplayName("Тестирование цены звонка")

    void testCallPricing(){
        System.out.println("Цена звонка");
        BigDecimal cost = tariffPlan.getCallPricing().calculateCallCost(call,subscriber);
        System.out.println(cost);
        assertEquals(new BigDecimal("10.00"), cost,"Цена звонка должна быть 10 рублей");

    }
    @Test
    @DisplayName("Тестирование цены смс")
    void testSmsPricing(){
        System.out.println("Цена смс");
        BigDecimal cost = tariffPlan.getSmsPricing().calculateSmsCost(sms, subscriber);
        System.out.println(cost);
        assertEquals(new BigDecimal("10.00"),cost,"Цена смс должна быть 10 рублей");
    }
    @Test
    @DisplayName("Тестирование цены интернета")
    void testInternetPricing(){
        System.out.println("Цена интернета");
        BigDecimal cost = tariffPlan.getSmsPricing().calculateSmsCost(internet, subscriber);
        System.out.println(cost);
        assertEquals(new BigDecimal("10.00"),cost,"Цена интернета должна быть 10 рублей");
    }
    @Test
    @DisplayName("Тестирование тарифного кода")
    void testTariffCode(){
        assertEquals("A",tariffPlan.getCode(),"Код тарифа должен быть A");
    }

}
