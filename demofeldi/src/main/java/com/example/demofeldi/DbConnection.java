package com.example.demofeldi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/basicDb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "watermelon";
    private static Connection connection = null;

    private DbConnection() {
    }

    // Método para obtener la conexión a la base de datos
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión establecida con éxito.");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("No se pudo establecer la conexión.");
            }
        }
        return connection;
    }

    // Método para cerrar la conexión a la base de datos
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Conexión cerrada con éxito.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("No se pudo cerrar la conexión.");
            }
        }
    }

    // Método genérico para ejecutar consultas
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        setParameters(pstmt, params);
        return pstmt.executeQuery();
    }

    // Método genérico para ejecutar actualizaciones
    public static void executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        setParameters(pstmt, params);
        pstmt.executeUpdate();
        pstmt.close();
    }

    // Método para establecer parámetros en el PreparedStatement
    private static void setParameters(PreparedStatement pstmt, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }
}
