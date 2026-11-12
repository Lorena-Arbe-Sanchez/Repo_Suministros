package gui;

import dao.ProveedorDao;
import dao.PiezaDao;
import dao.ProyectoDao;
import dao.SuministroDao;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import models.Proveedor;

public class MainController {

    @FXML
    private VBox contentArea;

    private ProveedorDao proveedorDao;
    private PiezaDao piezaDao;
    private ProyectoDao proyectoDao;
    private SuministroDao suministroDao;

    private TableView<Proveedor> tablaProveedores;

    @FXML
    public void initialize() {

        try {
            // Crear los DAO
            proveedorDao = new ProveedorDao();
            piezaDao = new PiezaDao();
            proyectoDao = new ProyectoDao();
            suministroDao = new SuministroDao();

            mostrarInicio();

        } catch (RuntimeException e) {
            e.printStackTrace();
            mostrarInicioError();
        }
    }

    // Mostrar la pantalla de inicio
    @FXML
    private void mostrarInicio() {

        String numProveedores = String.valueOf(proveedorDao.findAll().size());
        String numPiezas = String.valueOf(piezaDao.findAll().size());
        String numProyectos = String.valueOf(proyectoDao.findAll().size());
        String numSuministros = String.valueOf(suministroDao.findAll().size());

        Label titulo = new Label("Inicio");
        titulo.getStyleClass().add("page-title");

        Label descripcion = new Label("Resumen general de la aplicación");
        descripcion.getStyleClass().add("page-description");

        GridPane tarjetas = new GridPane();
        tarjetas.setHgap(20);
        tarjetas.setVgap(20);

        tarjetas.add(crearTarjeta("Proveedores", numProveedores), 0, 0);
        tarjetas.add(crearTarjeta("Piezas", numPiezas), 1, 0);
        tarjetas.add(crearTarjeta("Proyectos", numProyectos), 0, 1);
        tarjetas.add(crearTarjeta("Suministros", numSuministros), 1, 1);

        Label informacion = new Label(
                "Datos obtenidos de la base de datos"
        );
        informacion.getStyleClass().add("info-text");

        contentArea.getChildren().setAll(
                titulo,
                descripcion,
                tarjetas,
                informacion
        );
    }

    // Mostrar un mensaje si no se pueden cargar los datos
    private void mostrarInicioError() {

        Label titulo = new Label("Inicio");
        titulo.getStyleClass().add("page-title");

        Label error = new Label(
                "No se han podido cargar los datos de la base de datos"
        );
        error.getStyleClass().add("info-text");

        contentArea.getChildren().setAll(titulo, error);
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

        Label titulo = new Label("Proveedores");
        titulo.getStyleClass().add("page-title");

        Label descripcion = new Label("Listado de proveedores registrados");
        descripcion.getStyleClass().add("page-description");

        tablaProveedores = new TableView<>();

        TableColumn<Proveedor, String> colCod = crearColumna("Código", "cod");
        TableColumn<Proveedor, String> colNombre = crearColumna("Nombre", "nombre");
        TableColumn<Proveedor, String> colApellido1 = crearColumna("Primer apellido", "apellido1");
        TableColumn<Proveedor, String> colApellido2 = crearColumna("Segundo apellido", "apellido2");
        TableColumn<Proveedor, String> colDireccion = crearColumna("Dirección", "direccion");

        tablaProveedores.getColumns().addAll(
                colCod,
                colNombre,
                colApellido1,
                colApellido2,
                colDireccion
        );

        tablaProveedores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            tablaProveedores.getItems().setAll(proveedorDao.findAll());
        } catch (RuntimeException e) {
            e.printStackTrace();

            Label error = new Label("No se han podido cargar los proveedores");
            error.getStyleClass().add("info-text");

            contentArea.getChildren().setAll(titulo, descripcion, error);
            return;
        }

        Button btnNuevo = new Button("Nuevo proveedor");
        btnNuevo.setOnAction(e -> nuevoProveedor());

        Button btnModificar = new Button("Modificar");
        btnModificar.setOnAction(e -> modificarProveedor());

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> eliminarProveedor());

        HBox botones = new HBox(10, btnNuevo, btnModificar, btnEliminar);

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Introduce un texto para buscar...");

        Button btnBuscar = new Button("Buscar");
        Button btnMostrarTodos = new Button("Mostrar todos");

        HBox busqueda = new HBox(10, txtBuscar, btnBuscar, btnMostrarTodos);

        btnBuscar.setOnAction(e -> {

            String texto = txtBuscar.getText().trim();

            if (texto.isEmpty()) {
                mostrarError("Introduce un texto para buscar");
                return;
            }

            try {
                tablaProveedores.getItems().setAll(proveedorDao.buscar(texto));

            } catch (RuntimeException ex) {
                ex.printStackTrace();
                mostrarError("No se ha podido realizar la búsqueda");
            }
        });

        btnMostrarTodos.setOnAction(e -> {

            txtBuscar.clear();

            try {
                tablaProveedores.getItems().setAll(proveedorDao.findAll());

            } catch (RuntimeException ex) {
                ex.printStackTrace();
                mostrarError("No se han podido cargar los proveedores");
            }
        });

        contentArea.getChildren().setAll(
                titulo,
                descripcion,
                botones,
                busqueda,
                tablaProveedores
        );
    }

    private TableColumn<Proveedor, String> crearColumna(String titulo, String propiedad) {

        TableColumn<Proveedor, String> columna = new TableColumn<>(titulo);
        columna.setCellValueFactory(new PropertyValueFactory<>(propiedad));

        return columna;
    }

    private void nuevoProveedor() {

        Dialog<Proveedor> dialog = new Dialog<>();

        dialog.setTitle("Nuevo proveedor");
        dialog.setHeaderText("Introduce los datos del proveedor");

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(guardar, cancelar);

        TextField txtCod = new TextField();
        TextField txtNombre = new TextField();
        TextField txtApellido1 = new TextField();
        TextField txtApellido2 = new TextField();
        TextField txtDireccion = new TextField();

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Código:"), 0, 0);
        formulario.add(txtCod, 1, 0);

        formulario.add(new Label("Nombre:"), 0, 1);
        formulario.add(txtNombre, 1, 1);

        formulario.add(new Label("Primer apellido:"), 0, 2);
        formulario.add(txtApellido1, 1, 2);

        formulario.add(new Label("Segundo apellido:"), 0, 3);
        formulario.add(txtApellido2, 1, 3);

        formulario.add(new Label("Dirección:"), 0, 4);
        formulario.add(txtDireccion, 1, 4);

        dialog.getDialogPane().setContent(formulario);

        dialog.setResultConverter(boton -> {

            if (boton == guardar) {
                return new Proveedor(
                        txtCod.getText().trim(),
                        txtNombre.getText().trim(),
                        txtApellido1.getText().trim(),
                        txtApellido2.getText().trim(),
                        txtDireccion.getText().trim(),
                        null
                );
            }

            return null;
        });

        dialog.showAndWait().ifPresent(proveedor -> {

            if (proveedor.getCod().isEmpty() ||
                    proveedor.getNombre().isEmpty() ||
                    proveedor.getApellido1().isEmpty() ||
                    proveedor.getApellido2().isEmpty() ||
                    proveedor.getDireccion().isEmpty()) {

                mostrarError("Todos los campos son obligatorios");
                return;
            }

            if (proveedorDao.find(proveedor.getCod()) != null) {
                mostrarError("Ya existe un proveedor con ese código");
                return;
            }

            try {
                proveedorDao.create(proveedor);

                tablaProveedores.getItems().setAll(proveedorDao.findAll());

                mostrarInformacion("Proveedor creado correctamente");

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("No se ha podido crear el proveedor");
            }
        });
    }

    private void modificarProveedor() {

        Proveedor seleccionado = tablaProveedores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Selecciona un proveedor de la tabla");
            return;
        }

        Dialog<Boolean> dialog = new Dialog<>();

        dialog.setTitle("Modificar proveedor");
        dialog.setHeaderText("Modifica los datos del proveedor");

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(guardar, cancelar);

        TextField txtCod = new TextField(seleccionado.getCod());
        TextField txtNombre = new TextField(seleccionado.getNombre());
        TextField txtApellido1 = new TextField(seleccionado.getApellido1());
        TextField txtApellido2 = new TextField(seleccionado.getApellido2());
        TextField txtDireccion = new TextField(seleccionado.getDireccion());

        // El código identifica al proveedor y no se puede modificar
        txtCod.setEditable(false);

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Código:"), 0, 0);
        formulario.add(txtCod, 1, 0);

        formulario.add(new Label("Nombre:"), 0, 1);
        formulario.add(txtNombre, 1, 1);

        formulario.add(new Label("Primer apellido:"), 0, 2);
        formulario.add(txtApellido1, 1, 2);

        formulario.add(new Label("Segundo apellido:"), 0, 3);
        formulario.add(txtApellido2, 1, 3);

        formulario.add(new Label("Dirección:"), 0, 4);
        formulario.add(txtDireccion, 1, 4);

        dialog.getDialogPane().setContent(formulario);

        dialog.setResultConverter(boton -> {

            if (boton == guardar) {
                return true;
            }

            return null;
        });

        dialog.showAndWait().ifPresent(resultado -> {

            String nombre = txtNombre.getText().trim();
            String apellido1 = txtApellido1.getText().trim();
            String apellido2 = txtApellido2.getText().trim();
            String direccion = txtDireccion.getText().trim();

            if (nombre.isEmpty() ||
                    apellido1.isEmpty() ||
                    apellido2.isEmpty() ||
                    direccion.isEmpty()) {

                mostrarError("Todos los campos son obligatorios");
                return;
            }

            // Modificar el proveedor únicamente después de validar los campos
            seleccionado.setNombre(nombre);
            seleccionado.setApellido1(apellido1);
            seleccionado.setApellido2(apellido2);
            seleccionado.setDireccion(direccion);

            try {
                proveedorDao.update(seleccionado);

                tablaProveedores.getItems().setAll(proveedorDao.findAll());

                mostrarInformacion("Proveedor actualizado correctamente");

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("No se ha podido modificar el proveedor");
            }
        });
    }

    private void eliminarProveedor() {

        Proveedor seleccionado = tablaProveedores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Selecciona un proveedor de la tabla");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("Eliminar proveedor");
        confirmacion.setHeaderText("¿Quieres eliminar este proveedor?");
        confirmacion.setContentText(
                "Código: " + seleccionado.getCod() +
                        "\nNombre: " + seleccionado.getNombre()
        );

        confirmacion.showAndWait().ifPresent(respuesta -> {

            if (respuesta == ButtonType.OK) {

                try {
                    boolean eliminado = proveedorDao.delete(seleccionado);

                    if (eliminado) {
                        tablaProveedores.getItems().setAll(proveedorDao.findAll());
                        mostrarInformacion("Proveedor eliminado correctamente");
                    } else {
                        mostrarError(
                                "No se puede eliminar el proveedor porque tiene suministros asociados"
                        );
                    }

                } catch (RuntimeException e) {
                    e.printStackTrace();
                    mostrarError("No se ha podido eliminar el proveedor");
                }
            }
        });
    }

    private void mostrarError(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
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

    // Cerrar los EntityManager de los DAO
    public void cerrar() {

        if (proveedorDao != null) proveedorDao.cerrar();
        if (piezaDao != null) piezaDao.cerrar();
        if (proyectoDao != null) proyectoDao.cerrar();
        if (suministroDao != null) suministroDao.cerrar();
    }
}