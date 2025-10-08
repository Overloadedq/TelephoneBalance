package org.billing.io;

import org.billing.domain.Subscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class FileTest {
    private InMemorySubscriberRepo subscriberRepo;
    @BeforeEach
    public void setup() {
        subscriberRepo = new InMemorySubscriberRepo();
    }
    @Test
    public void testFile() throws IOException {
        String resourcePath ="data/subscribers.csv";
        subscriberRepo.loadFromFileCsv(resourcePath);
        List<Subscriber> subscribers =  subscriberRepo.findAll();
        if(subscribers.isEmpty()) {
            System.out.println("Список абонентов пуст");
        }

        System.out.println("Проверка вывода записей");
        for(Subscriber sub : subscribers) {
            System.out.println("Phone: " + sub.getPhoneNumber());
            System.out.println("Balance: " + sub.getBalance());
            System.out.println("TariffPlan: "+sub.getTariffCode());

        }

    }
}
