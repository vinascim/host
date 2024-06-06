package br.edu.fesa.host.dao;

import br.edu.fesa.host.model.Evento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventoDAO implements GenericoDAO<Evento> {

    @Override
    public List<Evento> listar() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM EVENTO";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql);  ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                eventos.add(new Evento(
                        resultSet.getInt("ID_EVENTO"),
                        resultSet.getString("USUARIO_CPF"),
                        resultSet.getString("NOME"),
                        resultSet.getDate("INICIO"),
                        resultSet.getDate("FIM"),
                        resultSet.getString("ENDERECO"),
                        resultSet.getInt("LOTACAO"),
                        resultSet.getDouble("VALOR"),
                        resultSet.getString("DESCRICAO")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eventos;
    }

    @Override
    public void inserir(Evento evento) {
        String sql = "INSERT INTO EVENTO (USUARIO_CPF, NOME, INICIO, FIM, ENDERECO, LOTACAO, VALOR, DESCRICAO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, evento.getUsuarioCpf());
            statement.setString(2, evento.getNome());
            statement.setDate(3, new java.sql.Date(evento.getInicio().getTime()));
            statement.setDate(4, new java.sql.Date(evento.getFim().getTime()));
            statement.setString(5, evento.getEndereco());
            statement.setInt(6, evento.getLotacao());
            statement.setDouble(7, evento.getValor());
            statement.setString(8, evento.getDescricao());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void alterar(Evento evento) {
        String sql = "UPDATE EVENTO SET USUARIO_CPF = ?, NOME = ?, INICIO = ?, FIM = ?, ENDERECO = ?, LOTACAO = ?, VALOR = ?, DESCRICAO = ? WHERE ID_EVENTO = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, evento.getUsuarioCpf());
            statement.setString(2, evento.getNome());
            statement.setDate(3, new java.sql.Date(evento.getInicio().getTime()));
            statement.setDate(4, new java.sql.Date(evento.getFim().getTime()));
            statement.setString(5, evento.getEndereco());
            statement.setInt(6, evento.getLotacao());
            statement.setDouble(7, evento.getValor());
            statement.setString(8, evento.getDescricao());
            statement.setInt(9, evento.getId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void remover(Evento evento) {
        String sql = "DELETE FROM EVENTO WHERE ID_EVENTO = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, evento.getId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Evento listarPorID(Evento evento) {
        String sql = "SELECT * FROM EVENTO WHERE ID_EVENTO = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, evento.getId());

            try ( ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Evento(
                            resultSet.getInt("ID_EVENTO"),
                            resultSet.getString("USUARIO_CPF"),
                            resultSet.getString("NOME"),
                            resultSet.getDate("INICIO"),
                            resultSet.getDate("FIM"),
                            resultSet.getString("ENDERECO"),
                            resultSet.getInt("LOTACAO"),
                            resultSet.getDouble("VALOR"),
                            resultSet.getString("DESCRICAO")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Evento buscarPorNome(String nome) {
        String sql = "SELECT * FROM EVENTO WHERE NOME = ?";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);

            try ( ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Evento(
                            resultSet.getInt("ID_EVENTO"),
                            resultSet.getString("USUARIO_CPF"),
                            resultSet.getString("NOME"),
                            resultSet.getDate("INICIO"),
                            resultSet.getDate("FIM"),
                            resultSet.getString("ENDERECO"),
                            resultSet.getInt("LOTACAO"),
                            resultSet.getDouble("VALOR"),
                            resultSet.getString("DESCRICAO")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int buscarUltimoIdEvento() {
        String sql = "SELECT MAX(ID_EVENTO) AS LAST_ID FROM EVENTO";
        try ( Connection connection = Conexao.getConexao().getConnection();  PreparedStatement statement = connection.prepareStatement(sql);  ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("LAST_ID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; // Retornar -1 em caso de falha ou se não houver eventos
    }

}
