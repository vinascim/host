package br.edu.fesa.host.controller;

import br.edu.fesa.host.App;
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
        this.usuarioDAO = new UsuarioDAO();
    }

    @FXML
    private void initialize() {
        txtCPF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
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

        if (cpf.isEmpty() || recoveryANS.isEmpty()) {
            showAlert("Erro", "CPF e resposta de recuperação devem ser preenchidos.");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorCPF(cpf);

        if (usuario != null && usuario.getRecoveryANS().equals(recoveryANS)) {
            abrirNovaTela();
        } else {
            showAlert("Erro", "Resposta de recuperação incorreta.");
        }
    }

    private void abrirNovaTela() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/fesa/host/novaSenha.fxml"));
        Parent root = loader.load();

        NovaSenhaController novaSenhaController = loader.getController();
        novaSenhaController.setCpf(txtCPF.getText());

        Scene scene = new Scene(root);
        Stage stage = (Stage) txtCPF.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Nova Senha");
        stage.show();
    }

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
