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
                failMessages.add("Ya existe un libro con este título y autor.");
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
                    System.err.println("Imagen predeterminada no encontrada.");
                }
            } catch (Exception e) {
                errores.add("Error al cargar la imagen predeterminada.");
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
                mensajes.add("Libro actualizado correctamente.");
                confirmacion(mensajes);
                Stage stage = (Stage) txtTitulo.getScene().getWindow();
                stage.close();
            } else {
                ArrayList<String> mensajes = new ArrayList<>();
                mensajes.add("Error al actualizar el libro.");
                alerta(mensajes);
            }
        } else {
            // Insertar nuevo
            int id = DaoLibro.insertar(nuevo);
            if (id == -1) {
                ArrayList<String> mensajes = new ArrayList<>();
                mensajes.add("Error al guardar el libro.");
                alerta(mensajes);
            } else {
                ArrayList<String> mensajes = new ArrayList<>();
                mensajes.add("Libro guardado correctamente.");
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
