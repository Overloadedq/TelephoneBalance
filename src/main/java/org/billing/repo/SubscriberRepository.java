package org.billing.repo;

import org.billing.domain.Subscriber;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface SubscriberRepository {
    Optional<Subscriber> findByPhone(Connection conn, String phoneNumber);
    void save(Subscriber subscriber);
    List<Subscriber> findAll();

    void create(Connection conn, Subscriber subscriber);
    void update(Connection conn, Subscriber subscriber);

    Connection getConnection();
}
