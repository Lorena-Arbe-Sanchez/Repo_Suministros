package gui;

import dao.Dao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    private MainController controller;

    @Override
    public void start(Stage stage) throws IOException {

        // Cargar la interfaz principal definida en el archivo FXML
        FXMLLoader loader = new FXMLLoader(
                MainFX.class.getResource("/gui/main.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 750);

        // Cargar los estilos CSS de la aplicación
        scene.getStylesheets().add(
                MainFX.class.getResource("/gui/styles.css").toExternalForm()
        );

        // Obtener el controlador asociado a la interfaz
        controller = loader.getController();

        stage.setTitle("Gestión de Suministros");
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {

        // Cerrar los recursos al finalizar la aplicación
        if (controller != null) {
            controller.cerrar();
        }

        Dao.cerrarFabrica();
    }

    public static void main(String[] args) {
        launch(args);
    }
}