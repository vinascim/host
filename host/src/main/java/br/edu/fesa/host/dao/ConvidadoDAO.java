/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.host.dao;

/**
 *
 * @author victo
 */
import br.edu.fesa.host.dao.Conexao;
import br.edu.fesa.host.dao.GenericoDAO;
import br.edu.fesa.host.model.Convidado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConvidadoDAO implements GenericoDAO<Convidado> {

    @Override
    public List<Convidado> listar() {
        List<Convidado> convidados = new ArrayList<>();
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM EMPRESA.CONVIDADO");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Convidado convidado = new Convidado(
                        resultSet.getString("CPF"),
                        resultSet.getString("NOME"),
                        resultSet.getInt("IDADE"),
                        resultSet.getBoolean("FL_COMIDA"),
                        resultSet.getBoolean("FL_BEBIDA"),
                        resultSet.getBoolean("FL_BEBIDA_ALCOOLICA"),
                        resultSet.getBoolean("FL_OUTROS")
                );
                convidados.add(convidado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return convidados;
    }

    @Override
    public void inserir(Convidado convidado) {
        String sql = "INSERT INTO EMPRESA.CONVIDADO (CPF, NOME, IDADE, FL_COMIDA, FL_BEBIDA, FL_BEBIDA_ALCOOLICA, FL_OUTROS) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, convidado.getCpf());
            statement.setString(2, convidado.getNome());
            statement.setInt(3, convidado.getIdade());
            statement.setBoolean(4, convidado.isFlComida());
            statement.setBoolean(5, convidado.isFlBebida());
            statement.setBoolean(6, convidado.isFlBebidaAlcoolica());
            statement.setBoolean(7, convidado.isFlOutros());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void alterar(Convidado convidado) {
        String sql = "UPDATE EMPRESA.CONVIDADO SET NOME = ?, IDADE = ?, FL_COMIDA = ?, FL_BEBIDA = ?, FL_BEBIDA_ALCOOLICA = ?, FL_OUTROS = ? WHERE CPF = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, convidado.getNome());
            statement.setInt(2, convidado.getIdade());
            statement.setBoolean(3, convidado.isFlComida());
            statement.setBoolean(4, convidado.isFlBebida());
            statement.setBoolean(5, convidado.isFlBebidaAlcoolica());
            statement.setBoolean(6, convidado.isFlOutros());
            statement.setString(7, convidado.getCpf());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void remover(Convidado convidado) {
        String sql = "DELETE FROM EMPRESA.CONVIDADO WHERE CPF = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, convidado.getCpf());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Convidado listarPorID(Convidado convidado) {
        String sql = "SELECT * FROM EMPRESA.CONVIDADO WHERE CPF = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, convidado.getCpf());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Convidado(
                            resultSet.getString("CPF"),
                            resultSet.getString("NOME"),
                            resultSet.getInt("IDADE"),
                            resultSet.getBoolean("FL_COMIDA"),
                            resultSet.getBoolean("FL_BEBIDA"),
                            resultSet.getBoolean("FL_BEBIDA_ALCOOLICA"),
                            resultSet.getBoolean("FL_OUTROS")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

