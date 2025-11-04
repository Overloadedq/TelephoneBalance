package org.billing.io;

import org.billing.domain.Subscriber;
import org.billing.repo.SubscriberRepository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.*;

public class InMemorySubscriberRepo implements SubscriberRepository {
    private final Map<String, Subscriber> subscribers;


    @Override
    public void update(Connection conn, Subscriber subscriber) {
        // Для in-memory просто обновляем объект в списке
        subscribers.replace(subscriber.getPhoneNumber(), subscriber);
    }
    public InMemorySubscriberRepo() {
        subscribers = new LinkedHashMap<>();

    }



    @Override
    public Optional<Subscriber> findByPhone(Connection conn, String phoneNumber) {
        if(subscribers.isEmpty()) {
            return Optional.empty();
        }
        Subscriber subscriber = subscribers.get(phoneNumber);
        return Optional.ofNullable(subscriber);

    }

    @Override
    public void save(Subscriber subscriber) {
        if(subscriber==null) {
            throw new IllegalArgumentException("Subscriber is null");
        }
        String phoneNumber = subscriber.getPhoneNumber();
        if(phoneNumber==null||phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number is null or empty");
        }
        subscribers.put(phoneNumber, subscriber);

    }

    @Override
    public List<Subscriber> findAll() {
        return new ArrayList<>(subscribers.values());
    }

    @Override
    public void create(Connection conn, Subscriber subscriber) {

    }

    void loadFromFileCsv(String resourcePath) throws IOException {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("Resource path is null or empty");
        }
        if (!resourcePath.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Resource path is not a CSV file");
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        try (br) {
           br.lines()
                   .filter(line -> !line.isEmpty())
                   .forEach(line -> {
                       try{
                           String[] parts = line.split(";",-1);
                           if(parts.length!=3) {
                               System.err.println("Некорректная строка"+line);
                               return;
                           }
                           String phoneNumber = parts[0].replace("\"", "").trim();
                           String balanceStr = parts[1].replace("\"", "").trim();
                           String tariffCode = parts[2].replace("\"", "").trim();

                           // Валидация phoneNumber
                           if (phoneNumber.isEmpty()) {
                               System.err.println("Пропущен номер телефона в строке: " + line);
                               return;
                           }

                           // Преобразование balance в BigDecimal
                           BigDecimal balance;
                           try {
                               balance = new BigDecimal(balanceStr);
                           } catch (NumberFormatException e) {
                               System.err.println("Некорректный формат balance в строке: " + line);
                               return;
                           }
                           Subscriber subscriber = new Subscriber(phoneNumber,balance,tariffCode,BigDecimal.ZERO);
                           save(subscriber);
                       }
                       catch (IllegalArgumentException e) {
                           System.err.println("Ошибка обработки строки CSV: " + line + "; Причина: " + e.getMessage());
                           e.printStackTrace();
                       }
                   });
       }
    }
    @Override
    public Connection getConnection() {
        return null; // для in-memory репозитория, в тестах Connection не нужен
    }


    void saveAllToFileCsv(String resourcePath) throws IOException {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("Resource path is null or empty");
        }
        if (!resourcePath.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Resource path is not a CSV file");
        }
        Path resPath = Path.of(resourcePath);
        try(BufferedWriter bw = Files.newBufferedWriter(resPath,StandardCharsets.UTF_8))
        {
            for(Subscriber subscriber : subscribers.values()) {
                String phoneNumber = subscriber.getPhoneNumber().replace("\"", "");
                String balanceStr = subscriber.getBalance().toString().replace("\"", "");
                String tariffCode = subscriber.getTariffCode().replace("\"", "");
                String csvLine = "\"" + phoneNumber + "\";\"" + balanceStr + "\";\"" + tariffCode + "\"";
                bw.write(csvLine);
                bw.newLine();

            }


        }
    }

}
