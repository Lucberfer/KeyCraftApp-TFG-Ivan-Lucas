package com.example.KeyCraftApp.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The Database class is responsible for managing the connection to the SQLite database.
 * It provides a method to establish and return a connection to the database.
 */
public class Database {

    // Constants for database connection details
    private static final String URL = "jdbc:sqlite:keycraftManagement.db?journal_mode=WAL";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    /**
     * Establishes a connection to the SQLite database.
     *
     * @return A Connection object to interact with the database
     * @throws RuntimeException If there is an error during the connection process
     */
    public static Connection connectionDB() {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish and return the database connection
            return DriverManager.getConnection(URL);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
