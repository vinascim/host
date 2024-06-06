package br.edu.fesa.host.dao;

import br.edu.fesa.host.model.Convidado;
import br.edu.fesa.host.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConvidadoDAO implements GenericoDAO<Convidado> {

    @Override
    public List<Convidado> listar() {
        List<Convidado> convidados = new ArrayList<>();
        String sql = "SELECT * FROM CONVIDADOS";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql);  ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                convidados.add(new Convidado(
                        resultSet.getString("CPF"),
                        resultSet.getString("NOME"),
                        resultSet.getDate("DT_NASCIMENTO").toLocalDate(),
                        resultSet.getBoolean("FL_COMIDA"),
                        resultSet.getBoolean("FL_BEBIDA"),
                        resultSet.getBoolean("FL_BEBIDA_ALCOOLICA"),
                        resultSet.getBoolean("FL_OUTROS"),
                        resultSet.getInt("ID_EVENTO"),
                        resultSet.getBoolean("FL_PAGO") // Novo campo
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return convidados;
    }

    @Override
    public void inserir(Convidado convidado) {
        String sql = "INSERT INTO CONVIDADOS (CPF, NOME, DT_NASCIMENTO, FL_COMIDA, FL_BEBIDA, FL_BEBIDA_ALCOOLICA, FL_OUTROS, ID_EVENTO, FL_PAGO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, convidado.getCpf());
            statement.setString(2, convidado.getNome());
            statement.setDate(3, java.sql.Date.valueOf(convidado.getIdade()));
            statement.setInt(4, convidado.isFlComida() ? 1 : 0);
            statement.setInt(5, convidado.isFlBebida() ? 1 : 0);
            statement.setInt(6, convidado.isFlBebidaAlcoolica() ? 1 : 0);
            statement.setInt(7, convidado.isFlOutros() ? 1 : 0);
            statement.setInt(8, convidado.getEvento());
            statement.setInt(9, convidado.isFlPago() ? 1 : 0); // Novo campo

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void alterar(Convidado convidado) {
        String sql = "UPDATE CONVIDADOS SET NOME = ?, DT_NASCIMENTO = ?, FL_COMIDA = ?, FL_BEBIDA = ?, FL_BEBIDA_ALCOOLICA = ?, FL_OUTROS = ?, ID_EVENTO = ?, FL_PAGO = ? WHERE CPF = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, convidado.getNome());
            statement.setDate(2, java.sql.Date.valueOf(convidado.getIdade()));
            statement.setInt(3, convidado.isFlComida() ? 1 : 0);
            statement.setInt(4, convidado.isFlBebida() ? 1 : 0);
            statement.setInt(5, convidado.isFlBebidaAlcoolica() ? 1 : 0);
            statement.setInt(6, convidado.isFlOutros() ? 1 : 0);
            statement.setInt(7, convidado.getEvento());
            statement.setInt(8, convidado.isFlPago() ? 1 : 0); // Novo campo
            statement.setString(9, convidado.getCpf());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void remover(Convidado convidado) {
        String sql = "DELETE FROM CONVIDADOS WHERE CPF = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, convidado.getCpf());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Convidado listarPorID(Convidado convidado) {
        String sql = "SELECT * FROM CONVIDADOS WHERE CPF = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, convidado.getCpf());

            try ( ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Convidado(
                            resultSet.getString("CPF"),
                            resultSet.getString("NOME"),
                            resultSet.getDate("DT_NASCIMENTO").toLocalDate(),
                            resultSet.getBoolean("FL_COMIDA"),
                            resultSet.getBoolean("FL_BEBIDA"),
                            resultSet.getBoolean("FL_BEBIDA_ALCOOLICA"),
                            resultSet.getBoolean("FL_OUTROS"),
                            resultSet.getInt("ID_EVENTO"),
                            resultSet.getBoolean("FL_PAGO") // Novo campo
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Convidado buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM CONVIDADOS WHERE CPF = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf);

            try ( ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Convidado(
                            resultSet.getString("CPF"),
                            resultSet.getString("NOME"),
                            resultSet.getDate("DT_NASCIMENTO").toLocalDate(),
                            resultSet.getBoolean("FL_COMIDA"),
                            resultSet.getBoolean("FL_BEBIDA"),
                            resultSet.getBoolean("FL_BEBIDA_ALCOOLICA"),
                            resultSet.getBoolean("FL_OUTROS"),
                            resultSet.getInt("ID_EVENTO"),
                            resultSet.getBoolean("FL_PAGO")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Convidado> listarPorEvento(int idEvento) {
        List<Convidado> convidados = new ArrayList<>();
        String sql = "SELECT * FROM CONVIDADOS WHERE ID_EVENTO = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idEvento);
            try ( ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    convidados.add(new Convidado(
                            resultSet.getString("CPF"),
                            resultSet.getString("NOME"),
                            resultSet.getDate("DT_NASCIMENTO").toLocalDate(),
                            resultSet.getBoolean("FL_COMIDA"),
                            resultSet.getBoolean("FL_BEBIDA"),
                            resultSet.getBoolean("FL_BEBIDA_ALCOOLICA"),
                            resultSet.getBoolean("FL_OUTROS"),
                            resultSet.getInt("ID_EVENTO"),
                            resultSet.getBoolean("FL_PAGO")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConvidadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return convidados;
    }

}
