-- =====================================================================
-- JEU DE DONNÉES DE DÉMONSTRATION RÉALISTE POUR TOUTES LES PAGES DGI
-- =====================================================================

BEGIN;

-- 1. UTILISATEURS SUPPLÉMENTAIRES (Mots de passe : 'admin')
INSERT INTO jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, created_by, created_date, last_modified_by, last_modified_date)
VALUES 
    (2, 'technicien', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Moussa', 'Kaboré', 'technicien@dgi.bf', '', true, 'fr', 'system', NOW(), 'system', NOW()),
    (3, 'responsable', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Ousmane', 'Sawadogo', 'responsable@dgi.bf', '', true, 'fr', 'system', NOW(), 'system', NOW()),
    (4, 'agent', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Salif', 'Compaoré', 'agent@dgi.bf', '', true, 'fr', 'system', NOW(), 'system', NOW()),
    (5, 'awa.traore', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Awa', 'Traoré', 'awa.traore@dgi.bf', '', true, 'fr', 'system', NOW(), 'system', NOW()),
    (6, 'ibrahim.ouedraogo', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Ibrahim', 'Ouédraogo', 'ibrahim.ouedraogo@dgi.bf', '', true, 'fr', 'system', NOW(), 'system', NOW()),
    (7, 'fatou.diallo', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Fatou', 'Diallo', 'fatou.diallo@dgi.bf', '', true, 'fr', 'system', NOW(), 'system', NOW()),
    (8, 'paul.zongo', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Paul', 'Zongo', 'paul.zongo@dgi.bf', '', true, 'fr', 'system', NOW(), 'system', NOW())
ON CONFLICT (id) DO NOTHING;

-- RÔLES ET AUTORISATIONS DES UTILISATEURS
INSERT INTO jhi_user_authority (user_id, authority_name)
VALUES
    (2, 'ROLE_TECHNICIEN'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_RESPONSABLE'),
    (3, 'ROLE_USER'),
    (4, 'ROLE_AGENT'),
    (4, 'ROLE_USER'),
    (5, 'ROLE_AGENT'),
    (5, 'ROLE_USER'),
    (6, 'ROLE_AGENT'),
    (6, 'ROLE_USER'),
    (7, 'ROLE_AGENT'),
    (7, 'ROLE_USER'),
    (8, 'ROLE_TECHNICIEN'),
    (8, 'ROLE_USER')
ON CONFLICT DO NOTHING;

-- 2. SERVICES DGI
INSERT INTO service_dgi (id, nom_service, chef_service)
VALUES
    (4, 'Direction des Grandes Entreprises (DGE)', 'M. Ousmane Sawadogo'),
    (5, 'Direction des Moyennes Entreprises (DME)', 'Mme Alimata Ouattara'),
    (6, 'Direction du Contrôle et des Enquêtes Fiscales (DCEF)', 'M. Jean-Baptiste Tiendrebéogo'),
    (7, 'Direction des Ressources Humaines et des Moyens Généraux (DRH-MG)', 'M. Boukary Zida'),
    (8, 'Centre des Impôts Ouaga-Nord', 'M. Rasmané Sanfo'),
    (9, 'Direction du Cadastre et des Affaires Foncières (DCAF)', 'Mme Salimata Somé')
ON CONFLICT (id) DO NOTHING;

-- 3. AGENTS DGI
UPDATE agent SET nom = 'Compaoré', prenom = 'Salif', service_id = 7, utilisateur_id = 4 WHERE id = 2;

INSERT INTO agent (id, nom, prenom, service_id, utilisateur_id)
VALUES
    (3, 'Kaboré', 'Moussa', 1, 2),
    (4, 'Sawadogo', 'Ousmane', 4, 3),
    (5, 'Ouédraogo', 'Ibrahim', 6, 6),
    (6, 'Diallo', 'Fatou', 5, 7),
    (7, 'Zongo', 'Paul', 1, 8),
    (8, 'Koné', 'Aminata', 8, 5)
ON CONFLICT (id) DO NOTHING;

-- 4. FOURNISSEURS
INSERT INTO fournisseur (id, nom, contact, email, telephone)
VALUES
    (3, 'CFAO Technologies Burkina', 'Michel Somé', 'commercial@cfao-tech.bf', '+226 25 30 65 00'),
    (4, 'Burotop Iris International', 'Nadia Ouédraogo', 'ventes@burotop.bf', '+226 25 31 40 20'),
    (5, 'Microtec Informatique Faso', 'Adama Sanou', 'support@microtec-faso.com', '+226 25 33 12 18'),
    (6, 'Société Burkinabè de Bureautique (SBB)', 'Gérard Ilboudo', 'contact@sbb-burkina.bf', '+226 25 37 80 90')
ON CONFLICT (id) DO NOTHING;

-- 5. ACTIFS
INSERT INTO actif (id, code_inventaire, designation, marque, modele, numero_serie, code_barre, type, etat, localisation, date_acquisition, valeur_acquisition, categorie_id)
VALUES
    (6, 'DGI-INFO-006', 'Serveur de base de données PostgreSQL', 'Dell', 'PowerEdge R750', 'SN-R750-001', 'CB-DGI-006', 'SERVEUR', 'EN_SERVICE', 'Salle serveurs DSI - Baie A', '2024-06-10', 4850000.0, 2),
    (7, 'DGI-INFO-007', 'Copieur Multifonction Départemental', 'Canon', 'imageRUNNER ADV 2630i', 'SN-CAN-2630', 'CB-DGI-007', 'IMPRIMANTE', 'EN_SERVICE', 'Direction Générale - Palier R+1', '2024-01-20', 2200000.0, 6),
    (8, 'DGI-INFO-008', 'Switch Core Réseau DSI 48 Ports', 'Cisco', 'Catalyst 3850-48P', 'SN-CSCO-3850', 'CB-DGI-008', 'RESEAU', 'EN_SERVICE', 'Salle serveurs DSI - Baie Réseau', '2023-11-15', 1850000.0, 10),
    (9, 'DGI-INFO-009', 'Onduleur On-Line 10 kVA', 'APC Schneider', 'Smart-UPS RT 10000VA', 'SN-APC-10K', 'CB-DGI-009', 'PERIPHERIQUE', 'EN_SERVICE', 'Local Technique Électrique', '2024-02-05', 3400000.0, 5),
    (10, 'DGI-INFO-010', 'Ordinateur portable Gestionnaire Recettes', 'Dell', 'Latitude 5430', 'SN-DEL-5430', 'CB-DGI-010', 'POSTE_TRAVAIL', 'EN_SERVICE', 'Centre des Impôts Ouaga-Nord', '2024-04-12', 780000.0, 4),
    (11, 'DGI-INFO-011', 'Ordinateur portable Inspecteur Fiscal', 'Lenovo', 'ThinkPad T14 Gen 3', 'SN-LEN-T14-01', 'CB-DGI-011', 'POSTE_TRAVAIL', 'EN_SERVICE', 'Direction du Contrôle Fiscal (DCEF)', '2024-05-18', 850000.0, 4),
    (12, 'DGI-INFO-012', 'Station de travail DAO Cadastre', 'HP', 'Z2 G9 Workstation', 'SN-HP-Z2-99', 'CB-DGI-012', 'POSTE_TRAVAIL', 'EN_SERVICE', 'Direction du Cadastre (DCAF)', '2024-03-01', 1450000.0, 7),
    (13, 'DGI-INFO-013', 'Imprimante réseau dédiée Recettes', 'HP', 'LaserJet Pro M404dn', 'SN-HP-M404', 'CB-DGI-013', 'IMPRIMANTE', 'EN_MAINTENANCE', 'Service Guichet Unique', '2023-09-10', 320000.0, 1),
    (14, 'DGI-INFO-014', 'Scanner de production bordereaux', 'Fujitsu', 'fi-7160', 'SN-FUJ-7160', 'CB-DGI-014', 'PERIPHERIQUE', 'EN_SERVICE', 'Service Archivage et Numérisation', '2024-01-10', 650000.0, 11),
    (15, 'DGI-INFO-015', 'Vidéoprojecteur Salle de Réunion', 'Epson', 'EB-2250U Full HD', 'SN-EPS-2250', 'CB-DGI-015', 'PERIPHERIQUE', 'EN_SERVICE', 'Salle de Conférence DGI', '2023-12-05', 750000.0, 9),
    (16, 'DGI-INFO-016', 'Téléphone IP Secrétariat Général', 'Cisco', 'IP Phone 8845', 'SN-CSCO-8845', 'CB-DGI-016', 'PERIPHERIQUE', 'EN_SERVICE', 'Cabinet Direction Générale', '2024-02-14', 195000.0, 8),
    (17, 'DGI-INFO-017', 'Écran professionnel 27 pouces 4K', 'Dell', 'UltraSharp U2723QE', 'SN-DEL-U27', 'CB-DGI-017', 'PERIPHERIQUE', 'EN_SERVICE', 'Bureau Directeur DSI', '2024-05-02', 310000.0, 3),
    (18, 'DGI-INFO-018', 'Poste fixe en attente affectation', 'Dell', 'OptiPlex 7090', 'SN-DEL-7090-02', 'CB-DGI-018', 'POSTE_TRAVAIL', 'EN_SERVICE', 'Magasin Informatique DSI', '2024-06-25', 620000.0, 7),
    (19, 'DGI-INFO-019', 'Portable en panne carte mère', 'HP', 'EliteBook 840 G7', 'SN-HP-840-05', 'CB-DGI-019', 'POSTE_TRAVAIL', 'EN_MAINTENANCE', 'Atelier Maintenance DSI', '2022-08-14', 820000.0, 4),
    (20, 'DGI-INFO-020', 'Serveur de sauvegarde hors service', 'Dell', 'PowerEdge R620', 'SN-DEL-R620-OBS', 'CB-DGI-020', 'SERVEUR', 'REFORME', 'Zone Déclassement / Réforme', '2017-04-10', 3200000.0, 2)
ON CONFLICT (id) DO NOTHING;

-- 6. CONTRATS
INSERT INTO contrat (id, type_contrat, reference, date_debut, date_fin, fournisseur_id, actif_id)
VALUES
    (3, 'MAINTENANCE', 'MAINT-2026-003', '2026-01-01', '2026-12-31', 3, 6),
    (4, 'GARANTIE', 'GAR-2026-004', '2024-01-20', '2027-01-19', 4, 7),
    (5, 'MAINTENANCE', 'MAINT-2026-005', '2026-03-01', '2027-02-28', 5, 8),
    (6, 'GARANTIE', 'GAR-2026-006', '2024-02-05', '2027-02-04', 6, 9)
ON CONFLICT (id) DO NOTHING;

-- 7. AFFECTATIONS & AFFECTATION-ACTIFS
INSERT INTO affectation (id, date_affectation, motif, date_restitution, agent_id)
VALUES
    (3, '2026-02-01', 'Dotation équipement standard poste de travail', NULL, 2),
    (4, '2026-03-01', 'Outillage et matériel technicien support', NULL, 3),
    (5, '2026-03-15', 'Attribution poste direction DGE', NULL, 4),
    (6, '2026-04-10', 'Dotation inspection et enquêtes fiscales', NULL, 5),
    (7, '2025-05-10', 'Affectation temporaire clôturée', '2026-01-10', 6)
ON CONFLICT (id) DO NOTHING;

INSERT INTO affectation_actif (id, observation, statut, affectation_id, actif_id)
VALUES
    (4, 'Poste portable en parfait état avec sacoche', 'ACTIVE', 3, 10),
    (5, 'Écran secondaire haute résolution remis avec câble HDMI', 'ACTIVE', 3, 17),
    (6, 'Matériel mobile de diagnostic réseau et support terrain', 'ACTIVE', 4, 2),
    (7, 'Station mobile avec suite bureautique et VPN configuré', 'ACTIVE', 5, 11),
    (8, 'Station de calcul et DAO cadastre', 'ACTIVE', 6, 12),
    (9, 'Matériel restitué suite à réaffectation de service', 'CLOTUREE', 7, 5)
ON CONFLICT (id) DO NOTHING;

-- 8. TRANSFERTS & TRANSFERT-ACTIFS & BORDEREAUX
INSERT INTO transfert (id, date_transfert, statut, commentaire_rejet, date_traitement, service_origine_id, service_destinataire_id, demandeur_id, validateur_id)
VALUES
    (2, '2026-08-10', 'EN_ATTENTE', NULL, NULL, 1, 4, 1, NULL),
    (3, '2026-08-15', 'VALIDE', NULL, '2026-08-16', 2, 5, 1, 1),
    (4, '2026-08-20', 'REJETE', 'Refusé : Matériel requis en priorité pour la clôture fiscale annuelle', '2026-08-21', 3, 6, 1, 1),
    (5, '2026-09-01', 'VALIDE', NULL, '2026-09-02', 4, 1, 1, 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO transfert_actif (id, observation, transfert_id, actif_id)
VALUES
    (2, 'Transfert en attente de validation pour la DGE', 2, 10),
    (3, 'Transfert validé avec accusé de réception signé', 3, 13),
    (4, 'Transfert rejeté suite à arbitrage DSI', 4, 14),
    (5, 'Retour en magasin central pour diagnostic technique', 5, 19)
ON CONFLICT (id) DO NOTHING;

INSERT INTO bordereau (id, numero, date_emission, type_bordereau, statut_validation, date_validation, transfert_id, affectation_id, emetteur_id)
VALUES
    (3, 'BR-2026-002', '2026-08-10', 'TRANSFERT', 'EN_ATTENTE', NULL, 2, NULL, 1),
    (4, 'BR-2026-003', '2026-08-15', 'TRANSFERT', 'VALIDE', '2026-08-16', 3, NULL, 1),
    (5, 'BR-2026-004', '2026-08-20', 'TRANSFERT', 'REJETE', '2026-08-21', 4, NULL, 1),
    (6, 'BR-2026-005', '2026-09-01', 'TRANSFERT', 'VALIDE', '2026-09-02', 5, NULL, 1),
    (7, 'BA-2026-002', '2026-02-01', 'AFFECTATION', 'VALIDE', '2026-02-01', NULL, 3, 1),
    (8, 'BA-2026-003', '2026-03-01', 'AFFECTATION', 'VALIDE', '2026-03-01', NULL, 4, 1)
ON CONFLICT (id) DO NOTHING;

-- 9. PANNES
INSERT INTO panne (id, description, date_declaration, statut_panne, actif_id)
VALUES
    (3, 'Écran d erreur 59.F0 moteur et bourrage systématique au bac 2', '2026-07-15', 'SIGNALEE', 13),
    (4, 'Court-circuit carte mère après surtension, appareil ne démarre plus', '2026-08-01', 'EN_COURS', 19),
    (5, 'Batteries en fin de vie, alarme sonore continue et voyant rouge', '2026-08-12', 'EN_COURS', 9),
    (6, 'Ports 24 et 25 désynchronisés, perte de paquets sur le VLAN DGE', '2026-07-28', 'RESOLUE', 8),
    (7, 'Secteurs défectueux disque dur, lenteurs extrêmes du système', '2026-08-05', 'RESOLUE', 1),
    (8, 'Batterie défaillante et extinction inopinée sur batterie', '2026-08-10', 'SIGNALEE', 10)
ON CONFLICT (id) DO NOTHING;

-- 10. MAINTENANCES
INSERT INTO maintenance (id, type_maintenance, date_panne, statut, compte_rendu, date_cloture, actif_id, technicien_id)
VALUES
    (3, 'CORRECTIVE', '2026-07-15', 'OUVERTE', 'Prise en charge panne moteur imprimante réseau', NULL, 13, 2),
    (4, 'CORRECTIVE', '2026-08-01', 'EN_COURS', 'Diagnostic atelier : commande nouvelle carte mère en cours', NULL, 19, 2),
    (5, 'PREVENTIVE', '2026-08-15', 'OUVERTE', 'Mise à jour firmware iDRAC et dépoussiérage baie serveur', NULL, 6, 2),
    (6, 'CORRECTIVE', '2026-07-28', 'CLOTUREE', 'Reconfiguration port Cisco et changement cordon RJ45. Connectivité 1 Gbps rétablie.', '2026-07-30', 8, 2)
ON CONFLICT (id) DO NOTHING;

-- 11. PLANNINGS DE MAINTENANCE & INTERVENTIONS
INSERT INTO planning_maintenance (id, date_prevue, periodicite, statut, description)
VALUES
    (3, '2026-09-15', 'Trimestrielle', 'PLANIFIER', 'Maintenance préventive des onduleurs et climatisation salle serveurs'),
    (4, '2026-10-01', 'Semestrielle', 'PLANIFIER', 'Audit et nettoyage préventif des copieurs multifonctions DGI'),
    (5, '2026-11-10', 'Annuelle', 'PLANIFIER', 'Contrôle global des postes de travail et mise à niveau sécurité')
ON CONFLICT (id) DO NOTHING;

INSERT INTO intervention (id, date_declaration, type_intervention, statut, description, panne_id)
VALUES
    (3, '2026-07-16', 'CORRECTIVE', 'EN_COURS', 'Remplacement rouleau d entraînement et test impression', 3),
    (4, '2026-08-02', 'CORRECTIVE', 'EN_COURS', 'Test alimentation atelier et inspection multimètre composants', 4),
    (5, '2026-08-15', 'PREVENTIVE', 'CLOTUREE', 'Mise à niveau microcodes processeurs et contrôle thermie', 5),
    (6, '2026-07-29', 'CORRECTIVE', 'CLOTUREE', 'Test continuité câble et remplacement port GbE', 6)
ON CONFLICT (id) DO NOTHING;

INSERT INTO rel_planning_maintenance__intervention (intervention_id, planning_maintenance_id)
VALUES
    (3, 2),
    (4, 1),
    (5, 3),
    (6, 1)
ON CONFLICT DO NOTHING;

-- 12. INVENTAIRES, RECENSEMENTS & ÉQUIPEMENTS RECENSÉS
INSERT INTO inventaire (id, nom_fichier, date_import, actif_id)
VALUES
    (5, 'inventaire-serveurs-reseau-aout-2026.xlsx', '2026-08-01', 6),
    (6, 'inventaire-copieurs-direction-aout-2026.xlsx', '2026-08-05', 7),
    (7, 'inventaire-portables-cadres-sept-2026.xlsx', '2026-09-01', 10),
    (8, 'inventaire-stock-magasin-sept-2026.xlsx', '2026-09-10', 18)
ON CONFLICT (id) DO NOTHING;

INSERT INTO recensement (id, date_debut, date_fin, statut)
VALUES
    (2, '2026-09-01', '2026-09-30', 'CLOTUREE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO equipement_recensement (id, etat_constate, date_constat, emplacement_constate, anomalie_constatee, recensement_id, actif_id)
VALUES
    (5, 'EN_SERVICE', '2026-07-04', 'Salle serveurs DSI - Baie A', false, 1, 6),
    (6, 'EN_SERVICE', '2026-07-04', 'Direction Générale - Palier R+1', false, 1, 7),
    (7, 'EN_PANNE', '2026-07-05', 'Service Guichet Unique', true, 1, 13),
    (8, 'EN_SERVICE', '2026-07-06', 'Magasin Informatique DSI', false, 1, 18),
    (9, 'EN_SERVICE', '2026-09-10', 'Centre des Impôts Ouaga-Nord', false, 2, 10),
    (10, 'EN_SERVICE', '2026-09-12', 'Direction du Contrôle Fiscal (DCEF)', false, 2, 11)
ON CONFLICT (id) DO NOTHING;

-- 13. HISTORIQUE DES ACTIONS (TRAÇABILITÉ)
INSERT INTO historique_action (id, date_action, type_action, entite_ciblee, ancienne_valeur, nouvelle_valeur, utilisateur_id)
VALUES
    (4, '2026-07-15 08:30:00', 'CREATION', 'Panne', NULL, 'Signalement de la panne #3 sur imprimante HP M404', 1),
    (5, '2026-07-16 09:15:00', 'CREATION', 'Maintenance', NULL, 'Ouverture du dossier maintenance corrective #3', 2),
    (6, '2026-08-01 11:20:00', 'MODIFICATION', 'Transfert', 'EN_ATTENTE', 'Transfert #3 validé vers la DME', 1),
    (7, '2026-08-10 14:00:00', 'CREATION', 'Affectation', NULL, 'Nouvelle affectation #3 enregistrée pour Salif Compaoré', 1),
    (8, '2026-08-15 16:45:00', 'CREATION', 'Actif', NULL, 'Enregistrement nouvel actif DGI-INFO-010', 1),
    (9, '2026-08-20 10:10:00', 'MODIFICATION', 'Transfert', 'EN_ATTENTE', 'Transfert #4 rejeté : Matériel requis pour clôture fiscale', 1),
    (10, '2026-09-01 08:00:00', 'CREATION', 'Recensement', NULL, 'Lancement de la campagne de recensement T3 2026', 1)
ON CONFLICT (id) DO NOTHING;

-- 14. RAPPORTS
INSERT INTO rapport (id, titre, type_rapport, description, chemin_fichier, date_generation, genere_par, format_export, parametres)
VALUES
    (3, 'Bilan trimestriel des pannes T2 2026', 'MAINTENANCE', 'Analyse quantitative et temps moyen de résolution des incidents matériels', NULL, '2026-08-01 10:00:00', 'admin', 'PDF', '{"trimestre":"T2-2026"}'),
    (4, 'Inventaire physique annuel du parc informatique DGI', 'INVENTAIRE', 'Rapprochement comptable et inventaire physique exhaustif des immobilisations IT', NULL, '2026-08-15 15:30:00', 'admin', 'EXCEL', '{"exercice":"2026"}'),
    (5, 'Rapport des réformes et déclassements 2026', 'REFORME', 'Liste certifiée des équipements obsolètes et hors d usage proposés à la commission de réforme', NULL, '2026-08-25 11:00:00', 'admin', 'PDF', '{"statut":"REFORME"}'),
    (6, 'Statistiques d affectation et taux d équipement par direction', 'AFFECTATION', 'Indicateurs de déploiement des postes informatiques par service DGI', NULL, '2026-09-01 09:30:00', 'admin', 'CSV', '{"annee":"2026"}')
ON CONFLICT (id) DO NOTHING;

-- 15. MISE À JOUR DE TOUTES LES SÉQUENCES POSTGRESQL
SELECT setval('actif_seq', COALESCE((SELECT MAX(id) FROM actif), 0) + 1, false);
SELECT setval('affectation_seq', COALESCE((SELECT MAX(id) FROM affectation), 0) + 1, false);
SELECT setval('affectation_actif_seq', COALESCE((SELECT MAX(id) FROM affectation_actif), 0) + 1, false);
SELECT setval('agent_seq', COALESCE((SELECT MAX(id) FROM agent), 0) + 1, false);
SELECT setval('bordereau_seq', COALESCE((SELECT MAX(id) FROM bordereau), 0) + 1, false);
SELECT setval('categorie_materiel_seq', COALESCE((SELECT MAX(id) FROM categorie_materiel), 0) + 1, false);
SELECT setval('contrat_seq', COALESCE((SELECT MAX(id) FROM contrat), 0) + 1, false);
SELECT setval('equipement_recensement_seq', COALESCE((SELECT MAX(id) FROM equipement_recensement), 0) + 1, false);
SELECT setval('fournisseur_seq', COALESCE((SELECT MAX(id) FROM fournisseur), 0) + 1, false);
SELECT setval('historique_action_seq', COALESCE((SELECT MAX(id) FROM historique_action), 0) + 1, false);
SELECT setval('intervention_seq', COALESCE((SELECT MAX(id) FROM intervention), 0) + 1, false);
SELECT setval('inventaire_seq', COALESCE((SELECT MAX(id) FROM inventaire), 0) + 1, false);
SELECT setval('maintenance_seq', COALESCE((SELECT MAX(id) FROM maintenance), 0) + 1, false);
SELECT setval('panne_seq', COALESCE((SELECT MAX(id) FROM panne), 0) + 1, false);
SELECT setval('planning_maintenance_seq', COALESCE((SELECT MAX(id) FROM planning_maintenance), 0) + 1, false);
SELECT setval('rapport_seq', COALESCE((SELECT MAX(id) FROM rapport), 0) + 1, false);
SELECT setval('recensement_seq', COALESCE((SELECT MAX(id) FROM recensement), 0) + 1, false);
SELECT setval('service_dgi_seq', COALESCE((SELECT MAX(id) FROM service_dgi), 0) + 1, false);
SELECT setval('transfert_seq', COALESCE((SELECT MAX(id) FROM transfert), 0) + 1, false);
SELECT setval('transfert_actif_seq', COALESCE((SELECT MAX(id) FROM transfert_actif), 0) + 1, false);
SELECT setval('jhi_user_seq', COALESCE((SELECT MAX(id) FROM jhi_user), 0) + 1, false);

COMMIT;
