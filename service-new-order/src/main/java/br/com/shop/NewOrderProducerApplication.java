package br.com.shop;

import br.com.shop.domain.Email;
import br.com.shop.domain.Order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class NewOrderProducerApplication {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try (KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();) {
            try (KafkaDispatcher<Email> emailKafkaDispatcher = new KafkaDispatcher<>();) {
                for (int i = 0; i < 10; i++) {

                    String value = "123123,123123,conta";
                    String key = value;

                    String email = "email@host.com";
                    String msg = "Obrigado pela compra!";

                    Order order = new Order(
                            UUID.randomUUID().toString(),
                            UUID.randomUUID().toString(),
                            BigDecimal.valueOf(Math.random() * 5000 + 1)
                    );

                    orderKafkaDispatcher.send("ecommerce.new.order", order.getUserId(), order);
                    emailKafkaDispatcher.send("ecommerce.send.email", order.getUserId(), new Email(
                            email, msg
                    ));
                }
            }

        }

    }
}
