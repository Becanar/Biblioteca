package com.benat.cano.biblioteca.controller;

import com.benat.cano.biblioteca.app.App;
import com.benat.cano.biblioteca.model.Propiedades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class BibliotecaController {

    @FXML
    private MenuBar barraMenu;

    @FXML
    private Button btAniadir;

    @FXML
    private ComboBox<?> comboBoxDatos;

    @FXML
    private ImageView imgOlD;

    @FXML
    private ImageView imgOlI;

    @FXML
    private ImageView imgOlI1;

    @FXML
    private Label lblCombo;

    @FXML
    private Label lblGestion;

    @FXML
    private Menu menuAyuda;

    @FXML
    private MenuItem menuEn;

    @FXML
    private MenuItem menuEs;

    @FXML
    private Menu menuIdioma;

    @FXML
    private HBox panelHueco;

    @FXML
    private FlowPane panelListado;

    @FXML
    private VBox rootPane;

    @FXML
    private TableView tablaVista;

    @FXML
    private TextField txtNombre;

    @FXML
    private ResourceBundle resources;

    @FXML
    void aniadir(ActionEvent event) {
        /*String seleccion = comboBoxDatos.getSelectionModel().getSelectedItem();
        System.out.println(seleccion);
        if (seleccion.equals(resources.getString("olympics"))) {

            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/olimpiadasV.fxml"), bundle);
                OlimpiadasVController controlador = new OlimpiadasVController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("olympics"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarOlimpiadas();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        } else if (seleccion.equals(resources.getString("athletes"))) {
            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/deportista.fxml"), bundle);
                DeportistaController controlador = new DeportistaController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("athletes"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarDeportistas();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        } else if (seleccion.equals(resources.getString("teams"))) {
            // Agregar nuevo Equipo
            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/equipo.fxml"), bundle);
                EquiposController controlador = new EquiposController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle((resources.getString("teams")));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarEquipos();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        } else if (seleccion.equals(resources.getString("events"))) {
            // Agregar nuevo Evento
            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/evento.fxml"), bundle);
                EventoController controlador = new EventoController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("events"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarEventos();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        } else if (seleccion.equals(resources.getString("sports"))) {
            // Agregar nuevo Deporte
            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/deportes.fxml"), bundle);
                DeportesController controlador = new DeportesController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("sports"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarDeportes();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        }else if (seleccion.equals(resources.getString("participations"))) {
            // Agregar nuevo Deporte
            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/participacion.fxml"), bundle);
                ParticipacionController controlador = new ParticipacionController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("participations"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarParticipaciones();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        }
    }*/}

    /**
     * Cambia el idioma de la aplicación a inglés.
     * Este método se ejecuta cuando se selecciona la opción de cambiar a inglés.
     *
     * @param actionEvent El evento que se genera cuando se activa la acción (por ejemplo, un clic de botón).
     */
    public void cambiarIngles(ActionEvent actionEvent) {
        cambiarIdioma("en");
    }
    /**
     * Cambia el idioma de la aplicación a español.
     * Este método se ejecuta cuando se selecciona la opción de cambiar a español.
     *
     * @param actionEvent El evento que se genera cuando se activa la acción (por ejemplo, un clic de botón).
     */
    public void cambiarEsp(ActionEvent actionEvent) {
        cambiarIdioma("es");
    }

    /**
     * Cambia el idioma de la interfaz a uno específico.
     *
     * @param idioma El código del idioma a cambiar (eu, es, en).
     */

    public void cambiarIdioma(String idioma) {
        // Actualizar el archivo db.properties con el nuevo idioma
        Propiedades.setIdioma("language", idioma);

        // Cargar el nuevo ResourceBundle con el idioma seleccionado
        ResourceBundle bundle = ResourceBundle.getBundle("com.benat.cano.biblioteca.languages.lan", new Locale(idioma));

        // Obtener la ventana actual (en este caso el Stage principal)
        Stage stage = (Stage) btAniadir.getScene().getWindow();

        // Actualizar la ventana principal
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/benat/cano/biblioteca/fxml/biblioteca.fxml"), bundle);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(bundle.getString("app.name"));
            stage.setResizable(false);

            try {
                Image img = new Image(getClass().getResource("/com/benat/cano/biblioteca/images/logo.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                System.out.println(bundle.getString("error.img") + e.getMessage());
            }

            scene.getStylesheets().add(getClass().getResource("/com/benat/cano/biblioteca/estilo/style.css").toExternalForm());
            stage.setTitle(bundle.getString("app.name"));
            stage.setScene(scene);
            stage.show();

            // Cierra todas las ventanas auxiliares y vuelve a abrirlas con el idioma actualizado (si es necesario)
            // Ejemplo:
            if (stage.getOwner() != null) {
                Stage owner = (Stage)stage.getOwner();
                // Aquí puedes usar un código para reiniciar o actualizar otras ventanas hijas
                // owner.close();  // O también puedes recargar otras ventanas
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra una alerta con los mensajes proporcionados.
     *
     * @param mensajes Una lista de mensajes que se mostrarán en la alerta.
     */
    private void alerta(ArrayList<String> mensajes) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(resources.getString("advert"));
        alert.setHeaderText(null);

        StringBuilder contenido = new StringBuilder();
        for (String mensaje : mensajes) {
            contenido.append(mensaje).append("\n");
        }

        alert.setContentText(contenido.toString().trim());
        alert.showAndWait();
    }
    /**
     * Muestra una alerta de confirmación con el mensaje proporcionado.
     *
     * @param mensajes Mensaje o lista de mensajes que se mostrarán en la alerta.
     */
    public void confirmacion(String mensajes) {
        String contenido = String.join("\n", mensajes);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

}
