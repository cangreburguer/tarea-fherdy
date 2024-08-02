package com.example.demofeldi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException; // Importación añadida para IOException

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Carga el archivo FXML de la vista de inicio de sesión.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            // Configura la escena y el título de la ventana principal.
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show(); // Muestra la ventana.
        } catch (IOException e) {
            e.printStackTrace();
            // Muestra un mensaje de error si no se puede cargar la vista.
            System.err.println("Failed to load the login view.");
        }
    }

    public static void main(String[] args) {
        launch(args); // Lanza la aplicación JavaFX.
    }
}
