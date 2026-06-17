package com.restaurant.gestion_restaurant.database;

import com.restaurant.gestion_restaurant.models.Commande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour les opérations CRUD sur la table commandes.
 */
public class CommandeDAO {

    /** Récupère toutes les commandes avec nom menu et client. */
    public List<Commande> findAll() throws SQLException {
        List<Commande> list = new ArrayList<>();
        String sql = "SELECT c.*, m.nom AS menu_nom, " +
                     "CONCAT(cl.prenom, ' ', cl.nom) AS client_nom " +
                     "FROM commandes c " +
                     "JOIN menus m ON c.menu_id = m.id " +
                     "LEFT JOIN clients cl ON c.client_id = cl.id " +
                     "ORDER BY c.date_commande DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Commande(
                    rs.getInt("id"),
                    rs.getInt("numero_table"),
                    rs.getInt("menu_id"),
                    rs.getString("menu_nom"),
                    rs.getInt("quantite"),
                    rs.getString("statut"),
                    rs.getString("date_commande"),
                    rs.getString("client_nom")
                ));
            }
        }
        return list;
    }

    /** Récupère les commandes d'un client spécifique. */
    public List<Commande> findByClientId(int clientId) throws SQLException {
        List<Commande> list = new ArrayList<>();
        String sql = "SELECT c.*, m.nom AS menu_nom, " +
                     "CONCAT(cl.prenom, ' ', cl.nom) AS client_nom " +
                     "FROM commandes c " +
                     "JOIN menus m ON c.menu_id = m.id " +
                     "LEFT JOIN clients cl ON c.client_id = cl.id " +
                     "WHERE c.client_id = ? " +
                     "ORDER BY c.date_commande DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Commande(
                    rs.getInt("id"),
                    rs.getInt("numero_table"),
                    rs.getInt("menu_id"),
                    rs.getString("menu_nom"),
                    rs.getInt("quantite"),
                    rs.getString("statut"),
                    rs.getString("date_commande"),
                    rs.getString("client_nom")
                ));
            }
        }
        return list;
    }

    /** Ajoute une commande avec client_id. */
    public void insert(Commande c) throws SQLException {
        String sql = "INSERT INTO commandes (numero_table, menu_id, quantite, statut, client_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getNumeroTable());
            ps.setInt(2, c.getMenuId());
            ps.setInt(3, c.getQuantite());
            ps.setString(4, c.getStatut());
            if (c.getClientId() > 0)
                ps.setInt(5, c.getClientId());
            else
                ps.setNull(5, Types.INTEGER);
            ps.executeUpdate();
        }
    }

    /** Met à jour une commande (statut, quantite, etc). */
    public void update(Commande c) throws SQLException {
        String sql = "UPDATE commandes SET numero_table=?, menu_id=?, quantite=?, statut=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getNumeroTable());
            ps.setInt(2, c.getMenuId());
            ps.setInt(3, c.getQuantite());
            ps.setString(4, c.getStatut());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        }
    }

    /** Supprime / annule une commande par ID. */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM commandes WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /** Met à jour uniquement le statut (pour le cuisinier). */
    public void updateStatut(int id, String statut) throws SQLException {
        String sql = "UPDATE commandes SET statut=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}