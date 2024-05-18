package br.edu.fesa.host;

import br.edu.fesa.host.model.Usuario;
import br.edu.fesa.host.dao.UsuarioDAO;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroController {

    @FXML
    private TextField txtCPF;
    @FXML
    private TextField txtNome;
    @FXML
    private DatePicker dateDataNascimento;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private ComboBox<String> txtRecoveryQST;
    @FXML
    private TextField txtRecoveryANS;

    private UsuarioDAO usuarioDAO;

    public CadastroController() {
        usuarioDAO = new UsuarioDAO();
    }

    @FXML
    private void initialize() {
        // Configura os itens da ComboBox
        txtRecoveryQST.setItems(FXCollections.observableArrayList(
            "Opção 1",
            "Opção 2",
            "Opção 3"
        ));
    }
    
    @FXML
    private void btCadastrar() throws IOException {
        try {
            String cpf = txtCPF.getText();
            String nome = txtNome.getText();
            LocalDate dataNascimento = dateDataNascimento.getValue();
            String email = txtEmail.getText();
            String senha = txtSenha.getText();
            String recoveryQST = txtRecoveryQST.getValue();
            String recoveryANS = txtRecoveryANS.getText();
            
            if (cpf.isEmpty() || nome.isEmpty() || dataNascimento == null || email.isEmpty() || senha.isEmpty() || recoveryQST.isEmpty() || recoveryANS.isEmpty()) {;
                showAlert("Erro", "Todos os campos devem ser preenchidos.");
                return;
            }

            // Criptografa a senha
            String senhaCriptografada = hashPassword(senha);

            // Cria o objeto Usuario
            Usuario usuario = new Usuario();
            usuario.setCpf(cpf);
            usuario.setNome(nome);
            usuario.setDataNascimento(dataNascimento);
            usuario.setEmail(email);
            usuario.setSenha(senhaCriptografada);
            usuario.setRecoveryQST(recoveryQST);
            usuario.setRecoveryANS(recoveryANS);

            // Salva o usuário no banco de dados
            usuarioDAO.inserir(usuario);

            AbrirTelaCadastroConfirmado();

        } catch (NullPointerException e) {
            // Handle the NullPointerException
            showAlert("Erro", "Erro:" + e);
            e.printStackTrace(); // Optionally, print the stack trace for debugging
        }
    }

    // Método para exibir um alerta
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    
    private void AbrirTelaCadastroConfirmado() {
        try {
            // Carregar o arquivo FXML da tela de cadastro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cadastroConcluido.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova cena
            Scene scene = new Scene(root);
            
            // Criar uma nova janela (palco)
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Cadastro concluído!"); // Define o título da janela
            stage.show(); // Exibe a nova janela
            
                Stage stageCadastro = (Stage) txtCPF.getScene().getWindow(); // Substitua txtCPF pelo elemento relevante da cena
        stageCadastro.close();
        } catch (IOException e) {
            e.printStackTrace(); // Tratar exceção adequadamente
        }
    }
    
}
