package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, conta.getNumero());
            ps.setBigDecimal(2, BigDecimal.ZERO);
            ps.setString(3, conta.getTitular().getNome());
            ps.setString(4, conta.getTitular().getCpf());
            ps.setString(5, conta.getTitular().getEmail());

            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Set<Conta> listar() {
        PreparedStatement ps;
        ResultSet rs;
        Set<Conta> contas = new HashSet<>();
        String sql = "SELECT * FROM conta";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Integer numero = rs.getInt(1);
                BigDecimal saldo = rs.getBigDecimal(2);
                String nome = rs.getString(3);
                String cpf = rs.getString(4);
                String email = rs.getString(5);
                Cliente titular = new Cliente(new DadosCadastroCliente(nome, cpf, email));
                contas.add(new Conta(numero, titular));
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return contas;

    }

    public Conta buscarPorNumero(Integer numero) {

        String sql = "SELECT * FROM conta WHERE numero = ?";

        Conta conta = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, numero);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                BigDecimal saldo = rs.getBigDecimal(2);
                String nome = rs.getString(3);
                String cpf = rs.getString(4);
                String email = rs.getString(5);
                Cliente titular = new Cliente(new DadosCadastroCliente(nome, cpf, email));
                conta = new Conta(numero, titular);
            }
            
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return conta;
    }
}
