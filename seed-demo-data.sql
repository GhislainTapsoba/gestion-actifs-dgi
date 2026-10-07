-- Run after clean-data.sql. This script keeps the seeded admin account and
-- fills every business entity page with linked sample records.
BEGIN;

DO $$
DECLARE
    admin_id bigint;
    category_desktop_id bigint;
    category_laptop_id bigint;
    category_printer_id bigint;
    category_server_id bigint;
    service_it_id bigint;
    service_hr_id bigint;
    service_finance_id bigint;
    supplier_primary_id bigint;
    supplier_secondary_id bigint;
    agent_primary_id bigint;
    agent_secondary_id bigint;
    actif_desktop_id bigint;
    actif_laptop_id bigint;
    actif_printer_id bigint;
    actif_server_id bigint;
    affectation_primary_id bigint;
    affectation_secondary_id bigint;
    transfert_id bigint;
    panne_primary_id bigint;
    panne_secondary_id bigint;
    intervention_primary_id bigint;
    intervention_secondary_id bigint;
    planning_primary_id bigint;
    planning_secondary_id bigint;
    recensement_id bigint;
    bordereau_transfert_id bigint;
    bordereau_affectation_id bigint;
    maintenance_primary_id bigint;
    maintenance_secondary_id bigint;
    contrat_primary_id bigint;
    contrat_secondary_id bigint;
    inventaire_id bigint;
    rapport_primary_id bigint;
    rapport_secondary_id bigint;
BEGIN
    IF EXISTS (SELECT 1 FROM service_dgi) OR EXISTS (SELECT 1 FROM actif) THEN
        RAISE EXCEPTION 'Run clean-data.sql before seed-demo-data.sql';
    END IF;

    SELECT id INTO admin_id FROM jhi_user WHERE login = 'admin';
    IF admin_id IS NULL THEN
        RAISE EXCEPTION 'The seeded admin account is required';
    END IF;

    SELECT id INTO category_desktop_id FROM categorie_materiel WHERE libelle = 'Ordinateur de bureau' ORDER BY id LIMIT 1;
    SELECT id INTO category_laptop_id FROM categorie_materiel WHERE libelle = 'Ordinateur portable' ORDER BY id LIMIT 1;
    SELECT id INTO category_printer_id FROM categorie_materiel WHERE libelle = 'Imprimante' ORDER BY id LIMIT 1;
    SELECT id INTO category_server_id FROM categorie_materiel WHERE libelle = 'Serveur' ORDER BY id LIMIT 1;
    IF category_desktop_id IS NULL OR category_laptop_id IS NULL OR category_printer_id IS NULL OR category_server_id IS NULL THEN
        RAISE EXCEPTION 'Run seed-categories.sql before seed-demo-data.sql';
    END IF;

    INSERT INTO service_dgi (id, nom_service, chef_service)
    VALUES (nextval('service_dgi_seq'), 'Direction des systèmes d''information', 'Awa Traoré')
    RETURNING id INTO service_it_id;
    INSERT INTO service_dgi (id, nom_service, chef_service)
    VALUES (nextval('service_dgi_seq'), 'Service des ressources humaines', 'Moussa Koné')
    RETURNING id INTO service_hr_id;
    INSERT INTO service_dgi (id, nom_service, chef_service)
    VALUES (nextval('service_dgi_seq'), 'Service financier', 'Aminata Diallo')
    RETURNING id INTO service_finance_id;

    INSERT INTO fournisseur (id, nom, contact, email, telephone)
    VALUES (nextval('fournisseur_seq'), 'DigiTech Afrique', 'Koffi Yao', 'contact@digitech.example', '+225 01 02 03 04')
    RETURNING id INTO supplier_primary_id;
    INSERT INTO fournisseur (id, nom, contact, email, telephone)
    VALUES (nextval('fournisseur_seq'), 'Solutions Réseau CI', 'Mariame Cissé', 'ventes@reseau.example', '+225 05 06 07 08')
    RETURNING id INTO supplier_secondary_id;

    INSERT INTO agent (id, nom, prenom, service_id, utilisateur_id)
    VALUES (nextval('agent_seq'), 'Traoré', 'Awa', service_it_id, admin_id)
    RETURNING id INTO agent_primary_id;
    INSERT INTO agent (id, nom, prenom, service_id)
    VALUES (nextval('agent_seq'), 'Koné', 'Moussa', service_hr_id)
    RETURNING id INTO agent_secondary_id;

    INSERT INTO actif (
        id, code_inventaire, designation, marque, modele, numero_serie, code_barre,
        type, etat, localisation, date_acquisition, valeur_acquisition, categorie_id
    ) VALUES (
        nextval('actif_seq'), 'DGI-INFO-001', 'Poste fixe accueil', 'Dell', 'OptiPlex 7010', 'SN-DGI-001', 'CB-DGI-001',
        'POSTE_TRAVAIL', 'EN_SERVICE', 'Abidjan - Plateau', DATE '2024-03-15', 650000, category_desktop_id
    ) RETURNING id INTO actif_desktop_id;
    INSERT INTO actif (
        id, code_inventaire, designation, marque, modele, numero_serie, code_barre,
        type, etat, localisation, date_acquisition, valeur_acquisition, categorie_id
    ) VALUES (
        nextval('actif_seq'), 'DGI-INFO-002', 'Portable chef de service', 'HP', 'ProBook 450', 'SN-DGI-002', 'CB-DGI-002',
        'POSTE_TRAVAIL', 'EN_SERVICE', 'Abidjan - Plateau', DATE '2025-01-20', 720000, category_laptop_id
    ) RETURNING id INTO actif_laptop_id;
    INSERT INTO actif (
        id, code_inventaire, designation, marque, modele, numero_serie, code_barre,
        type, etat, localisation, date_acquisition, valeur_acquisition, categorie_id
    ) VALUES (
        nextval('actif_seq'), 'DGI-INFO-003', 'Imprimante réseau étage 2', 'Brother', 'MFC-L8900', 'SN-DGI-003', 'CB-DGI-003',
        'IMPRIMANTE', 'EN_SERVICE', 'Abidjan - Plateau, étage 2', DATE '2023-11-08', 410000, category_printer_id
    ) RETURNING id INTO actif_printer_id;
    INSERT INTO actif (
        id, code_inventaire, designation, marque, modele, numero_serie, code_barre,
        type, etat, localisation, date_acquisition, valeur_acquisition, categorie_id
    ) VALUES (
        nextval('actif_seq'), 'DGI-INFO-004', 'Serveur applicatif', 'HPE', 'ProLiant DL380', 'SN-DGI-004', 'CB-DGI-004',
        'SERVEUR', 'EN_MAINTENANCE', 'Salle informatique', DATE '2022-06-01', 4800000, category_server_id
    ) RETURNING id INTO actif_server_id;
    INSERT INTO actif (
        id, code_inventaire, designation, marque, modele, numero_serie, code_barre,
        type, etat, localisation, date_acquisition, valeur_acquisition, categorie_id
    ) VALUES (
        nextval('actif_seq'), 'DGI-INFO-005', 'Ancien poste réformé', 'Lenovo', 'ThinkCentre M720', 'SN-DGI-005', 'CB-DGI-005',
        'POSTE_TRAVAIL', 'REFORME', 'Magasin informatique', DATE '2018-09-12', 350000, category_desktop_id
    );

    INSERT INTO affectation (id, date_affectation, motif, agent_id)
    VALUES (nextval('affectation_seq'), DATE '2026-01-15', 'Dotation initiale', agent_primary_id)
    RETURNING id INTO affectation_primary_id;
    INSERT INTO affectation (id, date_affectation, motif, date_restitution, agent_id)
    VALUES (nextval('affectation_seq'), DATE '2026-02-10', 'Remplacement temporaire', DATE '2026-12-31', agent_secondary_id)
    RETURNING id INTO affectation_secondary_id;

    INSERT INTO affectation_actif (id, observation, statut, affectation_id, actif_id)
    VALUES (nextval('affectation_actif_seq'), 'Matériel remis en bon état', 'ACTIVE', affectation_primary_id, actif_desktop_id);
    INSERT INTO affectation_actif (id, observation, statut, affectation_id, actif_id)
    VALUES (nextval('affectation_actif_seq'), 'Chargeur fourni', 'ACTIVE', affectation_primary_id, actif_laptop_id);
    INSERT INTO affectation_actif (id, observation, statut, affectation_id, actif_id)
    VALUES (nextval('affectation_actif_seq'), 'Restitution enregistrée', 'CLOTUREE', affectation_secondary_id, actif_printer_id);

    INSERT INTO transfert (id, date_transfert, statut, date_traitement, service_origine_id, service_destinataire_id, demandeur_id)
    VALUES (nextval('transfert_seq'), DATE '2026-04-05', 'VALIDE', DATE '2026-04-06', service_it_id, service_hr_id, admin_id)
    RETURNING id INTO transfert_id;
    INSERT INTO transfert_actif (id, observation, transfert_id, actif_id)
    VALUES (nextval('transfert_actif_seq'), 'Transfert validé vers les ressources humaines', transfert_id, actif_laptop_id);

    INSERT INTO bordereau (
        id, numero, date_emission, type_bordereau, statut_validation, date_validation, transfert_id, emetteur_id
    ) VALUES (
        nextval('bordereau_seq'), 'BR-2026-001', DATE '2026-04-05', 'TRANSFERT', 'VALIDE', DATE '2026-04-06', transfert_id, admin_id
    ) RETURNING id INTO bordereau_transfert_id;
    INSERT INTO bordereau (
        id, numero, date_emission, type_bordereau, statut_validation, date_validation, affectation_id, emetteur_id
    ) VALUES (
        nextval('bordereau_seq'), 'BA-2026-001', DATE '2026-01-15', 'AFFECTATION', 'VALIDE', DATE '2026-01-15', affectation_primary_id, admin_id
    ) RETURNING id INTO bordereau_affectation_id;

    INSERT INTO maintenance (id, type_maintenance, date_panne, statut, compte_rendu, date_cloture, actif_id, technicien_id)
    VALUES (nextval('maintenance_seq'), 'PREVENTIVE', DATE '2026-05-10', 'OUVERTE', 'Contrôle périodique du serveur.', NULL, actif_server_id, admin_id)
    RETURNING id INTO maintenance_primary_id;
    INSERT INTO maintenance (id, type_maintenance, date_panne, statut, compte_rendu, date_cloture, actif_id)
    VALUES (nextval('maintenance_seq'), 'CORRECTIVE', DATE '2026-05-12', 'EN_COURS', 'Diagnostic de l''imprimante réseau.', NULL, actif_printer_id)
    RETURNING id INTO maintenance_secondary_id;

    INSERT INTO panne (id, description, date_declaration, statut_panne, actif_id)
    VALUES (nextval('panne_seq'), 'Le serveur redémarre de manière inattendue.', DATE '2026-05-10', 'EN_COURS', actif_server_id)
    RETURNING id INTO panne_primary_id;
    INSERT INTO panne (id, description, date_declaration, statut_panne, actif_id)
    VALUES (nextval('panne_seq'), 'Bourrage papier récurrent.', DATE '2026-05-12', 'SIGNALEE', actif_printer_id)
    RETURNING id INTO panne_secondary_id;

    INSERT INTO intervention (id, date_declaration, type_intervention, statut, description, panne_id)
    VALUES (nextval('intervention_seq'), DATE '2026-05-10', 'CORRECTIVE', 'EN_COURS', 'Analyse des journaux du serveur.', panne_primary_id)
    RETURNING id INTO intervention_primary_id;
    INSERT INTO intervention (id, date_declaration, type_intervention, statut, description, panne_id)
    VALUES (nextval('intervention_seq'), DATE '2026-05-12', 'PREVENTIVE', 'EN_COURS', 'Nettoyage et vérification du chemin papier.', panne_secondary_id)
    RETURNING id INTO intervention_secondary_id;

    INSERT INTO planning_maintenance (id, date_prevue, periodicite, statut, description)
    VALUES (nextval('planning_maintenance_seq'), DATE '2026-06-01', 'Mensuelle', 'PLANIFIER', 'Maintenance préventive du serveur.')
    RETURNING id INTO planning_primary_id;
    INSERT INTO planning_maintenance (id, date_prevue, periodicite, statut, description)
    VALUES (nextval('planning_maintenance_seq'), DATE '2026-06-15', 'Trimestrielle', 'PLANIFIER', 'Entretien du parc imprimantes.')
    RETURNING id INTO planning_secondary_id;
    INSERT INTO rel_planning_maintenance__intervention (planning_maintenance_id, intervention_id)
    VALUES (planning_primary_id, intervention_primary_id), (planning_secondary_id, intervention_secondary_id);

    INSERT INTO recensement (id, date_debut, date_fin, statut)
    VALUES (nextval('recensement_seq'), DATE '2026-07-01', DATE '2026-07-31', 'EN_COURS')
    RETURNING id INTO recensement_id;
    INSERT INTO equipement_recensement (id, etat_constate, date_constat, emplacement_constate, anomalie_constatee, recensement_id, actif_id)
    VALUES (nextval('equipement_recensement_seq'), 'EN_SERVICE', DATE '2026-07-02', 'Abidjan - Plateau', false, recensement_id, actif_desktop_id);
    INSERT INTO equipement_recensement (id, etat_constate, date_constat, emplacement_constate, anomalie_constatee, recensement_id, actif_id)
    VALUES (nextval('equipement_recensement_seq'), 'EN_SERVICE', DATE '2026-07-02', 'Abidjan - Plateau', false, recensement_id, actif_laptop_id);
    INSERT INTO equipement_recensement (id, etat_constate, date_constat, emplacement_constate, anomalie_constatee, recensement_id, actif_id)
    VALUES (nextval('equipement_recensement_seq'), 'EN_PANNE', DATE '2026-07-03', 'Abidjan - Plateau, étage 2', true, recensement_id, actif_printer_id);
    INSERT INTO equipement_recensement (id, etat_constate, date_constat, emplacement_constate, anomalie_constatee, recensement_id, actif_id)
    VALUES (nextval('equipement_recensement_seq'), 'EN_MAINTENANCE', DATE '2026-07-03', 'Salle informatique', true, recensement_id, actif_server_id);

    INSERT INTO inventaire (id, nom_fichier, date_import, actif_id)
    VALUES (nextval('inventaire_seq'), 'inventaire-dgi-juillet-2026.xlsx', DATE '2026-07-01', actif_desktop_id)
    RETURNING id INTO inventaire_id;
    INSERT INTO inventaire (id, nom_fichier, date_import, actif_id)
    VALUES
        (nextval('inventaire_seq'), 'inventaire-mobilite-avril-2026.xlsx', DATE '2026-04-01', actif_laptop_id),
        (nextval('inventaire_seq'), 'inventaire-imprimantes-mai-2026.xlsx', DATE '2026-05-05', actif_printer_id),
        (nextval('inventaire_seq'), 'inventaire-serveurs-juin-2026.xlsx', DATE '2026-06-01', actif_server_id);

    INSERT INTO contrat (id, type_contrat, reference, date_debut, date_fin, fournisseur_id, actif_id)
    VALUES (nextval('contrat_seq'), 'GARANTIE', 'GAR-2026-001', DATE '2025-01-20', DATE '2027-01-19', supplier_primary_id, actif_laptop_id)
    RETURNING id INTO contrat_primary_id;
    INSERT INTO contrat (id, type_contrat, reference, date_debut, date_fin, fournisseur_id, actif_id)
    VALUES (nextval('contrat_seq'), 'MAINTENANCE', 'MAINT-2026-002', DATE '2026-01-01', DATE '2026-12-31', supplier_secondary_id, actif_server_id)
    RETURNING id INTO contrat_secondary_id;

    INSERT INTO historique_action (id, date_action, type_action, entite_ciblee, ancienne_valeur, nouvelle_valeur, utilisateur_id)
    VALUES (nextval('historique_action_seq'), TIMESTAMPTZ '2026-04-05 09:00:00+00', 'CREATION', 'Transfert', NULL, 'Transfert BR-2026-001 créé', admin_id);
    INSERT INTO historique_action (id, date_action, type_action, entite_ciblee, ancienne_valeur, nouvelle_valeur, utilisateur_id)
    VALUES (nextval('historique_action_seq'), TIMESTAMPTZ '2026-05-10 10:30:00+00', 'MODIFICATION', 'Maintenance', 'OUVERTE', 'EN_COURS', admin_id);
    INSERT INTO historique_action (id, date_action, type_action, entite_ciblee, ancienne_valeur, nouvelle_valeur, utilisateur_id)
    VALUES (nextval('historique_action_seq'), TIMESTAMPTZ '2026-07-01 08:15:00+00', 'CREATION', 'Inventaire', NULL, 'Inventaire importé', admin_id);

    INSERT INTO rapport (id, titre, type_rapport, description, date_generation, genere_par, format_export, parametres)
    VALUES (nextval('rapport_seq'), 'État du parc informatique', 'INVENTAIRE', 'Synthèse des équipements recensés.', TIMESTAMPTZ '2026-07-31 16:00:00+00', 'admin', 'PDF', '{"periode":"2026-07"}')
    RETURNING id INTO rapport_primary_id;
    INSERT INTO rapport (id, titre, type_rapport, description, date_generation, genere_par, format_export, parametres)
    VALUES (nextval('rapport_seq'), 'Suivi des maintenances', 'MAINTENANCE', 'Interventions ouvertes et clôturées.', TIMESTAMPTZ '2026-07-31 16:05:00+00', 'admin', 'CSV', '{"statut":"EN_COURS"}')
    RETURNING id INTO rapport_secondary_id;
END $$;

COMMIT;