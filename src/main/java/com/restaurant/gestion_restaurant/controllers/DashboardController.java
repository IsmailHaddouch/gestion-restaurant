package com.restaurant.gestion_restaurant.controllers;

import com.restaurant.gestion_restaurant.database.CommandeDAO;
import com.restaurant.gestion_restaurant.models.Commande;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Contrôleur du Dashboard — statistiques des commandes.
 */
public class DashboardController {

    @FXML private Label lblTotal, lblAttente, lblPreparation, lblServie, lblAnnulee;

    @FXML private TableView<Commande> tableRecentes;
    @FXML private TableColumn<Commande, Integer> colId, colTable, colQte;
    @FXML private TableColumn<Commande, String>  colClient, colPlat, colStatut, colDate;

    private final CommandeDAO commandeDAO = new CommandeDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("numeroTable"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("clientNom"));
        colPlat.setCellValueFactory(new PropertyValueFactory<>("menuNom"));
        colQte.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));

        // Couleurs par statut
        tableRecentes.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Commande c, boolean empty) {
                super.updateItem(c, empty);
                if (c == null || empty) setStyle("");
                else switch (c.getStatut()) {
                    case "En attente"     -> setStyle("-fx-background-color:#fff3cd;");
                    case "En préparation" -> setStyle("-fx-background-color:#cce5ff;");
                    case "Servie"         -> setStyle("-fx-background-color:#d4edda;");
                    case "Annulée"        -> setStyle("-fx-background-color:#f8d7da;");
                    default               -> setStyle("");
                }
            }
        });

        loadDashboard();
    }

    private void loadDashboard() {
        try {
            List<Commande> all = commandeDAO.findAll();

            // Compteurs
            long total       = all.size();
            long attente     = all.stream().filter(c -> "En attente".equals(c.getStatut())).count();
            long preparation = all.stream().filter(c -> "En préparation".equals(c.getStatut())).count();
            long servie      = all.stream().filter(c -> "Servie".equals(c.getStatut())).count();
            long annulee     = all.stream().filter(c -> "Annulée".equals(c.getStatut())).count();

            lblTotal.setText(String.valueOf(total));
            lblAttente.setText(String.valueOf(attente));
            lblPreparation.setText(String.valueOf(preparation));
            lblServie.setText(String.valueOf(servie));
            lblAnnulee.setText(String.valueOf(annulee));

            // Tableau dernières commandes
            tableRecentes.setItems(FXCollections.observableArrayList(all));

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleActualiser() {
        loadDashboard();
    }
}