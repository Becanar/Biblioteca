package com.benat.cano.biblioteca.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    private TableView<?> tablaVista;

    @FXML
    private TextField txtNombre;

    @FXML
    void aniadir(ActionEvent event) {

    }

    @FXML
    void cambiarEsp(ActionEvent event) {

    }

    @FXML
    void cambiarIngles(ActionEvent event) {

    }

}
