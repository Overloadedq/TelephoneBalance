
package org.billing.service;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;
import org.billing.pricing.TariffFactory;
import org.billing.pricing.TariffPlan;
import org.billing.repo.SubscriberRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillingEngine {
    private final SubscriberRepository repo;
    private final TariffFactory tariffFactory;

    public BillingEngine(SubscriberRepository repo, TariffFactory tariffFactory) {
        this.repo = repo;
        this.tariffFactory = tariffFactory;
    }

    public List<BillingResult> processAll(List<UsageRecord> records) {
        List<BillingResult> results = new ArrayList<>();
        for (UsageRecord record : records) {
            Optional<Subscriber> subscriber = repo.findByPhone(record.getSource());
            if (subscriber.isEmpty()) {
                results.add(new BillingResult(record, BigDecimal.ZERO, null, null, false, "Subscriber not found"));
                continue;
            }
            Subscriber sub = subscriber.get();
            TariffPlan tariff = tariffFactory.getTariff(sub.getTariffCode());
            if (tariff == null) {
                results.add(new BillingResult(record, BigDecimal.ZERO, sub.getBalance(), sub.getBalance(), false, "Tariff not found"));
                continue;
            }
            BigDecimal cost;
            switch (record.getType()) {
                case CALL:
                    cost = tariff.getCallPricing().calculateCallCost(record, sub);
                    break;
                case SMS:
                    cost = tariff.getSmsPricing().calculateSmsCost(record, sub);
                    break;
                case INTERNET:
                    cost = tariff.getInternetPricing().calculateInternetCost(record, sub);
                    break;
                default:
                    throw new IllegalStateException("Unknown usage type: " + record.getType());
            }
            BigDecimal oldBalance = sub.getBalance();
            sub.debit(cost);
            // repo.save(sub); // Раскомментировать для файлового репозитория
            results.add(new BillingResult(record, cost, oldBalance, sub.getBalance(), true, ""));
        }
        return results;
    }
}
