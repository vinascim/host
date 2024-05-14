/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.host;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author Everymind
 */
public class loginController {
    @FXML
    private TextField txtCPF;
    
    @FXML
    private PasswordField txtSenha;
    
     @FXML
    private void btEntrar() throws IOException {
        String username = txtCPF.getText();
        String password = txtSenha.getText();

        // Verifica as credenciais (simulação simples)
        if (username.equals("admin") && password.equals("admin")) {
            System.out.println("Login bem-sucedido!");
            // Aqui você pode abrir outra janela ou realizar outras ações após o login bem-sucedido
        } else {
            System.out.println("Credenciais inválidas. Tente novamente.");
            // Aqui você pode exibir uma mensagem de erro na interface de login
        }
    }
}
