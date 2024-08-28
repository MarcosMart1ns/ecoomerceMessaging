package br.com.shop;

import br.com.shop.consumer.ConsumerService;
import br.com.shop.consumer.ServiceRunner;
import br.com.shop.domain.Message;
import br.com.shop.domain.Order;
import br.com.shop.producer.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;


public class FraudDetectorServiceApplication implements ConsumerService <Order>{

    private static final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    private final LocalDatabase localDatabase;

    public FraudDetectorServiceApplication() throws SQLException {
        this.localDatabase = new LocalDatabase("frauds_database");
        this.localDatabase.createIfNotExists("""
                create table Orders(
                uuid varchar(200) primary key,
                isFraude varchar(5)
                )
                """);
    }

    public static void main(String[] args) {
        new ServiceRunner<>(FraudDetectorServiceApplication::new).start(1);
    }

    @Override
    public String getTopic() {
        return "ecommerce.new.order";
    }

    @Override
    public String getConsumerGroup() {
        return  FraudDetectorServiceApplication.class.getSimpleName();
    }

    private static boolean isFraud(Order order) {
        return order.getAmmount().compareTo(new BigDecimal("4500")) >= 0;
    }

    public void parse(ConsumerRecord<String, Message<Order>> record) {
        System.out.println("Processando novo evento");

        try {
            Order order = record.value().getPayload();

            Thread.sleep(5000);
            if (isFraud(order)) {
                localDatabase.update("insert into Orders(uuid, isFraud) values(?,true)", order.getOrderId());
                //pretending fraud happens when the ammout is above 4500
                System.out.println("FRAUD DETECTED");
                orderDispatcher.send("ecommerce.order.rejected", record.value().getPayload().getEmail(),record.value().getId().continueWith(FraudDetectorServiceApplication.class.getSimpleName()),  record.value().getPayload());
            } else {
                localDatabase.update("insert into Orders(uuid, isFraud) values(?,false)", order.getOrderId());
                System.out.println("ORDER APROVED:" + record.value());
                orderDispatcher.send("ecommerce.order.aproved", record.value().getPayload().getEmail(), record.value().getId().continueWith(FraudDetectorServiceApplication.class.getSimpleName()), record.value().getPayload());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        System.out.println(record.value());
        System.out.println(record.offset());
    }
}
