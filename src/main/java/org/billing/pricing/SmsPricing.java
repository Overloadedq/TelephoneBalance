package org.billing.pricing;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;

import java.math.BigDecimal;

public interface SmsPricing {
    BigDecimal calculateSmsPricing(UsageRecord sms, Subscriber subscriber);
}
