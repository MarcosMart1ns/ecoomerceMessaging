package br.com.shop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LocalDatabase {

    private final Connection connection;

    public LocalDatabase(String name) throws SQLException {
        String url = "jdbc:sqlite:target\\" + name + ".db";
        this.connection = DriverManager.getConnection(url);


    }


    public void createIfNotExists(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update(String sql, String... params) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i + 1, params[i]);
            }

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet query(String query, String... params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setString(i + 1, params[i]);
        }

       return preparedStatement.executeQuery();
    }
}
