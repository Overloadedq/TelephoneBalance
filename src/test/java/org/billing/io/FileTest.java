package org.billing.io;

import org.billing.domain.Subscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FileTest {
    private InMemorySubscriberRepo subscriberRepo;
    @BeforeEach
    public void setup() {
        subscriberRepo = new InMemorySubscriberRepo();
    }
    @Test
    public void testFile() throws IOException {
        String resourcePath ="data/subscribers.csv";
        subscriberRepo.loadFromFileCsv(resourcePath);
        List<Subscriber> subscribers =  subscriberRepo.findAll();
        if(subscribers.isEmpty()) {
            System.out.println("Список абонентов пуст");
        }

        System.out.println("Проверка вывода записей");
        for(Subscriber sub : subscribers) {
            System.out.println("Phone: " + sub.getPhoneNumber());
            System.out.println("Balance: " + sub.getBalance());
            System.out.println("TariffPlan: "+sub.getTariffCode());

        }
        Subscriber firstSubscriber = subscribers.get(0);
        assertEquals("00375-55-1234567", firstSubscriber.getPhoneNumber(), "Номер телефона первой записи");
        assertEquals(new BigDecimal("1000.00"), firstSubscriber.getBalance(), "Баланс первой записи");
        assertEquals("A", firstSubscriber.getTariffCode(), "Тариф первой записи");


    }
    @Test
    void testSaveAllToFileCsv_Success() throws IOException {

        Subscriber sub1 = new Subscriber("00375-55-1234567", new BigDecimal("1000.00"), "A", BigDecimal.ZERO);
        Subscriber sub2 = new Subscriber("00375-25-5671238", new BigDecimal("500.00"), "B", BigDecimal.ZERO);
        subscriberRepo.save(sub1);
        subscriberRepo.save(sub2);
        Path tempFile = Paths.get("target/test-output.csv");
        String resourcePath = tempFile.toString();

        // Act
        subscriberRepo.saveAllToFileCsv(resourcePath);

        // Assert
        List<String> lines = Files.readAllLines(tempFile, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            fail("Файл должен содержать данные");
        }

        System.out.println("Содержимое записанного CSV:");
        for (String line : lines) {
            System.out.println(line);
        }

        assertEquals(2, lines.size(), "Должно быть записано 2 строки");
        assertEquals("\"00375-55-1234567\";\"1000.00\";\"A\"", lines.get(0), "Первая строка CSV");
        assertEquals("\"00375-25-5671238\";\"500.00\";\"B\"", lines.get(1), "Вторая строка CSV");
    }






}
