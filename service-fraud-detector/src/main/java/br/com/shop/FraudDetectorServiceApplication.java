package br.com.shop;

import br.com.shop.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.HashMap;


public class FraudDetectorServiceApplication {
    public static void main(String[] args) {
        KafkaService kafkaService = new KafkaService(
                "ecommerce.new.order",
                FraudDetectorServiceApplication::parse,
                FraudDetectorServiceApplication.class.getSimpleName(),
                Order.class,
                new HashMap<>());

        kafkaService.run();
    }

    private static void parse(ConsumerRecord<String, Order> record) {
        System.out.println("Processando novo evento");
        System.out.println(record.value());
        System.out.println(record.offset());
    }
}
