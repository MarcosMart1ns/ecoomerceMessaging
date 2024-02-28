package br.com.alura.ecommerce.consumers;

import br.com.alura.ecommerce.commons.KafkaService;
import br.com.alura.ecommerce.producers.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;


public class FraudDetectorService {
    public static void main(String[] args) {
        KafkaService kafkaService = new KafkaService(
                "ecommerce.new.order",
                FraudDetectorService::parse,
                FraudDetectorService.class.getSimpleName(),
                Order.class);

        kafkaService.run();
    }

    private static void parse(ConsumerRecord<String, Order> record) {
        System.out.println("Processando novo evento");
        System.out.println(record.value());
        System.out.println(record.offset());
    }
}
