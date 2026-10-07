-- Script de nettoyage des données métier
-- Conserve : jhi_user (admin), jhi_authority, jhi_user_authority

TRUNCATE TABLE
    rel_planning_maintenance__intervention,
    historique_action,
    equipement_recensement,
    recensement,
    inventaire,
    rapport,
    intervention,
    planning_maintenance,
    panne,
    maintenance,
    bordereau,
    transfert_actif,
    affectation_actif,
    transfert,
    affectation,
    contrat,
    actif,
    agent,
    service_dgi,
    categorie_materiel,
    fournisseur
CASCADE;

-- Supprimer l'utilisateur "user" par défaut (id=2), garder admin (id=1)
DELETE FROM jhi_user_authority WHERE user_id = 2;
DELETE FROM jhi_user WHERE id = 2;

-- Repartir de 1 pour les tables métier vidées.
ALTER SEQUENCE actif_seq RESTART WITH 1;
ALTER SEQUENCE affectation_seq RESTART WITH 1;
ALTER SEQUENCE affectation_actif_seq RESTART WITH 1;
ALTER SEQUENCE agent_seq RESTART WITH 1;
ALTER SEQUENCE bordereau_seq RESTART WITH 1;
ALTER SEQUENCE categorie_materiel_seq RESTART WITH 1;
ALTER SEQUENCE contrat_seq RESTART WITH 1;
ALTER SEQUENCE equipement_recensement_seq RESTART WITH 1;
ALTER SEQUENCE fournisseur_seq RESTART WITH 1;
ALTER SEQUENCE historique_action_seq RESTART WITH 1;
ALTER SEQUENCE intervention_seq RESTART WITH 1;
ALTER SEQUENCE inventaire_seq RESTART WITH 1;
ALTER SEQUENCE maintenance_seq RESTART WITH 1;
ALTER SEQUENCE panne_seq RESTART WITH 1;
ALTER SEQUENCE planning_maintenance_seq RESTART WITH 1;
ALTER SEQUENCE rapport_seq RESTART WITH 1;
ALTER SEQUENCE recensement_seq RESTART WITH 1;
ALTER SEQUENCE service_dgi_seq RESTART WITH 1;
ALTER SEQUENCE transfert_seq RESTART WITH 1;
ALTER SEQUENCE transfert_actif_seq RESTART WITH 1;
SELECT setval('jhi_user_seq', COALESCE((SELECT MAX(id) FROM jhi_user), 0) + 1, false);
