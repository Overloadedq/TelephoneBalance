package org.billing.service;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;
import org.billing.pricing.TariffFactory;
import org.billing.pricing.TariffPlan;
import org.billing.repo.SubscriberRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
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

        // Открываем одно соединение для всей транзакции
        try (Connection conn = repo.getConnection()) {
            conn.setAutoCommit(false); // начинаем транзакцию

            for (UsageRecord record : records) {
                Optional<Subscriber> subscriberOpt = repo.findByPhone(conn, record.getSource());

                if (subscriberOpt.isEmpty()) {
                    results.add(new BillingResult(record, BigDecimal.ZERO, null, null, false, "Subscriber not found"));
                    continue;
                }

                Subscriber sub = subscriberOpt.get();
                TariffPlan tariff = tariffFactory.getTariff(sub.getTariffCode());

                if (tariff == null) {
                    results.add(new BillingResult(record, BigDecimal.ZERO, sub.getBalance(), sub.getBalance(), false, "Tariff not found"));
                    continue;
                }

                // Расчёт стоимости
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

                // Сохраняем изменения через репозиторий, передаём Connection
                repo.update(conn, sub);

                results.add(new BillingResult(record, cost, oldBalance, sub.getBalance(), true, ""));
            }

            conn.commit(); // фиксируем транзакцию
        } catch (SQLException e) {
            e.printStackTrace();
            // Здесь можно добавить rollback через отдельный try-catch,
            // но try-with-resources закрывает Connection автоматически
        }

        return results;
    }
}
