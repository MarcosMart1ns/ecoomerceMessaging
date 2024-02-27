package br.com.alura.ecommerce.consumers;

import br.com.alura.ecommerce.commons.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {
        KafkaService kafkaService = new KafkaService(
                Pattern.compile("ecommerce.*"),
                LogService::parse,
                LogService.class.getSimpleName());

        kafkaService.run();
    }

    private static void parse(ConsumerRecord<String, String> record) {
        System.out.println(record.value());
        System.out.println(record.offset());
        System.out.println(record.topic());
    }
}
