package gui;

import dao.ProveedorDao;
import dao.PiezaDao;
import dao.ProyectoDao;
import dao.SuministroDao;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import models.Pieza;
import models.Proveedor;
import models.Proyecto;
import models.Suministro;

import java.time.LocalDate;

public class MainController {

    @FXML
    private VBox contentArea;

    private ProveedorDao proveedorDao;
    private PiezaDao piezaDao;
    private ProyectoDao proyectoDao;
    private SuministroDao suministroDao;

    private TableView<Proveedor> tablaProveedores;
    private TableView<Pieza> tablaPiezas;
    private TableView<Proyecto> tablaProyectos;
    private TableView<Suministro> tablaSuministros;

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

        Label titulo = new Label("Piezas");
        titulo.getStyleClass().add("page-title");

        Label descripcion = new Label("Listado de piezas registradas");
        descripcion.getStyleClass().add("page-description");

        tablaPiezas = new TableView<>();

        TableColumn<Pieza, String> colCod = new TableColumn<>("Código");
        colCod.setCellValueFactory(new PropertyValueFactory<>("cod"));

        TableColumn<Pieza, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Pieza, Double> colPrecio = new TableColumn<>("Precio (€)");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        TableColumn<Pieza, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tablaPiezas.getColumns().addAll(
                colCod,
                colNombre,
                colPrecio,
                colDescripcion
        );

        tablaPiezas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            tablaPiezas.getItems().setAll(piezaDao.findAll());

        } catch (RuntimeException e) {
            e.printStackTrace();

            Label error = new Label("No se han podido cargar las piezas");
            error.getStyleClass().add("info-text");

            contentArea.getChildren().setAll(titulo, descripcion, error);
            return;
        }

        Button btnNuevo = new Button("Nueva pieza");
        btnNuevo.setOnAction(e -> nuevaPieza());

        Button btnModificar = new Button("Modificar");
        btnModificar.setOnAction(e -> modificarPieza());

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> eliminarPieza());

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
                tablaPiezas.getItems().setAll(piezaDao.buscar(texto));

            } catch (RuntimeException ex) {
                ex.printStackTrace();
                mostrarError("No se ha podido realizar la búsqueda");
            }
        });

        btnMostrarTodos.setOnAction(e -> {

            txtBuscar.clear();

            try {
                tablaPiezas.getItems().setAll(piezaDao.findAll());

            } catch (RuntimeException ex) {
                ex.printStackTrace();
                mostrarError("No se han podido cargar las piezas");
            }
        });

        contentArea.getChildren().setAll(
                titulo,
                descripcion,
                botones,
                busqueda,
                tablaPiezas
        );
    }

    private void nuevaPieza() {

        Dialog<Boolean> dialog = new Dialog<>();

        dialog.setTitle("Nueva pieza");
        dialog.setHeaderText("Introduce los datos de la pieza");

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(guardar, cancelar);

        TextField txtCod = new TextField();
        TextField txtNombre = new TextField();
        TextField txtPrecio = new TextField();
        TextField txtDescripcion = new TextField();

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Código:"), 0, 0);
        formulario.add(txtCod, 1, 0);

        formulario.add(new Label("Nombre:"), 0, 1);
        formulario.add(txtNombre, 1, 1);

        formulario.add(new Label("Precio (€):"), 0, 2);
        formulario.add(txtPrecio, 1, 2);

        formulario.add(new Label("Descripción:"), 0, 3);
        formulario.add(txtDescripcion, 1, 3);

        dialog.getDialogPane().setContent(formulario);

        dialog.setResultConverter(boton -> {
            if (boton == guardar) {
                return true;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(resultado -> {

            String cod = txtCod.getText().trim();
            String nombre = txtNombre.getText().trim();
            String precioTexto = txtPrecio.getText().trim().replace(",", ".");
            String descripcion = txtDescripcion.getText().trim();

            if (cod.isEmpty() || nombre.isEmpty() ||
                    precioTexto.isEmpty() || descripcion.isEmpty()) {

                mostrarError("Todos los campos son obligatorios");
                return;
            }

            double precio;

            try {
                precio = Double.parseDouble(precioTexto);

                if (!Double.isFinite(precio) || precio <= 0) {
                    mostrarError("El precio debe ser un número mayor que cero");
                    return;
                }

            } catch (NumberFormatException e) {
                mostrarError("El precio debe ser un número válido");
                return;
            }

            if (piezaDao.find(cod) != null) {
                mostrarError("Ya existe una pieza con ese código");
                return;
            }

            Pieza pieza = new Pieza(
                    cod,
                    nombre,
                    precio,
                    descripcion,
                    null
            );

            try {
                piezaDao.create(pieza);

                tablaPiezas.getItems().setAll(piezaDao.findAll());

                mostrarInformacion("Pieza creada correctamente");

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("No se ha podido crear la pieza");
            }
        });
    }

    private void modificarPieza() {

        Pieza seleccionado = tablaPiezas.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Selecciona una pieza de la tabla");
            return;
        }

        Dialog<Boolean> dialog = new Dialog<>();

        dialog.setTitle("Modificar pieza");
        dialog.setHeaderText("Modifica los datos de la pieza");

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(guardar, cancelar);

        TextField txtCod = new TextField(seleccionado.getCod());
        TextField txtNombre = new TextField(seleccionado.getNombre());
        TextField txtPrecio = new TextField(String.valueOf(seleccionado.getPrecio()));
        TextField txtDescripcion = new TextField(seleccionado.getDescripcion());

        // El código identifica a la pieza y no se puede modificar
        txtCod.setEditable(false);

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Código:"), 0, 0);
        formulario.add(txtCod, 1, 0);

        formulario.add(new Label("Nombre:"), 0, 1);
        formulario.add(txtNombre, 1, 1);

        formulario.add(new Label("Precio (€):"), 0, 2);
        formulario.add(txtPrecio, 1, 2);

        formulario.add(new Label("Descripción:"), 0, 3);
        formulario.add(txtDescripcion, 1, 3);

        dialog.getDialogPane().setContent(formulario);

        dialog.setResultConverter(boton -> {
            if (boton == guardar) {
                return true;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(resultado -> {

            String nombre = txtNombre.getText().trim();
            String precioTexto = txtPrecio.getText().trim().replace(",", ".");
            String descripcion = txtDescripcion.getText().trim();

            if (nombre.isEmpty() || precioTexto.isEmpty() || descripcion.isEmpty()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }

            double precio;

            try {
                precio = Double.parseDouble(precioTexto);

                if (!Double.isFinite(precio) || precio <= 0) {
                    mostrarError("El precio debe ser un número mayor que cero");
                    return;
                }

            } catch (NumberFormatException e) {
                mostrarError("El precio debe ser un número válido");
                return;
            }

            // Modificar la pieza únicamente después de validar los campos
            seleccionado.setNombre(nombre);
            seleccionado.setPrecio(precio);
            seleccionado.setDescripcion(descripcion);

            try {
                piezaDao.update(seleccionado);

                tablaPiezas.getItems().setAll(piezaDao.findAll());

                mostrarInformacion("Pieza actualizada correctamente");

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("No se ha podido modificar la pieza");
            }
        });
    }

    private void eliminarPieza() {

        Pieza seleccionado = tablaPiezas.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Selecciona una pieza de la tabla");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("Eliminar pieza");
        confirmacion.setHeaderText("¿Quieres eliminar esta pieza?");
        confirmacion.setContentText(
                "Código: " + seleccionado.getCod() +
                        "\nNombre: " + seleccionado.getNombre()
        );

        confirmacion.showAndWait().ifPresent(respuesta -> {

            if (respuesta == ButtonType.OK) {

                try {
                    boolean eliminado = piezaDao.delete(seleccionado);

                    if (eliminado) {
                        tablaPiezas.getItems().setAll(piezaDao.findAll());
                        mostrarInformacion("Pieza eliminada correctamente");
                    } else {
                        mostrarError(
                                "No se puede eliminar la pieza porque tiene suministros asociados"
                        );
                    }

                } catch (RuntimeException e) {
                    e.printStackTrace();
                    mostrarError("No se ha podido eliminar la pieza");
                }
            }
        });
    }

    @FXML
    private void mostrarProyectos() {

        Label titulo = new Label("Proyectos");
        titulo.getStyleClass().add("page-title");

        Label descripcion = new Label("Listado de proyectos registrados");
        descripcion.getStyleClass().add("page-description");

        tablaProyectos = new TableView<>();

        TableColumn<Proyecto, String> colCod = new TableColumn<>("Código");
        colCod.setCellValueFactory(new PropertyValueFactory<>("cod"));

        TableColumn<Proyecto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Proyecto, String> colCiudad = new TableColumn<>("Ciudad");
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));

        tablaProyectos.getColumns().addAll(
                colCod,
                colNombre,
                colCiudad
        );

        tablaProyectos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            tablaProyectos.getItems().setAll(proyectoDao.findAll());

        } catch (RuntimeException e) {
            e.printStackTrace();

            Label error = new Label("No se han podido cargar los proyectos");
            error.getStyleClass().add("info-text");

            contentArea.getChildren().setAll(titulo, descripcion, error);
            return;
        }

        Button btnNuevo = new Button("Nuevo proyecto");
        btnNuevo.setOnAction(e -> nuevoProyecto());

        Button btnModificar = new Button("Modificar");
        btnModificar.setOnAction(e -> modificarProyecto());

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> eliminarProyecto());

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
                tablaProyectos.getItems().setAll(proyectoDao.buscar(texto));

            } catch (RuntimeException ex) {
                ex.printStackTrace();
                mostrarError("No se ha podido realizar la búsqueda");
            }
        });

        btnMostrarTodos.setOnAction(e -> {

            txtBuscar.clear();

            try {
                tablaProyectos.getItems().setAll(proyectoDao.findAll());

            } catch (RuntimeException ex) {
                ex.printStackTrace();
                mostrarError("No se han podido cargar los proyectos");
            }
        });

        contentArea.getChildren().setAll(
                titulo,
                descripcion,
                botones,
                busqueda,
                tablaProyectos
        );
    }

    private void nuevoProyecto() {

        Dialog<Boolean> dialog = new Dialog<>();

        dialog.setTitle("Nuevo proyecto");
        dialog.setHeaderText("Introduce los datos del proyecto");

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(guardar, cancelar);

        TextField txtCod = new TextField();
        TextField txtNombre = new TextField();
        TextField txtCiudad = new TextField();

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Código:"), 0, 0);
        formulario.add(txtCod, 1, 0);

        formulario.add(new Label("Nombre:"), 0, 1);
        formulario.add(txtNombre, 1, 1);

        formulario.add(new Label("Ciudad:"), 0, 2);
        formulario.add(txtCiudad, 1, 2);

        dialog.getDialogPane().setContent(formulario);

        dialog.setResultConverter(boton -> {
            if (boton == guardar) {
                return true;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(resultado -> {

            String cod = txtCod.getText().trim();
            String nombre = txtNombre.getText().trim();
            String ciudad = txtCiudad.getText().trim();

            if (cod.isEmpty() || nombre.isEmpty() || ciudad.isEmpty()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }

            if (proyectoDao.find(cod) != null) {
                mostrarError("Ya existe un proyecto con ese código");
                return;
            }

            Proyecto proyecto = new Proyecto(
                    cod,
                    nombre,
                    ciudad,
                    null
            );

            try {
                proyectoDao.create(proyecto);

                tablaProyectos.getItems().setAll(proyectoDao.findAll());

                mostrarInformacion("Proyecto creado correctamente");

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("No se ha podido crear el proyecto");
            }
        });
    }

    private void modificarProyecto() {

        Proyecto seleccionado = tablaProyectos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Selecciona un proyecto de la tabla");
            return;
        }

        Dialog<Boolean> dialog = new Dialog<>();

        dialog.setTitle("Modificar proyecto");
        dialog.setHeaderText("Modifica los datos del proyecto");

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(guardar, cancelar);

        TextField txtCod = new TextField(seleccionado.getCod());
        TextField txtNombre = new TextField(seleccionado.getNombre());
        TextField txtCiudad = new TextField(seleccionado.getCiudad());

        // El código identifica al proyecto y no se puede modificar
        txtCod.setEditable(false);

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Código:"), 0, 0);
        formulario.add(txtCod, 1, 0);

        formulario.add(new Label("Nombre:"), 0, 1);
        formulario.add(txtNombre, 1, 1);

        formulario.add(new Label("Ciudad:"), 0, 2);
        formulario.add(txtCiudad, 1, 2);

        dialog.getDialogPane().setContent(formulario);

        dialog.setResultConverter(boton -> {
            if (boton == guardar) {
                return true;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(resultado -> {

            String nombre = txtNombre.getText().trim();
            String ciudad = txtCiudad.getText().trim();

            if (nombre.isEmpty() || ciudad.isEmpty()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }

            // Modificar el proyecto únicamente después de validar los campos
            seleccionado.setNombre(nombre);
            seleccionado.setCiudad(ciudad);

            try {
                proyectoDao.update(seleccionado);

                tablaProyectos.getItems().setAll(proyectoDao.findAll());

                mostrarInformacion("Proyecto actualizado correctamente");

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("No se ha podido modificar el proyecto");
            }
        });
    }

    private void eliminarProyecto() {

        Proyecto seleccionado = tablaProyectos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Selecciona un proyecto de la tabla");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("Eliminar proyecto");
        confirmacion.setHeaderText("¿Quieres eliminar este proyecto?");
        confirmacion.setContentText(
                "Código: " + seleccionado.getCod() +
                        "\nNombre: " + seleccionado.getNombre()
        );

        confirmacion.showAndWait().ifPresent(respuesta -> {

            if (respuesta == ButtonType.OK) {

                try {
                    boolean eliminado = proyectoDao.delete(seleccionado);

                    if (eliminado) {
                        tablaProyectos.getItems().setAll(proyectoDao.findAll());
                        mostrarInformacion("Proyecto eliminado correctamente");
                    } else {
                        mostrarError(
                                "No se puede eliminar el proyecto porque tiene suministros asociados"
                        );
                    }

                } catch (RuntimeException e) {
                    e.printStackTrace();
                    mostrarError("No se ha podido eliminar el proyecto");
                }
            }
        });
    }

    @FXML
    private void mostrarSuministros() {

        Label titulo = new Label("Suministros");
        titulo.getStyleClass().add("page-title");

        Label descripcion = new Label("Listado de suministros registrados");
        descripcion.getStyleClass().add("page-description");

        tablaSuministros = new TableView<>();

        TableColumn<Suministro, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(String.valueOf(data.getValue().getId())));

        TableColumn<Suministro, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getFecha().toString()));

        TableColumn<Suministro, String> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(String.valueOf(data.getValue().getCantidad())));

        TableColumn<Suministro, String> colProveedor = new TableColumn<>("Proveedor");
        colProveedor.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().getProveedor().getCod() + " - " +
                                data.getValue().getProveedor().getNombre()));

        TableColumn<Suministro, String> colPieza = new TableColumn<>("Pieza");
        colPieza.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().getPieza().getCod() + " - " +
                                data.getValue().getPieza().getNombre()));

        TableColumn<Suministro, String> colProyecto = new TableColumn<>("Proyecto");
        colProyecto.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().getProyecto().getCod() + " - " +
                                data.getValue().getProyecto().getNombre()));

        tablaSuministros.getColumns().addAll(
                colId,
                colFecha,
                colCantidad,
                colProveedor,
                colPieza,
                colProyecto
        );

        tablaSuministros.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            tablaSuministros.getItems().setAll(suministroDao.findAll());

        } catch (RuntimeException e) {
            e.printStackTrace();

            Label error = new Label("No se han podido cargar los suministros");
            error.getStyleClass().add("info-text");

            contentArea.getChildren().setAll(titulo, descripcion, error);
            return;
        }

        Button btnNuevo = new Button("Nuevo suministro");
        btnNuevo.setOnAction(e -> nuevoSuministro());

        Button btnModificar = new Button("Modificar");
        btnModificar.setOnAction(e -> modificarSuministro());

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> eliminarSuministro());

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
                tablaSuministros.getItems().setAll(suministroDao.buscar(texto));

            } catch (RuntimeException ex) {
                ex.printStackTrace();
                mostrarError("No se ha podido realizar la búsqueda");
            }
        });

        btnMostrarTodos.setOnAction(e -> {

            txtBuscar.clear();

            try {
                tablaSuministros.getItems().setAll(suministroDao.findAll());

            } catch (RuntimeException ex) {
                ex.printStackTrace();
                mostrarError("No se han podido cargar los suministros");
            }
        });

        contentArea.getChildren().setAll(
                titulo,
                descripcion,
                botones,
                busqueda,
                tablaSuministros
        );
    }

    private void nuevoSuministro() {

        Dialog<Boolean> dialog = new Dialog<>();

        dialog.setTitle("Nuevo suministro");
        dialog.setHeaderText("Introduce los datos del suministro");

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(guardar, cancelar);

        DatePicker fecha = new DatePicker(LocalDate.now());
        TextField txtCantidad = new TextField();

        ComboBox<Proveedor> cmbProveedor = new ComboBox<>(
                FXCollections.observableArrayList(proveedorDao.findAll())
        );

        ComboBox<Pieza> cmbPieza = new ComboBox<>(
                FXCollections.observableArrayList(piezaDao.findAll())
        );

        ComboBox<Proyecto> cmbProyecto = new ComboBox<>(
                FXCollections.observableArrayList(proyectoDao.findAll())
        );

        cmbProveedor.setPromptText("Selecciona un proveedor");
        cmbPieza.setPromptText("Selecciona una pieza");
        cmbProyecto.setPromptText("Selecciona un proyecto");

        cmbProveedor.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Proveedor proveedor) {
                return proveedor == null ? "" :
                        proveedor.getCod() + " - " + proveedor.getNombre();
            }

            @Override
            public Proveedor fromString(String texto) {
                return null;
            }
        });

        cmbPieza.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Pieza pieza) {
                return pieza == null ? "" :
                        pieza.getCod() + " - " + pieza.getNombre();
            }

            @Override
            public Pieza fromString(String texto) {
                return null;
            }
        });

        cmbProyecto.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Proyecto proyecto) {
                return proyecto == null ? "" :
                        proyecto.getCod() + " - " + proyecto.getNombre();
            }

            @Override
            public Proyecto fromString(String texto) {
                return null;
            }
        });

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Fecha:"), 0, 0);
        formulario.add(fecha, 1, 0);

        formulario.add(new Label("Cantidad:"), 0, 1);
        formulario.add(txtCantidad, 1, 1);

        formulario.add(new Label("Proveedor:"), 0, 2);
        formulario.add(cmbProveedor, 1, 2);

        formulario.add(new Label("Pieza:"), 0, 3);
        formulario.add(cmbPieza, 1, 3);

        formulario.add(new Label("Proyecto:"), 0, 4);
        formulario.add(cmbProyecto, 1, 4);

        dialog.getDialogPane().setContent(formulario);

        dialog.setResultConverter(boton -> {
            if (boton == guardar) {
                return true;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(resultado -> {

            LocalDate fechaSeleccionada = fecha.getValue();
            String cantidadTexto = txtCantidad.getText().trim();

            Proveedor proveedor = cmbProveedor.getValue();
            Pieza pieza = cmbPieza.getValue();
            Proyecto proyecto = cmbProyecto.getValue();

            if (fechaSeleccionada == null || cantidadTexto.isEmpty() ||
                    proveedor == null || pieza == null || proyecto == null) {

                mostrarError("Todos los campos son obligatorios");
                return;
            }

            int cantidad;

            try {
                cantidad = Integer.parseInt(cantidadTexto);

                if (cantidad <= 0) {
                    mostrarError("La cantidad debe ser un número mayor que cero");
                    return;
                }

            } catch (NumberFormatException e) {
                mostrarError("La cantidad debe ser un número entero válido");
                return;
            }

            Suministro suministro = new Suministro(
                    0,
                    fechaSeleccionada,
                    cantidad,
                    proveedor,
                    pieza,
                    proyecto
            );

            try {
                suministroDao.create(suministro);

                tablaSuministros.getItems().setAll(suministroDao.findAll());

                mostrarInformacion("Suministro creado correctamente");

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("No se ha podido crear el suministro");
            }
        });
    }

    private void modificarSuministro() {

        Suministro seleccionado = tablaSuministros.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Selecciona un suministro para modificar");
            return;
        }

        Dialog<Boolean> dialog = new Dialog<>();

        dialog.setTitle("Modificar suministro");
        dialog.setHeaderText("Modificar suministro ID: " + seleccionado.getId());

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(guardar, cancelar);

        DatePicker fecha = new DatePicker(seleccionado.getFecha());
        TextField txtCantidad = new TextField(String.valueOf(seleccionado.getCantidad()));

        ComboBox<Proveedor> cmbProveedor = new ComboBox<>(
                FXCollections.observableArrayList(proveedorDao.findAll())
        );

        ComboBox<Pieza> cmbPieza = new ComboBox<>(
                FXCollections.observableArrayList(piezaDao.findAll())
        );

        ComboBox<Proyecto> cmbProyecto = new ComboBox<>(
                FXCollections.observableArrayList(proyectoDao.findAll())
        );

        cmbProveedor.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Proveedor proveedor) {
                return proveedor == null ? "" :
                        proveedor.getCod() + " - " + proveedor.getNombre();
            }

            @Override
            public Proveedor fromString(String texto) {
                return null;
            }
        });

        cmbPieza.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Pieza pieza) {
                return pieza == null ? "" :
                        pieza.getCod() + " - " + pieza.getNombre();
            }

            @Override
            public Pieza fromString(String texto) {
                return null;
            }
        });

        cmbProyecto.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Proyecto proyecto) {
                return proyecto == null ? "" :
                        proyecto.getCod() + " - " + proyecto.getNombre();
            }

            @Override
            public Proyecto fromString(String texto) {
                return null;
            }
        });

        cmbProveedor.setValue(seleccionado.getProveedor());
        cmbPieza.setValue(seleccionado.getPieza());
        cmbProyecto.setValue(seleccionado.getProyecto());

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Fecha:"), 0, 0);
        formulario.add(fecha, 1, 0);

        formulario.add(new Label("Cantidad:"), 0, 1);
        formulario.add(txtCantidad, 1, 1);

        formulario.add(new Label("Proveedor:"), 0, 2);
        formulario.add(cmbProveedor, 1, 2);

        formulario.add(new Label("Pieza:"), 0, 3);
        formulario.add(cmbPieza, 1, 3);

        formulario.add(new Label("Proyecto:"), 0, 4);
        formulario.add(cmbProyecto, 1, 4);

        dialog.getDialogPane().setContent(formulario);

        dialog.setResultConverter(boton -> {
            if (boton == guardar) {
                return true;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(resultado -> {

            LocalDate fechaSeleccionada = fecha.getValue();
            String cantidadTexto = txtCantidad.getText().trim();

            Proveedor proveedor = cmbProveedor.getValue();
            Pieza pieza = cmbPieza.getValue();
            Proyecto proyecto = cmbProyecto.getValue();

            if (fechaSeleccionada == null || cantidadTexto.isEmpty() ||
                    proveedor == null || pieza == null || proyecto == null) {

                mostrarError("Todos los campos son obligatorios");
                return;
            }

            int cantidad;

            try {
                cantidad = Integer.parseInt(cantidadTexto);

                if (cantidad <= 0) {
                    mostrarError("La cantidad debe ser un número mayor que cero");
                    return;
                }

            } catch (NumberFormatException e) {
                mostrarError("La cantidad debe ser un número entero válido");
                return;
            }

            seleccionado.setFecha(fechaSeleccionada);
            seleccionado.setCantidad(cantidad);
            seleccionado.setProveedor(proveedor);
            seleccionado.setPieza(pieza);
            seleccionado.setProyecto(proyecto);

            try {
                suministroDao.update(seleccionado);

                tablaSuministros.getItems().setAll(suministroDao.findAll());

                mostrarInformacion("Suministro modificado correctamente");

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("No se ha podido modificar el suministro");
            }
        });
    }

    private void eliminarSuministro() {

        Suministro seleccionado = tablaSuministros.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Selecciona un suministro para eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("Eliminar suministro");
        confirmacion.setHeaderText("¿Quieres eliminar el suministro ID: " + seleccionado.getId() + "?");

        confirmacion.setContentText(
                "Fecha: " + seleccionado.getFecha() +
                        "\nCantidad: " + seleccionado.getCantidad() +
                        "\nProveedor: " + seleccionado.getProveedor().getCod() +
                        "\nPieza: " + seleccionado.getPieza().getCod() +
                        "\nProyecto: " + seleccionado.getProyecto().getCod()
        );

        confirmacion.showAndWait().ifPresent(respuesta -> {

            if (respuesta == ButtonType.OK) {

                try {
                    boolean eliminado = suministroDao.delete(seleccionado);

                    if (eliminado) {
                        tablaSuministros.getItems().setAll(suministroDao.findAll());
                        mostrarInformacion("Suministro eliminado correctamente");
                    } else {
                        mostrarError("No se ha podido eliminar el suministro");
                    }

                } catch (RuntimeException e) {
                    e.printStackTrace();
                    mostrarError("Ha ocurrido un error al eliminar el suministro");
                }
            }
        });
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