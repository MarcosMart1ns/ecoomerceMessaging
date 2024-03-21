package br.com.shop;

import br.com.shop.domain.Message;
import br.com.shop.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BatchSendServiceService {
    private final String DB_URL = "jdbc:sqlite:\\users_database.db";
    private final Connection connection;

    public BatchSendServiceService() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL);

        try {
            connection.createStatement().execute("create table Users(" +
                    "uuid varchar(200) primary key," +
                    "email varchar(200)" +
                    ")");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.err.println(e);
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        BatchSendServiceService batchSendServiceService = new BatchSendServiceService();

        try (KafkaService<Order> kafkaService = new KafkaService(
                "send.message.to.all.users",
                batchSendServiceService::parse,
                CreateUserService.class.getSimpleName(),
                String.class,
                new HashMap<>())) {
            kafkaService.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void parse(ConsumerRecord<String, Message<String>> record) throws ExecutionException, InterruptedException, SQLException {
        Message<String> message = record.value();

        System.out.println("-------------------");
        System.out.println("Processing new Batch");
        System.out.println("topic: " + message.getPayload());
        System.out.println(record.offset());
        try (KafkaDispatcher<User> userKafkaDispatcher = new KafkaDispatcher<>()) {

            for (User user : getAllUsers()) {
                userKafkaDispatcher.send(message.getPayload(), user.getUuid(), user);
            }
        }
    }

    private List<User> getAllUsers() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select uuid from Users");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> users = new LinkedList<>();

        while (resultSet.next()) {
            users.add(new User(resultSet.getString(1)));
        }

        return users;
    }
}
