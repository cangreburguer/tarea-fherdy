package com.example.demofeldi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public LoginController() {
    }

    @FXML
    private void handleLogin() {
        // Recupera el nombre de usuario y la contraseña de los campos de texto.
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();

        // Verifica que los campos no estén vacíos.
        if (!username.isEmpty() && !password.isEmpty()) {
            try {
                Connection connection = DbConnection.getConnection(); // Obtiene la conexión a la base de datos.

                // Consulta SQL para verificar el usuario y la contraseña.
                String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contraseña = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) { // Si se encuentra el usuario, carga la vista de empleados.
                    this.loadEmpleadoView();
                } else { // Muestra alerta si las credenciales son incorrectas.
                    this.showAlert("Error", "Invalid username or password.");
                }

                DbConnection.closeConnection(); // Cierra la conexión a la base de datos.
            } catch (SQLException e) {
                e.printStackTrace();
                this.showAlert("Error", "Database error.");
            }
        } else {
            this.showAlert("Error", "Username and Password cannot be empty.");
        }
    }

    @FXML
    private void handleCancel() {
        // Limpia los campos de texto.
        this.usernameField.clear();
        this.passwordField.clear();
    }

    private void loadEmpleadoView() {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("empleados.fxml"));
            Parent root = (Parent)loader.load();
            Stage stage = (Stage)this.usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Employee Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            this.showAlert("Error", "Failed to load the employee view.");
        }
    }

    @FXML
    private void handleRegister() {
        try {
            // Carga la vista de registro.
            Stage stage = (Stage)this.usernameField.getScene().getWindow();
            stage.setScene(new Scene((Parent)FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("register.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
            this.showAlert("Error", "Error loading registration form.");
        }
    }

    private void showAlert(String title, String message) {
        // Muestra una alerta con el mensaje proporcionado.
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}