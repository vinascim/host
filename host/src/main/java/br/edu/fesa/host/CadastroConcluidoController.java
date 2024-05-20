/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.host;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author victo
 */
public class CadastroConcluidoController {
 
    
   @FXML
    private Button btContinuar;
    
      private void btContinuar() {
        try {
            // Carregar o arquivo FXML da tela de cadastro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            // Criar uma nova cena
            Scene scene = new Scene(root);

            // Criar uma nova janela (palco)
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Cadastro concluído!"); // Define o título da janela
            stage.show(); // Exibe a nova janela

            Stage stageCadastro = (Stage) btContinuar.getScene().getWindow(); // Substitua txtCPF pelo elemento relevante da cena
            stageCadastro.close();
        } catch (IOException e) {
            e.printStackTrace(); // Tratar exceção adequadamente
        }
    }
}
