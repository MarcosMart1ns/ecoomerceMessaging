package br.com.shop;

import br.com.shop.domain.Email;
import br.com.shop.domain.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.HashMap;

public class EmailServiceApplication {
    public static void main(String[] args) {
        KafkaService kafkaService = new KafkaService(
                "ecommerce.send.email",
                EmailServiceApplication::parse,
                EmailServiceApplication.class.getSimpleName(),
                new HashMap<>());

        kafkaService.run();
    }

    private static void parse(ConsumerRecord<String, Message<Email>> consumerRecord){
        System.out.println("Processando novo email------------>");
        System.out.println(consumerRecord.key());
        System.out.println(consumerRecord.value().getPayload());
        System.out.println(consumerRecord.offset());
    }
}
