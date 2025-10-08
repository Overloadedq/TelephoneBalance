package org.billing.io;

import org.billing.domain.Subscriber;
import org.billing.repo.SubscriberRepository;

import java.util.*;

public class InMemorySubscriberRepo implements SubscriberRepository {
    private final Map<String, Subscriber> subscribers;
    public InMemorySubscriberRepo() {
        subscribers = new HashMap<>();
    }



    @Override
    public Optional<Subscriber> findByPhone(String phoneNumber) {
        if(subscribers.isEmpty()) {
            return Optional.empty();
        }
        Subscriber subscriber = subscribers.get(phoneNumber);
        return Optional.ofNullable(subscriber);

    }

    @Override
    public void save(Subscriber subscriber) {
        if(subscriber==null) {
            throw new IllegalArgumentException("Subscriber is null");
        }
        String phoneNumber = subscriber.getPhoneNumber();
        if(phoneNumber==null||phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number is null or empty");
        }
        subscribers.put(phoneNumber, subscriber);

    }

    @Override
    public List<Subscriber> findAll() {
        return new ArrayList<>(subscribers.values());
    }
}
