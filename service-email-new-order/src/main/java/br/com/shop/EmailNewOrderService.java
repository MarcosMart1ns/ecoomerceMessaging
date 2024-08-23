package br.com.shop;

import br.com.shop.consumer.ConsumerService;
import br.com.shop.consumer.ServiceRunner;
import br.com.shop.domain.CorrelationId;
import br.com.shop.domain.Message;
import br.com.shop.domain.Order;
import br.com.shop.producer.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;


public class EmailNewOrderService implements ConsumerService<Order> {

    public static void main(String[] args) {
        new ServiceRunner<>(EmailNewOrderService::new).start(5);
    }

    @Override
    public String getTopic() {
        return "ecommerce.new.order";
    }

    @Override
    public String getConsumerGroup() {
        return EmailNewOrderService.class.getSimpleName();
    }

    public void parse(ConsumerRecord<String, Message<Order>> record) {
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

}
