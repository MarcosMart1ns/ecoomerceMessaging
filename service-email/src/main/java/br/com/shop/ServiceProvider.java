package br.com.shop;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class ServiceProvider<T> implements Callable<Void> {

    private final ServiceFactory<T> serviceFactory;

    public ServiceProvider(ServiceFactory<T> serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @Override
    public Void call() throws IOException {

        var service = serviceFactory.create();

        try (KafkaService kafkaService = new KafkaService(
                service.getTopic(),
                service::parse,
                service.getConsumerGroup(),
                new HashMap<>())) {

            kafkaService.run();
        }
        return null;
    }
}
