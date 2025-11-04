package org.billing.repo;

import org.billing.domain.Subscriber;
import org.billing.domain.SubscriberDB;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriberRepository {
    Optional<Subscriber> findByPhone(String phoneNumber);
    void save(Subscriber subscriber);
    List<Subscriber> findAll();

    void create(Connection conn, Subscriber subscriber);
    void update(Connection conn, Subscriber subscriber);
}
