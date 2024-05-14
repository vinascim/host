module br.edu.fesa.host {
    requires javafx.controls;
    requires javafx.fxml;

    opens br.edu.fesa.host to javafx.fxml;
    exports br.edu.fesa.host;
}
