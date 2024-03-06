package br.com.shop;

import br.com.shop.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CreateUserService {

    private final String DB_URL = "jdbc:sqlite:\\users_database.db";
    private final Connection connection;

    public CreateUserService() throws SQLException{
        this.connection = DriverManager.getConnection(DB_URL);

        try {
            connection.createStatement().execute("create table Users(" +
                    "uuid varchar(200) primary key," +
                    "email varchar(200)" +
                    ")");
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.err.println(e);
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        CreateUserService createUserService = new CreateUserService();

        try (KafkaService<Order> kafkaService = new KafkaService(
                "ecommerce.new.order",
                createUserService::parse,
                CreateUserService.class.getSimpleName(),
                Order.class,
                new HashMap<>())){
            kafkaService.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("Processando novo evento");
        System.out.println("Persistindo evento");
        System.out.println(record.value());
        System.out.println(record.offset());

        if (ifIsNewUser(record.value().getEmail())) {
            insertNewUser(record.value().getUserId(),record.value().getEmail());
        }
    }

    private void insertNewUser(String id, String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into Users(uuid,email)values (?,?)");
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, email);
        preparedStatement.execute();
        System.out.println("Email persistido::" + email);
    }

    private boolean ifIsNewUser(String email) throws SQLException {
        PreparedStatement preparedStatement =  connection.prepareStatement("select uuid from Users where email = ? limit 1");
        preparedStatement.setString(1,email);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

}
