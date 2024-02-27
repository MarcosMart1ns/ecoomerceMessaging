package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

public class KafkaService {

    private final KafkaConsumer<String, String> consumer;
    private final ConsumerFunction parse;
    private final String consumerGroup;

    public KafkaService(String topic, ConsumerFunction parse, String consumerGroup) {
        this.parse = parse;
        this.consumerGroup = consumerGroup;
        consumer = new KafkaConsumer<>(this.properties());
        consumer.subscribe(Collections.singleton(topic));
    }

    public KafkaService(Pattern topic, ConsumerFunction parse, String consumerGroup) {
        this.parse = parse;
        this.consumerGroup = consumerGroup;
        consumer = new KafkaConsumer<>(this.properties());
        consumer.subscribe(topic);
    }

    public void run() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100L));
            if (!records.isEmpty()) {
                System.out.println("Found " + records.count() + " events");
                records.forEach(parse::consumer);
            }
        }
    }

    private Properties properties() {
        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,consumerGroup);
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"1");
        return properties;
    }
}
