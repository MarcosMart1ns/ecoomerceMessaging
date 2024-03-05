package br.com.shop;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class KafkaService<T> {

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction parse;
    private final String consumerGroup;
    private final Class<T> classType;

    private final Map<String, String> properties;

    public KafkaService(String topic, ConsumerFunction parse, String consumerGroup, Class<T> classType, Map<String,String> properties) {
        this.parse = parse;
        this.consumerGroup = consumerGroup;
        this.classType = classType;
        this.properties = properties;
        consumer = new KafkaConsumer<>(this.properties());
        consumer.subscribe(Collections.singleton(topic));
    }

    public KafkaService(Pattern topic, ConsumerFunction parse, String consumerGroup, Class<T> classType, Map<String,String> properties) {
        this.parse = parse;
        this.consumerGroup = consumerGroup;
        this.classType = classType;
        this.properties = properties;
        consumer = new KafkaConsumer<>(this.properties());
        consumer.subscribe(topic);
    }

    public void run() {
        while (true) {
            ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100L));
            if (!records.isEmpty()) {
                System.out.println("Found " + records.count() + " events");

                for (ConsumerRecord<String, T> a : records) {
                    try{
                        parse.consumer(a);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private Properties properties() {
        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,consumerGroup);
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"1");
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, classType.getName());
        properties.putAll(this.properties);
        return properties;
    }
}
