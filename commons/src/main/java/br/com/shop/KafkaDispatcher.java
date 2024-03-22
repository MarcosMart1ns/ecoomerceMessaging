package br.com.shop;

import br.com.shop.domain.CorrelationId;
import br.com.shop.domain.Message;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {

    private final KafkaProducer<String, Message<T>> producer;
    private String name;

    public KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());

        return properties;
    }

    public void send(String topic, String key, CorrelationId correlationId, T payload) throws ExecutionException, InterruptedException {
        Message<T> message = new Message<>(correlationId, payload);

        ProducerRecord<String, Message<T>> mensagem = new ProducerRecord<>(topic, key, message);

        producer.send(mensagem, callback).get();
    }

    Callback callback = (metadata, exception) -> {
        if (exception != null) {
            exception.printStackTrace();
            return;
        }

        System.out.printf("%s ::::: %s\n", metadata.topic(),metadata.offset());
    };

    @Override
    public void close() {
        this.producer.close();
    }
}
