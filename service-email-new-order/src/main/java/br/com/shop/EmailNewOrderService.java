package br.com.shop;

import br.com.shop.consumer.KafkaService;
import br.com.shop.domain.CorrelationId;
import br.com.shop.domain.Message;
import br.com.shop.domain.Order;
import br.com.shop.producer.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class EmailNewOrderService {

    public static void main(String[] args) {
        KafkaService<Order> kafkaService = new KafkaService(
                "ecommerce.new.order",
                EmailNewOrderService::parse,
                EmailNewOrderService.class.getSimpleName(),
                new HashMap<>());

        kafkaService.run();
    }

    private static void parse(ConsumerRecord<String, Message<Order>> record) {
        System.out.println("Preparando Email");


        try (KafkaDispatcher<String> emailKafkaDispatcher = new KafkaDispatcher<>()) {
            Order order = record.value().getPayload();
            String emailMsg = " Thank you for your order!";
            CorrelationId correlationId = record
                    .value()
                    .getId()
                    .continueWith(EmailNewOrderService.class.getSimpleName());

            emailKafkaDispatcher.send(
                    "ecommerce.send.email",
                    order.getEmail(),
                    correlationId,
                    emailMsg);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isFraud(Order order) {
        return order.getAmmount().compareTo(new BigDecimal("4500")) >= 0;
    }
}
