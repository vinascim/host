package br.edu.fesa.host;

import br.edu.fesa.host.dao.UsuarioDAO;
import br.edu.fesa.host.model.Usuario;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EsqueceuSenhaController {

    @FXML
    private TextField txtCPF;

    @FXML
    private TextField txtRecoveryANS;

    @FXML
    private Label txtRecoveryQST;


    private UsuarioDAO usuarioDAO;

   

    public EsqueceuSenhaController() {
        this.usuarioDAO = new UsuarioDAO(); // Inicializa o DAO
    }

    @FXML
    private void initialize() {
        // Adiciona um listener ao campo CPF para que a busca seja realizada quando o usuário terminar de digitar
        txtCPF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Quando o campo perde o foco
                buscarRecoveryANS(txtCPF.getText());
            }
        });
    }

    private void buscarRecoveryANS(String cpf) {
        Usuario usuario = usuarioDAO.buscarPorCPF(cpf);
        if (usuario != null) {
            txtRecoveryQST.setText(usuario.getRecoveryQST());
        } else {
            txtRecoveryQST.setText("CPF não encontrado.");
        }
    }

    @FXML
    private void btProsseguir() throws IOException {
        String cpf = txtCPF.getText();
        String recoveryANS = txtRecoveryANS.getText();

        // Verifica se o CPF e a resposta de recuperação foram preenchidos
        if (cpf.isEmpty() || recoveryANS.isEmpty()) {
            showAlert("Erro", "CPF e resposta de recuperação devem ser preenchidos.");
            return;
        }

        // Busca o usuário no banco de dados pelo CPF
        Usuario usuario = usuarioDAO.buscarPorCPF(cpf);

        if (usuario != null && usuario.getRecoveryANS().equals(recoveryANS)) {
            // A resposta de recuperação está correta, abre a nova tela
            abrirNovaTela();
        } else {
            showAlert("Erro", "Resposta de recuperação incorreta.");
        }
    }

   

    private void abrirNovaTela() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("novaSenha.fxml"));
        Parent root = loader.load();

        // Passar o CPF para o controlador da nova tela
        NovaSenhaController novaSenhaController = loader.getController();
        novaSenhaController.setCpf(txtCPF.getText());

        Scene scene = new Scene(root);
        Stage stage = (Stage) txtCPF.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Nova Senha");
        stage.show();
    }

    // Implementação do método showAlert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao criptografar a senha", e);
        }
    }
}
