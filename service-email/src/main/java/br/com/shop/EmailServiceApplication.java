package br.com.shop;

import br.com.shop.domain.Email;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.HashMap;

public class EmailServiceApplication {
    public static void main(String[] args) {
        KafkaService kafkaService = new KafkaService(
                "ecommerce.send.email",
                EmailServiceApplication::parse,
                EmailServiceApplication.class.getSimpleName(),
                Email.class,
                new HashMap<>());

        kafkaService.run();
    }

    private static void parse(ConsumerRecord<String,Email> consumerRecord){
        System.out.println("Processando novo email------------>");
        System.out.println(consumerRecord.key());
        System.out.println(consumerRecord.value());
        System.out.println(consumerRecord.offset());
    }
}
