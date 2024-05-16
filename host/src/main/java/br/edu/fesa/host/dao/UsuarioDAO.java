package br.edu.fesa.host.dao;

import br.edu.fesa.host.dao.Conexao;
import br.edu.fesa.host.dao.GenericoDAO;
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
    public List<Usuario> listar(){
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM EMPRESA.USUARIO");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Usuario usuario = new Usuario(
                        resultSet.getString("CPF"),
                        resultSet.getString("NOME"),
                        resultSet.getDate("DATA_NASCIMENTO").toLocalDate(),
                        resultSet.getString("EMAIL"),
                        resultSet.getString("SENHA"),
                        resultSet.getString("RECOVERY_QST"),
                        resultSet.getString("RECOVERY_ANS")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);           
        }
        return usuarios;
    }

    @Override
    public void inserir(Usuario usuario){
        String sql = "INSERT INTO EMPRESA.USUARIO (CPF, NOME, DATA_NASCIMENTO, EMAIL, SENHA, RECOVERY_QST, RECOVERY_ANS) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getCpf());
            statement.setString(2, usuario.getNome());
            statement.setDate(3, java.sql.Date.valueOf(usuario.getDataNascimento()));
            statement.setString(4, usuario.getEmail());
            statement.setString(5, hashPassword(usuario.getSenha()));
            statement.setString(6, usuario.getRecoveryQST());
            statement.setString(7, usuario.getRecoveryANS());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            //throw new PersistenciaException("Erro ao inserir usuário", ex);
        }
    }

    @Override
    public void alterar(Usuario usuario) {
        String sql = "UPDATE EMPRESA.USUARIO SET NOME = ?, DATA_NASCIMENTO = ?, EMAIL = ?, SENHA = ?, RECOVERY_QST = ?, RECOVERY_ANS = ? WHERE CPF = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getNome());
            statement.setDate(2, java.sql.Date.valueOf(usuario.getDataNascimento()));
            statement.setString(3, usuario.getEmail());
            statement.setString(4, hashPassword(usuario.getSenha()));
            statement.setString(5, usuario.getRecoveryQST());
            statement.setString(6, usuario.getRecoveryANS());
            statement.setString(7, usuario.getCpf());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            //throw new PersistenciaException("Erro ao alterar usuário", ex);
        }
    }

    @Override
    public void remover(Usuario usuario){
        String sql = "DELETE FROM EMPRESA.USUARIO WHERE CPF = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getCpf());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            //throw new PersistenciaException("Erro ao remover usuário", ex);
        }
    }

    @Override
    public Usuario listarPorID(Usuario usuario) {
        String sql = "SELECT * FROM EMPRESA.USUARIO WHERE CPF = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getCpf());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Usuario(
                            resultSet.getString("CPF"),
                            resultSet.getString("NOME"),
                            resultSet.getDate("DATA_NASCIMENTO").toLocalDate(),
                            resultSet.getString("EMAIL"),
                            resultSet.getString("SENHA"),
                            resultSet.getString("RECOVERY_QST"),
                            resultSet.getString("RECOVERY_ANS")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            //throw new PersistenciaException("Erro ao buscar usuário por CPF", ex);
        }
        return null;
    }

  public Usuario buscarPorCPF(String cpf) {
    String sql = "SELECT * FROM EMPRESA.USUARIO WHERE CPF = ?";
    try (Connection connection = Conexao.getConexao().getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, cpf);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return new Usuario(
                        resultSet.getString("CPF"),
                        resultSet.getString("NOME"),
                        resultSet.getDate("DATA_NASCIMENTO").toLocalDate(),
                        resultSet.getString("EMAIL"),
                        resultSet.getString("SENHA"),
                        resultSet.getString("RECOVERY_QST"),
                        resultSet.getString("RECOVERY_ANS")
                );
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        // Aqui você pode tratar o erro de alguma forma, como lançar uma exceção
    }
    return null;
}

    
    // Método para criptografar a senha usando SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
