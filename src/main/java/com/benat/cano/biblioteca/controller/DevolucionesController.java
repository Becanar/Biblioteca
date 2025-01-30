package com.benat.cano.biblioteca.controller;

import com.benat.cano.biblioteca.dao.DaoHistoricoPrestamo;
import com.benat.cano.biblioteca.dao.DaoPrestamo;
import com.benat.cano.biblioteca.model.Alumno;
import com.benat.cano.biblioteca.model.HistoricoPrestamos;
import com.benat.cano.biblioteca.model.Libro;
import com.benat.cano.biblioteca.model.Prestamo; // Asumimos que tienes una clase Prestamo
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DevolucionesController implements Initializable {
    private Prestamo p;
    private HistoricoPrestamos hp;
    @FXML
    private ListView<Alumno> lstAlumno;

    @FXML
    private ListView<Libro> lstLibro;

    @FXML
    private ComboBox<String> cmbEstado;

    @FXML
    private TextField txtFecha;

    @FXML
    private TextField txtFechaDev;
    private ResourceBundle resources;


    /**
     * Constructor para la edición de un alumno existente.
     *
     * @param libro El alumno a editar.
     */
    public DevolucionesController(Prestamo libro) {
        this.p = libro;
        hp=new HistoricoPrestamos();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        lstAlumno.getItems().add(p.getAlumno());
        lstLibro.getItems().add(p.getLibro());
        txtFecha.setText(p.getFecha_prestamo().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime fecha = LocalDateTime.now();
        txtFechaDev.setText(fecha.format(formatter));

        hp.setId_prestamo(p.getId_prestamo());
        hp.setAlumno(p.getAlumno());
        hp.setLibro(p.getLibro());
        hp.setFecha_prestamo(p.getFecha_prestamo());
        hp.setFecha_devolucion(fecha);

        cargarDatosComboBox();
        cmbEstado.setValue(p.getLibro().getEstado());
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtFecha.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = new ArrayList<>();
        hp.getLibro().setEstado(cmbEstado.getValue());
        if(DaoHistoricoPrestamo.insertar(hp)) {
            DaoPrestamo.eliminar(p);
            confirmacion(resources.getString("save.devolution"));
            closeWindow();
        }else {
        errores.add(resources.getString("save.fail"));}
    }
    private void cargarDatosComboBox() {
        ObservableList<String> opciones = FXCollections.observableArrayList(
                resources.getString("new"), resources.getString("new.used"), resources.getString("used.semi"),resources.getString("used"),resources.getString("restaured")
        );
        cmbEstado.setItems(opciones);
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

    // Close the window
    private void closeWindow() {
        Stage stage = (Stage) txtFecha.getScene().getWindow();
        stage.close();
    }

}
