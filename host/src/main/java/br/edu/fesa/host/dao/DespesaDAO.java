package br.edu.fesa.host.dao;

import br.edu.fesa.host.model.Despesa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DespesaDAO implements GenericoDAO<Despesa> {

    @Override
    public List<Despesa> listar() {
        List<Despesa> despesas = new ArrayList<>();
        String sql = "SELECT * FROM DESPESAS";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql);  ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                despesas.add(new Despesa(
                        resultSet.getInt("ID_EVENTO"),
                        resultSet.getInt("ID_CATEGORIA"),
                        resultSet.getString("NOME"),
                        resultSet.getFloat("VALOR")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return despesas;
    }

    @Override
    public void inserir(Despesa despesa) {
        String sql = "INSERT INTO DESPESAS (ID_EVENTO, ID_CATEGORIA, NOME, VALOR) VALUES (?, ?, ?, ?)";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, despesa.getIdEvento());
            statement.setInt(2, despesa.getIdCategoria());
            statement.setString(3, despesa.getNome());
            statement.setDouble(4, despesa.getValor());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void alterar(Despesa despesa) {
        String sql = "UPDATE DESPESAS SET ID_CATEGORIA = ?, VALOR = ? WHERE NOME = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, despesa.getIdCategoria());
            statement.setDouble(2, despesa.getValor());
            statement.setString(3, despesa.getNome());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void remover(Despesa despesa) {
        String sql = "DELETE FROM DESPESAS WHERE NOME = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, despesa.getNome());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Despesa listarPorID(Despesa despesa) {
        String sql = "SELECT * FROM DESPESAS WHERE NOME = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, despesa.getNome());

            try ( ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Despesa(
                            resultSet.getInt("ID_EVENTO"),
                            resultSet.getInt("ID_CATEGORIA"),
                            resultSet.getString("NOME"),
                            resultSet.getFloat("VALOR")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Despesa buscarPorNome(String nome) {
        String sql = "SELECT * FROM DESPESAS WHERE NOME = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);

            try ( ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Despesa(
                            resultSet.getInt("ID_EVENTO"),
                            resultSet.getInt("ID_CATEGORIA"),
                            resultSet.getString("NOME"),
                            resultSet.getFloat("VALOR")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Despesa> listarPorEvento(int idEvento) {
        List<Despesa> despesas = new ArrayList<>();
        String sql = "SELECT * FROM DESPESAS WHERE ID_EVENTO = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idEvento);
            try ( ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    despesas.add(new Despesa(
                            resultSet.getInt("ID_EVENTO"),
                            resultSet.getInt("ID_CATEGORIA"),
                            resultSet.getString("NOME"),
                            resultSet.getDouble("VALOR")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return despesas;
    }

}
