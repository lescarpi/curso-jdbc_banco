package br.com.alura.bytebank;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection recuperarConexao() {

        try {
            return createDataSource().getConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
        config.setUsername("leandro");
        config.setPassword("leandro");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }

}
