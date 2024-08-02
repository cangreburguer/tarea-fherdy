package com.example.demofeldi;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

public class CrudController {

    @FXML private TableView<Empleado> empleadosTable;
    @FXML private TableColumn<Empleado, Integer> idColumn;
    @FXML private TableColumn<Empleado, String> nombreColumn;
    @FXML private TableColumn<Empleado, String> apellidoColumn;
    @FXML private TableColumn<Empleado, LocalDate> fechaNacimientoColumn;
    @FXML private TableColumn<Empleado, String> identificacionColumn;
    @FXML private TableColumn<Empleado, String> telefonoColumn;
    @FXML private TableColumn<Empleado, Double> salarioColumn;
    @FXML private TextField nombreField;
    @FXML private TextField apellidoField;
    @FXML private DatePicker fechaNacimientoField;
    @FXML private TextField identificacionField;
    @FXML private TextField telefonoField;
    @FXML private TextField salarioField;
    private ObservableList<Empleado> empleadosData;

    @FXML
    private void initialize() {
        // Configura columnas y carga datos
        empleadosData = FXCollections.observableArrayList();
        empleadosTable.setItems(empleadosData);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        fechaNacimientoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        identificacionColumn.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        salarioColumn.setCellValueFactory(new PropertyValueFactory<>("salario"));
        loadEmpleados();
        empleadosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                handleTableClick();
            }
        });
    }

    @FXML
    private void handleAdd() {
        // Añade un nuevo empleado si los campos están completos
        if (areFieldsValid()) {
            try {
                DbConnection.executeUpdate("INSERT INTO empleados (nombre, apellido, fecha_nacimiento, identificacion, telefono, salario) VALUES (?, ?, ?, ?, ?, ?)",
                        nombreField.getText(), apellidoField.getText(), Date.valueOf(fechaNacimientoField.getValue()),
                        identificacionField.getText(), telefonoField.getText(), Double.parseDouble(salarioField.getText()));
                loadEmpleados();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Error adding employee.");
            }
        } else {
            showAlert("Error", "All fields must be filled.");
        }
    }

    @FXML
    private void handleUpdate() {
        // Actualiza el empleado seleccionado
        Empleado selectedEmpleado = empleadosTable.getSelectionModel().getSelectedItem();
        if (selectedEmpleado == null) {
            showAlert("Error", "No employee selected.");
        } else if (areFieldsValid()) {
            try {
                DbConnection.executeUpdate("UPDATE empleados SET nombre = ?, apellido = ?, fecha_nacimiento = ?, identificacion = ?, telefono = ?, salario = ? WHERE id = ?",
                        nombreField.getText(), apellidoField.getText(), Date.valueOf(fechaNacimientoField.getValue()),
                        identificacionField.getText(), telefonoField.getText(), Double.parseDouble(salarioField.getText()), selectedEmpleado.getId());
                loadEmpleados();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Error updating employee.");
            }
        } else {
            showAlert("Error", "All fields must be filled.");
        }
    }

    @FXML
    private void handleDelete() {
        // Elimina el empleado seleccionado
        Empleado selectedEmpleado = empleadosTable.getSelectionModel().getSelectedItem();
        if (selectedEmpleado == null) {
            showAlert("Error", "No employee selected.");
        } else {
            try {
                DbConnection.executeUpdate("DELETE FROM empleados WHERE id = ?", selectedEmpleado.getId());
                loadEmpleados();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Error deleting employee.");
            }
        }
    }

    @FXML
    private void handleRefresh() {
        // Recarga la lista de empleados
        loadEmpleados();
    }

    @FXML
    private void handleCancel() {
        // Limpia los campos de entrada
        clearFields();
    }

    private void handleTableClick() {
        // Rellena los campos con la información del empleado seleccionado
        Empleado selectedEmpleado = empleadosTable.getSelectionModel().getSelectedItem();
        if (selectedEmpleado != null) {
            nombreField.setText(selectedEmpleado.getNombre());
            apellidoField.setText(selectedEmpleado.getApellido());
            fechaNacimientoField.setValue(selectedEmpleado.getFechaNacimiento());
            identificacionField.setText(selectedEmpleado.getIdentificacion());
            telefonoField.setText(selectedEmpleado.getTelefono());
            salarioField.setText(String.valueOf(selectedEmpleado.getSalario()));
        }
    }

    private void loadEmpleados() {
        // Carga todos los empleados desde la base de datos
        try {
            empleadosData.clear();
            var rs = DbConnection.executeQuery("SELECT * FROM empleados");
            while (rs.next()) {
                empleadosData.add(new Empleado(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"),
                        rs.getDate("fecha_nacimiento").toLocalDate(), rs.getString("identificacion"),
                        rs.getString("telefono"), rs.getDouble("salario")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Error loading employee data.");
        }
    }

    private boolean areFieldsValid() {
        // Verifica si todos los campos están completos
        return !nombreField.getText().isEmpty() && !apellidoField.getText().isEmpty()
                && fechaNacimientoField.getValue() != null && !identificacionField.getText().isEmpty()
                && !salarioField.getText().isEmpty();
    }

    private void clearFields() {
        // Limpia los campos de entrada
        nombreField.clear();
        apellidoField.clear();
        fechaNacimientoField.setValue(null);
        identificacionField.clear();
        telefonoField.clear();
        salarioField.clear();
    }

    private void showAlert(String title, String message) {
        // Muestra un mensaje de alerta
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
