# --- Étape 1 : Build de l'application ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copier le pom.xml et télécharger les dépendances (mise en cache optimale)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source et build le fichier JAR (en ignorant les tests pour accélérer le déploiement)
COPY src ./src
RUN mvn clean package -DskipTests

# --- Étape 2 : Exécution de l'application ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copier le JAR généré lors de la première étape
COPY --from=build /app/target/fiducia-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port par défaut de Spring Boot
EXPOSE 8080

# Commande d'exécution
ENTRYPOINT ["java", "-jar", "app.jar"]
