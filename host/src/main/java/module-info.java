module br.edu.fesa.host {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens br.edu.fesa.host to javafx.fxml;
    exports br.edu.fesa.host;
    exports br.edu.fesa.host.controller to javafx.fxml;
}
