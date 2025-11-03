package org.billing.db;

import org.billing.domain.SubscriberDB;
import org.billing.repo.JdbcSubscriberRepository;
import org.billing.repo.SubRepoBD;
import org.billing.repo.SubscriberRepository;
import org.billing.domain.Subscriber;

import java.util.List;

public class TestSubRepoDB {
    public static void main(String[] args) {
        SubRepoBD repo = new JdbcSubscriberRepository();

        List<SubscriberDB> all = repo.findAll();
        all.forEach(s -> System.out.println(s.getUsername() + " | " + s.getEmail()));

        // Проверка поиска по ID
        repo.findById(1).ifPresent(s -> System.out.println("Found by ID: " + s.getUsername()));

        // Проверка поиска по username
        repo.findByUsername("ivan_petrov").ifPresent(s -> System.out.println("Found by username: " + s.getEmail()));
    }
}
