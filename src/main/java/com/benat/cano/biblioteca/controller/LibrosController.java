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
/**
 * Controlador de la vista para gestionar los libros en la biblioteca.
 * Permite crear, editar y gestionar la foto de portada de un libro.
 */
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
    /**
     * Método que se ejecuta al inicializar la vista del controlador.
     * Si el libro no es nulo, se cargan los datos del libro para su edición.
     *
     * @param url La URL de la vista FXML.
     * @param resourceBundle El ResourceBundle que contiene los textos traducidos.
     */
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
        }
    }

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
     * Método que guarda los datos del libro.
     * Si el libro es nuevo, lo inserta en la base de datos.
     * Si el libro es editado, actualiza los datos existentes.
     *
     * @param event El evento generado al hacer clic en el botón de guardar.
     */
    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = validar();

        if (!errores.isEmpty()) {
            alerta(errores);
            return;
        }

        boolean esEdicion = (this.libro != null && this.libro.getCodigo() > 0);

        Libro nuevo = new Libro();
        nuevo.setTitulo(txtTitulo.getText().trim());
        nuevo.setAutor(txtAutor.getText().trim());
        nuevo.setEditorial(txtEditorial.getText().trim());
        nuevo.setEstado(txtEstado.getText().trim());

        if (esEdicion) {
            nuevo.setCodigo(this.libro.getCodigo());
            nuevo.setBaja(this.libro.getBaja());
            nuevo.setPortada(this.libro.getPortada());
        } else {
            nuevo.setBaja(0);
        }

        // Verificar si el libro ya existe antes de insertar (solo si es nuevo)
        if (!esEdicion) {
            Libro libroExistente = DaoLibro.getLibroPorTituloYAutor(nuevo.getTitulo(), nuevo.getAutor());
            if (libroExistente != null) {
                ArrayList<String> failMessages = new ArrayList<>();
                failMessages.add(resources.getString("duplicate.book"));
                alerta(failMessages);
                return; // Evitar que se inserte
            }
        }

        // Si no se ha seleccionado una imagen, usar la predeterminada
        if (this.imagen == null) {
            try {
                URL resourceUrl = getClass().getResource("/com/benat/cano/biblioteca/images/null.jpg");
                if (resourceUrl != null) {
                    File imagenDefault = new File(resourceUrl.toURI());
                    Blob defaultBlob = DaoLibro.convertFileToBlob(imagenDefault);
                    nuevo.setPortada(defaultBlob);
                } else {
                    errores.add(resources.getString("error.img"));
                }
            } catch (Exception e) {
                errores.add(resources.getString("error.img"));
                alerta(errores);
                return;
            }
        } else {
            nuevo.setPortada(this.imagen);
        }

        // Si estamos editando, modificar
        if (esEdicion) {
            if (DaoLibro.modificar(nuevo)) {
                ArrayList<String> mensajes = new ArrayList<>();
                mensajes.add(resources.getString("update.book"));
                confirmacion(mensajes);
                Stage stage = (Stage) txtTitulo.getScene().getWindow();
                stage.close();
            } else {
                ArrayList<String> mensajes = new ArrayList<>();
                mensajes.add(resources.getString("save.fail"));
                alerta(mensajes);
            }
        } else {
            // Insertar nuevo
            int id = DaoLibro.insertar(nuevo);
            if (id == -1) {
                ArrayList<String> mensajes = new ArrayList<>();
                mensajes.add(resources.getString("save.fail"));
                alerta(mensajes);
            } else {
                ArrayList<String> mensajes = new ArrayList<>();
                mensajes.add(resources.getString("save.book"));
                confirmacion(mensajes);
                Stage stage = (Stage) txtTitulo.getScene().getWindow();
                stage.close();
            }
        }
    }

    /**
     * Método para seleccionar una imagen para el libro.
     */
    @FXML
    void seleccionImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resources.getString("book.photo.chooser"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(null);
        try {
            double kbs = (double) file.length() / 1024;
            if (kbs > 64) {
                ArrayList<String> failMessages = new ArrayList<>();
                failMessages.add(resources.getString("book.photo.chooser.size"));
                alerta(failMessages);
            } else {
                InputStream imagen = new FileInputStream(file);
                Blob blob = DaoLibro.convertFileToBlob(file);
                this.imagen = blob;
                foto.setImage(new Image(imagen));
                btnFotoBorrar.setDisable(false);
            }
        } catch (IOException | NullPointerException e) {
            ArrayList<String> failMessages = new ArrayList<>();
            failMessages.add(resources.getString("save.fail"));
            alerta(failMessages);
        } catch (SQLException e) {
            ArrayList<String> failMessages = new ArrayList<>();
            failMessages.add(resources.getString("book.photo.chooser.fail"));
            alerta(failMessages);
        }
    }

    /**
     * Valida los datos ingresados del libro.
     */
    private ArrayList<String> validar() {
        ArrayList<String> errores = new ArrayList<>();

        if (txtTitulo.getText().isEmpty()) {
            errores.add(resources.getString("validate.book.name"));
        }
        if (txtAutor.getText().isEmpty()) {
            errores.add(resources.getString("validate.book.id"));
        }
        if (txtEditorial.getText().isEmpty()) {
            errores.add(resources.getString("validate.book.editorial"));
        }
        if (txtEstado.getText().isEmpty()) {
            errores.add(resources.getString("validate.book.estate"));
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
        alerta.setTitle(resources.getString("error"));
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
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }


}
