package br.edu.fesa.host;

import br.edu.fesa.host.model.Usuario;
import br.edu.fesa.host.dao.UsuarioDAO;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtCPF;

    @FXML
    private PasswordField txtSenha;

    private UsuarioDAO usuarioDAO;

    public LoginController() {
        usuarioDAO = new UsuarioDAO();
    }
    
    @FXML
    private void btEntrar() throws IOException {
        String cpf = txtCPF.getText();
        String senha = txtSenha.getText();

        // Verifica se o CPF está em um formato válido
        if (!isValidCPF(cpf)) {
            System.out.println("CPF inválido. Tente novamente.");
            return; // Sai do método se o CPF for inválido
        }

        // Criptografa a senha
        String hashedPassword = hashPassword(senha);

        // Busca o usuário no banco de dados pelo CPF
        Usuario usuario = usuarioDAO.buscarPorCPF(cpf);

        // Verifica se o usuário existe e se a senha está correta
        if (usuario != null && usuario.getSenha().equals(hashedPassword)) {
            System.out.println("Login bem-sucedido!");
            // Redireciona para a tela home
            redirecionarParaHome();
            
            Stage stage = (Stage) txtCPF.getScene().getWindow(); // Substitua txtCPF pelo elemento relevante da cena
            stage.close();
        } else {
            System.out.println("Credenciais inválidas. Tente novamente.");
            // Exibe uma mensagem de erro na interface de login
            // Aqui você pode implementar uma interface gráfica para exibir a mensagem de erro
        }
    }
    
       @FXML
    private Label btAbrirCadastro;

    @FXML
    private void initialize() {
        btAbrirCadastro.setOnMouseClicked(event -> abrirCadastro());
    }

    private void abrirCadastro() {
        try {
            // Carregar o arquivo FXML da tela de cadastro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cadastrar.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova cena
            Scene scene = new Scene(root);
            
            // Criar uma nova janela (palco)
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Cadastro"); // Define o título da janela
            stage.show(); // Exibe a nova janela
            
                 Stage stageLogin = (Stage) txtCPF.getScene().getWindow(); // Substitua txtCPF pelo elemento relevante da cena
        stageLogin.close();
        } catch (IOException e) {
            e.printStackTrace(); // Tratar exceção adequadamente
        }
    }
    
    // Método para validar o CPF usando regex
    private boolean isValidCPF(String cpf) {
        String regex = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cpf);
        return matcher.matches();
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

     //Método para redirecionar para a tela "home"
    private void redirecionarParaHome() throws IOException {
        Stage stage = (Stage) txtCPF.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    

}
