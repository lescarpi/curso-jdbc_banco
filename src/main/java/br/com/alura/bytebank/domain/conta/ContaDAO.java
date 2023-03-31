package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContaDAO {

    private Connection con;

    ContaDAO(Connection connection) {
        this.con = connection;
    }

    public void salvar(DadosAberturaConta dadosAberturaConta) {
        Cliente cliente = new Cliente(dadosAberturaConta.dadosCliente());
        Conta conta = new Conta(dadosAberturaConta.numero(), cliente);

        String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setInt(1, conta.getNumero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3, conta.getTitular().getNome());
            preparedStatement.setString(4, conta.getTitular().getCpf());
            preparedStatement.setString(5, conta.getTitular().getEmail());

            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
