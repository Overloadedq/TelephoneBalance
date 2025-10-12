package org.billing.pricing;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;

import java.math.BigDecimal;
import java.util.Optional;

public interface SmsPricing {


    BigDecimal calculateSmsCost(UsageRecord record, Subscriber sub);
}
