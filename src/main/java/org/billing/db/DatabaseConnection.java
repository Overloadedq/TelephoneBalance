package org.billing.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnection {

    // Метод возвращает подключение к базе
    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();

        // 1. Загружаем файл db.properties из ресурсов
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new RuntimeException("Файл db.properties не найден в classpath");
            }

            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении db.properties", e);
        }

        // 2. Получаем данные из файла
        String url = props.getProperty("jdbc.url");
        String user = props.getProperty("jdbc.user");
        String password = props.getProperty("jdbc.password");
        String driver = props.getProperty("jdbc.driver");

        // 3. Загружаем драйвер
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер JDBC не найден", e);
        }

        // 4. Создаём и возвращаем соединение
        return DriverManager.getConnection(url, user, password);
    }
}
