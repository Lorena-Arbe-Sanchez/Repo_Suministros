package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private VBox contentArea;

    @FXML
    public void initialize() {
        mostrarInicio();
    }

    @FXML
    private void mostrarInicio() {

        Label titulo = new Label("Inicio");
        titulo.getStyleClass().add("page-title");

        Label descripcion = new Label("Resumen general de la aplicación");
        descripcion.getStyleClass().add("page-description");

        GridPane tarjetas = new GridPane();
        tarjetas.setHgap(20);
        tarjetas.setVgap(20);

        tarjetas.add(crearTarjeta("Proveedores", "—"), 0, 0);
        tarjetas.add(crearTarjeta("Piezas", "—"), 1, 0);
        tarjetas.add(crearTarjeta("Proyectos", "—"), 0, 1);
        tarjetas.add(crearTarjeta("Suministros", "—"), 1, 1);

        Label informacion = new Label(
                "Los datos se cargarán desde la base de datos"
        );
        informacion.getStyleClass().add("info-text");

        contentArea.getChildren().setAll(
                titulo,
                descripcion,
                tarjetas,
                informacion
        );
    }

    private VBox crearTarjeta(String nombre, String valor) {

        Label etiqueta = new Label(nombre);
        etiqueta.getStyleClass().add("card-title");

        Label numero = new Label(valor);
        numero.getStyleClass().add("card-value");

        VBox tarjeta = new VBox(12, etiqueta, numero);
        tarjeta.getStyleClass().add("dashboard-card");

        tarjeta.setPrefWidth(260);
        tarjeta.setPrefHeight(140);

        return tarjeta;
    }

    private void mostrarPagina(String titulo, String descripcion) {

        Label encabezado = new Label(titulo);
        encabezado.getStyleClass().add("page-title");

        Label texto = new Label(descripcion);
        texto.getStyleClass().add("page-description");

        contentArea.getChildren().setAll(encabezado, texto);
    }

    @FXML
    private void mostrarProveedores() {
        mostrarPagina("Proveedores", "Gestión de proveedores");
    }

    @FXML
    private void mostrarPiezas() {
        mostrarPagina("Piezas", "Gestión de piezas");
    }

    @FXML
    private void mostrarProyectos() {
        mostrarPagina("Proyectos", "Gestión de proyectos");
    }

    @FXML
    private void mostrarSuministros() {
        mostrarPagina("Suministros", "Gestión de suministros");
    }

    @FXML
    private void mostrarAnalisis() {
        mostrarPagina("Análisis y estadísticas", "Consultas e informes de la aplicación");
    }

    @FXML
    private void mostrarExportaciones() {
        mostrarPagina("Exportaciones", "Exportación de información a XML y JSON");
    }

    @FXML
    private void mostrarMetadatos() {
        mostrarPagina("Metadatos", "Información sobre la estructura de la base de datos");
    }

    @FXML
    private void mostrarAyuda() {
        mostrarPagina("Ayuda", "Información y funcionamiento de la aplicación");
    }
}