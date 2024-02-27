package br.com.alura.ecommerce.commons;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFunction {

    void consumer (ConsumerRecord<String,String> record);
}
