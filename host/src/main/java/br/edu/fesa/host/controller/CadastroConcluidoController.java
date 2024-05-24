package br.edu.fesa.host.controller;

import br.edu.fesa.host.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CadastroConcluidoController {
    
    @FXML
    private Button btContinuar;
    
    @FXML
    private void btContinuar() {
        try {
            App.setRoot("home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
