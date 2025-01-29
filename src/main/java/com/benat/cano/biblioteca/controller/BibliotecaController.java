package com.benat.cano.biblioteca.controller;

import com.benat.cano.biblioteca.app.App;
import com.benat.cano.biblioteca.dao.DaoAlumno;
import com.benat.cano.biblioteca.dao.DaoHistoricoPrestamo;
import com.benat.cano.biblioteca.dao.DaoLibro;
import com.benat.cano.biblioteca.dao.DaoPrestamo;
import com.benat.cano.biblioteca.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Supplier;

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
    private RadioButton radioNombre;

    @FXML
    private RadioButton radioCodigo;

    @FXML
    private RadioButton radioOtro;

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
        ToggleGroup grupoBusqueda = new ToggleGroup();
        radioNombre.setToggleGroup(grupoBusqueda);
        radioCodigo.setToggleGroup(grupoBusqueda);
        radioOtro.setToggleGroup(grupoBusqueda);
        radioNombre.setSelected(true);
    }

    /**
     * Filtra las entidades mostradas en la tabla según el texto ingresado en el campo de búsqueda.
     * El filtro es sensible al texto y buscará coincidencias en los nombres de las entidades.
     */
    private void filtrar() {
        String valor = txtNombre.getText();
        if (valor == null || valor.isEmpty()) {
            // Si no hay texto, mostramos toda la lista
            tablaVista.setItems(lstEntera);
            return;
        }

        valor = valor.toLowerCase();
        lstFiltrada.clear(); // Limpiamos la lista filtrada

        boolean buscarPorNombre = radioNombre.isSelected();
        boolean buscarPorCodigo = radioCodigo.isSelected();// Verifica qué radio button está seleccionado

        // Iteramos por todos los elementos de lstEntera
        for (Object item : lstEntera) {
            String criterioBusqueda = "";
            if (buscarPorNombre) {
                if (item instanceof Alumno) {
                    criterioBusqueda = ((Alumno) item).getNombre();
                } else if (item instanceof Libro) {
                    criterioBusqueda = ((Libro) item).getTitulo();
                }else if (item instanceof Prestamo) {
                    criterioBusqueda = ((Prestamo) item).getAlumno().getDni();
                }else{
                    criterioBusqueda = ((HistoricoPrestamos) item).getAlumno().getDni();
                }
            } else if(buscarPorCodigo){
                if (item instanceof Alumno) {
                    criterioBusqueda = ((Alumno) item).getDni();
                } else if (item instanceof Libro) {
                    criterioBusqueda = String.valueOf(((Libro) item).getCodigo());
                }
                else if (item instanceof Prestamo) {
                    criterioBusqueda = String.valueOf(((Prestamo) item).getLibro().getCodigo());
                }else{
                    criterioBusqueda = String.valueOf(((HistoricoPrestamos) item).getLibro().getCodigo());
                }
            }else{
                if (item instanceof Alumno) {
                    criterioBusqueda = ((Alumno) item).getApellido1();
                } else if (item instanceof Libro) {
                    criterioBusqueda =((Libro) item).getAutor();
                }
                else if (item instanceof Prestamo) {
                    criterioBusqueda = String.valueOf(((Prestamo) item).getFecha_prestamo());
                }else{
                    criterioBusqueda = String.valueOf(((HistoricoPrestamos) item).getFecha_devolucion());
                }
            }
            // Convertimos a minúsculas para hacer la búsqueda insensible al caso
            if (criterioBusqueda != null && criterioBusqueda.toLowerCase().contains(valor)) {
                lstFiltrada.add(item);  // Añadimos el item a la lista filtrada si coincide
            }
        }

        // Actualizamos la vista con los elementos filtrados
        tablaVista.setItems(lstFiltrada);
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
        String prestamos = resources.getString("borrows");
        String historicos = resources.getString("historico");


        // Obtener el valor seleccionado del ComboBox
        String seleccion = comboBoxDatos.getValue();

        // Limpiar la tabla antes de actualizarla
        tablaVista.getItems().clear();
        tablaVista.getColumns().clear(); // Limpiar columnas antes de configurar nuevas

        // Usar if-else para comparar las opciones seleccionadas
        if (alumnos.equals(seleccion)) {
            cargarAlumnos();
            radioNombre.setText("Nombre");
            radioCodigo.setText("DNI");
            radioOtro.setText("Apellido1");
        } else if (libros.equals(seleccion)) {
            cargarLibros();
            radioNombre.setText("Titulo");
            radioCodigo.setText("Código");
            radioOtro.setText("Autor");
        } else if (prestamos.equals(seleccion)) {
            radioNombre.setText("DNI Alumno");
            radioCodigo.setText("Código libro");
            radioOtro.setText("Fecha");
            cargarPrestamos();
        } else if (historicos.equals(seleccion)) {
            radioNombre.setText("DNI Alumno");
            radioCodigo.setText("Código libro");
            radioOtro.setText("Fecha Devolucion");
            cargarHistorico();
        }
    }

    private void cargarHistorico() {
        ObservableList<HistoricoPrestamos> prestamos = DaoHistoricoPrestamo.cargarListado();

        // Limpiar la lista antes de agregar los nuevos datos
        lstEntera.clear();

        // Añadir los préstamos a la lista observable
        lstEntera.addAll(prestamos);
        tablaVista.setItems(lstEntera); // Establecer la lista como los datos de la tabla

        // Limpiar las columnas existentes y crear nuevas


        // Columna de ID del préstamo
        TableColumn<HistoricoPrestamos, Integer> colId = new TableColumn<>("ID Préstamo");
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_prestamo()).asObject());

        // Columna de Alumno
        TableColumn<HistoricoPrestamos, String> colAlumno = new TableColumn<>("DNI Alumno");
        colAlumno.setCellValueFactory(cellData -> {
            Alumno alumno = cellData.getValue().getAlumno();
            return alumno != null ? new SimpleStringProperty(alumno.getDni()) : new SimpleStringProperty("");
        });

        // Columna de Libro
        TableColumn<HistoricoPrestamos, Integer> colLibro = new TableColumn<>("Código Libro");
        colLibro.setCellValueFactory(cellData -> {
            Libro libro = cellData.getValue().getLibro();
            return libro != null ? new SimpleIntegerProperty(libro.getCodigo()).asObject() : new SimpleIntegerProperty(0).asObject();
        });

        // Columna de Fecha de Préstamo
        TableColumn<HistoricoPrestamos, String> colFecha = new TableColumn<>("Fecha de Préstamo");
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFecha_prestamo().toString()));
        TableColumn<HistoricoPrestamos, String> colFecha2 = new TableColumn<>("Fecha de Devolución");
        colFecha2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFecha_devolucion().toString()));

        tablaVista.getColumns().clear();
        tablaVista.getColumns().addAll(colId, colAlumno, colLibro, colFecha,colFecha2);
    }

    private void cargarPrestamos() {
        // Obtener la lista de préstamos desde el DAO
        ObservableList<Prestamo> prestamos = DaoPrestamo.cargarListado();

        // Limpiar la lista antes de agregar los nuevos datos
        lstEntera.clear();

        // Añadir los préstamos a la lista observable
        lstEntera.addAll(prestamos);
        tablaVista.setItems(lstEntera); // Establecer la lista como los datos de la tabla

        // Limpiar las columnas existentes y crear nuevas


        // Columna de ID del préstamo
        TableColumn<Prestamo, Integer> colId = new TableColumn<>("ID Préstamo");
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_prestamo()).asObject());

        // Columna de Alumno
        TableColumn<Prestamo, String> colAlumno = new TableColumn<>("DNI Alumno");
        colAlumno.setCellValueFactory(cellData -> {
            Alumno alumno = cellData.getValue().getAlumno();
            return alumno != null ? new SimpleStringProperty(alumno.getDni()) : new SimpleStringProperty("");
        });

        // Columna de Libro
        TableColumn<Prestamo, Integer> colLibro = new TableColumn<>("Código Libro");
        colLibro.setCellValueFactory(cellData -> {
            Libro libro = cellData.getValue().getLibro();
            return libro != null ? new SimpleIntegerProperty(libro.getCodigo()).asObject() : new SimpleIntegerProperty(0).asObject();
        });

        // Columna de Fecha de Préstamo
        TableColumn<Prestamo, String> colFecha = new TableColumn<>("Fecha de Préstamo");
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFecha_prestamo().toString()));

        tablaVista.getColumns().clear();
        tablaVista.getColumns().addAll(colId, colAlumno, colLibro, colFecha);
    }



    private void cargarLibros() {
        // Lista observable para almacenar los libros
        ObservableList<Libro> libros = DaoLibro.cargarListado();

        lstEntera.clear();
        // Definir la imagen predeterminada (null.jpg)
        String imagenPredeterminada = "/com/benat/cano/biblioteca/images/null.jpg";
        Image imagenNull = new Image(getClass().getResourceAsStream(imagenPredeterminada));
        lstEntera.addAll(libros);
        // Asignar la lista de libros al TableView
        tablaVista.setItems(libros);

        // Crear las columnas de la tabla para los libros
        TableColumn<Libro, Integer> codigoColumna = new TableColumn<>("Código");
        codigoColumna.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCodigo()).asObject());

        TableColumn<Libro, String> tituloColumna = new TableColumn<>("Título");
        tituloColumna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));

        TableColumn<Libro, String> autorColumna = new TableColumn<>("Autor");
        autorColumna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor()));

        TableColumn<Libro, String> editorialColumna = new TableColumn<>("Editorial");
        editorialColumna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEditorial()));

        TableColumn<Libro, String> estadoColumna = new TableColumn<>("Estado");
        estadoColumna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado()));

        // Columna de portada (imagen)
        TableColumn<Libro, ImageView> portadaColumna = new TableColumn<>("Portada");
        portadaColumna.setCellValueFactory(cellData -> {
            Blob portadaBlob = cellData.getValue().getPortada();
            ImageView imageView;

            if (portadaBlob != null) {
                try {
                    byte[] bytes = portadaBlob.getBytes(1, (int) portadaBlob.length());
                    Image img = new Image(new ByteArrayInputStream(bytes));
                    imageView = new ImageView(img);
                } catch (SQLException e) {
                    System.err.println("Error al leer el BLOB de la portada: " + e.getMessage());
                    imageView = new ImageView(imagenNull);
                }
            } else {
                imageView = new ImageView(imagenNull);
            }

            // Ajustar el tamaño de la imagen
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            return new SimpleObjectProperty<>(imageView);
        });

        tablaVista.getColumns().clear();
        tablaVista.getColumns().addAll(codigoColumna, tituloColumna, autorColumna, editorialColumna, estadoColumna, portadaColumna);
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
        TableColumn<Alumno, String> dniColumna = new TableColumn<>(resources.getString("dni"));
        dniColumna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDni()));
        TableColumn<Alumno, String> nombreColumna = new TableColumn<>(resources.getString("name"));
        nombreColumna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Alumno, String> apellido1Columna = new TableColumn<>(resources.getString("surname1"));
        apellido1Columna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido1()));

        TableColumn<Alumno, String> apellido2Columna = new TableColumn<>(resources.getString("surname2"));
        apellido2Columna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido2()));

        // Agregar las columnas a la tabla
        tablaVista.getColumns().clear(); // Limpiar las columnas actuales de la tabla
        tablaVista.getColumns().addAll(dniColumna, nombreColumna, apellido1Columna, apellido2Columna);
    }


    /**
     * Permite editar el elemento seleccionado en la tabla. Abre una ventana modal para editar
     * el elemento (Olimpiada, Deportista, Equipo, Evento, Deporte o Participación).
     *
     * @param o Objeto seleccionado (no se utiliza directamente en este método).
     */

    private void editar(Object o) {
        Object seleccion = tablaVista.getSelectionModel().getSelectedItem();

        if (seleccion != null) {
            String item = comboBoxDatos.getSelectionModel().getSelectedItem();

            if (item.equals(resources.getString("students"))) {
                // Olimpiada
                Alumno alumno = (Alumno) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/benat/cano/biblioteca/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/benat/cano/biblioteca/fxml/alumnos.fxml"), bundle);
                    AlumnosController controlador = new AlumnosController(alumno);  // Pasamos la Participación seleccionada
                    fxmlLoader.setController(controlador);
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    try {
                        Image img = new Image(getClass().getResource("/com/benat/cano/biblioteca/images/logo.png").toString());
                        stage.getIcons().add(img);
                    } catch (Exception e) {
                        System.out.println("error.img " + e.getMessage());
                    }
                    scene.getStylesheets().add(getClass().getResource("/com/benat/cano/biblioteca/estilo/style.css").toExternalForm());
                    stage.setTitle(resources.getString("students"));
                    stage.initOwner(ventana);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    cargarAlumnos();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
                }
            }
        } else {
            alerta(new ArrayList<>(Arrays.asList(resources.getString("select.ed"))));
        }
    }

    /**
     * Elimina un elemento de la tabla según la selección del usuario.
     * Muestra un mensaje de confirmación antes de realizar la eliminación de la entidad.
     * La eliminación está condicionada a si la entidad es eliminable o no.
     *
     * @param o El objeto a eliminar, dependiendo de la selección de la tabla.
     */
    private void borrar(Object o) {
        Object seleccion = tablaVista.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            String item = comboBoxDatos.getSelectionModel().getSelectedItem();

            if (item.equals(resources.getString("students"))) {
                Alumno olimpiada = (Alumno) seleccion;
                if (DaoAlumno.esEliminable(olimpiada)) {
                    mostrarConfirmacionYEliminar(resources.getString("students"), resources.getString("confirm.delete.olympics"),
                            () -> DaoAlumno.eliminar(olimpiada), this::cargarAlumnos);
                } else {
                    alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.olympic"))));
                }

            }else{
                if (item.equals(resources.getString("books"))) {
                    Libro olimpiada = (Libro) seleccion;
                    if (DaoLibro.esEliminable(olimpiada)) {
                        mostrarConfirmacionYEliminar(resources.getString("books"), resources.getString("confirm.delete.olympics"),
                                () -> DaoLibro.eliminar(olimpiada), this::cargarLibros);
                    } else {
                        alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.olympic"))));
                    }

                }
            }
        } else {
            alerta(new ArrayList<>(Arrays.asList(resources.getString("select"))));
        }

    }

    /**
     * Carga los datos en el ComboBox de selección de tipo de elementos, con las opciones disponibles
     * como "Olimpiadas", "Deportistas", "Equipos", "Eventos", "Deportes" y "Participaciones".
     */

    private void cargarDatosComboBox() {
        ObservableList<String> opciones = FXCollections.observableArrayList(
                resources.getString("students"), resources.getString("borrows"), resources.getString("books"),resources.getString("historico")
        );
        comboBoxDatos.setItems(opciones);
    }

    @FXML
    void aniadir(ActionEvent event) {
        String seleccion = comboBoxDatos.getSelectionModel().getSelectedItem();
        if (seleccion.equals(resources.getString("students"))) {

            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/benat/cano/biblioteca/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/benat/cano/biblioteca/fxml/alumnos.fxml"), bundle);
                AlumnosController controlador = new AlumnosController();  // Pasamos la Participación seleccionada
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/benat/cano/biblioteca/images/logo.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/benat/cano/biblioteca/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("students"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarAlumnos();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        }else{
            if (seleccion.equals(resources.getString("books"))) {

                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/benat/cano/biblioteca/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/benat/cano/biblioteca/fxml/libros.fxml"), bundle);
                    LibrosController controlador = new LibrosController();  // Pasamos la Participación seleccionada
                    fxmlLoader.setController(controlador);
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    try {
                        Image img = new Image(getClass().getResource("/com/benat/cano/biblioteca/images/logo.png").toString());
                        stage.getIcons().add(img);
                    } catch (Exception e) {
                        System.out.println("error.img " + e.getMessage());
                    }
                    scene.getStylesheets().add(getClass().getResource("/com/benat/cano/biblioteca/estilo/style.css").toExternalForm());
                    stage.setTitle(resources.getString("students"));
                    stage.initOwner(ventana);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    cargarLibros();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
                }
            }
        }
    }
    /**
     * Muestra una alerta de confirmación para la eliminación de una entidad seleccionada.
     * Si el usuario confirma, se realiza la eliminación y se actualiza la vista correspondiente.
     *
     * @param tipoElemento El nombre de la entidad que se va a eliminar (ej. "Olimpiada", "Deportista").
     * @param mensajeConfirmacion El mensaje de confirmación a mostrar.
     * @param eliminacion La acción a ejecutar para eliminar la entidad.
     * @param recargar La acción para recargar la vista después de la eliminación.
     */
    private void mostrarConfirmacionYEliminar(String tipoElemento, String mensajeConfirmacion, Supplier<Boolean> eliminacion, Runnable recargar) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(tablaVista.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle(resources.getString("confirmation"));
        alert.setContentText(mensajeConfirmacion);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (eliminacion.get()) {  // Llamar a .get() en lugar de .run() para el Supplier
                recargar.run();
                confirmacion(""+tipoElemento + " eliminado exitosamente.");
            } else {
                alerta(new ArrayList<>(Arrays.asList("No se pudo eliminar el " + tipoElemento.toLowerCase() + ".")));
            }
        }
    }
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

    public void generarReporte1(ActionEvent actionEvent) {
    }

    public void generarReporte2(ActionEvent actionEvent) {
    }

    public void generarReporte3(ActionEvent actionEvent) {
    }
}
