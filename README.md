# Gestion des Ressources - ENSA Tetouan

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Material Design](https://img.shields.io/badge/Material_Design-757575?style=for-the-badge&logo=material-design&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge&logo=square&logoColor=white)

## Description

Système complet de gestion des ressources humaines et matérielles pour l'École Nationale des Sciences Appliquées de Tétouan. Cette application intègre la gestion du personnel, le recrutement, les demandes administratives, la gestion des besoins en équipement, le suivi des commandes et la comptabilité des dépenses.

## Architecture

Le projet suit une architecture client-serveur moderne avec:

- **Frontend**: Application Android native utilisant l'architecture MVVM
- **Backend**: API REST développée avec Spring Boot
- **Base de données**: SQLite 

```
Android-Gestion-des-Ressources/
├── android/          # Application mobile Android
│   ├── app/
│   └── gradle/
│
└── backend/          # API REST Spring Boot
    ├── src/
    ├── pom.xml
    └── backend.db
```

## Fonctionnalités principales

### Module Ressources Humaines

#### Gestion des demandes
- Soumettre des demandes de congé ou de démission
- Suivi du statut des demandes (En attente, Acceptée, Refusée)
- Téléchargement de justificatifs
- Interface d'approbation/rejet pour les administrateurs
- Filtrage par statut et type de demande
- Réponse à la demande par courriel

#### Gestion du personnel
- Consultation des informations des employés
- Hiérarchie organisationnelle et départements
- Gestion des rôles et des accès

### Module Recrutement

#### Offres d'emploi
- Création et publication d'offres d'emploi
- Définition du type de contrat, département et nombre de postes
- Paramétrage des dates d'ouverture et de clôture
- Suivi du statut des recrutements

#### Gestion des candidatures
- Dépôt de candidatures avec CV et lettre de motivation
- Suivi du statut des candidatures
- Évaluation des candidats (notes écrit et oral)
- Sélection des candidats retenus
- Notifications automatiques par email

### Module Gestion des Ressources

#### Gestion des besoins
- Expression de besoins en équipement ou ressources
- Spécification de la quantité et du budget estimé
- Attribution de niveaux de priorité (Haute, Moyenne, Basse)
- Workflow de validation avec commentaires administrateurs
- Traçabilité complète du traitement

#### Bons de commande
- Création de commandes liées aux besoins validés
- Gestion des informations fournisseurs
- Suivi des dates de livraison prévues et effectives
- Mise à jour du statut des commandes
- Génération de numéros de bons de commande

#### Suivi des dépenses
- Enregistrement des dépenses liées aux besoins
- Catégorisation des dépenses
- Suivi des factures et fournisseurs
- Enregistrement des modes de paiement
- Historique complet avec traçabilité

### Fonctionnalités transverses

- **Gestion de fichiers**: Upload de documents (CV, lettres de motivation, justificatifs) jusqu'à 10 MB
- **Authentification**: Système de rôles (ADMIN_RH, ADMIN_ECO, PERSONNEL)
- **Synchronisation**: Synchronisation en arrière-plan des données
- **Mode hors ligne**: Cache local avec Room Database

## Technologies utilisées

### Frontend (Android)

| Technologie | Version | Usage |
|------------|---------|-------|
| Android SDK | 24-36 | Plateforme mobile |
| Java | - | Langage de programmation |
| Material Design | 1.10.0 | Composants UI |
| Retrofit | 2.9.0 | Client HTTP REST |
| Room | 2.5.2 | Base de données locale |
| Navigation Component | 2.7.2 | Navigation entre fragments |
| LiveData & ViewModel | 2.6.2 | Architecture MVVM |
| WorkManager | 2.8.1 | Tâches en arrière-plan |
| Gson | 2.10.1 | Sérialisation JSON |
| OkHttp | 4.11.0 | Intercepteurs HTTP |

### Backend (Spring Boot)

| Technologie | Version | Usage |
|------------|---------|-------|
| Spring Boot | 4.0.1 | Framework backend |
| Java | 21 | Langage de programmation |
| Spring Data JPA | - | ORM et accès aux données |
| Spring Security | - | Authentification/Autorisation |
| Spring Mail | - | Envoi d'emails |
| Flyway | - | Migrations de base de données |
| SQLite | - | Base de données (dev) |
| MySQL Connector | - | Base de données (prod) |
| Lombok | - | Génération de code |
| Maven | - | Gestionnaire de dépendances |

## Installation et configuration

### Prérequis

- JDK 21 ou supérieur
- Android Studio Hedgehog ou supérieur
- Maven 3.6+
- Git

### Backend

1. Cloner le repository:
```bash
git clone <repository-url>
cd Android-Gestion-des-Ressources/backend
```

2. Configuration de la base de données:
   - Le projet utilise SQLite par défaut avec le fichier `backend.db`
   - Les migrations Flyway s'exécutent automatiquement au démarrage

3. Configuration de l'email (optionnelle):
   - Éditer `application.properties`
   - Configurer les paramètres SMTP:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=votre-email@gmail.com
spring.mail.password=votre-mot-de-passe-application
```

4. Démarrer le serveur:
```bash
mvn spring-boot:run
```

Le serveur démarre sur `http://localhost:8080`

### Frontend (Android)

1. Ouvrir Android Studio

2. Ouvrir le projet Android:
   - File > Open
   - Sélectionner le dossier `android/`

3. Synchroniser Gradle:
   - Android Studio synchronise automatiquement les dépendances

4. Configuration de l'URL backend:
   - Par défaut: `http://10.0.2.2:8080/` (émulateur Android)
   - Pour un appareil physique, modifier dans `RetrofitClient.java`:
```java
private static final String BASE_URL = "http://VOTRE_IP:8080/";
```

5. Lancer l'application:
   - Connecter un appareil ou démarrer un émulateur
   - Run > Run 'app'

## Endpoints API

### Demandes
- `GET /api/v1/demandes` - Liste des demandes (filtre par statut optionnel)
- `GET /api/v1/demandes/{id}` - Détails d'une demande
- `POST /api/v1/demandes` - Créer une demande
- `POST /api/v1/demandes/{id}/status` - Mettre à jour le statut

### Besoins
- `GET /api/v1/besoins` - Liste des besoins
- `GET /api/v1/besoins/{id}` - Détails d'un besoin
- `POST /api/v1/besoins` - Créer un besoin
- `PUT /api/v1/besoins/{id}` - Mettre à jour un besoin
- `DELETE /api/v1/besoins/{id}` - Supprimer un besoin
- `POST /api/v1/besoins/{id}/status` - Changer le statut

### Commandes
- `GET /api/v1/commandes` - Liste des commandes
- `GET /api/v1/commandes/{id}` - Détails d'une commande
- `POST /api/v1/commandes` - Créer une commande
- `PUT /api/v1/commandes/{id}` - Mettre à jour une commande
- `DELETE /api/v1/commandes/{id}` - Supprimer une commande
- `GET /api/v1/commandes/by-besoin/{besoinId}` - Commandes d'un besoin

### Dépenses
- `GET /api/v1/depenses` - Liste des dépenses
- `GET /api/v1/depenses/{id}` - Détails d'une dépense
- `POST /api/v1/depenses` - Créer une dépense
- `PUT /api/v1/depenses/{id}` - Mettre à jour une dépense
- `DELETE /api/v1/depenses/{id}` - Supprimer une dépense
- `GET /api/v1/depenses/by-besoin/{besoinId}` - Dépenses d'un besoin

### Recrutements
- `GET /api/v1/recrutements` - Liste des recrutements
- `GET /api/v1/recrutements/{id}` - Détails d'un recrutement
- `POST /api/v1/recrutements` - Créer un recrutement
- `PUT /api/v1/recrutements/{id}` - Mettre à jour un recrutement
- `DELETE /api/v1/recrutements/{id}` - Supprimer un recrutement
- `POST /api/v1/recrutements/{id}/status` - Changer le statut
- `GET /api/v1/recrutements/{id}/candidatures` - Liste des candidatures
- `POST /api/v1/recrutements/{id}/select` - Sélectionner les candidats retenus

### Candidatures
- `GET /api/v1/candidatures-recrutement/by-recrutement/{recrutementId}` - Liste des candidatures
- `GET /api/v1/candidatures-recrutement/{id}` - Détails d'une candidature
- `POST /api/v1/candidatures-recrutement` - Soumettre une candidature
- `POST /api/v1/candidatures-recrutement/{id}/status` - Mettre à jour le statut

### Fichiers
- `POST /api/v1/uploads` - Upload de fichier (multipart/form-data, max 10 MB)

## Schéma de base de données

### Tables principales

- **personnel** - Enregistrements des employés
- **utilisateurs** - Comptes utilisateurs et authentification
- **demandes** - Demandes de congé et de démission
- **budget** - Suivi budgétaire annuel
- **besoins** - Besoins en équipement et ressources
- **recettes** - Enregistrement des recettes
- **dépenses** - Suivi des dépenses
- **commandes** - Bons de commande
- **recrutements** - Offres d'emploi
- **candidatures_recrutement** - Candidatures aux offres
- **notifications** - Notifications utilisateurs
- **historique** - Piste d'audit

### Migrations Flyway

Les migrations se trouvent dans `backend/src/main/resources/db/migration/`:
- V1: Schéma initial avec toutes les tables principales
- V2: Données initiales de recrutement et indexes
- V3: Schéma des tables recrutements et candidatures
- V4: Alignement du schéma recrutements
- V5: Mise à jour du schéma demandes
- V6: Ajout du champ justificatif aux demandes

## Architecture applicative

### Pattern MVVM (Android)

```
View (Fragment/Activity)
    ↓
ViewModel (LiveData)
    ↓
Repository (API + Cache)
    ↓
[Retrofit API Service | Room Database]
    ↓
Backend REST API
```

### Flux de données

1. L'utilisateur interagit avec un Fragment
2. Le Fragment observe un ViewModel via LiveData
3. Le ViewModel délègue au Repository
4. Le Repository consulte d'abord le cache local (Room)
5. Si nécessaire, effectue un appel API via Retrofit
6. Les données sont mises en cache et exposées via LiveData
7. Le Fragment se met à jour automatiquement

### Synchronisation hors ligne

- Cache local avec Room Database
- WorkManager pour la synchronisation en arrière-plan
- Politique de synchronisation périodique
- Gestion des conflits de données

## Sécurité

### Configuration actuelle (Développement)

- CSRF désactivé
- Autorisation permissive (tous les endpoints accessibles)
- Adapté pour les tests et le développement

### Configuration recommandée (Production)

- Activer Spring Security
- Implémenter JWT pour l'authentification
- Configurer les rôles et permissions
- Activer HTTPS
- Configurer CORS de manière restrictive
- Protéger les endpoints sensibles

## Rôles utilisateurs

- **RH** - Administrateur Ressources Humaines
  - Gestion complète des demandes et du personnel
  - Validation des demandes de congé/démission
  - Gestion des recrutements

- **ADMIN_ECO** - Administrateur Économique
  - Gestion des besoins, commandes et dépenses
  - Validation des achats
  - Suivi budgétaire

- **PERSONNEL** - Employé
  - Soumission de demandes
  - Consultation de ses propres demandes
  - Candidature aux offres internes
