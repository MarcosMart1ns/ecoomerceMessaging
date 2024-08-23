package br.com.shop.consumer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class ServiceProvider<T> implements Callable<Void> {

    private final ServiceFactory<T> serviceFactory;

    public ServiceProvider(ServiceFactory<T> serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @Override
    public Void call() throws IOException, SQLException {

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
