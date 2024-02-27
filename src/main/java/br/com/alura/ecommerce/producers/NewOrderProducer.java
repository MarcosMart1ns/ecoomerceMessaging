package br.com.alura.ecommerce.producers;

import br.com.alura.ecommerce.commons.KafkaDispatcher;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;


public class NewOrderProducer {
    public static void main(String[] args) {

        try ( KafkaDispatcher kafkaDispatcher= new KafkaDispatcher();) {

            String value = "123123,123123,conta";
            String key = value;

            String email = "email@host.com";
            String msg = "Obrigado pela compra!";

            kafkaDispatcher.send("ecommerce.new.order",key,value);
            kafkaDispatcher.send("ecommerce.send.email",email,msg);

        } catch (ExecutionException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
