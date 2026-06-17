package com.restaurant.gestion_restaurant.database;

import com.restaurant.gestion_restaurant.models.Client;

import java.sql.*;

/**
 * DAO pour les opérations sur la table clients.
 */
public class ClientDAO {

    /** Insère un client et retourne son ID généré. */
    public int insert(Client c) throws SQLException {
        String sql = "INSERT INTO clients (nom, prenom, telephone) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getTelephone());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }
}