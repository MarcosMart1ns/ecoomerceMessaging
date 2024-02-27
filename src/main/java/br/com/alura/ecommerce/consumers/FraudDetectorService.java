package br.com.alura.ecommerce.consumers;

import br.com.alura.ecommerce.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;


public class FraudDetectorService {
    public static void main(String[] args) {
        KafkaService kafkaService = new KafkaService(
                "ecommerce.new.order",
                FraudDetectorService::parse,
                FraudDetectorService.class.getSimpleName());

        kafkaService.run();
    }

    private static void parse(ConsumerRecord<String,String> record){
        System.out.println("Processando novo evento");
        System.out.println(record.value());
        System.out.println(record.offset());
    }
}
