module com.benat.cano.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.benat.cano.biblioteca to javafx.fxml;
    exports com.benat.cano.biblioteca;
}