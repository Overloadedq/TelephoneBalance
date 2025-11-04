package org.billing.service;

import org.billing.db.DatabaseConnection;
import org.billing.domain.Subscriber;
import org.billing.domain.SubscriberDB;
import org.billing.repo.JdbcSubscriberRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class SubscriberService {
    private final JdbcSubscriberRepository subscriberRepository=new JdbcSubscriberRepository();
    public Optional<String> getSubscriberById(int id) {
        try(Connection connection = DatabaseConnection.getConnection())
        {
            connection.setAutoCommit(false);
            Optional<SubscriberDB> subscriber=subscriberRepository.findConId(connection,id);
            connection.commit();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
