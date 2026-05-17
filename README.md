# Fiducia-JEE : Système de Gestion des Doctorants 🎓

Fiducia-JEE est une application web basée sur Spring Boot permettant la gestion complète du cycle de vie des doctorants, de l'inscription initiale à la soutenance de thèse.

## 🚀 Fonctionnalités Principales

*   **Gestion des Utilisateurs et Sécurité :** Authentification et autorisation sécurisées (Spring Security, JWT).
*   **Gestion des Inscriptions (`DossierInscription`) :** Soumission, suivi et validation des dossiers d'inscription au doctorat.
*   **Gestion des Soutenances (`DemandeSoutenance`) :** Planification et gestion des demandes de soutenance de thèse.
*   **Gestion des Fichiers (`FileStorageService`) :** Téléchargement et stockage des documents justificatifs et des thèses.
*   **Rôles Multiples :** 
    *   `Doctorant` (Utilisateur) : Soumet son dossier et sa demande de soutenance.
    *   `Directeur de Thèse` : Supervise et valide les travaux de recherche.
    *   `Personnel Administratif` : Gère les campagnes d'inscription et valide les dossiers.
*   **Gestion des Campagnes (`Campagne`) :** Ouverture et fermeture des périodes d'inscription.

## 🛠️ Technologies Utilisées

*   **Backend :** Java 17, Spring Boot 3.5
*   **Sécurité :** Spring Security, JWT (JSON Web Tokens)
*   **Base de Données :** PostgreSQL (Production), H2 Database (Tests)
*   **Accès aux données :** Spring Data JPA
*   **Frontend :** Thymeleaf (Templates HTML)
*   **Utilitaires :** Lombok, Maven

## ⚙️ Prérequis et Installation

### Prérequis

*   [Docker](https://docs.docker.com/get-docker/)
*   [Docker Compose](https://docs.docker.com/compose/install/)

### Installation avec Docker (Recommandé)

1.  **Cloner le dépôt :**
    ```bash
    git clone https://github.com/achraal/Fiducia-JEE.git
    cd fiducia-project
    ```

2.  **Configuration de l'environnement :**
    *   Assurez-vous que le fichier `.env` est présent et configuré à la racine du projet avec vos variables (identifiants PostgreSQL, configuration JWT, etc.).

3.  **Lancer l'application avec Docker Compose :**
    ```bash
    docker-compose up --build -d
    ```
    *Cette commande va télécharger l'image PostgreSQL, construire l'image de l'application Spring Boot, et démarrer les conteneurs en arrière-plan.*

4.  **Accéder à l'application :**
    *   L'application sera accessible à l'adresse : `http://localhost:8080`

### Arrêter l'application

Pour arrêter les conteneurs :
```bash
docker-compose down
```

## 👥 Équipe du Projet et Contributions

Ce projet a été réalisé en collaboration. Voici la répartition des tâches pour chaque membre de l'équipe :

| Membre de l'équipe | Rôle / Tâches accomplies |
| :--- | :--- |
| **EZ-ZAHRAOUI El-Mehdi** | *(**Module 1** - Développement du module de sécurité JWT, conception de la base de données, etc.)* |
| **AIT LAHCEN Achraf** | *(**Module 2** - Gestion des dossiers d'inscription, intégration Thymeleaf, etc.)* |
| **LAHLOU Chaimaa** | *(**Module 3** - Système de gestion des soutenances et upload de fichiers, etc.)* |


## 📄 Licence

Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.
