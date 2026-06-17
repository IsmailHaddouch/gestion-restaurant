package com.restaurant.gestion_restaurant.controllers;

import com.restaurant.gestion_restaurant.database.CommandeDAO;
import com.restaurant.gestion_restaurant.database.MenuDAO;
import com.restaurant.gestion_restaurant.models.Commande;
import com.restaurant.gestion_restaurant.models.Menu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

/**
 * Contrôleur pour la gestion des commandes (serveur + cuisine).
 */
public class CommandeController {

    @FXML private TextField tfTable, tfQuantite;
    @FXML private ComboBox<Menu> cbMenu;
    @FXML private ComboBox<String> cbStatut;
    @FXML private TableView<Commande> tableCommandes;
    @FXML private TableColumn<Commande, Integer> colId, colTable, colQte;
    @FXML private TableColumn<Commande, String> colPlat, colStatut, colDate, colClient;

    // Bouton cuisine pour changer statut rapidement
    @FXML private Button btnEnPreparation;
    @FXML private Button btnServie;

    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final MenuDAO     menuDAO     = new MenuDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("numeroTable"));
        colPlat.setCellValueFactory(new PropertyValueFactory<>("menuNom"));
        colQte.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("clientNom"));

        cbStatut.setItems(FXCollections.observableArrayList(
            "En attente", "En préparation", "Servie", "Annulée"
        ));

        try {
            cbMenu.setItems(FXCollections.observableArrayList(menuDAO.findAll()));
        } catch (SQLException e) {
            showAlert("Erreur", "Chargement menus échoué.");
        }

        // Colorier les lignes selon le statut
        tableCommandes.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Commande c, boolean empty) {
                super.updateItem(c, empty);
                if (c == null || empty) {
                    setStyle("");
                } else {
                    switch (c.getStatut()) {
                        case "En attente"     -> setStyle("-fx-background-color: #fff3cd;");
                        case "En préparation" -> setStyle("-fx-background-color: #cce5ff;");
                        case "Servie"         -> setStyle("-fx-background-color: #d4edda;");
                        case "Annulée"        -> setStyle("-fx-background-color: #f8d7da;");
                        default               -> setStyle("");
                    }
                }
            }
        });

        tableCommandes.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                tfTable.setText(String.valueOf(sel.getNumeroTable()));
                tfQuantite.setText(String.valueOf(sel.getQuantite()));
                cbStatut.setValue(sel.getStatut());
                cbMenu.getItems().stream()
                    .filter(m -> m.getId() == sel.getMenuId())
                    .findFirst().ifPresent(cbMenu::setValue);
            }
        });

        loadData();
    }

    private void loadData() {
        try {
            tableCommandes.setItems(FXCollections.observableArrayList(commandeDAO.findAll()));
        } catch (SQLException e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML private void handleAjouter() {
        if (!validateFields()) return;
        try { commandeDAO.insert(buildCommande()); clearFields(); loadData(); }
        catch (SQLException e) { showAlert("Erreur", e.getMessage()); }
    }

    @FXML private void handleModifier() {
        Commande sel = tableCommandes.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Attention", "Sélectionnez une commande."); return; }
        if (!validateFields()) return;
        try {
            Commande c = buildCommande();
            c.setId(sel.getId());
            commandeDAO.update(c); clearFields(); loadData();
        } catch (SQLException e) { showAlert("Erreur", e.getMessage()); }
    }

    @FXML private void handleSupprimer() {
        Commande sel = tableCommandes.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Attention", "Sélectionnez une commande."); return; }
        try { commandeDAO.delete(sel.getId()); clearFields(); loadData(); }
        catch (SQLException e) { showAlert("Erreur", e.getMessage()); }
    }

    @FXML private void handleActualiser() { loadData(); }

    /** Cuisine — passe en "En préparation" en un clic */
    @FXML private void handleEnPreparation() {
        Commande sel = tableCommandes.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Attention", "Sélectionnez une commande."); return; }
        try { commandeDAO.updateStatut(sel.getId(), "En préparation"); loadData(); }
        catch (SQLException e) { showAlert("Erreur", e.getMessage()); }
    }

    /** Cuisine — passe en "Servie" en un clic */
    @FXML private void handleServie() {
        Commande sel = tableCommandes.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Attention", "Sélectionnez une commande."); return; }
        try { commandeDAO.updateStatut(sel.getId(), "Servie"); loadData(); }
        catch (SQLException e) { showAlert("Erreur", e.getMessage()); }
    }

    private Commande buildCommande() {
        Commande c = new Commande();
        c.setNumeroTable(Integer.parseInt(tfTable.getText().trim()));
        c.setMenuId(cbMenu.getValue().getId());
        c.setQuantite(Integer.parseInt(tfQuantite.getText().trim()));
        c.setStatut(cbStatut.getValue() != null ? cbStatut.getValue() : "En attente");
        return c;
    }

    private boolean validateFields() {
        if (tfTable.getText().isBlank() || cbMenu.getValue() == null || tfQuantite.getText().isBlank()) {
            showAlert("Validation", "Table, Plat et Quantité obligatoires."); return false;
        }
        try { Integer.parseInt(tfTable.getText()); Integer.parseInt(tfQuantite.getText()); }
        catch (NumberFormatException e) { showAlert("Validation", "Table et Quantité doivent être des entiers."); return false; }
        return true;
    }

    private void clearFields() {
        tfTable.clear(); tfQuantite.clear(); cbMenu.setValue(null); cbStatut.setValue(null);
    }

    private void showAlert(String t, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}