package com.dhruvijain.deliveryplanner.dao;

import com.dhruvijain.deliveryplanner.model.Delivery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDAO implements BaseDAO<Delivery> {

    @Override
    public void create(Delivery delivery) {
        String sql = "INSERT INTO deliveries(address, status, driver_id) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, delivery.getAddress());
            pstmt.setString(2, delivery.getStatus());
            pstmt.setInt(3, delivery.getDriverId());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    delivery.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Delivery read(int id) {
        String sql = "SELECT * FROM deliveries WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Delivery(
                        rs.getInt("id"),
                        rs.getString("address"),
                        rs.getString("status"),
                        rs.getInt("driver_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Delivery> readAll() {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM deliveries";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                deliveries.add(new Delivery(
                        rs.getInt("id"),
                        rs.getString("address"),
                        rs.getString("status"),
                        rs.getInt("driver_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    @Override
    public void update(Delivery delivery) {
        String sql = "UPDATE deliveries SET address = ?, status = ?, driver_id = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, delivery.getAddress());
            pstmt.setString(2, delivery.getStatus());
            pstmt.setInt(3, delivery.getDriverId());
            pstmt.setInt(4, delivery.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM deliveries WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
