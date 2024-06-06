package br.edu.fesa.host.dao;

import br.edu.fesa.host.model.QuestaoRecuperacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestaoRecuperacaoDAO implements GenericoDAO<QuestaoRecuperacao> {

    @Override
    public List<QuestaoRecuperacao> listar() {
        List<QuestaoRecuperacao> questoes = new ArrayList<>();
        String sql = "SELECT * FROM QUESTAO_RECUPERACAO";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                questoes.add(new QuestaoRecuperacao(
                        resultSet.getInt("ID_QUESTAO"),
                        resultSet.getString("PERGUNTA")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(QuestaoRecuperacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return questoes;
    }

    @Override
    public void inserir(QuestaoRecuperacao questao) {
        String sql = "INSERT INTO QUESTAO_RECUPERACAO (ID_QUESTAO, PERGUNTA) VALUES (?, ?)";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, questao.getIdQuestao());
            statement.setString(2, questao.getPergunta());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestaoRecuperacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void alterar(QuestaoRecuperacao questao) {
        String sql = "UPDATE QUESTAO_RECUPERACAO SET PERGUNTA = ? WHERE ID_QUESTAO = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, questao.getPergunta());
            statement.setInt(2, questao.getIdQuestao());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestaoRecuperacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void remover(QuestaoRecuperacao questao) {
        String sql = "DELETE FROM QUESTAO_RECUPERACAO WHERE ID_QUESTAO = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, questao.getIdQuestao());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestaoRecuperacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public QuestaoRecuperacao listarPorID(QuestaoRecuperacao questao) {
        String sql = "SELECT * FROM QUESTAO_RECUPERACAO WHERE ID_QUESTAO = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, questao.getIdQuestao());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new QuestaoRecuperacao(
                            resultSet.getInt("ID_QUESTAO"),
                            resultSet.getString("PERGUNTA")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(QuestaoRecuperacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public QuestaoRecuperacao buscarPorId(int idQuestao) {
        String sql = "SELECT * FROM QUESTAO_RECUPERACAO WHERE ID_QUESTAO = ?";
        try (Connection connection = Conexao.getConexao().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idQuestao);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new QuestaoRecuperacao(
                            resultSet.getInt("ID_QUESTAO"),
                            resultSet.getString("PERGUNTA")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(QuestaoRecuperacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
