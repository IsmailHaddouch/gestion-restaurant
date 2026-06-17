package com.restaurant.gestion_restaurant.controllers;

import com.restaurant.gestion_restaurant.database.CommandeDAO;
import com.restaurant.gestion_restaurant.models.Commande;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

/**
 * Contrôleur pour l'espace cuisine.
 */
public class CuisineController {

    @FXML private TableView<Commande> tableCommandes;
    @FXML private TableColumn<Commande, Integer> colId, colTable, colQte;
    @FXML private TableColumn<Commande, String>  colPlat, colStatut, colDate, colClient;
    @FXML private Label lblStatut;

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
        tableCommandes.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Commande c, boolean empty) {
                super.updateItem(c, empty);
                if (c == null || empty) setStyle("");
                else switch (c.getStatut()) {
                    case "En attente"     -> setStyle("-fx-background-color: #fff3cd;");
                    case "En préparation" -> setStyle("-fx-background-color: #cce5ff;");
                    case "Servie"         -> setStyle("-fx-background-color: #d4edda;");
                    case "Annulée"        -> setStyle("-fx-background-color: #f8d7da;");
                    default               -> setStyle("");
                }
            }
        });

        loadData();
    }

    private void loadData() {
        try {
            tableCommandes.setItems(
                FXCollections.observableArrayList(commandeDAO.findAll())
            );
        } catch (SQLException e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    /** Passe la commande sélectionnée en "En préparation" */
    @FXML
    private void handleEnPreparation() {
        Commande sel = tableCommandes.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Attention", "Sélectionnez une commande."); return; }
        if (!sel.getStatut().equals("En attente")) {
            showAlert("Attention", "Seulement les commandes 'En attente' peuvent être préparées.");
            return;
        }
        try {
            commandeDAO.updateStatut(sel.getId(), "En préparation");
            lblStatut.setText("🔥 Commande #" + sel.getId() + " en préparation !");
            lblStatut.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
            loadData();
        } catch (SQLException e) { showAlert("Erreur", e.getMessage()); }
    }

    /** Passe la commande sélectionnée en "Servie" */
    @FXML
    private void handleServie() {
        Commande sel = tableCommandes.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Attention", "Sélectionnez une commande."); return; }
        if (!sel.getStatut().equals("En préparation")) {
            showAlert("Attention", "Seulement les commandes 'En préparation' peuvent être servies.");
            return;
        }
        try {
            commandeDAO.updateStatut(sel.getId(), "Servie");
            lblStatut.setText("✅ Commande #" + sel.getId() + " servie avec succès !");
            lblStatut.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            loadData();
        } catch (SQLException e) { showAlert("Erreur", e.getMessage()); }
    }

    /** Actualiser la liste */
    @FXML
    private void handleActualiser() {
        loadData();
        lblStatut.setText("🔄 Liste actualisée.");
        lblStatut.setStyle("-fx-text-fill: #7f8c8d;");
    }

    private void showAlert(String t, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}