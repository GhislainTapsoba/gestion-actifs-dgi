# Gestion des Actifs Informatiques — DGI

Application de gestion du parc informatique de la Direction Générale des Impôts (DGI) : suivi des équipements, affectations, transferts, maintenance, fournisseurs et contrats de garantie.

Ce projet est généré et structuré avec **JHipster**, une plateforme qui génère automatiquement une application web complète (backend Java + frontend Angular + base de données) à partir d'un modèle de données. Ce README explique comment le faire fonctionner, même si tu n'as jamais utilisé JHipster.

---

## Table des matières

1. [Comprendre les grandes briques du projet](#1-comprendre-les-grandes-briques-du-projet)
2. [Prérequis à installer](#2-prérequis-à-installer)
3. [Installation du projet](#3-installation-du-projet)
4. [Lancer l'application](#4-lancer-lapplication)
5. [Se connecter](#5-se-connecter)
6. [Les rôles et les droits](#6-les-rôles-et-les-droits)
7. [Structure du projet](#7-structure-du-projet)
8. [Modifier une entité (ajouter un champ, etc.)](#8-modifier-une-entité)
9. [Problèmes fréquents](#9-problèmes-fréquents)
10. [Où trouver de l'aide](#10-où-trouver-de-laide)

---

## 1. Comprendre les grandes briques du projet

Avant de toucher au code, voici les pièces du puzzle :

| Brique              | Rôle                                                                                   | Technologie          |
| ------------------- | -------------------------------------------------------------------------------------- | -------------------- |
| **Backend**         | La logique métier, les règles de gestion, l'accès à la base de données                 | Java (Spring Boot)   |
| **Frontend**        | Ce que l'utilisateur voit et manipule dans son navigateur                              | Angular (TypeScript) |
| **Base de données** | Le stockage permanent des données (actifs, utilisateurs, etc.)                         | PostgreSQL           |
| **Liquibase**       | Un outil qui crée/modifie automatiquement les tables de la base quand le modèle change | Intégré au backend   |
| **Docker**          | Fait tourner PostgreSQL sans avoir à l'installer directement sur ta machine            | -                    |

**Ce que fait JHipster concrètement** : à partir d'un modèle décrivant les données (fichier `gestion-actifs-dgi.jdl` à la racine du projet), il génère tout le code répétitif — formulaires, listes, API, base de données — pour qu'on n'ait qu'à ajouter la logique métier spécifique par-dessus.

---

## 2. Prérequis à installer

Sur une machine Windows neuve, il faut installer, dans cet ordre :

1. **Node.js** (version LTS) — [nodejs.org](https://nodejs.org)
2. **Java 21** (Eclipse Temurin recommandé) — [adoptium.net](https://adoptium.net)
3. **Docker Desktop** — [docker.com](https://www.docker.com/products/docker-desktop)
4. **Git** et **GitHub Desktop** — pour récupérer et versionner le code

Vérifie chaque installation dans un terminal PowerShell :

```powershell
node --version
java -version
docker --version
git --version
```

> **Si PowerShell refuse d'exécuter des commandes** (`npm` par exemple) avec une erreur de politique d'exécution : ouvre PowerShell **en administrateur** et lance `Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser`, puis confirme.

---

## 3. Installation du projet

```powershell
# Cloner le dépôt (récupérer le code depuis GitHub)
git clone https://github.com/GhislainTapsoba/gestion-actifs-dgi.git
cd gestion-actifs-dgi

# Installer les dépendances (peut prendre plusieurs minutes)
npm install
```

---

## 4. Lancer l'application

**Étape 1 — démarrer la base de données** (Docker Desktop doit être ouvert) :

```powershell
docker compose -f src/main/docker/postgresql.yml up -d
```

**Étape 2 — démarrer l'application** :

```powershell
.\mvnw
```

La première fois, cette commande télécharge toutes les dépendances Java et compile le frontend Angular — ça peut prendre plusieurs minutes. Les fois suivantes seront plus rapides.

Quand tu vois dans le terminal :

```
Application 'gestionActifsDgi' is running!
Local: http://localhost:8080/
```

➡️ ouvre **http://localhost:8080** dans ton navigateur.

Pour arrêter l'application : `Ctrl+C` dans le terminal.

---

## 5. Se connecter

Un compte administrateur est créé automatiquement :

| Identifiant | Mot de passe |
| ----------- | ------------ |
| `admin`     | `admin`      |

⚠️ **Change ce mot de passe avant toute mise en production réelle.**

---

## 6. Les rôles et les droits

L'application distingue 4 profils, qui déterminent ce que chacun peut voir et faire :

| Rôle                                            | Peut faire                                                                                                                    |
| ----------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| **Administrateur** (`ROLE_ADMIN`)               | Tout : gestion des utilisateurs, suppression, configuration                                                                   |
| **Technicien** (`ROLE_TECHNICIEN`)              | Créer/modifier les actifs, affectations, transferts, maintenances                                                             |
| **Responsable de service** (`ROLE_RESPONSABLE`) | Valider ou rejeter les demandes de transfert, consulter                                                                       |
| **Agent** (`ROLE_AGENT`)                        | Consulter uniquement **ses propres** actifs/affectations/transferts/maintenances, demander un transfert ou signaler une panne |

### Attribuer un rôle à un utilisateur

1. Se connecter en tant qu'`admin`
2. Menu **Administration > Gestion des utilisateurs**
3. Cliquer sur l'utilisateur concerné, cocher le(s) rôle(s), enregistrer

### Le workflow des transferts

Un transfert suit ce cycle :

```
EN_ATTENTE  →  VALIDE   (par un Responsable de service)
            →  REJETE   (par un Responsable de service, avec commentaire obligatoire)
```

Cette validation se fait via des boutons dédiés dans l'interface (pas via la simple modification d'un transfert), pour garantir que la règle "un rejet doit être motivé" est toujours respectée.

---

## 7. Structure du projet

```
gestion-actifs-dgi/
├── gestion-actifs-dgi.jdl          → le modèle de données (à lire en premier pour comprendre les entités)
├── src/main/java/.../domain/       → les entités (Actif, Affectation, Transfert...)
├── src/main/java/.../repository/   → l'accès à la base de données
├── src/main/java/.../service/      → la logique métier
├── src/main/java/.../web/rest/     → les endpoints de l'API (ce que le frontend appelle)
├── src/main/webapp/app/entities/   → les écrans Angular (listes, formulaires) par entité
├── src/main/resources/config/liquibase/  → l'historique des évolutions de la base de données
└── src/main/docker/                → la configuration Docker (base de données)
```

**Pour comprendre le fonctionnement d'une fonctionnalité** (ex: comment fonctionne la création d'un Actif), suis le chemin dans cet ordre :

1. `web/rest/ActifResource.java` (reçoit la requête)
2. `service/ActifService.java` (la logique)
3. `repository/ActifRepository.java` (l'accès aux données)
4. `src/main/webapp/app/entities/actif/` (l'écran correspondant)

---

## 8. Modifier une entité

Si tu dois ajouter un champ à une entité existante (ex: ajouter "numéro de série" à Actif) :

1. Modifie le fichier `gestion-actifs-dgi.jdl` pour ajouter le champ
2. Relance : `jhipster jdl gestion-actifs-dgi.jdl`
3. JHipster va régénérer le code concerné et te demander confirmation avant d'écraser les fichiers existants
4. Relance `.\mvnw` pour vérifier que tout compile

> ⚠️ Cette régénération peut écraser des modifications manuelles faites directement dans le code généré (comme nos ajouts de sécurité `@PreAuthorize`). Vérifie toujours le diff Git après une régénération JDL avant de committer.

---

## 9. Problèmes fréquents

**`npm` refuse de s'exécuter dans PowerShell**
→ Voir la note dans la section [Prérequis](#2-prérequis-à-installer).

**Liquibase ne trouve pas un fichier de changelog**
→ Vérifie que le nom du fichier référencé dans `src/main/resources/config/liquibase/master.xml` correspond **exactement** (underscores, pas d'espaces) au nom réel du fichier dans le dossier `changelog/`.

**`git commit` échoue avec une erreur "ligne de commande trop longue" (Husky/Prettier)**
→ Utilise `git commit --no-verify -m "message"` pour ce commit précis, ou committe moins de fichiers à la fois.

**L'application affiche "An error has occurred" au démarrage**
→ Le frontend n'a probablement pas fini de compiler. Lance `npm run webapp:build` séparément pour voir le message d'erreur détaillé (souvent une simple question interactive — comme le partage de données Angular Analytics — qui bloque silencieusement le process).

**Erreur de doublon lors d'une insertion Liquibase (`duplicate key value`)**
→ La donnée existe déjà en base. Il faut rendre le changelog "idempotent" avec une `<preConditions onFail="MARK_RAN">` qui vérifie avant d'insérer (voir les exemples dans `20260902210000_added_custom_authorities.xml`).

---

## 10. Où trouver de l'aide

- **Documentation officielle JHipster** : [jhipster.tech](https://www.jhipster.tech)
- **Le cahier des charges et l'expression des besoins** du projet définissent toutes les règles métier — s'y référer avant toute décision fonctionnelle
- Les scripts PowerShell à la racine du projet (`apply-preauthorize-*.ps1`, `add-agent-filter-*.ps1`) documentent, par leur contenu, comment les règles de droits ont été appliquées — utile pour comprendre ou reproduire la même logique sur une nouvelle entité
