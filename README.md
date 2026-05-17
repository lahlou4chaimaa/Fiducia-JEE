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

*   [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
*   [Maven](https://maven.apache.org/download.cgi)
*   PostgreSQL & MongoDB (en cours d'exécution sur votre machine locale ou serveur)

### Installation

1.  **Cloner le dépôt :**
    ```bash
    git clone https://github.com/achraal/Fiducia-JEE.git
    cd fiducia-project
    ```

2.  **Configuration de la base de données :**
    *   Vérifiez le fichier `.env` ou `src/main/resources/application.properties` (ou `.yml`) pour configurer les identifiants de vos bases de données PostgreSQL et MongoDB.

3.  **Compiler et exécuter l'application :**
    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

4.  **Accéder à l'application :**
    *   L'application sera accessible à l'adresse : `http://localhost:8080`

## 👥 Équipe du Projet et Contributions

Ce projet a été réalisé en collaboration. Voici la répartition des tâches pour chaque membre de l'équipe :

| Membre de l'équipe | Rôle / Tâches accomplies |
| :--- | :--- |
| **EZ-ZAHRAOUI El-Mehdi** | *(Exemple : Développement du module de sécurité JWT, conception de la base de données, etc.)* Veuillez détailler ici vos contributions. |
| **AIT LAHCEN Achraf** | *(Exemple : Gestion des dossiers d'inscription, intégration Thymeleaf, etc.)* Veuillez détailler ici vos contributions. |
| **LAHLOU Chaimaa** | *(Exemple : Système de gestion des soutenances et upload de fichiers, etc.)* Veuillez détailler ici vos contributions. |

> **Note :** *Remplissez le tableau ci-dessus avec les noms des membres de votre équipe (ex: Achraf, Mehdi, Chaimaa) et les détails spécifiques de ce que chacun a développé dans le projet.*

## 📄 Licence

Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.
