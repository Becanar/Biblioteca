package com.benat.cano.biblioteca.controller;

import com.benat.cano.biblioteca.app.App;
import com.benat.cano.biblioteca.dao.DaoAlumno;
import com.benat.cano.biblioteca.model.Alumno;
import com.benat.cano.biblioteca.model.Libro;
import com.benat.cano.biblioteca.model.Prestamo;
import com.benat.cano.biblioteca.model.Propiedades;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
    private ComboBox<String> comboBoxDatos;

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

    private ObservableList<Object> lstEntera = FXCollections.observableArrayList();
    private ObservableList<Object> lstFiltrada = FXCollections.observableArrayList();

    /**
     * Método de inicialización que se ejecuta al cargar la vista.
     * Se encargará de cargar los datos en los controles (ComboBox, TableView),
     * configurar las acciones de los botones y permitir la búsqueda a través del campo de texto.
     */
    @FXML
    private void initialize() {

        cargarDatosComboBox();
        comboBoxDatos.setValue(resources.getString("students"));
        actualizarTabla(null);
        comboBoxDatos.setOnAction(this::actualizarTabla);
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem(resources.getString("edit"));
        editItem.setOnAction(event -> editar(null));


        MenuItem deleteItem = new MenuItem(resources.getString("delete"));
        deleteItem.setOnAction(event -> borrar(null));

        contextMenu.getItems().addAll(editItem, deleteItem);

        tablaVista.setContextMenu(contextMenu);
        rootPane.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.F) {
                txtNombre.requestFocus();
                event.consume();
            }
        });
        txtNombre.setOnKeyTyped(keyEvent -> filtrar());
    }
    /**
     * Filtra las entidades mostradas en la tabla según el texto ingresado en el campo de búsqueda.
     * El filtro es sensible al texto y buscará coincidencias en los nombres de las entidades.
     */
    private void filtrar() {
        String valor = txtNombre.getText();
        if (valor == null || valor.isEmpty()) {
            tablaVista.setItems(lstEntera);
        } else {
            valor = valor.toLowerCase();
            lstFiltrada.clear();
            for (Object item : lstEntera) {
                String nombre;
                if (item instanceof Alumno) {
                    nombre = ((Alumno) item).getNombre();
                } else if (item instanceof Libro) {
                    nombre = ((Libro) item).getTitulo();
                } /*else if (item instanceof Prestamo) {
                    nombre = ((Equipo) item).getNombre();
                } else if (item instanceof Evento) {
                    nombre = ((Evento) item).getNombre();
                } else if (item instanceof Deporte) {
                    nombre = ((Deporte) item).getNombre();
                } */else {
                    continue;
                }
                if (nombre.toLowerCase().contains(valor)) {
                    lstFiltrada.add(item);
                }
            }
            tablaVista.setItems(lstFiltrada);
        }
    }
    /**
     * Actualiza la tabla según el valor seleccionado en el ComboBox.
     * Dependiendo de la opción seleccionada (Olimpiadas, Deportistas, Equipos, Eventos, Deportes o Participaciones),
     * carga los datos correspondientes en la tabla.
     *
     * @param event El evento que se genera cuando se activa la acción (por ejemplo, un clic de botón).
     */
    public void actualizarTabla(ActionEvent event) {
        // Cargar el ResourceBundle para acceder a los mensajes
        String idioma = Propiedades.getValor("language");
        ResourceBundle bundle = ResourceBundle.getBundle("/com/benat/cano/biblioteca/languages/lan", new Locale(idioma));

        // Obtener el valor de la clave "participations" desde el archivo de recursos
        String alumnos = resources.getString("students");
        String libros = resources.getString("books");
        String prestamos = resources.getString("teams");
        //String historico = resources.getString("historico");



        // Obtener el valor seleccionado del ComboBox
        String seleccion = comboBoxDatos.getValue();

        // Limpiar la tabla antes de actualizarla
        tablaVista.getItems().clear();
        tablaVista.getColumns().clear(); // Limpiar columnas antes de configurar nuevas

        // Usar if-else para comparar las opciones seleccionadas
        if (alumnos.equals(seleccion)) {
            cargarAlumnos();
        } else if (libros.equals(seleccion)) {
            cargarLibros();
        } else if (prestamos.equals(seleccion)) {
            cargarPrestamos();
        } /*else if (historico.equals(seleccion)) {
            cargarHistorico();
        }*/
    }

    private void cargarHistorico() {
    }

    private void cargarPrestamos() {
    }

    private void cargarLibros() {
    }

    private void cargarAlumnos() {
        // Obtener la lista de alumnos desde el DAO
        ObservableList<Alumno> alumnos = DaoAlumno.cargarListado();

        // Limpiar la lista antes de agregar los nuevos datos
        lstEntera.clear();

        // Añadir los alumnos a la lista observable
        lstEntera.addAll(alumnos);
        tablaVista.setItems(lstEntera); // Establecer la lista como los datos de la tabla

        // Crear las columnas de la tabla
        TableColumn<Alumno, String> nombreColumna = new TableColumn<>(resources.getString("name"));
        nombreColumna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Alumno, String> apellido1Columna = new TableColumn<>(resources.getString("surname1"));
        apellido1Columna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido1()));

        TableColumn<Alumno, String> apellido2Columna = new TableColumn<>(resources.getString("surname2"));
        apellido2Columna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido2()));

        // Agregar las columnas a la tabla
        tablaVista.getColumns().clear(); // Limpiar las columnas actuales de la tabla
        tablaVista.getColumns().addAll(nombreColumna, apellido1Columna, apellido2Columna);
    }


    /**
     * Permite editar el elemento seleccionado en la tabla. Abre una ventana modal para editar
     * el elemento (Olimpiada, Deportista, Equipo, Evento, Deporte o Participación).
     *
     * @param o Objeto seleccionado (no se utiliza directamente en este método).
     */

    private void editar(Object o) {
        // Obtener el objeto seleccionado desde la tabla
       /* Object seleccion = tablaVista.getSelectionModel().getSelectedItem();

        if (seleccion != null) {
            String item = comboBoxDatos.getSelectionModel().getSelectedItem();

            if (item.equals(resources.getString("olympics"))) {
                // Olimpiada
                Olimpiada olimpiada = (Olimpiada) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/olimpiadasV.fxml"), bundle);
                    OlimpiadasVController controlador = new OlimpiadasVController(olimpiada);  // Pasamos la Olimpiada seleccionada
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

            } else if (item.equals(resources.getString("athletes"))) {
                // Deportista
                Deportista deportista = (Deportista) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/deportista.fxml"), bundle);
                    DeportistaController controlador = new DeportistaController(deportista);  // Pasamos el Deportista seleccionado
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

            } else if (item.equals(resources.getString("teams"))) {
                // Equipo
                Equipo equipo = (Equipo) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/equipo.fxml"), bundle);
                    EquiposController controlador = new EquiposController(equipo);  // Pasamos el Equipo seleccionado
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
                    stage.setTitle(resources.getString("teams"));
                    stage.initOwner(ventana);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    cargarEquipos();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
                }

            } else if (item.equals(resources.getString("events"))) {
                // Evento
                Evento evento = (Evento) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/evento.fxml"), bundle);
                    EventoController controlador = new EventoController(evento);  // Pasamos el Evento seleccionado
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

            } else if (item.equals(resources.getString("sports"))) {
                // Deporte
                Deporte deporte = (Deporte) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/deportes.fxml"), bundle);
                    DeportesController controlador = new DeportesController(deporte);  // Pasamos el Deporte seleccionado
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

            } else if (item.equals(resources.getString("participations"))) {
                // Participación
                Participacion participacion = (Participacion) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/participacion.fxml"), bundle);
                    ParticipacionController controlador = new ParticipacionController(participacion);  // Pasamos la Participación seleccionada
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
        } else {
            alerta(new ArrayList<>(Arrays.asList(resources.getString("select.ed"))));
        }*/
    }
    /**
     * Elimina un elemento de la tabla según la selección del usuario.
     * Muestra un mensaje de confirmación antes de realizar la eliminación de la entidad.
     * La eliminación está condicionada a si la entidad es eliminable o no.
     *
     * @param o El objeto a eliminar, dependiendo de la selección de la tabla.
     */
    private void borrar(Object o) {/*
        Object seleccion = tablaVista.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            String item = comboBoxDatos.getSelectionModel().getSelectedItem();

            if (item.equals(resources.getString("olympics"))) {
                // Olimpiada
                Olimpiada olimpiada = (Olimpiada) seleccion;
                if (DaoOlimpiada.esEliminable(olimpiada)) {
                    mostrarConfirmacionYEliminar(resources.getString("olympic"), resources.getString("confirm.delete.olympics"),
                            () -> DaoOlimpiada.eliminar(olimpiada), this::cargarOlimpiadas);
                } else {
                    alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.olympic"))));
                }

            } else if (item.equals(resources.getString("athletes"))) {
                // Deportista
                Deportista deportista = (Deportista) seleccion;
                if (DaoDeportista.esEliminable(deportista)) {
                    mostrarConfirmacionYEliminar(resources.getString("athlete"), resources.getString("confirm.delete.athlete"),
                            () -> DaoDeportista.eliminar(deportista), this::cargarDeportistas);
                } else {

                    alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.athlete"))));
                }

            } else if (item.equals(resources.getString("teams"))) {
                // Equipo
                Equipo equipo = (Equipo) seleccion;
                if (DaoEquipo.esEliminable(equipo)) {
                    mostrarConfirmacionYEliminar(resources.getString("team"), resources.getString("confirm.delete.team"),
                            () -> DaoEquipo.eliminar(equipo), this::cargarEquipos);
                } else {
                    alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.team"))));
                }

            } else if (item.equals(resources.getString("events"))) {
                // Evento
                Evento evento = (Evento) seleccion;
                if (DaoEvento.esEliminable(evento)) {
                    mostrarConfirmacionYEliminar(resources.getString("event"),  resources.getString("confirm.delete.event"),
                            () -> DaoEvento.eliminar(evento), this::cargarEventos);
                } else {
                    alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.event"))));
                }

            } else if (item.equals(resources.getString("sports"))) {
                // Deporte
                Deporte deporte = (Deporte) seleccion;
                if (DaoDeporte.esEliminable(deporte)) {
                    mostrarConfirmacionYEliminar(resources.getString("sport"),  resources.getString("confirm.delete.sport"),
                            () -> DaoDeporte.eliminar(deporte), this::cargarDeportes);
                } else {
                    alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.sport"))));

                }

            } else if (item.equals(resources.getString("participations"))) {
                // Participación
                Participacion participacion = (Participacion) seleccion;
                mostrarConfirmacionYEliminar(resources.getString("participation"), resources.getString("confirm.delete.participation"),
                        () -> DaoParticipacion.eliminar(participacion), this::cargarParticipaciones);
            }
        } else {
            alerta(new ArrayList<>(Arrays.asList(resources.getString("select"))));
        }

*/
    }
    /**
     * Carga los datos en el ComboBox de selección de tipo de elementos, con las opciones disponibles
     * como "Olimpiadas", "Deportistas", "Equipos", "Eventos", "Deportes" y "Participaciones".
     */

    private void cargarDatosComboBox() {
        ObservableList<String> opciones = FXCollections.observableArrayList(
                resources.getString("students"), resources.getString("borrows"), resources.getString("books")
        );
        comboBoxDatos.setItems(opciones);
    }
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
