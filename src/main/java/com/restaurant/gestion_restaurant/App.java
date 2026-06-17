package com.restaurant.gestion_restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader dashboardLoader = new FXMLLoader(App.class.getResource("/views/dashboard.fxml"));
        FXMLLoader menuLoader      = new FXMLLoader(App.class.getResource("/views/menu.fxml"));
        FXMLLoader commandeLoader  = new FXMLLoader(App.class.getResource("/views/commande.fxml"));
        FXMLLoader clientLoader    = new FXMLLoader(App.class.getResource("/views/client.fxml"));
        FXMLLoader cuisineLoader   = new FXMLLoader(App.class.getResource("/views/cuisine.fxml"));

        Tab tabDashboard = new Tab("📊 Dashboard",   dashboardLoader.load());
        Tab tabMenus     = new Tab("🍽️ Menus",       menuLoader.load());
        Tab tabCommandes = new Tab("📋 Commandes",   commandeLoader.load());
        Tab tabClient    = new Tab("👤 Client",      clientLoader.load());
        Tab tabCuisine   = new Tab("👨‍🍳 Cuisine",    cuisineLoader.load());

        tabDashboard.setClosable(false);
        tabMenus.setClosable(false);
        tabCommandes.setClosable(false);
        tabClient.setClosable(false);
        tabCuisine.setClosable(false);

        TabPane tabPane = new TabPane(
            tabDashboard, tabMenus, tabCommandes, tabClient, tabCuisine
        );

        Scene scene = new Scene(tabPane, 950, 700);
        primaryStage.setTitle("🍴 Restaurant Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}