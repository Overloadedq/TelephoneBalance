package org.billing.repo;

import org.billing.domain.Subscriber;
import org.billing.domain.SubscriberDB;

import java.util.List;
import java.util.Optional;

public interface SubscriberRepository {
    Optional<Subscriber> findByPhone(String phoneNumber);
    void save(Subscriber subscriber);
    List<Subscriber> findAll();

}
