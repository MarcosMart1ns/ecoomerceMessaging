package br.com.shop;

import br.com.shop.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class FraudDetectorServiceApplication {

    private static final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) {
        KafkaService<Order> kafkaService = new KafkaService(
                "ecommerce.new.order",
                FraudDetectorServiceApplication::parse,
                FraudDetectorServiceApplication.class.getSimpleName(),
                Order.class,
                new HashMap<>());

        kafkaService.run();
    }



    private static void parse(ConsumerRecord<String, Order> record) {
        System.out.println("Processando novo evento");

        try {
            Thread.sleep(5000);
            if (isFraud(record.value())) {
                //pretending fraud happens when the ammout is above 4500
                System.out.println("FRAUD DETECTED");
                orderDispatcher.send("ecommerce.order.rejected",record.value().getUserId(),record.value());
            }else {
                System.out.println("ORDER APROVED:"+ record.value());
                orderDispatcher.send("ecommerce.order.aproved",record.value().getUserId(),record.value());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        System.out.println(record.value());
        System.out.println(record.offset());
    }

    private static boolean isFraud(Order order) {
        return order.getAmmount().compareTo(new BigDecimal("4500")) >= 0;
    }
}
