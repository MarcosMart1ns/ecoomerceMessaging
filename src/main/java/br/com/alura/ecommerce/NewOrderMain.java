package br.com.alura.ecommerce;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;


public class NewOrderMain {
    public static void main(String[] args) {
        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties())) {

            String value = "123123,123123,conta";
            ProducerRecord<String, String> mensagem = new ProducerRecord<>("ecommerce.new.order", value, value);

            kafkaProducer.send(mensagem,((metadata, exception) -> {
                if (exception != null){
                    exception.printStackTrace();
                    return;
                }

                System.out.println(metadata.topic()+":::::"+metadata.offset());
            })).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties properties() {
        Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }
}
