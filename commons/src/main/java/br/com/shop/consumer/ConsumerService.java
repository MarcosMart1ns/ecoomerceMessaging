package br.com.shop.consumer;

import br.com.shop.domain.Email;
import br.com.shop.domain.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.SQLException;

public interface ConsumerService<T> {

    void parse(ConsumerRecord<String, Message<T>> consumerRecord) throws SQLException;

    String getTopic();

    String getConsumerGroup();
}
