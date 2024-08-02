package com.example.demofeldi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField emailField; // Añadido para el campo de correo electrónico
    @FXML
    private TextField roleField; // Añadido para el campo de rol

    // Constructor predeterminado
    public RegisterController() {
    }

    @FXML
    private void handleRegister() {
        // Recupera los datos de registro.
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        String confirmPassword = this.confirmPasswordField.getText();
        String email = this.emailField.getText();
        String role = this.roleField.getText();

        // Verifica que los campos no estén vacíos y que las contraseñas coincidan.
        if (!username.isEmpty() && !password.isEmpty() && password.equals(confirmPassword) && !email.isEmpty() && !role.isEmpty()) {
            try {
                Connection connection = DbConnection.getConnection(); // Obtiene la conexión a la base de datos.

                // Consulta SQL para insertar un nuevo usuario.
                String sql = "INSERT INTO usuarios (nombre_usuario, contraseña, correo_electronico, rol) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, email);  // Establece el correo electrónico
                pstmt.setString(4, role);   // Establece el rol
                pstmt.executeUpdate();

                this.showAlert("Success", "Registration successful.", AlertType.INFORMATION);
                DbConnection.closeConnection(); // Cierra la conexión a la base de datos.
                this.loadLoginView(); // Carga la vista de login.
            } catch (SQLException e) {
                e.printStackTrace();
                this.showAlert("Error", "Database error: " + e.getMessage(), AlertType.ERROR); // Mostrar el mensaje de error específico
            }
        } else {
            this.showAlert("Error", "All fields must be filled and passwords must match.", AlertType.ERROR);
        }
    }

    private void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            this.showAlert("Error", "Failed to load the login view.", AlertType.ERROR);
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            this.showAlert("Error", "Failed to load the login view.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}