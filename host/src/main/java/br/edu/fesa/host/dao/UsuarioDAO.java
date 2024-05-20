package br.edu.fesa.host.dao;

import br.edu.fesa.host.model.Usuario;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO implements GenericoDAO<Usuario> {

    @Override
    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                usuarios.add(new Usuario(
                        resultSet.getString("CPF"),
                        resultSet.getString("NOME"),
                        resultSet.getDate("DT_NASCIMENTO").toLocalDate(),
                        resultSet.getString("EMAIL"),
                        resultSet.getString("SENHA"),
                        resultSet.getString("RECOVERY_QST_ID"),
                        resultSet.getString("RECOVERY_ANS")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarios;
    }

    @Override
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (CPF, NOME, DT_NASCIMENTO, EMAIL, SENHA, RECOVERY_QST_ID, RECOVERY_ANS) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getCpf());
            statement.setString(2, usuario.getNome());
            statement.setDate(3, java.sql.Date.valueOf(usuario.getDataNascimento()));
            statement.setString(4, usuario.getEmail());
            statement.setString(5, usuario.getSenha());
            statement.setString(6, usuario.getRecoveryQST());
            statement.setString(7, usuario.getRecoveryANS());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void alterar(Usuario usuario) {
        String sql = "UPDATE USUARIO SET NOME = ?, DT_NASCIMENTO = ?, EMAIL = ?, SENHA = ?, RECOVERY_QST_ID = ?, RECOVERY_ANS = ? WHERE CPF = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getNome());
            statement.setDate(2, java.sql.Date.valueOf(usuario.getDataNascimento()));
            statement.setString(3, usuario.getEmail());
            statement.setString(4, usuario.getSenha());
            statement.setString(5, usuario.getRecoveryQST());
            statement.setString(6, usuario.getRecoveryANS());
            statement.setString(7, usuario.getCpf());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void remover(Usuario usuario) {
        String sql = "DELETE FROM USUARIO WHERE CPF = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getCpf());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Usuario listarPorID(Usuario usuario) {
        String sql = "SELECT * FROM USUARIO WHERE CPF = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getCpf());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Usuario(
                            resultSet.getString("CPF"),
                            resultSet.getString("NOME"),
                            resultSet.getDate("DT_NASCIMENTO").toLocalDate(),
                            resultSet.getString("EMAIL"),
                            resultSet.getString("SENHA"),
                            resultSet.getString("RECOVERY_QST_ID"),
                            resultSet.getString("RECOVERY_ANS")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Usuario buscarPorCPF(String cpf) {
        String sql = "SELECT * FROM USUARIO WHERE CPF = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Usuario(
                            resultSet.getString("CPF"),
                            resultSet.getString("NOME"),
                            resultSet.getDate("DT_NASCIMENTO").toLocalDate(),
                            resultSet.getString("EMAIL"),
                            resultSet.getString("SENHA"),
                            resultSet.getString("RECOVERY_QST_ID"),
                            resultSet.getString("RECOVERY_ANS")
                    );
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
        return null;
    }
}
