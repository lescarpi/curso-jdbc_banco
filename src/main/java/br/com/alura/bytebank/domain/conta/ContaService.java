package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.Cliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaService {

    private ConnectionFactory connection;

    public ContaService() {
        this.connection = new ConnectionFactory();
    }

    private Set<Conta> contas = new HashSet<>();

    public Set<Conta> listarContasAbertas() {
        Connection con = connection.recuperarConexao();
        return new ContaDAO(con).listar();
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) {
        Connection con = connection.recuperarConexao();
        new ContaDAO(con).salvar(dadosDaConta);
    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }
        Conta conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }
        BigDecimal saldoFinal = conta.getSaldo().subtract(valor);
        Connection con = connection.recuperarConexao();
        new ContaDAO(con).alterar(conta.getNumero(), saldoFinal);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }
        Conta conta = buscarContaPorNumero(numeroDaConta);
        BigDecimal saldoFinal = conta.getSaldo().add(valor);
        Connection con = connection.recuperarConexao();
        new ContaDAO(con).alterar(conta.getNumero(), saldoFinal);
    }

    public void realizarTransferencia(Integer numeroDaContaOrigem, Integer numeroDaContaDestino, BigDecimal valor) {
        this.realizarSaque(numeroDaContaOrigem, valor);
        this.realizarDeposito(numeroDaContaDestino, valor);
    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        contas.remove(conta);
    }

    private Conta buscarContaPorNumero(Integer numero) {
        Connection con = connection.recuperarConexao();
        Conta conta = new ContaDAO(con).buscarPorNumero(numero);
        if(conta == null) {
            throw new RegraDeNegocioException("Não existe conta cadastrada com esse número!");
        }
        return conta;
    }
}
