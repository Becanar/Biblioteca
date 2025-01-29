package com.benat.cano.biblioteca.controller;


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

public class AlumnosController implements Initializable {

    private Alumno alumno; // La olimpiada que estamos editando o creando

    // Elementos del FXML
    @FXML
    private Button btnEliminar;
    @FXML
    private RadioButton rbInvierno;
    @FXML
    private RadioButton rbVerano;
    @FXML
    private ToggleGroup tgTemporada;
    @FXML
    private TextField txtDni;
    @FXML
    private TextField txtAp1;
    @FXML
    private TextField txtNombre;
    @FXML
    private Label lblDelete;

    private ResourceBundle resources;

    /**
     * Constructor vacío para la creación de una nueva olimpiada.
     */
    public AlumnosController() {
        this.alumno = null;
    }

    /**
     * Constructor para la edición de una olimpiada existente.
     *
     * @param alumno La olimpiada a editar.
     */
    public AlumnosController(Alumno alumno) {
        this.alumno = alumno;
    }

    /**
     * Inicializa la vista. Si la olimpiada es nula, se prepara la vista para crear una nueva olimpiada.
     * Si no, carga los datos de la olimpiada para su edición.
     *
     * @param url La URL de la vista.
     * @param resourceBundle El recurso de internacionalización.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;

        if (alumno == null) {
            // Crear una nueva olimpiada (Campos vacíos)
            txtNombre.setText("");
            txtDni.setText("");
            txtAp1.setText("");

        } /*else {
            // Editar una olimpiada existente (Cargar datos)
            txtNombre.setText(olimpiada.getNombre());
            txtDni.setText(String.valueOf(olimpiada.getAnio()));
            if ("Winter".equals(olimpiada.getTemporada())) {
                rbInvierno.setSelected(true);
                rbVerano.setSelected(false);
            } else {
                rbVerano.setSelected(true);
                rbInvierno.setSelected(false);
            }
            txtAp1.setText(olimpiada.getCiudad());
            if (DaoOlimpiada.esEliminable(olimpiada)) {
                btnEliminar.setDisable(false);
            } else {
                lblDelete.setVisible(true);
            }*/
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
     * Muestra una confirmación para eliminar la olimpiada.
     * Si se confirma, elimina la olimpiada de la base de datos.
     *
     * @param event El evento de acción asociado al botón de eliminar.
     */
    @FXML
    void eliminar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(txtNombre.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle(resources.getString("window.confirm"));
        alert.setContentText(resources.getString("delete.olympics.prompt"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (DaoOlimpiada.eliminar(olimpiada)) {
                confirmacion(resources.getString("delete.olympics.success"));
            } else {
                ArrayList<String> errores = new ArrayList<>();
                errores.add(resources.getString("delete.olympics.fail"));
                alerta(errores);
            }
        }
    }

    /**
     * Método que se ejecuta al hacer clic en el botón "Guardar".
     * Valida los campos y guarda la olimpiada en la base de datos.
     * Si la olimpiada ya existe, muestra un mensaje de duplicado.
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
            alerta(errores);  // Pasamos el ArrayList de errores
        } else {
            Olimpiada nuevo = new Olimpiada();
            nuevo.setNombre(txtNombre.getText());

            // Comprobamos si la olimpiada ya existe
            if(DaoOlimpiada.getOlimpiada(nuevo.getNombre()) == null){
                nuevo.setAnio(Integer.parseInt(txtDni.getText()));
                if (rbInvierno.isSelected()) {
                    nuevo.setTemporada("Winter");
                } else {
                    nuevo.setTemporada("Summer");
                }
                nuevo.setCiudad(txtAp1.getText());

                // Crear o modificar la olimpiada
                if (this.olimpiada == null) {
                    int id = DaoOlimpiada.insertar(nuevo);
                    if (id == -1) {
                        errores.add(resources.getString("save.fail"));
                    } else {
                        confirmacion(resources.getString("save.olympics"));
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    if (DaoOlimpiada.modificar(this.olimpiada, nuevo)) {
                        confirmacion(resources.getString("update.olympics"));
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    } else {
                        errores.add(resources.getString("save.fail"));
                    }
                }
            } else {
                // Si ya existe, mostramos el mensaje de duplicado
                errores.add(resources.getString("duplicate.olympics"));
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
            error = resources.getString("validate.olympics.name") + "\n";
        }
        if (txtDni.getText().isEmpty()) {
            error += resources.getString("validate.olympics.year") + "\n";
        } else {
            try {
                Integer.parseInt(txtDni.getText());
            } catch (NumberFormatException e) {
                error += resources.getString("validate.olympics.year.num") + "\n";
            }
        }
        if (txtAp1.getText().isEmpty()) {
            error += resources.getString("validate.olympics.city") + "\n";
        }
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