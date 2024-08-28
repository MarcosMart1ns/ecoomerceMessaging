package br.com.shop;

import br.com.shop.consumer.ConsumerService;
import br.com.shop.consumer.ServiceRunner;
import br.com.shop.domain.Message;
import br.com.shop.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CreateUserService implements ConsumerService<Order> {

    private final LocalDatabase database;

    public CreateUserService() throws SQLException {
      this.database = new LocalDatabase("users_database");
      this.database.createIfNotExists("""
      create table Users(
      uuid varchar(200) primary key,
      email varchar(200)
      )
      """);
    }

    public static void main(String[] args) {

        new ServiceRunner<>(CreateUserService::new).start(1);

    }


    public void parse(ConsumerRecord<String, Message<Order>> record) throws SQLException {
        System.out.println("Processando novo evento");
        System.out.println("Persistindo evento");
        System.out.println(record.value());
        System.out.println(record.offset());

        if (ifIsNewUser(record.value().getPayload().getEmail())) {
            insertNewUser(record.value().getPayload().getEmail());
        }
    }

    @Override
    public String getTopic() {
        return "ecommerce.new.order";
    }

    @Override
    public String getConsumerGroup() {
        return CreateUserService.class.getSimpleName();
    }

    private void insertNewUser(String email) throws SQLException {
        this.database.update("insert into Users(uuid,email)values (?,?)", UUID.randomUUID().toString(), email);


        System.out.println("Email persistido::" + email);
    }

    private boolean ifIsNewUser(String email) throws SQLException {
        return this.database
                .query("select uuid from Users where email = ? limit 1")
                .next();
    }

}
