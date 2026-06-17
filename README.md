# 🍴 Restaurant Manager

> Mini-projet Java — Système de gestion de restaurant  
> Développé avec **JavaFX** + **MySQL** — Architecture **MVC**

---

## 📋 Description

Application de bureau développée en Java permettant de gérer un système 
de restaurant complet : commandes clients, suivi cuisine, gestion des menus 
et tableau de bord statistique.

---

## 🚀 Fonctionnalités

### 📊 Dashboard
- Statistiques en temps réel des commandes
- Compteurs : Total / En attente / En préparation / Servies / Annulées
- Tableau des dernières commandes avec code couleur

### 🍽️ Gestion des Menus
- Ajouter, modifier, supprimer des plats
- Catégories : Entrée, Plat, Dessert, Boisson
- Affichage des prix en MAD

### 📋 Gestion des Commandes
- CRUD complet des commandes
- Liaison avec les plats du menu
- Suivi du statut de chaque commande

### 👤 Espace Client
- Saisie des informations client
- Choix du plat et quantité
- Suivi et annulation de commande

### 👨‍🍳 Espace Cuisine
- Vue dédiée au cuisinier
- Changement de statut en un clic
- Code couleur pour les priorités

---

## 🛠️ Technologies

| Technologie | Version |
|---|---|
| Java | 17 |
| JavaFX | 21 |
| MySQL | 8.0 |
| Maven | 3.x |
| Architecture | MVC + DAO |

---

## 🗄️ Structure du projet

```
src/main/java/com/restaurant/gestion_restaurant/
├── App.java
├── controllers/
│   ├── DashboardController.java
│   ├── MenuController.java
│   ├── CommandeController.java
│   ├── ClientController.java
│   └── CuisineController.java
├── models/
│   ├── Menu.java
│   ├── Commande.java
│   └── Client.java
└── database/
    ├── DatabaseConnection.java
    ├── MenuDAO.java
    ├── CommandeDAO.java
    └── ClientDAO.java
```

---

## ⚙️ Installation

### Prérequis
- Java 17+
- Maven 3+
- MySQL 8+

### Étapes

**1. Cloner le projet**
```bash
git clone https://github.com/IsmailHaddouch/gestion-restaurant.git
cd gestion-restaurant
```

**2. Créer la base de données**
```bash
mysql -u root -p < restaurant.sql
```

**3. Configurer la connexion** dans `DatabaseConnection.java`
```java
private static final String URL      = "jdbc:mysql://localhost:3306/restaurant_db";
private static final String USER     = "root";
private static final String PASSWORD = "votre_mot_de_passe";
```

**4. Lancer l'application**
```bash
mvn javafx:run
```

---

## 👥 Auteur

**Ismail Haddouch**  
SUPMTI Rabat — Module : Programmation Java  
Année universitaire 2025-2026  
Enseignant : Pr. Soufiane HAMIDA