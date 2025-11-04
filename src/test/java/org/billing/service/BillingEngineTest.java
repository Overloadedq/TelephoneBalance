package org.billing.service;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;
import org.billing.domain.UsageType;

import org.billing.pricing.SimpleTariffFactory;
import org.billing.pricing.TariffFactory;
import org.billing.repo.SubscriberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BillingEngineTest {
    private BillingEngine billingService;
    private InMemorySubscriberRepo repo;
    private TariffFactory tariffFactory;
    private Subscriber subscriber;
    private List<UsageRecord> records;

    static class InMemorySubscriberRepo implements SubscriberRepository {
        private Subscriber subscriber;

        void setSubscriber(Subscriber subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public Optional<Subscriber> findByPhone(Connection conn, String phoneNumber) {
            if (subscriber != null && subscriber.getPhoneNumber().equals(phoneNumber)) {
                return Optional.of(subscriber);
            }
            return Optional.empty();
        }

        @Override
        public void save(Subscriber subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public List<Subscriber> findAll() {
            return List.of();
        }

        @Override
        public void create(Connection conn, Subscriber subscriber) {

        }

        @Override
        public void update(Connection conn, Subscriber subscriber) {

        }

        @Override
        public Connection getConnection() {
            return null;
        }
    }

    @BeforeEach
    void setUp() {
        // Создаём заглушку для Subscriber
        subscriber = new Subscriber("00375-55-1234567", new BigDecimal("1000.00"), "A", BigDecimal.ZERO);

        // Создаём InMemorySubscriberRepo с одним абонентом
        repo = new InMemorySubscriberRepo();
        repo.setSubscriber(subscriber);

        // Создаём SimpleTariffFactory
        tariffFactory = new SimpleTariffFactory();

        // Инициализируем BillingEngine
        billingService = new BillingEngine(repo, tariffFactory);

        // Создаём 3 UsageRecord (CALL, SMS, INTERNET)
        records = new ArrayList<>();
        records.add(new UsageRecord(UsageType.CALL, "00375-55-1234567", "00375-25-7654321",
                LocalDateTime.parse("2025-10-01T10:00:00"), LocalDateTime.parse("2025-10-01T10:01:25"), 0));
        records.add(new UsageRecord(UsageType.SMS, "00375-55-1234567", "00375-44-0000000",
                LocalDateTime.parse("2025-10-01T11:00:00"), LocalDateTime.parse("2025-10-01T11:00:00"), 0));
        records.add(new UsageRecord(UsageType.INTERNET, "00375-55-1234567", null,
                LocalDateTime.parse("2025-10-01T12:00:00"), LocalDateTime.parse("2025-10-01T12:20:00"), 125829120));
    }

    @Test
    @DisplayName("Успешная обработка трех записей")
    void testSuccessfulProcessing() {
        List<BillingResult> results = billingService.processAll(records);

        // Проверяем, что возвращено 3 результата
        assertEquals(3, results.size(), "Должно быть 3 результата");

        // Проверяем каждый результат
        BigDecimal expectedBalance = new BigDecimal("1000.00");
        for (int i = 0; i < results.size(); i++) {
            BillingResult result = results.get(i);
            assertTrue(result.getSuccess(), "Операция должна быть успешной");
            assertEquals(new BigDecimal("10.00"), result.getCost(), "Стоимость должна быть 10.00");
            assertEquals(expectedBalance, result.getOldBalance(), "Старый баланс должен быть корректным");
            expectedBalance = expectedBalance.subtract(new BigDecimal("10.00"));
            assertEquals(expectedBalance, result.getNewBalance(), "Новый баланс должен быть корректным");
            assertEquals("", result.getNote(), "Примечание должно быть пустым");
            assertEquals(records.get(i), result.getRecord(), "Запись должна соответствовать исходной");
        }

        // Проверяем итоговый баланс абонента
        assertEquals(new BigDecimal("970.00"), subscriber.getBalance(), "Итоговый баланс должен быть 970.00");
    }

    @Test
    @DisplayName("Абонент не найден")
    void testSubscriberNotFound() {
        UsageRecord unknownRecord = new UsageRecord(UsageType.CALL, "99999-99-9999999", "00375-25-7654321",
                LocalDateTime.parse("2025-10-01T10:00:00"), LocalDateTime.parse("2025-10-01T10:01:25"), 0);
        List<UsageRecord> singleRecord = List.of(unknownRecord);

        List<BillingResult> results = billingService.processAll(singleRecord);

        assertEquals(1, results.size(), "Должен быть 1 результат");
        BillingResult result = results.get(0);
        assertFalse(result.getSuccess(), "Операция должна быть неуспешной");
        assertEquals(BigDecimal.ZERO, result.getCost(), "Стоимость должна быть 0");
        assertNull(result.getOldBalance(), "Старый баланс должен быть null");
        assertNull(result.getNewBalance(), "Новый баланс должен быть null");
        assertEquals("Subscriber not found", result.getNote(), "Примечание должно быть 'Subscriber not found'");
        assertEquals(unknownRecord, result.getRecord(), "Запись должна соответствовать исходной");
    }

    @Test
    @DisplayName("Тариф не найден")
    void testTariffNotFound() {
        // Создаём абонента с неизвестным тарифом
        Subscriber unknownTariffSubscriber = new Subscriber("00375-55-1234567", new BigDecimal("1000.00"), "unknown", BigDecimal.ZERO);
        repo.setSubscriber(unknownTariffSubscriber);

        UsageRecord record = new UsageRecord(UsageType.CALL, "00375-55-1234567", "00375-25-7654321",
                LocalDateTime.parse("2025-10-01T10:00:00"), LocalDateTime.parse("2025-10-01T10:01:25"), 0);
        List<UsageRecord> singleRecord = List.of(record);

        List<BillingResult> results = billingService.processAll(singleRecord);

        assertEquals(1, results.size(), "Должен быть 1 результат");
        BillingResult result = results.get(0);
        assertFalse(result.getSuccess(), "Операция должна быть неуспешной");
        assertEquals(BigDecimal.ZERO, result.getCost(), "Стоимость должна быть 0");
        assertEquals(new BigDecimal("1000.00"), result.getOldBalance(), "Старый баланс должен быть 1000.00");
        assertEquals(new BigDecimal("1000.00"), result.getNewBalance(), "Новый баланс должен быть 1000.00");
        assertEquals("Tariff not found", result.getNote(), "Примечание должно быть 'Tariff not found'");
        assertEquals(record, result.getRecord(), "Запись должна соответствовать исходной");
    }

    @Test
    @DisplayName("Пустой список записей")
    void testEmptyRecordsList() {
        List<BillingResult> results = billingService.processAll(new ArrayList<>());
        assertTrue(results.isEmpty(), "Список результатов должен быть пустым");
    }
}