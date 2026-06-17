package com.restaurant.gestion_restaurant.controllers;

import com.restaurant.gestion_restaurant.database.ClientDAO;
import com.restaurant.gestion_restaurant.database.CommandeDAO;
import com.restaurant.gestion_restaurant.database.MenuDAO;
import com.restaurant.gestion_restaurant.models.Client;
import com.restaurant.gestion_restaurant.models.Commande;
import com.restaurant.gestion_restaurant.models.Menu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

/**
 * Contrôleur pour l'interface client.
 */
public class ClientController {

    // Infos client
    @FXML private TextField tfNom, tfPrenom, tfTelephone, tfTable;

    // Commande
    @FXML private ComboBox<Menu> cbMenu;
    @FXML private TextField tfQuantite;

    // Table mes commandes
    @FXML private TableView<Commande> tableCommandes;
    @FXML private TableColumn<Commande, Integer> colId, colTable, colQte;
    @FXML private TableColumn<Commande, String>  colPlat, colStatut, colDate;

    // Label confirmation
    @FXML private Label lblConfirmation;

    private final ClientDAO   clientDAO   = new ClientDAO();
    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final MenuDAO     menuDAO     = new MenuDAO();

    private int currentClientId = -1;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("numeroTable"));
        colPlat.setCellValueFactory(new PropertyValueFactory<>("menuNom"));
        colQte.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));

        // Couleurs statut
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

        try {
            cbMenu.setItems(FXCollections.observableArrayList(menuDAO.findAll()));
        } catch (SQLException e) {
            showAlert("Erreur", "Chargement des plats échoué.");
        }
    }

    /** Le client s'enregistre et passe sa commande */
    @FXML
    private void handleCommander() {
        if (tfNom.getText().isBlank() || tfPrenom.getText().isBlank()
                || tfTable.getText().isBlank() || cbMenu.getValue() == null
                || tfQuantite.getText().isBlank()) {
            showAlert("Validation", "Tous les champs sont obligatoires.");
            return;
        }

        try {
            // Créer le client
            Client client = new Client();
            client.setNom(tfNom.getText().trim());
            client.setPrenom(tfPrenom.getText().trim());
            client.setTelephone(tfTelephone.getText().trim());
            currentClientId = clientDAO.insert(client);

            // Créer la commande
            Commande commande = new Commande();
            commande.setNumeroTable(Integer.parseInt(tfTable.getText().trim()));
            commande.setMenuId(cbMenu.getValue().getId());
            commande.setQuantite(Integer.parseInt(tfQuantite.getText().trim()));
            commande.setStatut("En attente");
            commande.setClientId(currentClientId);
            commandeDAO.insert(commande);

            lblConfirmation.setText("✅ Commande passée avec succès ! Votre ID client : " + currentClientId);
            lblConfirmation.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

            loadMesCommandes();

        } catch (NumberFormatException e) {
            showAlert("Validation", "Table et Quantité doivent être des nombres.");
        } catch (SQLException e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    /** Voir mes commandes */
    @FXML
    private void handleVoirCommandes() {
        if (currentClientId == -1) {
            showAlert("Attention", "Passez d'abord une commande.");
            return;
        }
        loadMesCommandes();
    }

    /** Annuler une commande sélectionnée */
    @FXML
    private void handleAnnuler() {
        Commande sel = tableCommandes.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Attention", "Sélectionnez une commande à annuler."); return; }
        if (sel.getStatut().equals("Servie")) {
            showAlert("Attention", "Impossible d'annuler une commande déjà servie."); return;
        }
        try {
            commandeDAO.updateStatut(sel.getId(), "Annulée");
            lblConfirmation.setText("❌ Commande #" + sel.getId() + " annulée.");
            lblConfirmation.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            loadMesCommandes();
        } catch (SQLException e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    private void loadMesCommandes() {
        try {
            tableCommandes.setItems(FXCollections.observableArrayList(
                commandeDAO.findByClientId(currentClientId)
            ));
        } catch (SQLException e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    private void showAlert(String t, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}