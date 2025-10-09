package org.billing.io;

import org.billing.domain.Subscriber;
import org.billing.domain.UsageRecord;
import org.billing.domain.UsageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    @Test
    void testCsvUsageReader() throws IOException {

        String resourcePath = "data/usage.csv";
        Path path = Paths.get(ClassLoader.getSystemResource(resourcePath).getPath());


        if (!Files.exists(path)) {
            fail("Файл " + resourcePath + " не найден");
        }

        // Выводим содержимое файла
        System.out.println("Содержимое файла usage.csv:");
        List<String> fileLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        fileLines.forEach(line -> System.out.println(line));


        CsvUsageReader reader = new CsvUsageReader();
        List<UsageRecord> records = reader.read(path);




        assertEquals(3, records.size(), "Должно быть 3 записи");

        // Дополнительные проверки полей
        UsageRecord callRecord = records.get(0);
        assertEquals(UsageType.CALL, callRecord.getType(), "Тип первой записи должен быть CALL");
        assertEquals("00375-55-1234567", callRecord.getSource(), "Source первой записи");
        assertEquals("00375-25-7654321", callRecord.getDestination(), "Destination первой записи");
        assertEquals(LocalDateTime.parse("2025-10-01T10:00:00"), callRecord.getStart(), "Start первой записи");
        assertEquals(LocalDateTime.parse("2025-10-01T10:01:25"), callRecord.getEnd(), "End первой записи");
        assertEquals(0, callRecord.getBytes(), "Bytes для CALL должны быть 0");

        UsageRecord smsRecord = records.get(1);
        assertEquals(UsageType.SMS, smsRecord.getType(), "Тип второй записи должен быть SMS");
        assertEquals("00375-55-1234567", smsRecord.getSource(), "Source второй записи");
        assertEquals("00375-44-0000000", smsRecord.getDestination(), "Destination второй записи");
        assertEquals(LocalDateTime.parse("2025-10-01T11:00:00"), smsRecord.getStart(), "Start второй записи");
        assertEquals(LocalDateTime.parse("2025-10-01T11:00:00"), smsRecord.getEnd(), "End второй записи");
        assertEquals(0, smsRecord.getBytes(), "Bytes для SMS должны быть 0");

        UsageRecord internetRecord = records.get(2);
        assertEquals(UsageType.INTERNET, internetRecord.getType(), "Тип третьей записи должен быть INTERNET");
        assertEquals("00375-55-1234567", internetRecord.getSource(), "Source третьей записи");
        assertNull(internetRecord.getDestination(), "Destination для INTERNET должен быть null");
        assertEquals(LocalDateTime.parse("2025-10-01T12:00:00"), internetRecord.getStart(), "Start третьей записи");
        assertEquals(LocalDateTime.parse("2025-10-01T12:20:00"), internetRecord.getEnd(), "End третьей записи");
        assertEquals(125829120, internetRecord.getBytes(), "Bytes для INTERNET должны быть 125829120");
    }





}
