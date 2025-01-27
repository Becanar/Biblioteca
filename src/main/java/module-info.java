module com.benat.cano.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.benat.cano.biblioteca to javafx.fxml;
    exports com.benat.cano.biblioteca;
    exports com.benat.cano.biblioteca.app;
    opens com.benat.cano.biblioteca.app to javafx.fxml;
    exports com.benat.cano.biblioteca.controller;
    opens com.benat.cano.biblioteca.controller to javafx.fxml;
}