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
/**
 * Controlador para la funcionalidad de devoluciones de libros en la biblioteca.
 * Permite gestionar la devolución de un libro por parte de un alumno, registrar los datos
 * en el historial y actualizar el estado del libro.
 */
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
    /**
     * Método que inicializa el controlador.
     * Se llama automáticamente cuando se carga la vista FXML.
     * Carga los datos iniciales del préstamo y configura los valores por defecto.
     *
     * @param url La URL de la vista FXML.
     * @param resourceBundle El ResourceBundle que contiene los textos traducidos.
     */
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
    /**
     * Método que se ejecuta cuando se cancela la acción de devolución.
     * Cierra la ventana actual sin realizar cambios.
     *
     * @param event El evento generado al hacer clic en el botón de cancelar.
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtFecha.getScene().getWindow();
        stage.close();
    }
    /**
     * Método que guarda los cambios realizados en la devolución del libro.
     * Si la devolución es exitosa, se registra en el historial y se elimina el préstamo.
     *
     * @param event El evento generado al hacer clic en el botón de guardar.
     */
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
    /**
     * Método que carga los valores posibles para el ComboBox de estado del libro.
     * Los valores son proporcionados por el archivo de recursos (i18n).
     */
    private void cargarDatosComboBox() {
        ObservableList<String> opciones = FXCollections.observableArrayList(
                resources.getString("new"), resources.getString("new.used"), resources.getString("used.semi"),resources.getString("used"),resources.getString("restaured")
        );
        cmbEstado.setItems(opciones);
    }

    /**
     * Muestra una alerta con los mensajes de error proporcionados.
     *
     * @param mensajes Lista de mensajes de error a mostrar en la alerta.
     */
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

    /**
     * Muestra un mensaje de confirmación después de realizar una acción exitosa.
     *
     * @param texto El texto de confirmación que se mostrará en la alerta.
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(texto);
        alerta.showAndWait();
    }


    /**
     * Cierra la ventana actual.
     */
    private void closeWindow() {
        Stage stage = (Stage) txtFecha.getScene().getWindow();
        stage.close();
    }

}
