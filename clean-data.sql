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
