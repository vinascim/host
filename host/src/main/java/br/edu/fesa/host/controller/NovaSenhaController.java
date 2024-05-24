package br.edu.fesa.host.controller;

import br.edu.fesa.host.App;
import br.edu.fesa.host.dao.UsuarioDAO;
import br.edu.fesa.host.model.Usuario;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class NovaSenhaController {

    @FXML
    private TextField txtNovaSenha;

    private String cpf;

    private UsuarioDAO usuarioDAO;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public NovaSenhaController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    @FXML
    private void btNovaSenha() {
        try {
            String cpf = getCpf();
            String novaSenha = txtNovaSenha.getText();

            if (novaSenha.isEmpty()) {
                showAlert("Erro", "A nova senha não pode estar vazia.");
                return;
            }

            String senhaCriptografada = hashPassword(novaSenha);

            Usuario usuario = usuarioDAO.buscarPorCPF(cpf);

            if (usuario == null) {
                showAlert("Erro", "Usuário não encontrado.");
                return;
            }

            usuario.setSenha(senhaCriptografada);
            usuarioDAO.alterar(usuario);
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
