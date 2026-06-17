package com.restaurant.gestion_restaurant.controllers;

import com.restaurant.gestion_restaurant.database.MenuDAO;
import com.restaurant.gestion_restaurant.models.Menu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class MenuController {

    @FXML private TextField tfNom, tfDescription, tfPrix;
    @FXML private ComboBox<String> cbCategorie;
    @FXML private TableView<Menu> tableMenus;
    @FXML private TableColumn<Menu, Integer> colId;
    @FXML private TableColumn<Menu, String>  colNom, colDescription, colCategorie;
    @FXML private TableColumn<Menu, Double>  colPrix;

    private final MenuDAO menuDAO = new MenuDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        cbCategorie.setItems(FXCollections.observableArrayList(
            "Entrée", "Plat", "Dessert", "Boisson"
        ));

        tableMenus.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                tfNom.setText(sel.getNom());
                tfDescription.setText(sel.getDescription());
                tfPrix.setText(String.valueOf(sel.getPrix()));
                cbCategorie.setValue(sel.getCategorie());
            }
        });
        loadData();
    }

    private void loadData() {
        try {
            tableMenus.setItems(FXCollections.observableArrayList(menuDAO.findAll()));
        } catch (SQLException e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML private void handleAjouter() {
        if (!validateFields()) return;
        try { menuDAO.insert(buildMenu()); clearFields(); loadData(); }
        catch (SQLException e) { showAlert("Erreur", e.getMessage()); }
    }

    @FXML private void handleModifier() {
        Menu sel = tableMenus.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Attention", "Sélectionnez un menu."); return; }
        if (!validateFields()) return;
        try {
            Menu m = buildMenu();
            m.setId(sel.getId());
            menuDAO.update(m); clearFields(); loadData();
        } catch (SQLException e) { showAlert("Erreur", e.getMessage()); }
    }

    @FXML private void handleSupprimer() {
        Menu sel = tableMenus.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Attention", "Sélectionnez un menu."); return; }
        try { menuDAO.delete(sel.getId()); clearFields(); loadData(); }
        catch (SQLException e) { showAlert("Erreur", e.getMessage()); }
    }

    @FXML private void handleActualiser() { loadData(); }

    private Menu buildMenu() {
        Menu m = new Menu();
        m.setNom(tfNom.getText().trim());
        m.setDescription(tfDescription.getText().trim());
        m.setPrix(Double.parseDouble(tfPrix.getText().trim()));
        m.setCategorie(cbCategorie.getValue());
        return m;
    }

    private boolean validateFields() {
        if (tfNom.getText().isBlank() || tfPrix.getText().isBlank() || cbCategorie.getValue() == null) {
            showAlert("Validation", "Nom, Prix et Catégorie obligatoires."); return false;
        }
        try { Double.parseDouble(tfPrix.getText().trim()); }
        catch (NumberFormatException e) { showAlert("Validation", "Prix invalide."); return false; }
        return true;
    }

    private void clearFields() {
        tfNom.clear(); tfDescription.clear(); tfPrix.clear(); cbCategorie.setValue(null);
    }

    private void showAlert(String t, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}