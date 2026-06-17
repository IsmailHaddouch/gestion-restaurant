package com.restaurant.gestion_restaurant.models;

/**
 * Classe modèle représentant une commande.
 */
public class Commande {
    private int id;
    private int numeroTable;
    private int menuId;
    private String menuNom;
    private int quantite;
    private String statut;
    private String dateCommande;
    private int clientId;
    private String clientNom;

    public Commande() {}

    public Commande(int id, int numeroTable, int menuId, String menuNom,
                    int quantite, String statut, String dateCommande, String clientNom) {
        this.id = id;
        this.numeroTable = numeroTable;
        this.menuId = menuId;
        this.menuNom = menuNom;
        this.quantite = quantite;
        this.statut = statut;
        this.dateCommande = dateCommande;
        this.clientNom = clientNom;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNumeroTable() { return numeroTable; }
    public void setNumeroTable(int numeroTable) { this.numeroTable = numeroTable; }
    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }
    public String getMenuNom() { return menuNom; }
    public void setMenuNom(String menuNom) { this.menuNom = menuNom; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getDateCommande() { return dateCommande; }
    public void setDateCommande(String dateCommande) { this.dateCommande = dateCommande; }
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }
}