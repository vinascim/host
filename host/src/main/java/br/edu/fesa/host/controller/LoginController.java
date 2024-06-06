package br.edu.fesa.host.controller;

import br.edu.fesa.host.App;
import br.edu.fesa.host.model.Usuario;
import br.edu.fesa.host.dao.UsuarioDAO;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtCPF;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private Label esqueceuSenha;

    @FXML
    private Label btAbrirCadastro;

    private UsuarioDAO usuarioDAO;

    public LoginController() {
        usuarioDAO = new UsuarioDAO();
    }

    @FXML
    private void btEntrar() throws IOException {
        String cpf = txtCPF.getText();
        String senha = txtSenha.getText();

        String hashedPassword = hashPassword(senha);
        Usuario usuario = usuarioDAO.buscarPorCPF(cpf);

        if (usuario != null && usuario.getSenha().equals(hashedPassword)) {
            // Salva o CPF no Singleton
            Usuario.getInstance().setUsuarioCpf(cpf);

            redirecionarParaHome(cpf);
        } else {
            showAlert("Erro", "Credenciais inválidas.");
        }
    }

    @FXML
    private void initialize() {
        btAbrirCadastro.setOnMouseClicked(event -> abrirCadastro());
        esqueceuSenha.setOnMouseClicked(event -> abrirEsqueceuSenha());
    }

    private void abrirCadastro() {
        try {
            App.setRoot("cadastrar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirEsqueceuSenha() {
        try {
            App.setRoot("esqueceuSenha");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidCPF(String cpf) {
        String regex = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cpf);
        return matcher.matches();
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

    private void redirecionarParaHome(String cpf) throws IOException {
        HomeController controller = new HomeController();
        //controller.initialize(cpf);
        App.setRoot("home");
    }

}
