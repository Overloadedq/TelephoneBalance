package org.billing.service;

import org.billing.db.DatabaseConnection;
import org.billing.domain.Subscriber;
import org.billing.domain.SubscriberDB;
import org.billing.repo.JdbcSubscriberRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SubscriberService {

    private final JdbcSubscriberRepository subscriberRepository;

    // Репозиторий передаём через конструктор
    public SubscriberService(JdbcSubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }
    public List<SubscriberDB> findAll() {
        return subscriberRepository.findAll(); // В JdbcSubscriberRepository должен быть findAll()
    }


    // Метод возвращает Optional<SubscriberDB> по id
    public Optional<SubscriberDB> findById(int id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            Optional<SubscriberDB> subscriberDB = subscriberRepository.findConId(connection, id);

            connection.commit();

            return subscriberDB;


        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
