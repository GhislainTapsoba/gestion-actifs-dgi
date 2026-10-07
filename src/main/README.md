# Architecture & Documentation du Répertoire `src/main`

Ce répertoire constitue le **cœur applicatif** du projet **Gestion des Actifs de la DGI** (Direction Générale des Impôts). Il regroupe l'intégralité du code source de production : la couche conteneurisation, le backend Java / Spring Boot, les ressources de configuration et migrations de données, ainsi que l'interface utilisateur Angular.

---

## 📁 1. Vue d'ensemble de la structure

```text
src/main/
├── docker/          # Environnements conteneurisés (Base de données, supervision, outils)
├── java/            # Code source backend Java 21 / Spring Boot (API REST & Logique métier)
├── resources/       # Fichiers de configuration, migrations Liquibase, i18n et templates
└── webapp/          # Application frontend Angular (SPA avec composants Standalone)
```

---

## 🐳 2. Conteneurisation & Outils (`src/main/docker/`)

Ce dossier fournit les descripteurs Docker Compose pour instancier l'écosystème technique requis en développement et production :

- **`postgresql.yml`** : Instance PostgreSQL dédiée au stockage des données de l'application.
- **`app.yml`** : Déploiement conteneurisé de l'application complète.
- **`monitoring.yml`**, **`prometheus/`**, **`grafana/`** : Stack de supervision des performances, santé JVM et métriques applicatives.
- **`jhipster-control-center.yml`** : Tableau de bord d'administration technique et surveillance des instances.
- **`sonar.yml`** : Analyse statique de code et qualité logicielle SonarQube.
- **`jib/`** : Scripts d'optimisation pour la construction d'images Docker sans démon local.

---

## ☕ 3. Backend Java (`src/main/java/com/dgi/gestionactifs/`)

Le backend suit une **architecture en couches** (N-Tier) pilotée par le domaine métier, robuste et sécurisée.

### Découpage des packages

- **`GestionActifsDgiApp.java`** : Classe principale d'amorçage Spring Boot.
- **`aop/`** : Aspects transversaux (journalisation dynamique des appels de service et des exceptions avec `LoggingAspect.java`).
- **`config/`** : Configuration du framework (Spring Security, JPA/Hibernate, Jackson, CORS, Cache, etc.).
- **`security/`** : Contrôle des accès, émission et validation des jetons JWT (`SecurityUtils.java`, `AuthoritiesConstants.java`).
  - **Rôles gérés** : `ROLE_ADMIN`, `ROLE_RESPONSABLE`, `ROLE_TECHNICIEN`, `ROLE_AGENT`, `ROLE_USER`.
- **`domain/`** : Modèle relationnel d'entités JPA / Hibernate.
- **`repository/`** : Interfaces d'accès aux données Spring Data JPA (avec pagination et filtrage).
- **`service/`** :
  - **Services métier (`*Service.java`, `*ServiceImpl.java`)** : Orchestration des règles de gestion transactionnelles.
  - **Services de recherche dynamique (`*QueryService.java`)** : Filtrage multicritère (Criteria API / JPA Specification).
  - **`dto/` & `mapper/`** : Transfert de données découplé et mapping automatique avec MapStruct.
  - **`UserService.java` & `MailService.java`** : Gestion des comptes, réinitialisation de mot de passe et notifications email.
- **`web/rest/`** : Contrôleurs REST (`@RestController`) exposant les routes HTTP de l'API avec sécurisation déclarative `@PreAuthorize`.

### Modules et Entités Métier

| Domaine Fonctionnel        | Entités Clés                                         | Rôle et Responsabilité                                                                                                                                                     |
| :------------------------- | :--------------------------------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Gestion du Parc**        | `Actif`, `CategorieMateriel`                         | Fiche d'inventaire complète (code inventaire, désignation, numéro de série, statut `EN_SERVICE`, `EN_MAINTENANCE`, `REFORME`, `PERDU_VOLE`, classification par catégorie). |
| **Organisation DGI**       | `ServiceDgi`, `Agent`                                | Représentation des directions/services DGI et des agents bénéficiaires.                                                                                                    |
| **Affectations**           | `Affectation`, `AffectationActif`                    | Attribution nominative d'un ou plusieurs équipements à un agent, avec motifs et dates de restitution.                                                                      |
| **Transferts**             | `Transfert`, `TransfertActif`                        | Mouvement de matériel entre services DGI, avec circuit de validation/rejet motivé (`EN_ATTENTE`, `VALIDE`, `REJETE`).                                                      |
| **Documents Officiels**    | `Bordereau`                                          | Génération et traçabilité des bordereaux d'affectation et de transfert avec numérotation officielle.                                                                       |
| **Support & Pannes**       | `Panne`, `Intervention`                              | Signalement des dysfonctionnements techniques et planification des interventions curatives.                                                                                |
| **Maintenance**            | `Maintenance`, `PlanningMaintenance`                 | Suivi des opérations préventives et correctives, calendrier périodique et fiches de clôture.                                                                               |
| **Audit & Recensement**    | `Recensement`, `EquipementRecensement`, `Inventaire` | Campagnes d'audit physique sur site, constat d'anomalies d'emplacement ou d'état, importation de fichiers d'inventaire.                                                    |
| **Contrats & Tiers**       | `Fournisseur`, `Contrat`                             | Suivi des garanties matérielles, prestataires et contrats d'infogérance/maintenance.                                                                                       |
| **Traçabilité & Rapports** | `HistoriqueAction`, `Rapport`                        | Journal d'audit de chaque action (création, modification, transfert) et gestion des rapports statistiques.                                                                 |

### 👥 Matrice des Acteurs et Droits d'Accès (RBAC)

| Acteur                                       | Type d'acteur              | Rôles et fonctionnalités dans le système                                                                                                                                                                                                                                                                                                     | Droits autorisés                                                                                           |
| :------------------------------------------- | :------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------------------------- |
| **Technicien** (`ROLE_TECHNICIEN`)           | Humain                     | • Gestion de l’inventaire (`/actif`, `/inventaire`, `/categorie-materiel`)<br>• Gestion des affectations (`/affectation`)<br>• Gestion des transferts et des restitutions (`/transfert`, validation & rejet)<br>• Gestion des opérations de maintenance (`/maintenance`, `/panne`, `/intervention`, `/planning-maintenance`, `/recensement`) | **CRUD complet** sur tous ses modules octroyés                                                             |
| **Responsable Service** (`ROLE_RESPONSABLE`) | Humain                     | • Gestion de l’inventaire<br>• Gestion des affectations<br>• Gestion des transferts et restitutions<br>• Gestion de la maintenance<br>• Consultation de l'historique et de la traçabilité (`/historique-action`)<br>• Consultation des statistiques et génération des rapports (`/rapport`, dashboard analytique)                            | **CRUD complet** sur tous ses modules octroyés                                                             |
| **Administrateur** (`ROLE_ADMIN`)            | Humain (Super-utilisateur) | • Gestion des utilisateurs (création, modification, suppression, désactivation des comptes et attribution des rôles/droits d'accès)<br>• Super-utilisateur disposant des droits intégraux sur le système                                                                                                                                     | **CRUD complet** sur les utilisateurs et sur l'ensemble du système                                         |
| **Agent** (`ROLE_AGENT`)                     | Humain                     | • Signaler une panne ou un incident sur un équipement (`/panne`)<br>• Consulter les équipements qui lui sont affectés (`/actif`)                                                                                                                                                                                                             | • **CRUD complet** sur les signalements et pannes<br>• **Consultation** (Read) de ses équipements affectés |

> **Règle de gestion RBAC** : Chaque rôle dispose de tous les droits (**Create, Read, Update, Delete**) dans l'ensemble des pages et entités où il est octroyé.

---

## ⚙️ 4. Configuration & Persistance (`src/main/resources/`)

- **`config/application*.yml`** :
  - `application.yml` : Paramètres globaux (JWT, règles de sécurité, envoi d'emails, métriques).
  - `application-dev.yml` : Environnement de développement local (logs détaillés, H2/PostgreSQL dev).
  - `application-prod.yml` : Profil de production durci (cache Hibernate de second niveau, compression HTTP, performance).
- **`config/liquibase/`** :
  - **`master.xml`** : Registre central de gestion des versions de schéma de base de données.
  - **`changelog/`** : Scripts de création des tables, contraintes d'intégrité référentielle et index pour chaque entité.
  - **`data/`** : Données initiales (comptes utilisateurs initiaux, autorités, catégories par défaut).
- **`i18n/`** : Traductions et messages système côté serveur (emails, alertes).
- **`templates/`** : Modèles d'emails HTML (Thymeleaf) pour les activations de compte et réinitialisations de mot de passe.
- **`logback-spring.xml`** : Stratégie de journalisation (rotation des logs, formateurs console et fichiers).

---

## 💻 5. Frontend Angular (`src/main/webapp/`)

L'interface est une **Single Page Application (SPA)** développée avec Angular (Standalone Components), TypeScript et Bootstrap/SCSS.

- **`app/core/`** : Socle d'authentification (`AccountService`, `AuthServerProvider`), intercepteurs HTTP pour l'injection du token JWT et la gestion centralisée des erreurs réseau.
- **`app/home/`** : Tableau de bord d'accueil intelligent, affichant des indicateurs (KPIs) adaptés au profil de l'utilisateur :
  - **Vue Agent** : Liste de ses actifs attribués, ses demandes de transfert, ses signalements de pannes en cours.
  - **Vue Gestionnaire / Admin** : Total du parc, taux d'actifs en service, alertes sur les transferts en attente de validation, maintenances urgentes, graphiques de répartition.
- **`app/entities/`** : Modules complets pour chaque entité métier :
  - Liste tabulaire paginée avec tri et recherche avancée.
  - Formulaires réactifs de création / édition avec validation des contraintes.
  - Vues détaillées et boîtes de dialogue de confirmation de suppression.
  - Filtres d'accès direct (`/actif/en-maintenance`, `/actif/a-reformer`, `/actif/non-affectes`).
- **`app/entities/dgi/`** : Hub de navigation rapide pour les modules métier de la DGI avec compteurs en temps réel.
- **`app/entities/rapport/`** : Consultation, recherche et filtrage des rapports et états générés.
- **`app/layouts/`** : Barre de navigation supérieure responsive (`navbar`) adaptant ses menus selon les habilitations RBAC, pied de page (`footer`).
- **`app/admin/`** : Panneau d'administration technique (gestion des utilisateurs, consultation des métriques, configuration à chaud, santé des services).
- **`i18n/`** : Dictionnaires de traduction français/anglais pour l'ensemble des écrans utilisateurs.

---

## 🔄 6. Cycle de Vie Fonctionnel d'un Actif

```text
[Acquisition / Fournisseur]
           │
           ▼
[Stock / Non Affecté] ──► [Affectation à un Agent/Service]
           │                                │
           │                                ▼
           │                    [Transfert Inter-Services]
           │                    (Demande ──► Validation/Rejet)
           │                                │
           ▼                                ▼
     [Signalement Panne] ──► [Intervention / Maintenance]
           │                                │
           │◄───────────────────────────────┘
           │
           ▼
 [Recensement / Constat d'état] ──► [Mise au Rebut / Réforme]
```
