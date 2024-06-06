package br.edu.fesa.host.dao;

import br.edu.fesa.host.model.CategoriaDespesa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaDespesaDAO implements GenericoDAO<CategoriaDespesa> {

    @Override
    public List<CategoriaDespesa> listar() {
        List<CategoriaDespesa> categorias = new ArrayList<>();
        String sql = "SELECT * FROM CATEGORIA_DESPESAS";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                categorias.add(new CategoriaDespesa(
                        resultSet.getInt("ID_CATEGORIA"),
                        resultSet.getString("CATEGORIA")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categorias;
    }

    @Override
    public void inserir(CategoriaDespesa categoria) {
        String sql = "INSERT INTO CATEGORIA_DESPESAS (ID_CATEGORIA, CATEGORIA) VALUES (?, ?)";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoria.getIdCategoria());
            statement.setString(2, categoria.getCategoria());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void alterar(CategoriaDespesa categoria) {
        String sql = "UPDATE CATEGORIA_DESPESAS SET CATEGORIA = ? WHERE ID_CATEGORIA = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoria.getCategoria());
            statement.setInt(2, categoria.getIdCategoria());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void remover(CategoriaDespesa categoria) {
        String sql = "DELETE FROM CATEGORIA_DESPESAS WHERE ID_CATEGORIA = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoria.getIdCategoria());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public CategoriaDespesa listarPorID(CategoriaDespesa categoria) {
        String sql = "SELECT * FROM CATEGORIA_DESPESAS WHERE ID_CATEGORIA = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoria.getIdCategoria());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new CategoriaDespesa(
                            resultSet.getInt("ID_CATEGORIA"),
                            resultSet.getString("CATEGORIA")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public CategoriaDespesa buscarPorId(int idCategoria) {
        String sql = "SELECT * FROM CATEGORIA_DESPESAS WHERE ID_CATEGORIA = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idCategoria);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new CategoriaDespesa(
                            resultSet.getInt("ID_CATEGORIA"),
                            resultSet.getString("CATEGORIA")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDespesaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
