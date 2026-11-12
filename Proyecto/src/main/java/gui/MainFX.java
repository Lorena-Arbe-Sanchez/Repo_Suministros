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

        FXMLLoader loader = new FXMLLoader(
                MainFX.class.getResource("/gui/main.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 750);

        // Cargar estilos CSS
        scene.getStylesheets().add(
                MainFX.class.getResource("/gui/styles.css").toExternalForm()
        );

        controller = loader.getController();

        stage.setTitle("Gestión de Suministros");
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {

        if (controller != null) {
            controller.cerrar();
        }

        Dao.cerrarFabrica();
    }

    public static void main(String[] args) {
        launch(args);
    }
}