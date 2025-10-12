package org.billing.pricing;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;

import java.math.BigDecimal;

public interface InternetPricing {


    BigDecimal calculateInternetCost(UsageRecord record, Subscriber sub);
}
