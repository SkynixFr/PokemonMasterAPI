# Pokemon Master - API

## Description
Cette application est une API REST développée en Spring Boot qui utilise MongoDB pour le stockage des données.

## Prérequis
- **Java 17** ou plus récent
- **Gradle 7.x** ou plus récent
- **MongoDB** (installé et en cours d'exécution)
- **Git** (pour cloner le projet)

## Installation

### 1. Cloner le projet
Clonez le projet depuis votre dépôt GitHub :

```bash
git clone https://github.com/SkynixFr/PokemonMasterAPI.git
cd PokemonMasterAPI
```

### 2. Configuration de la base de données

#### 2.1. Créer la base de données

Connectez-vous à votre instance MongoDB et créez une base de données nommée `pokemon_master`.

#### 2.2. Créer un utilisateur

Créez un utilisateur pour la base de données `pokemon_master` avec tous les privilèges.

### 3. Configuration de l'application

#### 3.1. Configuration de l'application

Créez un fichier `application.properties` dans le répertoire `src/main/resources` et ajoutez les propriétés de configuration du fichier **application-example.properties**.
Ajustez les valeurs des propriétés pour correspondre à votre configuration.

### 4. Exécution de l'application

Exécutez l'application avec Gradle

```bash
gradle bootRun
```


## Utilisation

L'API est accessible à l'adresse `http://localhost:8080`.

