package org.billing.db;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        // Попробуем получить соединение через DatabaseConnection
        try (Connection conn = DatabaseConnection.getConnection()) {

            // Проверяем, что соединение реально активно
            if (conn != null && conn.isValid(2)) { // таймаут 2 секунды
                System.out.println("Connection OK!");
            } else {
                System.out.println("Connection FAILED!");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе:");
            e.printStackTrace();
        } catch (RuntimeException e) {
            // ловим ошибки, если DatabaseConnection бросил исключение
            System.out.println("Ошибка в DatabaseConnection:");
            e.printStackTrace();
        }
    }
}
