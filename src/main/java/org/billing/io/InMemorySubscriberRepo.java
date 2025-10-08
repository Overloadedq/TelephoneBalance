package org.billing.io;

import org.billing.domain.Subscriber;
import org.billing.repo.SubscriberRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemorySubscriberRepo implements SubscriberRepository {
    private Map<String, Subscriber> subscribers;
    public InMemorySubscriberRepo() {
        subscribers = new HashMap<>();
    }


    @Override
    public Optional<Subscriber> findByPhone(String phoneNumber) {
        return Optional.empty();
    }

    @Override
    public void save(Subscriber subscriber) {

    }

    @Override
    public List<Subscriber> findAll() {
        return List.of();
    }
}
