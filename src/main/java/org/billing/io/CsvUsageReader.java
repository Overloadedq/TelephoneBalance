package org.billing.io;

import org.billing.domain.UsageRecord;
import org.billing.domain.UsageType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvUsageReader implements UsageReader {
    @Override
    public List<UsageRecord> read(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<UsageRecord> result = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                System.out.println("Пропущена пустая строка: " + line);
                continue;
            }

            String[] fields = line.split(";");
            if (fields.length != 6) {
                System.out.println("Пропущена строка с неверным количеством полей: " + line);
                continue;
            }

            try {
                UsageType type = UsageType.valueOf(fields[0].toUpperCase());
                String source = fields[1];
                String destination = fields[2].isEmpty() ? null : fields[2];
                LocalDateTime start = LocalDateTime.parse(fields[3]);
                LocalDateTime end = fields[4].isEmpty() ? (type == UsageType.SMS ? start : null) : LocalDateTime.parse(fields[4]);
                long bytes = Long.parseLong(fields[5]);

                UsageRecord record = new UsageRecord(type, source, destination, start, end, bytes);
                result.add(record);
                System.out.println("Добавлена запись: " + record);
            } catch (IllegalArgumentException | DateTimeParseException e) {
                System.out.println("Ошибка парсинга строки: " + line + ", причина: " + e.getMessage());
                continue;
            }
        }

        return result;
    }
}
