package br.com.shop;

import br.com.shop.deserialize.GsonDeserializer;
import br.com.shop.domain.Message;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {

    private final KafkaConsumer<String, Message<T>> consumer;
    private final ConsumerFunction parse;
    private final String consumerGroup;
    private final Map<String, String> properties;

    public KafkaService(String topic, ConsumerFunction<T> parse, String consumerGroup,  Map<String,String> properties) {
        this.parse = parse;
        this.consumerGroup = consumerGroup;
        this.properties = properties;
        consumer = new KafkaConsumer<>(this.properties());
        consumer.subscribe(Collections.singleton(topic));
    }

    public KafkaService(Pattern topic, ConsumerFunction<T> parse, String consumerGroup, Map<String,String> properties) {
        this.parse = parse;
        this.consumerGroup = consumerGroup;
        this.properties = properties;
        consumer = new KafkaConsumer<>(this.properties());
        consumer.subscribe(topic);
    }

    public void run() {
        while (true) {
            ConsumerRecords<String,Message<T>> records = consumer.poll(Duration.ofMillis(100L));
            if (!records.isEmpty()) {
                System.out.println("Found " + records.count() + " events");

                for (ConsumerRecord<String, Message<T>> record : records) {
                    try{
                        parse.consumer(record);
                    } catch (ExecutionException | InterruptedException | SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private Properties properties() {
        Properties props = new Properties();

        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,consumerGroup);
        props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"1");
        props.putAll(this.properties);
        return props;
    }

    @Override
    public void close() throws IOException {
        consumer.close();
    }
}
