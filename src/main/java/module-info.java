module com.benat.cano.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.benat.cano.biblioteca.model to javafx.fxml;
    exports com.benat.cano.biblioteca.app;
    opens com.benat.cano.biblioteca.app to javafx.fxml;
    exports com.benat.cano.biblioteca.controller;
    opens com.benat.cano.biblioteca.controller to javafx.fxml;
}