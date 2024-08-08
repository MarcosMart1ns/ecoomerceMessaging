package br.com.shop;

import java.io.IOException;
import java.util.HashMap;

public class ServiceProvider {

    public <T> void run(ServiceFactory<T> factory) throws IOException {

        var service = factory.create();

        try (KafkaService kafkaService = new KafkaService(
                service.getTopic(),
                service::parse,
                service.getConsumerGroup(),
                new HashMap<>())) {

            kafkaService.run();
        }
    }
}
