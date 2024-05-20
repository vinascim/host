package br.edu.fesa.host;

import br.edu.fesa.host.model.Usuario;
import br.edu.fesa.host.dao.UsuarioDAO;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
                "Qual é a tradição familiar favorita?",
                "Qual foi o seu primeiro emprego?",
                "Qual era o nome do seu animal de estimação de infância?",
                "Qual é o nome da sua rua quando você era criança?",
                "Qual era o nome do seu melhor amigo quando você era criança?",
                "Qual era o seu sonho de infância?",
                "Qual era o seu programa de TV favorito quando você era criança?",
                "Qual é o seu filme favorito?",
                "Qual era a sua comida favorita quando você era criança?",
                "Qual é a cor favorita da sua mãe?",
                "Qual é seu número da sorte?",
                "Qual foi a sua primeira viagem de avião?",
                "Qual foi a sua primeira viagem que você fez?",
                "Qual é seu livro favorito?",
                "Qual foi o nome da sua primeira escola?",
                "Qual era o nome do seu brinquedo favorito quando você era criança?",
                "Qual é o seu lugar favorito para visitar?",
                "Qual foi o seu primeiro carro?",
                "Qual era o seu herói favorito quando você era criança?",
                "Qual é a sua música favorita?",
                "Qual é a sua cor favorita?",
                "Qual é o seu passatempo favorito?"
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

            if (cpf.isEmpty() || nome.isEmpty() || dataNascimento == null || email.isEmpty() || senha.isEmpty() || recoveryQST.isEmpty() || recoveryANS.isEmpty()) {
                showAlert("Erro", "Todos os campos devem ser preenchidos.");
                return;
            }

            if (!isValidCPF(cpf)) {
                showAlert("Erro", "CPF inválido. O formato correto é XXX.XXX.XXX-XX.");
                return;
            }

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

        } catch (Exception e) {
            // Handle the NullPointerException
            showAlert("Erro", "Erro:" + e);
        }
    }

    private boolean isValidCPF(String cpf) {
        String regex = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cpf);
        return matcher.matches();
    }

    // Método para exibir um alerta
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
