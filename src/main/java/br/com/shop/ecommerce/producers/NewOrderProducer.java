package br.com.shop.ecommerce.producers;

import br.com.shop.ecommerce.commons.KafkaDispatcher;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class NewOrderProducer {
    public static void main(String[] args) {

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
                            email,msg
                    ));
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
