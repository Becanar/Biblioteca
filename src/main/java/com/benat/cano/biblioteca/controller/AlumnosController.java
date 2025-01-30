package com.benat.cano.biblioteca.controller;

import com.benat.cano.biblioteca.dao.DaoAlumno;
import com.benat.cano.biblioteca.model.Alumno;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * Controlador de la vista para gestionar los alumnos de la biblioteca.
 * Permite crear, editar, eliminar y validar los datos de un alumno.
 */
public class AlumnosController implements Initializable {

    private Alumno alumno; // El alumno que estamos editando o creando

    // Elementos del FXML
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtS1;
    @FXML
    private TextField txtS2;
    @FXML
    private Label lblDelete;

    private ResourceBundle resources;

    /**
     * Constructor vacío para la creación de un nuevo alumno.
     */
    public AlumnosController() {
        this.alumno = null;
    }

    /**
     * Constructor para la edición de un alumno existente.
     *
     * @param alumno El alumno a editar.
     */
    public AlumnosController(Alumno alumno) {
        this.alumno = alumno;
    }

    /**
     * Inicializa la vista. Si el alumno es nulo, se prepara la vista para crear un nuevo alumno.
     * Si no, carga los datos del alumno para su edición.
     *
     * @param url            La URL de la vista.
     * @param resourceBundle El recurso de internacionalización.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;

        if (alumno == null) {
            // Crear un nuevo alumno (Campos vacíos)
            txtNombre.setText("");
            txtID.setText("");
            txtS1.setText("");
            txtS2.setText("");
            btnEliminar.setDisable(true);
            lblDelete.setVisible(false);
        } else {
            // Editar un alumno existente (Cargar datos)
            txtNombre.setText(alumno.getNombre());
            txtID.setText(alumno.getDni());  // Mostrar el DNI, pero no editable
            txtS1.setText(String.valueOf(alumno.getApellido1()));
            txtS2.setText(alumno.getApellido2());
            if (DaoAlumno.esEliminable(alumno)) {
                btnEliminar.setDisable(false);
            } else {
                lblDelete.setVisible(true);
            }

            // Deshabilitar el campo DNI para edición
            txtID.setDisable(true);  // Esto hace que el campo de DNI sea solo lectura
        }
    }

    /**
     * Método que se ejecuta al hacer clic en el botón "Cancelar".
     * Cierra la ventana sin realizar cambios.
     *
     * @param event El evento de acción asociado al botón de cancelar.
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Método que se ejecuta al hacer clic en el botón "Eliminar".
     * Muestra una confirmación para eliminar el alumno.
     * Si se confirma, elimina el alumno de la base de datos.
     *
     * @param event El evento de acción asociado al botón de eliminar.
     */
    @FXML
    void eliminar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(txtNombre.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle(resources.getString("window.confirm"));
        alert.setContentText(resources.getString("delete.student.prompt"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (DaoAlumno.eliminar(alumno)) {
                confirmacion(resources.getString("delete.student.success"));
                Stage stage = (Stage) txtNombre.getScene().getWindow();
                stage.close();
            } else {
                ArrayList<String> errores = new ArrayList<>();
                errores.add(resources.getString("delete.student.fail"));
                alerta(errores);
            }
        }
    }

    /**
     * Método que se ejecuta al hacer clic en el botón "Guardar".
     * Valida los campos y guarda el alumno en la base de datos.
     * Si el alumno ya existe, muestra un mensaje de duplicado.
     *
     * @param event El evento de acción asociado al botón de guardar.
     */
    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = new ArrayList<>();

        // Validación de los campos
        String error = validar();
        if (!error.isEmpty()) {
            errores.add(error);
        }

        // Si hay errores, mostrar alerta
        if (!errores.isEmpty()) {
            alerta(errores);
        } else {
            Alumno nuevo = new Alumno();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setDni(txtID.getText());  // El DNI no se debe modificar
            nuevo.setApellido1((txtS1.getText()));
            nuevo.setApellido2(txtS2.getText());
            // Comprobamos si el alumno ya existe
            if (this.alumno == null) {
                if (DaoAlumno.getAlumno(nuevo.getDni()) == null) {

                    // Crear o modificar el alumno

                    boolean id = DaoAlumno.insertar(nuevo);
                    if (!id) {
                        errores.add(resources.getString("save.fail"));
                    } else {
                        confirmacion(resources.getString("save.student"));
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    }

                } else {
                    // Si ya existe, mostramos el mensaje de duplicado
                    errores.add(resources.getString("duplicate.student"));
                }
            } else {

                if (DaoAlumno.modificar(nuevo)) {
                    confirmacion(resources.getString("update.student"));
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                } else {
                    errores.add(resources.getString("save.fail"));
                }
            }


            // Si hay errores de duplicado o de inserción, mostramos la alerta
            if (!errores.isEmpty()) {
                alerta(errores);
            }
        }
    }

    /**
     * Método de validación de los campos.
     *
     * @return Un mensaje de error si los campos son inválidos, de lo contrario una cadena vacía.
     */
    public String validar() {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error = resources.getString("validate.student.name") + "\n";
        }
        if (txtID.getText().isEmpty()) {
            error += resources.getString("validate.student.dni") + "\n";
        }
        if (txtS1.getText().isEmpty()) {
            error += resources.getString("validate.student.s1") + "\n";
        }

        if (txtS2.getText().isEmpty()) {
            error += resources.getString("validate.student.s2") + "\n";
        }
        System.out.println(error);
        return error;
    }

    /**
     * Método para mostrar alertas con múltiples errores.
     *
     * @param mensajes Los mensajes de error a mostrar en la alerta.
     */
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
     * Método para mostrar un mensaje de confirmación.
     *
     * @param texto El mensaje de confirmación a mostrar.
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}
