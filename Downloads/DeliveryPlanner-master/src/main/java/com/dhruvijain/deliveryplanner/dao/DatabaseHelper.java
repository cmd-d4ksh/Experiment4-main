package com.dhruvijain.deliveryplanner.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:deliveryplanner.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            
            String driversTable = "CREATE TABLE IF NOT EXISTS drivers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "contact TEXT," +
                    "license_number TEXT NOT NULL" +
                    ");";

            String deliveriesTable = "CREATE TABLE IF NOT EXISTS deliveries (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "address TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "driver_id INTEGER," +
                    "FOREIGN KEY(driver_id) REFERENCES drivers(id)" +
                    ");";

            String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL" +
                    ");";

            stmt.execute(driversTable);
            stmt.execute(deliveriesTable);
            stmt.execute(usersTable);
            
            // Insert default admin if users is empty
            stmt.execute("INSERT OR IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN')");

            // Populate mock data if drivers is empty
            java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM drivers");
            if (rs.next() && rs.getInt("total") == 0) {
                // Mock Drivers
                stmt.execute("INSERT INTO drivers (name, contact, license_number) VALUES ('Ramesh Singh', '+91-9876543210', 'DL-14202100123')");
                stmt.execute("INSERT INTO drivers (name, contact, license_number) VALUES ('Suresh Kumar', '+91-8765432109', 'DL-14202100124')");
                stmt.execute("INSERT INTO drivers (name, contact, license_number) VALUES ('Amit Sharma', '+91-7654321098', 'DL-14202100125')");
                
                // Mock Deliveries
                stmt.execute("INSERT INTO deliveries (address, status, driver_id) VALUES ('101, Main St, Bangalore', 'PENDING', 1)");
                stmt.execute("INSERT INTO deliveries (address, status, driver_id) VALUES ('205, Park Ave, Mumbai', 'IN-TRANSIT', 2)");
                stmt.execute("INSERT INTO deliveries (address, status, driver_id) VALUES ('Flat 4B, MG Road, Pune', 'DELIVERED', 1)");
                stmt.execute("INSERT INTO deliveries (address, status, driver_id) VALUES ('Highland Towers, Delhi', 'PENDING', 3)");
            }

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error initializing database.");
        }
    }
}
