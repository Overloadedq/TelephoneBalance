package org.billing.pricing;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;

import java.math.BigDecimal;
import java.util.Optional;

public interface CallPricing {


    BigDecimal calculateCallCost(UsageRecord record, Subscriber sub);
}

