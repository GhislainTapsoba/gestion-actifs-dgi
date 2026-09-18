# Gestion des Actifs DGI

Application web de gestion du parc informatique de la Direction Générale des Impôts (DGI), construite avec JHipster, Angular et Spring Boot.

Le système permet de gérer les actifs matériels, les affectations, les transferts, la maintenance, les fournisseurs, les contrats, les interventions, les recensements et les historiques d’actions. L’objectif est d’avoir un outil de suivi complet pour piloter les équipements de la structure, sécuriser les mouvements et appliquer les règles métiers de validation et de contrôle d’accès.

---

## 1. Vue d’ensemble du projet

Cette application est une application JHipster de type monolithique full-stack :

- Backend : Java 21 + Spring Boot 4 + Spring Security + JPA + Liquibase
- Frontend : Angular 22 + TypeScript + Bootstrap
- Base de données : PostgreSQL
- Conteneurisation : Docker
- Génération du modèle : fichier JDL à la racine du projet

Le cœur du projet est décrit dans le fichier [gestion-actifs-dgi.jdl](gestion-actifs-dgi.jdl). C’est ce modèle qui définit les entités, leurs champs, leurs relations et les enumérations métier.

### Principales fonctionnalités

- Gestion des actifs informatiques : matériel, catégorie, statut, localisation, date d’acquisition
- Affectation des actifs aux agents et services
- Suivi des transferts entre services avec validation et rejet motivé
- Gestion des maintenances, interventions et plans de maintenance
- Gestion des pannes et des opérations de recensement
- Suivi des fournisseurs et des contrats de garantie ou maintenance
- Journal d’actions pour la traçabilité des mouvements
- Gestion des utilisateurs, rôles et permissions
- Sécurité des accès selon le profil utilisateur

---

## 2. Stack technique

| Couche          | Technologie                                  | Rôle                                |
| --------------- | -------------------------------------------- | ----------------------------------- |
| Backend         | Java 21                                      | logique métier, API REST, sécurité  |
| Framework       | Spring Boot 4                                | application serveur                 |
| Sécurité        | Spring Security + JWT/OAuth2 Resource Server | authentification et autorisations   |
| ORM             | Spring Data JPA                              | accès aux données                   |
| Base de données | PostgreSQL                                   | stockage des données métier         |
| Migration       | Liquibase                                    | évolutions de schéma                |
| Frontend        | Angular 22                                   | interface utilisateur               |
| Style           | Bootstrap / SCSS                             | mise en page et composants UI       |
| Build           | Maven + npm                                  | compilation, packaging, dépendances |
| Conteneur       | Docker                                       | base de données et services         |

---

## 3. Structure du dépôt

```text
gestion-actifs-dgi/
├── README.md
├── package.json
├── pom.xml
├── mvnw / mvnw.cmd
├── gestion-actifs-dgi.jdl
├── angular.json
├── cypress.config.ts
├── eslint.config.ts
├── tsconfig.json
├── src/
│   ├── main/
│   │   ├── docker/
│   │   ├── java/com/dgi/gestionactifs/
│   │   ├── resources/
│   │   └── webapp/
│   └── test/
├── target/
├── build-plugins/
├── add-*.ps1
├── apply-preauthorize-*.ps1
├── fix-*.ps1
└── set-*.ps1
```

### À retenir

- [gestion-actifs-dgi.jdl](gestion-actifs-dgi.jdl) : modèle principal de l’application
- [src/main/java/com/dgi/gestionactifs](src/main/java/com/dgi/gestionactifs) : code backend Java
- [src/main/webapp/app](src/main/webapp/app) : écrans Angular et composants front
- [src/main/resources/config/liquibase](src/main/resources/config/liquibase) : changements de base de données
- [src/main/docker](src/main/docker) : fichiers de configuration Docker
- Plusieurs scripts PowerShell à la racine : personnalisations de sécurité, filtres, règles métier, etc.

---

## 4. Prérequis

Avant de démarrer le projet, il faut installer :

1. Java 21
2. Node.js 24.20+ (respecter la version indiquée dans [package.json](package.json))
3. npm / npx
4. Docker Desktop
5. Git

### Vérification rapide

```powershell
node --version
java -version
docker --version
git --version
```

> Sur Windows, si `npm` n’est pas reconnu dans PowerShell, il faut relancer le terminal ou ajuster la politique d’exécution avec :
>
> ```powershell
> Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
> ```

---

## 5. Installation du projet

```powershell
git clone <url-du-repo>
cd gestion-actifs-dgi
npm install
```

La commande `npm install` installe les dépendances frontend et prépare le projet pour les commandes de build et de démarrage.

---

## 6. Démarrage du projet

### 6.1. Lancer la base PostgreSQL

Le projet utilise une base PostgreSQL locale via Docker.

```powershell
docker compose -f src/main/docker/postgresql.yml up -d
```

### 6.2. Démarrer l’application

Sous Windows, avec Maven Wrapper :

```powershell
./mvnw
```

ou :

```powershell
.\mvnw
```

Le backend va démarrer, puis Angular va être compilé. L’application devient accessible sur :

```text
http://localhost:8080
```

### 6.3. Commandes utiles

```powershell
# Démarrage backend seulement
./mvnw -Dskip.installnodenpm -Dskip.npm -ntp --batch-mode

# Build frontend production
npm run webapp:build:prod

# Build backend test
./mvnw test

# Build global du projet
npm run build
```

---

## 7. Identifiants par défaut

Par défaut, l’application crée un compte administrateur :

| Login | Mot de passe |
| ----- | ------------ |
| admin | admin        |

> À utiliser uniquement en environnement local. Il est conseillé de changer ce mot de passe avant toute mise en production.

---

## 8. Rôles et droits d’accès

Le système définit plusieurs profils utilisateurs, chacun avec des droits spécifiques.

| Rôle             | Description                                                                    |
| ---------------- | ------------------------------------------------------------------------------ |
| ROLE_ADMIN       | Accès complet à l’administration et à la gestion globale                       |
| ROLE_TECHNICIEN  | Gestion des actifs, affectations, maintenances et opérations techniques        |
| ROLE_RESPONSABLE | Validation et rejet des demandes de transfert ou décisions métier              |
| ROLE_AGENT       | Consultation et demandes liées à ses propres éléments, selon les règles métier |

### Règles métier importantes

- Un agent ne voit pas forcément tout le parc.
- Les opérations de transfert suivent un cycle de validation.
- Un refus d’un transfert doit être justifié par un commentaire explicite.
- Les contrôles de sécurité sont appliqués côté backend via les annotations et la configuration Spring Security.

---

## 9. Modèle métier du projet

Les entités principales sont décrites dans le JDL. Voici les grandes familles de données :

### Entités clés

- Actif : matériel informatique
- CategorieMateriel : catégorie de matériel
- Agent : personne rattachée à un service
- ServiceDgi : service d’origine ou de destination
- Affectation : attribution d’un actif à un agent ou à un service
- Transfert : mouvement d’un actif d’un service à un autre
- Bordereau : document justificatif d’un transfert ou d’une affectation
- Maintenance : opération de maintenance
- PlanningMaintenance : planification
- Intervention : intervention technique
- Panne : signalement d’anomalie
- Recensement : inventaire physique
- EquipementRecensement : constat de recensement par équipement
- Fournisseur : fournisseur technique
- Contrat : garanties et contrats de maintenance
- HistoriqueAction : journal d’activité et traçabilité

### Enumérations métier

Par exemple :

- TypeActif : poste travail, imprimante, serveur, réseau, périphérique
- StatutActif : en service, en maintenance, reformé, perdu/volé
- StatutTransfert : EN_ATTENTE, VALIDE, REJETE
- StatutMaintenance : OUVERTE, EN_COURS, CLOTUREE
- StatutPanne : SIGNALEE, EN_COURS, RESOLUE

---

## 10. Workflow de transfert

Le flux des transferts est un élément central du projet :

```text
EN_ATTENTE -> VALIDE
EN_ATTENTE -> REJETE
```

### Règle métier

- Un transfert est d’abord créé en attente.
- Un responsable de service valide ou rejette la demande.
- Si le transfert est rejeté, un commentaire de rejet est attendu.
- Une validation se fait selon les permissions de l’utilisateur connecté.

C’est précisément cette logique que l’on retrouve dans les modèles et dans les scripts de personnalisation à la racine du projet.

---

## 11. Sécurité et permissions

Le backend est basé sur Spring Security. Le projet inclut également des scripts personnalisés comme :

- `add-agent-filter-actif.ps1`
- `add-agent-filter-affectation.ps1`
- `add-agent-filter-maintenance.ps1`
- `apply-preauthorize-actif.ps1`
- `apply-preauthorize-affectation.ps1`
- `apply-preauthorize-transfert.ps1`

Ces scripts sont utilisés pour appliquer certains filtres et règles de visibilité selon le profil connecté. Ils documentent les droits métiers et permettent de reproduire ou corriger la logique de sécurité au niveau des entités.

---

## 12. Développement et modification du modèle

Le projet est défini par un JDL. Si tu ajoutes une entité ou un champ, tu peux le modifier dans [gestion-actifs-dgi.jdl](gestion-actifs-dgi.jdl) puis régénérer le code avec JHipster.

### Exemple de workflow

```powershell
# Modifier le JDL
# puis relancer la génération JHipster
jhipster jdl gestion-actifs-dgi.jdl

# Vérifier que le projet compile
./mvnw
```

> Attention : une régénération JHipster peut écraser les fichiers générés, donc il faut toujours vérifier le diff Git avant un commit.

---

## 13. Tests et validation

Le projet est prêt pour des validations backend et frontend.

### Commandes de tests

```powershell
# Tests backend
./mvnw test

# Tests frontend
npm test

# Build Angular
npm run webapp:build:prod
```

### Vérifications utiles

- build Maven
- lint Angular / ESLint
- compilation TypeScript
- tests unitaires backend
- tests Cypress / e2e si activés

---

## 14. Déploiement et packaging

Le fichier [package.json](package.json) propose des commandes pour packager l’application :

```powershell
npm run java:jar:prod
npm run java:docker:prod
```

La commande `java:jar:prod` génère un artefact exécutable et `java:docker:prod` construit une image Docker.

---

## 15. Problèmes fréquents

### 1) PowerShell ne reconnaît pas `npm`

Vérifie que Node.js est bien installé puis redémarre le terminal.

### 2) La base PostgreSQL ne démarre pas

Vérifie si Docker Desktop est lancé, puis relance :

```powershell
docker compose -f src/main/docker/postgresql.yml up -d
```

### 3) L’application ne démarre pas

Vérifie les points suivants :

- Java 21 installé
- base DB démarrée
- dépendances installées (`npm install`)
- port 8080 libre

### 4) Liquibase ou migration SQL échoue

Vérifie les fichiers changelog et le schéma de base. Les migrations sont gérées automatiquement avec Liquibase.

### 5) Erreurs de droits / accès refusé

Vérifie bien le rôle attribué à l’utilisateur connecté et la configuration des scripts de sécurité sur les entités concernées.

---

## 16. Ressources utiles

- JHipster : https://www.jhipster.tech
- Spring Boot : https://spring.io/projects/spring-boot
- Angular : https://angular.io
- PostgreSQL : https://www.postgresql.org
- Docker : https://www.docker.com

---

## 17. Résumé rapide

Ce projet est une application de gestion de parc informatique adaptée à un environnement DGI. Il couvre la gestion des équipements, les affectations, les transferts, la maintenance, les fournisseurs, les contrats et la traçabilité. Il est conçu avec une architecture moderne JHipster, ce qui permet de développer rapidement, de sécuriser les accès, et de maintenir un modèle de données cohérent et évolutif.

Si tu veux développer dessus, la bonne manière est de travailler dans l’ordre suivant :

1. comprendre le modèle dans [gestion-actifs-dgi.jdl](gestion-actifs-dgi.jdl)
2. identifier l’entité concernée dans le backend
3. vérifier le code Spring Service / Repository
4. vérifier le composant Angular associé
5. valider les permissions et les règles métier avant de conclure un changement

---

## 18. Développement recommandé pour l’équipe

Pour garder le projet propre et compréhensible :

- respecter le modèle JDL comme source de vérité
- documenter chaque règle métier ajoutée
- tester les flux de validation et d’autorisation
- vérifier les scripts de sécurité lors d’une modification d’entité
- toujours faire un contrôle du diff Git avant un commit de génération automatique

Ce README est pensé comme document de référence pour démarrer, comprendre, développer et maintenir le projet sans passer par des suppositions.
