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
| **EZ-ZAHRAOUI El-Mehdi** | **Module 1 : Gestion des comptes et authentification**<br>Ce module est transverse et gère l'identité des utilisateurs :<br>• Création de compte pour les nouveaux candidats.<br>• Connexion / Déconnexion sécurisée pour tous les profils.<br>• Gestion des rôles et des permissions (un administrateur doit pouvoir assigner les rôles adéquats). |
| **AIT LAHCEN Achraf** | **Module 2 : Processus d'inscription, Conception & DevOps**<br>Ce module gère le début du cycle et son renouvellement annuel :<br>• Gestion des campagnes d'inscription/réinscription par l'administration (définition des dates d'ouverture et de fermeture).<br>• Formulaire d'inscription dynamique pour le candidat (informations personnelles, sujet de thèse, directeur de thèse, collaboration etc.).<br>• Téléversement de pièces justificatives (diplômes, CV, lettre de motivation, etc.) avec vérification du format (PDF, JPG...).<br>• **Circuit de validation en ligne :**<br>&nbsp;&nbsp;1. Le dossier est soumis par le candidat.<br>&nbsp;&nbsp;2. Notification au directeur de thèse pour avis.<br>&nbsp;&nbsp;3. Notification à l'administration pour vérification et validation administrative.<br>• Tableau de bord pour le doctorant lui permettant de suivre l'état de son dossier en temps réel.<br><br>**Architecture & Infrastructure :**<br>• Conception générale de l'architecture du système.<br>• Conteneurisation et migration de l'application sous **Docker**. |
| **LAHLOU Chaimaa** | **Module 3 : Processus de soutenance**<br>Ce module gère la fin du cycle doctoral :<br>• Formulaires de demande de soutenance initiés par le doctorant.<br>• **Check-list de prérequis à la soutenance (validée par le système et l'administration) :**<br>&nbsp;&nbsp;◦ Nombre de publications scientifiques.<br>&nbsp;&nbsp;◦ Crédits de formation doctorale obtenus.<br>&nbsp;&nbsp;◦ Liste des documents à fournir (Demande manuscrite adressée au chef de l’établissement, rapport de thèse, rapport anti-plagiat, rapport contenant les publications et les communications, les copies d'attestations de formations et autorisation de soutenance).<br>• **Proposition du jury par le Directeur de Thèse :**<br>&nbsp;&nbsp;◦ Désignation des rapporteurs, examinateurs, etc.<br>• Autorisation de soutenance et planification administrative : Une fois les rapports favorables reçus, l’administration donne l'autorisation finale, fixe et communique la date, l'heure et le lieu de la soutenance. |


## 📄 Licence

Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.
