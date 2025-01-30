package com.benat.cano.biblioteca.controller;

import com.benat.cano.biblioteca.dao.DaoAlumno;
import com.benat.cano.biblioteca.dao.DaoLibro;
import com.benat.cano.biblioteca.dao.DaoPrestamo;
import com.benat.cano.biblioteca.model.Alumno;
import com.benat.cano.biblioteca.model.Libro;
import com.benat.cano.biblioteca.model.Prestamo;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PrestamosController implements Initializable {

    @FXML
    private ListView<Alumno> lstAlumno;

    @FXML
    private ListView<Libro> lstLibro;

    @FXML
    private TextField txtFecha;




    private ResourceBundle resources;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        cargarListas();
        lstAlumno.setDisable(false);
        lstLibro.setDisable(false);
        txtFecha.setEditable(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        txtFecha.setText(LocalDateTime.now().format(formatter));
    }


    public void cargarListas() {
        ObservableList<Alumno> alumno = DaoAlumno.cargarListado();
        lstAlumno.getItems().addAll(alumno);

        ObservableList<Libro> libros = DaoLibro.cargarListado();
        ObservableList<Libro> librosEliminables = libros.filtered(DaoLibro::esEliminable);
        lstLibro.getItems().addAll(librosEliminables);
    }


    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = new ArrayList<>();

        // Validate input fields
        String error = validar();
        if (!error.isEmpty()) {
            errores.add(error);
        }

        // If errors found, show alert
        if (!errores.isEmpty()) {
            alerta(errores);
        } else {
            // Proceed with saving or updating participation
            Prestamo nuevo = new Prestamo();
            nuevo.setAlumno(lstAlumno.getSelectionModel().getSelectedItem());
            nuevo.setLibro(lstLibro.getSelectionModel().getSelectedItem());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            nuevo.setFecha_prestamo(LocalDateTime.parse(txtFecha.getText(), formatter));

            if (DaoPrestamo.insertar(nuevo) != -1) {
                confirmacion(resources.getString("save.borrow"));
                closeWindow();
            } else {
                errores.add(resources.getString("save.fail"));
            }
        }
        if (!errores.isEmpty()) {
            alerta(errores);
        }
    }


    public String validar() {
        StringBuilder error = new StringBuilder();

        if (lstAlumno.getSelectionModel().getSelectedItem() == null) {
            error.append(resources.getString("validate.borrow.students")).append("\n");
        }
        if (lstLibro.getSelectionModel().getSelectedItem() == null) {
            error.append(resources.getString("validate.borrow.books")).append("\n");
        }
        if (txtFecha.getText().isEmpty()) {
            error.append(resources.getString("validate.borrow.date")).append("\n");
        }

        return error.toString();
    }

    // Show alert with multiple error messages
    public void alerta(ArrayList<String> mensajes) {
        StringBuilder texto = new StringBuilder();
        for (String mensaje : mensajes) {
            texto.append(mensaje).append("\n");
        }

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(texto.toString());
        alerta.showAndWait();
    }

    // Show confirmation message
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtFecha.getScene().getWindow();
        stage.close();
    }
    // Close the window
    private void closeWindow() {
        Stage stage = (Stage) txtFecha.getScene().getWindow();
        stage.close();
    }

}
