package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection recuperarConexao() {

        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/byte_bank?user=leandro&password=leandro");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
