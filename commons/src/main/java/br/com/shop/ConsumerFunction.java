package br.com.shop;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

@FunctionalInterface
public interface ConsumerFunction<T> {

    void consumer(ConsumerRecord<String, T> record) throws ExecutionException, InterruptedException, SQLException, IOException;
}
