package br.com.alura.ecommerce.consumers;

import br.com.alura.ecommerce.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {
    public static void main(String[] args) {
        KafkaService kafkaService = new KafkaService(
                "ecommerce.send.email",
                EmailService::parse,
                EmailService.class.getSimpleName());

        kafkaService.run();
    }

    private static void parse(ConsumerRecord<String,String> consumerRecord){
        System.out.println("Processando novo email------------>");
        System.out.println(consumerRecord.key());
        System.out.println(consumerRecord.value());
        System.out.println(consumerRecord.offset());
    }
}
