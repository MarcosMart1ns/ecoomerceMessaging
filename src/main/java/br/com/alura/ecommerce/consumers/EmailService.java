package br.com.alura.ecommerce.consumers;

import br.com.alura.ecommerce.commons.KafkaService;
import br.com.alura.ecommerce.producers.Email;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.HashMap;

public class EmailService {
    public static void main(String[] args) {
        KafkaService kafkaService = new KafkaService(
                "ecommerce.send.email",
                EmailService::parse,
                EmailService.class.getSimpleName(),
                Email.class,
                new HashMap<>()
        );

        kafkaService.run();
    }

    private static void parse(ConsumerRecord<String,Email> consumerRecord){
        System.out.println("Processando novo email------------>");
        System.out.println(consumerRecord.key());
        System.out.println(consumerRecord.value());
        System.out.println(consumerRecord.offset());
    }
}
