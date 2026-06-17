package com.restaurant.gestion_restaurant.database;

import com.restaurant.gestion_restaurant.models.Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    public List<Menu> findAll() throws SQLException {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM menus";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public void insert(Menu m) throws SQLException {
        String sql = "INSERT INTO menus (nom, description, prix, categorie) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getDescription());
            ps.setDouble(3, m.getPrix());
            ps.setString(4, m.getCategorie());
            ps.executeUpdate();
        }
    }

    public void update(Menu m) throws SQLException {
        String sql = "UPDATE menus SET nom=?, description=?, prix=?, categorie=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getDescription());
            ps.setDouble(3, m.getPrix());
            ps.setString(4, m.getCategorie());
            ps.setInt(5, m.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM menus WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Menu mapRow(ResultSet rs) throws SQLException {
        return new Menu(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("description"),
            rs.getDouble("prix"),
            rs.getString("categorie")
        );
    }
}