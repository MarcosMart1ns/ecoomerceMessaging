package br.com.shop;

import br.com.shop.domain.Message;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.regex.Pattern;

public class LogServiceApplication {
    public static void main(String[] args) {
        KafkaService kafkaService = new KafkaService(
                Pattern.compile("ecommerce.*"),
                LogServiceApplication::parse,
                LogServiceApplication.class.getSimpleName(),
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));

        kafkaService.run();
    }

    private static void parse(ConsumerRecord<String, Message<String>> record) {
        System.out.println("-----------------------------");
        System.out.println(record.value());
        System.out.println(record.offset());
        System.out.println(record.topic());
        System.out.println("Value:::"+record.value());
    }
}
