package br.com.shop;

import br.com.shop.domain.Email;
import br.com.shop.domain.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService<T> {

    void parse(ConsumerRecord<String, Message<Email>> consumerRecord);

    String getTopic();

    String getConsumerGroup();
}
