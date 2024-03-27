package br.com.shop;

import br.com.shop.domain.CorrelationId;
import br.com.shop.domain.Email;
import br.com.shop.domain.Order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class NewOrderProducerApplication {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try (KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();) {

            for (int i = 0; i < 10; i++) {

                String email = "email@host.com";

                Order order = new Order(
                        UUID.randomUUID().toString(),
                        BigDecimal.valueOf(Math.random() * 5000 + 1),
                        email
                );

                orderKafkaDispatcher.send("ecommerce.new.order", order.getEmail(), new CorrelationId(NewOrderProducerApplication.class.getSimpleName()), order);

            }
        }
    }
}
