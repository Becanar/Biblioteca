package com.benat.cano.biblioteca.controller;


import com.benat.cano.biblioteca.dao.DaoLibro;
import com.benat.cano.biblioteca.model.Libro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LibrosController implements Initializable {
    private Libro libro;
    private Blob imagen;
    @FXML
    private Button btnFotoBorrar;

    @FXML
    private ImageView foto;

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtEditorial;

    @FXML
    private TextField txtEstado;

    private File imagenSeleccionada;
    @FXML
    private ResourceBundle resources;
    /**
     * Constructor vacío para la creación de un nuevo alumno.
     */
    public LibrosController() {
        this.libro = null;
    }

    /**
     * Constructor para la edición de un alumno existente.
     *
     * @param libro El alumno a editar.
     */
    public LibrosController(Libro libro) {
        this.libro = libro;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        this.imagen = null;

        if (libro != null) {
            // Rellenar los campos con los datos del deportista existente
            txtTitulo.setText(libro.getTitulo());
            txtAutor.setText(libro.getAutor());
            txtEditorial.setText(libro.getEditorial());
            txtEstado.setText(libro.getEstado());

            if (libro.getPortada() != null) {
                this.imagen = libro.getPortada();
                try {
                    InputStream imagenStream = libro.getPortada().getBinaryStream();
                    foto.setImage(new Image(imagenStream));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                btnFotoBorrar.setDisable(false);
            } else {
                // Si no tiene foto, cargar la imagen predeterminada (null.jpg)
                foto.setImage(new Image(getClass().getResourceAsStream("/com/benat/cano/biblioteca/images/null.jpg")));
                btnFotoBorrar.setDisable(true);
            }
    }}
    /**
     * Método para borrar la foto del libro.
     */
    @FXML
    void borrarFoto(ActionEvent event) {
        imagenSeleccionada = null;
        foto.setImage(new Image(getClass().getResourceAsStream("/com/benat/cano/biblioteca/images/null.jpg")));
        btnFotoBorrar.setDisable(true);
    }

    /**
     * Método para cancelar y cerrar la ventana.
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtTitulo.getScene().getWindow();
        stage.close();
    }

    /**
     * Método para guardar o actualizar un libro.
     */
    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = validar();

        if (!errores.isEmpty()) {
            alerta(errores);
        } else {
            Libro nuevo = new Libro();
            nuevo.setTitulo(txtTitulo.getText());
            nuevo.setAutor(txtAutor.getText());
            nuevo.setEditorial(txtAutor.getText());
            nuevo.setEstado(txtAutor.getText());
            nuevo.setPortada(this.imagen);

            Libro existente = DaoLibro.getLibroPorTitulo(txtTitulo.getText());
            if (/*!existente.getAutor().equals(nuevo.getAutor())*/existente==null ) {//esto seria que tienen mismo titulo y autor

                if (this.imagen == null) {
                    try {
                        // Usa una imagen predeterminada en caso de que la imagen esté ausente
                        URL resourceUrl = getClass().getResource("/com/benat/cano/biblioteca/images/null.jpg");
                        File imagenDefault = null;
                        if (resourceUrl != null) {
                            imagenDefault = new File(resourceUrl.toURI());
                        } else {
                            System.err.println("Imagen predeterminada no encontrada.");
                        }

                        Blob defaultBlob = DaoLibro.convertFileToBlob(imagenDefault);
                        nuevo.setPortada(defaultBlob);
                    } catch (Exception e) {
                        errores.add("Error al cargar la imagen predeterminada.");
                        alerta(errores);
                        return;
                    }
                } else {
                    nuevo.setPortada(this.imagen);
                }
                nuevo.setBaja(0);
                    int id = DaoLibro.insertar(nuevo);
                    if (id == -1) {
                        ArrayList<String> failMessages = new ArrayList<>();
                        failMessages.add(resources.getString("save.fail"));
                        alerta(failMessages);
                    } else {
                        ArrayList<String> successMessages = new ArrayList<>();
                        successMessages.add(resources.getString("save.athlete"));
                        confirmacion(successMessages);
                        Stage stage = (Stage) txtTitulo.getScene().getWindow();
                        stage.close();
                    }

            } else {
                ArrayList<String> failMessages = new ArrayList<>();
                failMessages.add(resources.getString("duplicate.athlete"));
                alerta(failMessages);
            }/*else {
                // Actualizar deportista existente
                if (DaoDeportista.modificar(this.deportista, nuevo)) {
                    ArrayList<String> successMessages = new ArrayList<>();
                    successMessages.add(resources.getString("update.athlete"));
                    confirmacion(successMessages);
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                } else {
                    ArrayList<String> failMessages = new ArrayList<>();
                    failMessages.add(resources.getString("save.fail"));
                    alerta(failMessages);
                }
            }*/
        }
    }

    /**
     * Método para seleccionar una imagen para el libro.
     */
    @FXML
    void seleccionImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resources.getString("athlete.photo.chooser"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(null);
        try {
            double kbs = (double) file.length() / 1024;
            if (kbs > 64) {
                ArrayList<String> failMessages = new ArrayList<>();
                failMessages.add(resources.getString("athlete.photo.chooser.size"));
                alerta(failMessages);
            } else {
                InputStream imagen = new FileInputStream(file);
                Blob blob = DaoLibro.convertFileToBlob(file);
                this.imagen = blob;
                foto.setImage(new Image(imagen));
                btnFotoBorrar.setDisable(false);
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("Imagen no seleccionada");
        } catch (SQLException e) {
            ArrayList<String> failMessages = new ArrayList<>();
            failMessages.add(resources.getString("athlete.photo.chooser.fail"));
            alerta(failMessages);
        }
    }

    /**
     * Valida los datos ingresados del libro.
     */
    private ArrayList<String> validar() {
        ArrayList<String> errores = new ArrayList<>();

        if (txtTitulo.getText().isEmpty()) {
            errores.add("El título del libro es obligatorio.");
        }
        if (txtAutor.getText().isEmpty()) {
            errores.add("El autor del libro es obligatorio.");
        }
        if (txtEditorial.getText().isEmpty()) {
            errores.add("El código del libro es obligatorio.");
        }
        if (txtEstado.getText().isEmpty()) {
            errores.add("El número de páginas es obligatorio.");
        }
        return errores;
    }

    /**
     * Muestra una alerta con los mensajes de error.
     */
    private void alerta(ArrayList<String> mensajes) {
        String contenido = String.join("\n", mensajes);
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("Error");
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Muestra una confirmación con los mensajes de éxito.
     */
    private void confirmacion(ArrayList<String> mensajes) {
        String contenido = String.join("\n", mensajes);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Información");
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }


}
