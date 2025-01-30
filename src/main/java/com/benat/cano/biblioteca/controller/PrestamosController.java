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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
/**
 * Controlador para la gestión de préstamos de libros en la biblioteca.
 * Permite realizar la creación de un nuevo préstamo, validar los datos y generar un informe en formato PDF.
 */
public class PrestamosController implements Initializable {

    @FXML
    private ListView<Alumno> lstAlumno;

    @FXML
    private ListView<Libro> lstLibro;

    @FXML
    private TextField txtFecha;




    private ResourceBundle resources;
    /**
     * Método que se ejecuta al inicializar el controlador. Carga las listas de alumnos y libros,
     * y establece la fecha actual en el campo de fecha.
     *
     * @param url URL de la vista cargada.
     * @param resourceBundle Recursos para la internacionalización.
     */
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

    /**
     * Carga las listas de alumnos y libros disponibles en las vistas.
     */
    public void cargarListas() {
        ObservableList<Alumno> alumno = DaoAlumno.cargarListado();
        lstAlumno.getItems().addAll(alumno);

        ObservableList<Libro> libros = DaoLibro.cargarListado();
        ObservableList<Libro> librosEliminables = libros.filtered(DaoLibro::esEliminable);
        lstLibro.getItems().addAll(librosEliminables);
    }

    /**
     * Método que se ejecuta al guardar un préstamo. Realiza la validación de los campos y, si no hay errores,
     * guarda el préstamo en la base de datos e imprime un informe con los detalles del préstamo.
     *
     * @param event Evento generado al hacer clic en el botón de guardar.
     */
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
                generarInforme(nuevo);
                closeWindow();
            } else {
                errores.add(resources.getString("save.fail"));
            }
        }
        if (!errores.isEmpty()) {
            alerta(errores);
        }
    }
    /**
     * Genera un informe en formato PDF para el préstamo realizado, usando JasperReports.
     *
     * @param p Objeto Prestamo con los datos del préstamo a reportar.
     */
    void generarInforme(Prestamo p) {
        HashMap<String, Object> parameters = new HashMap<>();
        String imagePath1 = getClass().getResource("/com/benat/cano/biblioteca/images/logo.png").toString();
        parameters.put("IMAGE_PATH", imagePath1);
        parameters.put("nombre", p.getAlumno().getNombre());
        parameters.put("apellidos", p.getAlumno().getApellido1() + " " + p.getAlumno().getApellido2());
        parameters.put("dni", p.getAlumno().getDni());
        parameters.put("titulo", p.getLibro().getTitulo());
        parameters.put("codigo", p.getLibro().getCodigo());
        parameters.put("autor", p.getLibro().getAutor());
        parameters.put("editorial", p.getLibro().getEditorial());
        parameters.put("estado", p.getLibro().getEstado());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

// Formatea las fechas antes de pasarlas a los parámetros
        String fechaPrestamoFormateada = p.getFecha_prestamo().format(formatter);
        String fechaLimiteFormateada = p.getFecha_prestamo().plusDays(15).format(formatter);

// Ahora puedes pasar las fechas formateadas como parámetros al reporte
        parameters.put("fecha", fechaPrestamoFormateada);
        parameters.put("fecha_limite", fechaLimiteFormateada);
        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/com/benat/cano/biblioteca/jasper/prestamo.jasper")); // Obtener el fichero del informe
            JasperPrint jprint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource()); // Cargar el informe
            JasperViewer viewer = new JasperViewer(jprint, false); // Instanciar la vista del informe para mostrar el informe
            viewer.setVisible(true); // Mostrar el informe al usuario
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    /**
     * Valida los campos de entrada antes de realizar el préstamo.
     *
     * @return Un string con los errores encontrados, o una cadena vacía si no hay errores.
     */
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
    /**
     * Muestra una alerta con los mensajes de error.
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
     * Muestra una alerta de confirmación con un mensaje.
     *
     * @param texto El texto de la confirmación a mostrar.
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
    /**
     * Cancela la operación de préstamo y cierra la ventana.
     *
     * @param event Evento generado al hacer clic en el botón de cancelar.
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtFecha.getScene().getWindow();
        stage.close();
    }
    /**
     * Cierra la ventana actual del controlador.
     */
    private void closeWindow() {
        Stage stage = (Stage) txtFecha.getScene().getWindow();
        stage.close();
    }

}
