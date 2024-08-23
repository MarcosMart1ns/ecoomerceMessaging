package br.com.shop.consumer;

import java.sql.SQLException;

public interface ServiceFactory<T> {

    ConsumerService<T> create() throws SQLException;
}
