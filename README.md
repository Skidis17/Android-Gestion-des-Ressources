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

Systeme complet de gestion des ressources humaines et materielles pour l'Ecole Nationale des Sciences Appliquees de Tetouan. Cette application integre la gestion du personnel, le recrutement, les demandes administratives, la gestion des besoins en equipement, le suivi des commandes et la comptabilite des depenses.

## Architecture

Le projet suit une architecture client-serveur moderne avec:

- **Frontend**: Application Android native utilisant l'architecture MVVM
- **Backend**: API REST developpee avec Spring Boot
- **Base de donnees**: SQLite pour le developpement avec support MySQL pour la production

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

## Fonctionnalites principales

### Module Ressources Humaines

#### Gestion des demandes
- Soumettre des demandes de conge ou de demission
- Suivi du statut des demandes (En attente, Acceptee, Refusee)
- Telechargement de justificatifs
- Interface d'approbation/rejet pour les administrateurs
- Filtrage par statut et type de demande

#### Gestion du personnel
- Consultation des informations des employes
- Hierarchie organisationnelle et departements
- Gestion des roles et des acces

### Module Recrutement

#### Offres d'emploi
- Creation et publication d'offres d'emploi
- Definition du type de contrat, departement et nombre de postes
- Parametrage des dates d'ouverture et de cloture
- Suivi du statut des recrutements

#### Gestion des candidatures
- Depot de candidatures avec CV et lettre de motivation
- Suivi du statut des candidatures
- Evaluation des candidats (notes ecrit et oral)
- Selection des candidats retenus
- Notifications automatiques par email

### Module Gestion des Ressources

#### Gestion des besoins
- Expression de besoins en equipement ou ressources
- Specification de la quantite et du budget estime
- Attribution de niveaux de priorite (Haute, Moyenne, Basse)
- Workflow de validation avec commentaires administrateurs
- Traçabilite complete du traitement

#### Bons de commande
- Creation de commandes liees aux besoins valides
- Gestion des informations fournisseurs
- Suivi des dates de livraison prevues et effectives
- Mise a jour du statut des commandes
- Generation de numeros de bons de commande

#### Suivi des depenses
- Enregistrement des depenses liees aux besoins
- Categorisation des depenses
- Suivi des factures et fournisseurs
- Enregistrement des modes de paiement
- Historique complet avec tracabilite

### Fonctionnalites transverses

- **Notifications**: Systeme de notifications in-app pour les changements de statut
- **Gestion de fichiers**: Upload de documents (CV, lettres de motivation, justificatifs) jusqu'a 10 MB
- **Authentification**: Systeme de roles (ADMIN_RH, ADMIN_ECO, PERSONNEL)
- **Synchronisation**: Synchronisation en arriere-plan des donnees
- **Mode hors ligne**: Cache local avec Room Database

## Technologies utilisees

### Frontend (Android)

| Technologie | Version | Usage |
|------------|---------|-------|
| Android SDK | 24-36 | Plateforme mobile |
| Java | - | Langage de programmation |
| Material Design | 1.10.0 | Composants UI |
| Retrofit | 2.9.0 | Client HTTP REST |
| Room | 2.5.2 | Base de donnees locale |
| Navigation Component | 2.7.2 | Navigation entre fragments |
| LiveData & ViewModel | 2.6.2 | Architecture MVVM |
| WorkManager | 2.8.1 | Taches en arriere-plan |
| Gson | 2.10.1 | Serialisation JSON |
| OkHttp | 4.11.0 | Intercepteurs HTTP |

### Backend (Spring Boot)

| Technologie | Version | Usage |
|------------|---------|-------|
| Spring Boot | 4.0.1 | Framework backend |
| Java | 21 | Langage de programmation |
| Spring Data JPA | - | ORM et acces aux donnees |
| Spring Security | - | Authentification/Autorisation |
| Spring Mail | - | Envoi d'emails |
| Flyway | - | Migrations de base de donnees |
| SQLite | - | Base de donnees (dev) |
| MySQL Connector | - | Base de donnees (prod) |
| Lombok | - | Generation de code |
| Maven | - | Gestionnaire de dependances |

## Installation et configuration

### Prerequisites

- JDK 21 ou superieur
- Android Studio Hedgehog ou superieur
- Maven 3.6+
- Git

### Backend

1. Cloner le repository:
```bash
git clone <repository-url>
cd Android-Gestion-des-Ressources/backend
```

2. Configuration de la base de donnees:
   - Le projet utilise SQLite par defaut avec le fichier `backend.db`
   - Les migrations Flyway s'executent automatiquement au demarrage

3. Configuration de l'email (optionnelle):
   - Editer `application.properties`
   - Configurer les parametres SMTP:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=votre-email@gmail.com
spring.mail.password=votre-mot-de-passe-application
```

4. Demarrer le serveur:
```bash
mvn spring-boot:run
```

Le serveur demarre sur `http://localhost:8080`

### Frontend (Android)

1. Ouvrir Android Studio

2. Ouvrir le projet Android:
   - File > Open
   - Selectionner le dossier `android/`

3. Synchroniser Gradle:
   - Android Studio synchronise automatiquement les dependances

4. Configuration de l'URL backend:
   - Par defaut: `http://10.0.2.2:8080/` (emulateur Android)
   - Pour un appareil physique, modifier dans `RetrofitClient.java`:
```java
private static final String BASE_URL = "http://VOTRE_IP:8080/";
```

5. Lancer l'application:
   - Connecter un appareil ou demarrer un emulateur
   - Run > Run 'app'

## Endpoints API

### Demandes
- `GET /api/v1/demandes` - Liste des demandes (filtre par statut optionnel)
- `GET /api/v1/demandes/{id}` - Details d'une demande
- `POST /api/v1/demandes` - Creer une demande
- `POST /api/v1/demandes/{id}/status` - Mettre a jour le statut

### Besoins
- `GET /api/v1/besoins` - Liste des besoins
- `GET /api/v1/besoins/{id}` - Details d'un besoin
- `POST /api/v1/besoins` - Creer un besoin
- `PUT /api/v1/besoins/{id}` - Mettre a jour un besoin
- `DELETE /api/v1/besoins/{id}` - Supprimer un besoin
- `POST /api/v1/besoins/{id}/status` - Changer le statut

### Commandes
- `GET /api/v1/commandes` - Liste des commandes
- `GET /api/v1/commandes/{id}` - Details d'une commande
- `POST /api/v1/commandes` - Creer une commande
- `PUT /api/v1/commandes/{id}` - Mettre a jour une commande
- `DELETE /api/v1/commandes/{id}` - Supprimer une commande
- `GET /api/v1/commandes/by-besoin/{besoinId}` - Commandes d'un besoin

### Depenses
- `GET /api/v1/depenses` - Liste des depenses
- `GET /api/v1/depenses/{id}` - Details d'une depense
- `POST /api/v1/depenses` - Creer une depense
- `PUT /api/v1/depenses/{id}` - Mettre a jour une depense
- `DELETE /api/v1/depenses/{id}` - Supprimer une depense
- `GET /api/v1/depenses/by-besoin/{besoinId}` - Depenses d'un besoin

### Recrutements
- `GET /api/v1/recrutements` - Liste des recrutements
- `GET /api/v1/recrutements/{id}` - Details d'un recrutement
- `POST /api/v1/recrutements` - Creer un recrutement
- `PUT /api/v1/recrutements/{id}` - Mettre a jour un recrutement
- `DELETE /api/v1/recrutements/{id}` - Supprimer un recrutement
- `POST /api/v1/recrutements/{id}/status` - Changer le statut
- `GET /api/v1/recrutements/{id}/candidatures` - Liste des candidatures
- `POST /api/v1/recrutements/{id}/select` - Selectionner les candidats retenus

### Candidatures
- `GET /api/v1/candidatures-recrutement/by-recrutement/{recrutementId}` - Liste des candidatures
- `GET /api/v1/candidatures-recrutement/{id}` - Details d'une candidature
- `POST /api/v1/candidatures-recrutement` - Soumettre une candidature
- `POST /api/v1/candidatures-recrutement/{id}/status` - Mettre a jour le statut

### Fichiers
- `POST /api/v1/uploads` - Upload de fichier (multipart/form-data, max 10 MB)

## Schema de base de donnees

### Tables principales

- **personnel** - Enregistrements des employes
- **utilisateurs** - Comptes utilisateurs et authentification
- **demandes** - Demandes de conge et de demission
- **budget** - Suivi budgetaire annuel
- **besoins** - Besoins en equipement et ressources
- **recettes** - Enregistrement des recettes
- **depenses** - Suivi des depenses
- **commandes** - Bons de commande
- **recrutements** - Offres d'emploi
- **candidatures_recrutement** - Candidatures aux offres
- **notifications** - Notifications utilisateurs
- **historique** - Piste d'audit

### Migrations Flyway

Les migrations se trouvent dans `backend/src/main/resources/db/migration/`:
- V1: Schema initial avec toutes les tables principales
- V2: Donnees initiales de recrutement et indexes
- V3: Schema des tables recrutements et candidatures
- V4: Alignement du schema recrutements
- V5: Mise a jour du schema demandes
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

### Flux de donnees

1. L'utilisateur interagit avec un Fragment
2. Le Fragment observe un ViewModel via LiveData
3. Le ViewModel delegue au Repository
4. Le Repository consulte d'abord le cache local (Room)
5. Si necessaire, effectue un appel API via Retrofit
6. Les donnees sont mises en cache et exposees via LiveData
7. Le Fragment se met a jour automatiquement

### Synchronisation hors ligne

- Cache local avec Room Database
- WorkManager pour la synchronisation en arriere-plan
- Politique de synchronisation periodique
- Gestion des conflits de donnees

## Securite

### Configuration actuelle (Developpement)

- CSRF desactive
- Autorisation permissive (tous les endpoints accessibles)
- Adapte pour les tests et le developpement

### Configuration recommandee (Production)

- Activer Spring Security
- Implementer JWT pour l'authentification
- Configurer les roles et permissions
- Activer HTTPS
- Configurer CORS de maniere restrictive
- Proteger les endpoints sensibles

## Roles utilisateurs

- **ADMIN_RH** - Administrateur Ressources Humaines
  - Gestion complete des demandes et du personnel
  - Validation des demandes de conge/demission
  - Gestion des recrutements

- **ADMIN_ECO** - Administrateur Economique
  - Gestion des besoins, commandes et depenses
  - Validation des achats
  - Suivi budgetaire

- **PERSONNEL** - Employe
  - Soumission de demandes
  - Consultation de ses propres demandes
  - Candidature aux offres internes

## Contribution

1. Forker le projet
2. Creer une branche pour votre fonctionnalite (`git checkout -b feature/NouvelleFonctionnalite`)
3. Commiter vos changements (`git commit -m 'Ajout d'une nouvelle fonctionnalite'`)
4. Pousser vers la branche (`git push origin feature/NouvelleFonctionnalite`)
5. Ouvrir une Pull Request

## Branches

- **main** - Branche principale de production
- **sadki-demandes** - Branche de developpement pour le module demandes

## Tests

### Backend
```bash
cd backend
mvn test
```

### Android
- Executer les tests depuis Android Studio
- Run > Run 'All Tests'

## Licence

Ce projet est developpe pour l'ENSA Tetouan dans un cadre academique.

## Contact et support

Pour toute question ou probleme, veuillez contacter l'equipe de developpement.

## Statut du projet

Le projet est actuellement en developpement actif avec les modules principaux implementes et fonctionnels:
- Module RH (Demandes) - Complet
- Module Recrutement - Complet
- Module Gestion des Ressources (Besoins, Commandes, Depenses) - Complet
- Module Notifications - En cours
- Module Authentification - A securiser pour la production
