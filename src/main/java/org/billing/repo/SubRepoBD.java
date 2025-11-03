package org.billing.repo;

import org.billing.domain.SubscriberDB;

import java.util.List;
import java.util.Optional;

public interface SubRepoBD {
    Optional<SubscriberDB> findByPhone(String phoneNumber);

    void save(SubscriberDB subscriber);

    List<SubscriberDB> findAll();

    Optional<SubscriberDB> findById(int id);    // Найти по ID
    Optional<SubscriberDB> findByUsername(String username); // Найти по username
    void create(SubscriberDB subscriber);       // Добавить нового абонента
    void update(SubscriberDB subscriber);       // Обновить данные
    void delete(int id);
}
